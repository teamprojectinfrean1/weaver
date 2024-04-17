package com.task.weaver.domain.authorization.service.impl;

import static com.task.weaver.common.exception.ErrorCode.NO_MATCHED_VERIFICATION_CODE;
import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.member.UnableSendMailException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRepository;
import com.task.weaver.common.redis.service.RedisService;
import com.task.weaver.common.util.CookieUtil;
import com.task.weaver.common.util.HttpHeaderUtil;
import com.task.weaver.domain.authorization.dto.MemberProjectDTO;
import com.task.weaver.domain.authorization.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.dto.response.ResponseUserOauth.AllMember;
import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.authorization.repository.MemberRepository;
import com.task.weaver.domain.authorization.service.MemberService;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.domain.member.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.member.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.member.user.dto.response.ResponseUuid;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import com.task.weaver.domain.member.util.MemberConverter;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final RedisService redisService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberConverter memberConverter;

	public ResponseToken reissue(String refreshToken, String loginType) {

		jwtTokenProvider.validateToken(refreshToken);

		Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
		log.info("authentication.getName() : " + authentication.getName());

		RefreshToken currentRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new RuntimeException(""));

		log.info("resolvedToken : " + refreshToken);
		log.info("findrefreshtoken : " + currentRefreshToken.getRefreshToken());
		if(!refreshToken.equals(currentRefreshToken.getRefreshToken()))
			throw new IllegalArgumentException("");

		String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
		String newAccessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.fromName(loginType));

		redisService.deleteRefreshToken(currentRefreshToken);
		refreshTokenRepository.save(new RefreshToken(currentRefreshToken.getId(), newRefreshToken));

		return ResponseToken.builder()
			.accessToken("Bearer "+ newAccessToken)
			.refreshToken(newRefreshToken)
			.build();
	}

	/**
	 * token 앞 "Bearer-" 제거
	 * @param accessToken
	 * @return
	 */
	private String resolveToken(String accessToken) {
		if(accessToken.startsWith("Bearer "))
			return accessToken.substring(7);
		// 예외 처리 추후 수정
		throw new RuntimeException("not valid refresh token");
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

	@Override
	public ResponseUuid getUuid(final String email, final Boolean checked) {
		if (!checked) {
			throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, ": Redis to SMTP DATA");
		}

		User findUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

		return ResponseUuid.builder()
				.uuid(findUser.getUserId())
				.build();
	}

	@Override
	public ResponseGetMember getMember(UUID uuid) {
		Member member = memberRepository.findById(uuid)
				.orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));
		UserOauthMember userOauthMemberData = member.resolveMemberByLoginType();
		return memberConverter.convert(userOauthMemberData, ResponseGetMember.class);
	}

	@Override
	public ResponseUserIdNickname getMember(String email, Boolean checked) {
		if (!checked) {
			throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, ": Redis to SMTP DATA");
		}

		User findUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

		return ResponseUserIdNickname.builder()
				.id(findUser.getId())
				.nickname(findUser.getNickname())
				.build();
	}

	@Override
	public ResponseGetUserForFront getMemberFromToken(HttpServletRequest request) {
		String memberUuid = jwtTokenProvider.getMemberIdByAssessToken(request);
		log.info("member uuid : " + memberUuid);
		Member member = memberRepository.findById(UUID.fromString(memberUuid))
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
		return memberConverter.convert(member.resolveMemberByLoginType(), ResponseGetUserForFront.class);
	}

	@Override
	public ResponsePageResult<MemberProjectDTO, Object[]> getMembers(RequestGetUserPage requestGetUserPage) throws BusinessException {
		UUID projectId = requestGetUserPage.getProjectId();

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

		Pageable pageable = requestGetUserPage.getPageable(Sort.by("userId").descending());

		Function<Object[], MemberProjectDTO> fn = (en -> entityToDTO((Member) en[0], (UserOauthMember) en[1]));
		Page<Object[]> members = memberRepository.findMembersByProject(projectId,
				requestGetUserPage.getNickname(), pageable);

		return new ResponsePageResult<>(members, fn);
	}

	@Override
	public AllMember getMembersForTest() {
		List<Member> userOauthMembers = memberRepository.findAll();
		List<User> users = userOauthMembers.stream().map(Member::getUser).toList();
		List<OauthUser> members = userOauthMembers.stream().map(Member::getOauthMember).toList();
		return AllMember.create(users, members);
	}

	@Override
	public ResponseToken getAuthentication(Member member) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(),
				member.getLoginType());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		log.info("Authentication Object = {}", SecurityContextHolder.getContext());
		String accessToken = jwtTokenProvider.createAccessToken(authentication, member.getLoginType());
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
		saveRefreshToken(member.getId(), refreshToken);

		return ResponseToken.builder()
				.accessToken("Bearer " + accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	private void saveRefreshToken(final UUID uuid, final String refreshToken) {
		refreshTokenRepository.save(new RefreshToken(uuid, refreshToken));
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
}
