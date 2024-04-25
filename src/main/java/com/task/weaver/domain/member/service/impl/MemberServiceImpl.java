package com.task.weaver.domain.member.service.impl;

import static com.task.weaver.common.exception.ErrorCode.NO_MATCHED_VERIFICATION_CODE;
import static com.task.weaver.common.exception.ErrorCode.REFRESH_TOKEN_RESOLVE;
import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.aop.annotation.Logger;
import com.task.weaver.common.aop.annotation.LoggingStopWatch;
import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.jwt.CannotResolveToken;
import com.task.weaver.common.exception.member.UnableSendMailException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRepository;
import com.task.weaver.common.redis.service.RedisService;
import com.task.weaver.common.util.CookieUtil;
import com.task.weaver.common.util.HttpHeaderUtil;
import com.task.weaver.domain.member.dto.MemberProjectDTO;
import com.task.weaver.domain.member.dto.request.MemberDto;
import com.task.weaver.domain.member.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.member.dto.response.ResponseUserOauth.AllMember;
import com.task.weaver.domain.member.dto.response.ResponseUserOauth.MemberDTO;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.member.service.MemberService;
import com.task.weaver.domain.project.repository.ProjectRepository;
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
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	private final ProjectMemberRepository projectMemberRepository;
	private final RedisService redisService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public ResponseToken reissue(String refreshToken, String loginType) {

		log.info("Current Refresh Token = {}", refreshToken);
		jwtTokenProvider.validateToken(refreshToken);
		Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
		RefreshToken currentRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new RuntimeException(""));

		validateMatchedToken(refreshToken, currentRefreshToken);

		String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
		String newAccessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.fromName(loginType));

		log.info("new Refresh Token = {}", newRefreshToken);
		redisService.deleteRefreshToken(currentRefreshToken);
		refreshTokenRepository.save(new RefreshToken(currentRefreshToken.getId(), newRefreshToken));

		return ResponseToken.builder()
				.accessToken("Bearer " + newAccessToken)
				.refreshToken(newRefreshToken)
				.build();
	}

	private static void validateMatchedToken(final String refreshToken, final RefreshToken currentRefreshToken) {
		if (!refreshToken.equals(currentRefreshToken.getRefreshToken())) {
			throw new IllegalArgumentException("");
		}
	}

	/**
	 * token 앞 "Bearer-" 제거
	 *
	 * @param accessToken
	 * @return
	 */
	private String resolveToken(String accessToken) {
		if (accessToken.startsWith("Bearer ")) {
			return accessToken.substring(7);
		}
		throw new CannotResolveToken(REFRESH_TOKEN_RESOLVE, REFRESH_TOKEN_RESOLVE.getMessage());
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
			throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, NO_MATCHED_VERIFICATION_CODE.getMessage());
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
		return ResponseGetMember.of(MemberDto.memberDomainToDto(userOauthMemberData, member));
	}

	@Override
	public ResponseUserIdNickname getMember(String email, Boolean checked) {
		if (!checked) {
			throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, ": Redis to SMTP DATA");
		}
		User findUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));
		return ResponseUserIdNickname.userToDto(findUser);
	}

	@Override
	public ResponseGetUserForFront getMemberFromToken(HttpServletRequest request) {
		String memberUuid = jwtTokenProvider.getMemberIdByAssessToken(request);
		Member member = memberRepository.findById(UUID.fromString(memberUuid))
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
		return ResponseGetUserForFront.of(MemberDto.memberDomainToDto(member.resolveMemberByLoginType(), member));
	}

	@LoggingStopWatch
	@Override
	public Page<MemberProjectDTO> getMembers(int page, int size, UUID projectId) throws BusinessException {
		Pageable pageable = getPageable(Sort.by("id").descending(), page, size);
		Page<Member> memberPage = getMembersByProject(projectId, pageable);
		return memberPage.map(member -> new MemberProjectDTO(member.resolveMemberByLoginType(),
														     member.hasAssigneeIssueInProgress(),
															 member.hasModifierIssueInProgress()));
	}

	private Page<Member> getMembersByProject(UUID projectId, Pageable pageable) {
		return memberRepository.findMembersByProject(projectId, pageable);
	}

	private Pageable getPageable(Sort sort, int page, int size) {
		return PageRequest.of(page - 1, size, sort);
	}

	@Logger
	@Override
	public AllMember getMembersForTest() {
		List<UserOauthMember> members = memberRepository.findAll()
				.stream().map(Member::resolveMemberByLoginType).toList();
		List<MemberDTO> memberDTOS = members.stream()
				.map(MemberDTO::create).toList();
		return AllMember.create(memberDTOS);
	}

	@Override
	public ResponseToken getAuthentication(Member member) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(),
				member.getLoginType());

		SecurityContextHolder.getContext().setAuthentication(authentication);

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
