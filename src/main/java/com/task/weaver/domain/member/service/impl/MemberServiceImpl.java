package com.task.weaver.domain.member.service.impl;

import static com.task.weaver.common.exception.ErrorCode.NO_MATCHED_VERIFICATION_CODE;
import static com.task.weaver.common.exception.ErrorCode.REFRESH_TOKEN_RESOLVE;
import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.aop.annotation.LoggingStopWatch;
import com.task.weaver.common.exception.jwt.CannotResolveToken;
import com.task.weaver.common.exception.jwt.RefreshTokenNotFoundException;
import com.task.weaver.common.exception.jwt.RefreshTokenNotMatchedException;
import com.task.weaver.common.exception.member.UnableSendMailException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRepository;
import com.task.weaver.common.redis.service.RedisService;
import com.task.weaver.common.util.CookieUtil;
import com.task.weaver.common.util.HttpHeaderUtil;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.dto.MemberProjectDTO;
import com.task.weaver.domain.member.dto.request.MemberDto;
import com.task.weaver.domain.member.dto.response.GetMemberListResponse;
import com.task.weaver.domain.member.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.member.dto.response.ResponseUserOauth.AllMember;
import com.task.weaver.domain.member.dto.response.ResponseUserOauth.MemberDTO;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.member.service.MemberService;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.ProjectMemberRepository;
import com.task.weaver.domain.userOauthMember.LoginType;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUuid;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.userOauthMember.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private static final String ACCESS_TOKEN_STARTS = "Bearer ";
    private static final String SORT_PROPERTIES = "id";
    private static final int BEGIN_INDEX = 7;

    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @LoggingStopWatch
    public ResponseToken reissue(String refreshToken, String loginType) {

        jwtTokenProvider.validateToken(refreshToken);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        RefreshToken currentRefreshToken = getCurrentRefreshToken(refreshToken);

        validateMatchedToken(refreshToken, currentRefreshToken);

        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.fromName(loginType));

        redisService.deleteRefreshToken(currentRefreshToken);
        refreshTokenRepository.save(new RefreshToken(currentRefreshToken.getId(), newRefreshToken));

        return ResponseToken.of(ACCESS_TOKEN_STARTS + newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(resolveToken(refreshToken));
        refreshTokenRepository.deleteById(authentication.getName());
    }

    @Override
    public Boolean checkMail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public Boolean checkId(String id) {
        return userRepository.findByUserId(id).isEmpty();
    }

    @Override
    public Boolean checkNickname(String nickname) {
        return userRepository.findByNickname(nickname).isEmpty();
    }

    @LoggingStopWatch
    @Override
    public ResponseUuid fetchUuid(final String email, final Boolean checked) {
        validVerificationCode(checked);
        User findUser = getUserByEmail(email);
        return ResponseUuid.of(findUser.getMemberUuid(), findUser.getUserId());
    }

    private static void validVerificationCode(final Boolean checked) {
        if (!checked) {
            throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, NO_MATCHED_VERIFICATION_CODE.getMessage());
        }
    }

    @LoggingStopWatch
    @Override
    public ResponseGetMember fetchMemberByUuid(UUID uuid) {
        Member member = getMemberById(uuid);
        UserOauthMember userOauthMemberData = member.resolveMemberByLoginType();
        return ResponseGetMember.of(MemberDto.memberDomainToDto(userOauthMemberData, member), member.getId());
    }

    @LoggingStopWatch
    @Override
    public ResponseUserIdNickname fetchMemberByEmail(String email, Boolean checked) {
        validVerificationCode(checked);
        return ResponseUserIdNickname.userToDto(getUserByEmail(email));
    }

    @LoggingStopWatch
    @Override
    public ResponseGetUserForFront fetchMemberFromToken(HttpServletRequest request) {
        String memberUuid = jwtTokenProvider.getMemberIdByAssessToken(request);
        Member member = getMemberById(UUID.fromString(memberUuid));
        return ResponseGetUserForFront.of(MemberDto.memberDomainToDto(member.resolveMemberByLoginType(), member));
    }

    @LoggingStopWatch
    @Override
    public Page<MemberProjectDTO> fetchSimplePagedMembers(int page, int size, UUID projectId) {
        Pageable pageable = getPageable(Sort.by(SORT_PROPERTIES).descending(), page, size);
        Page<ProjectMember> projectMembers = projectMemberRepository.findProjectMemberPageByProjectId(projectId,
                pageable);
        return projectMembers.map(
                projectMember -> new MemberProjectDTO(projectMember.getMember().resolveMemberByLoginType(),
                        hasAnyIssueInProgress(projectMember.getMember(), projectId), projectMember.getPermission()));
    }

    @LoggingStopWatch
    @Override
    public ResponsePageResult<GetMemberListResponse, Member> fetchPagedMembers(int page, int size, UUID projectId) {
        Pageable pageable = getPageable(Sort.by(SORT_PROPERTIES).descending(), page, size);
        List<Member> members = memberRepository.findMembersByProject(projectId);
        Page<Member> memberPage = createPage(members, pageable);
        Function<Member, GetMemberListResponse> fn = (en -> GetMemberListResponse.of(en,
                hasAnyIssueInProgress(en, projectId)));
        return new ResponsePageResult<>(memberPage, fn);
    }

    private Page<Member> createPage(List<Member> members, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), members.size());
        return new PageImpl<>(members.subList(start, end), pageable, members.size());
    }

    private boolean hasAnyIssueInProgress(Member member, UUID projectId) {
        return Optional.ofNullable(member.getAssigneeIssueList())
                .map(issues -> issues.stream()
                        .filter(issue -> getProjectIdByIssue(issue).equals(projectId))
                        .anyMatch(Issue::hasIssueProgress))
                .orElse(false);
    }

    private UUID getProjectIdByIssue(final Issue issue) {
        Project project = issue.getTask().getProject();
        return project.getProjectId();
    }

    @LoggingStopWatch
    @Override
    public AllMember fetchMembersForDeveloper() {
        List<MemberDTO> memberDTOS = memberRepository.findAll()
                .parallelStream()
                .map(Member::resolveMemberByLoginType)
                .map(MemberDTO::create).toList();
        return AllMember.create(memberDTOS);
    }

    @LoggingStopWatch
    @Override
    public List<MemberProjectDTO> fetchMembers(final UUID projectId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findByProjectId(projectId);
        Function<ProjectMember, MemberProjectDTO> fn = (MemberServiceImpl::getMemberProjectDTO);
        return projectMembers.stream().map(fn).collect(Collectors.toList());
    }

    private static MemberProjectDTO getMemberProjectDTO(final ProjectMember en) {
        return new MemberProjectDTO(en.getMember().resolveMemberByLoginType(),
                en.getMember().hasAssigneeIssueInProgress(), en.getPermission());
    }

    private Pageable getPageable(Sort sort, int page, int size) {
        return PageRequest.of(page - 1, size, sort);
    }

    @LoggingStopWatch
    @Override
    public ResponseToken getAuthentication(Member member) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(),
                member.getLoginType());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.createAccessToken(authentication, member.getLoginType());
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        saveRefreshToken(member.getId(), refreshToken);

        return ResponseToken.of(ACCESS_TOKEN_STARTS + accessToken, refreshToken);
    }

    public static HttpHeaders setCookieAndHeader(final ResponseToken responseToken) {
        HttpHeaders headers = new HttpHeaders();
        HttpHeaderUtil.setAccessToken(headers, responseToken.accessToken());
        CookieUtil.setRefreshCookie(headers, responseToken.refreshToken());
        return headers;
    }

    public static HttpHeaders setCookieAndHeader(final ResponseReIssueToken reIssueToken) {
        HttpHeaders headers = new HttpHeaders();
        HttpHeaderUtil.setAccessToken(headers, reIssueToken.accessToken());
        CookieUtil.setRefreshCookie(headers, reIssueToken.refreshToken());
        return headers;
    }

    private Member getMemberById(UUID uuid) {
        return memberRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
    }

    private User getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_EMAIL_NOT_FOUND, USER_EMAIL_NOT_FOUND.getMessage()));
    }

    private RefreshToken getCurrentRefreshToken(final String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(REFRESH_TOKEN_RESOLVE,
                        REFRESH_TOKEN_RESOLVE.getMessage()));
    }

    private static void validateMatchedToken(final String refreshToken, final RefreshToken currentRefreshToken) {
        if (!refreshToken.equals(currentRefreshToken.getRefreshToken())) {
            throw new RefreshTokenNotMatchedException(REFRESH_TOKEN_RESOLVE, REFRESH_TOKEN_RESOLVE.getMessage());
        }
    }

    private String resolveToken(String accessToken) {
        if (accessToken.startsWith(ACCESS_TOKEN_STARTS)) {
            return accessToken.substring(BEGIN_INDEX);
        }
        throw new CannotResolveToken(REFRESH_TOKEN_RESOLVE, REFRESH_TOKEN_RESOLVE.getMessage());
    }

    private void saveRefreshToken(final UUID uuid, final String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(uuid, refreshToken));
    }

    private List<ProjectMember> getProjectsByMember(final Member member) {
        return projectMemberRepository.findProjectMemberByMember(member);
    }

    private Page<Member> getMembersByProject(UUID projectId, Pageable pageable) {
        return memberRepository.findMembersByProject(projectId, pageable);
    }
}
