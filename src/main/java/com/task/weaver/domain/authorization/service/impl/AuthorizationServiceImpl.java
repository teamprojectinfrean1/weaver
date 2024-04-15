package com.task.weaver.domain.authorization.service.impl;

import static com.task.weaver.common.exception.ErrorCode.NO_MATCHED_VERIFICATION_CODE;
import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.member.UnableSendMailException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRepository;
import com.task.weaver.common.redis.service.RedisService;
import com.task.weaver.common.util.CookieUtil;
import com.task.weaver.common.util.HttpHeaderUtil;
import com.task.weaver.domain.authorization.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.dto.response.ResponseUserOauth.AllMember;
import com.task.weaver.domain.authorization.entity.UserOauthMember;
import com.task.weaver.domain.authorization.repository.UserOauthMemberRepository;
import com.task.weaver.domain.authorization.service.AuthorizationService;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserList;
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
public class AuthorizationServiceImpl implements AuthorizationService {

	private final UserOauthMemberRepository userOauthMemberRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final RedisService redisService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberConverter memberConverter;

	public ResponseToken reissue(String refreshToken, String loginType) {

		// refresh token 유효성 검증
		jwtTokenProvider.validateToken(refreshToken);

		Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
		log.info("authentication.getName() : " + authentication.getName());

		RefreshToken currentRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new RuntimeException(""));

		log.info("resolvedToken : " + refreshToken);
		log.info("findrefreshtoken : " + currentRefreshToken.getRefreshToken());
		if(!refreshToken.equals(currentRefreshToken.getRefreshToken()))
			throw new IllegalArgumentException("");

		// accessToken과 refreshToken 모두 재발행
		String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
		String newAccessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.fromName(loginType));

		redisService.deleteRefreshToken(currentRefreshToken);

		// redis 에 새로 발급한 refreshtoken 저장
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
		UserOauthMember member = userOauthMemberRepository.findById(uuid)
				.orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));
		Member memberData = member.resolveMemberByLoginType();
		return memberConverter.convert(memberData);
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
		String userId = jwtTokenProvider.getMemberIdByAssessToken(request);
		log.info("user id : " + userId);
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(userId))));
		return new ResponseGetUserForFront(user);
	}

	@Override
	public ResponsePageResult<ResponseGetUserList, User> getMembers(
			RequestGetUserPage requestGetUserPage) throws BusinessException {
		UUID projectId = requestGetUserPage.getProjectId();

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

		Pageable pageable = requestGetUserPage.getPageable(Sort.by("userId").descending());

		Page<User> users = userRepository.findUsersForProject(projectId, requestGetUserPage.getNickname(), pageable);

		Function<User, ResponseGetUserList> fn = (ResponseGetUserList::new);

		return new ResponsePageResult<>(users, fn);
	}

	@Override
	public AllMember getMembersForTest() {
		List<UserOauthMember> userOauthMembers = userOauthMemberRepository.findAll();
		List<User> users = userOauthMembers
				.stream()
				.map(UserOauthMember::getUser).toList();
		List<OauthMember> members = userOauthMembers
				.stream()
				.map(UserOauthMember::getOauthMember).toList();
		return AllMember.create(users, members);
	}

	@Override
	public ResponseToken getAuthentication(UserOauthMember userOauthMember) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userOauthMember.getId(),
				userOauthMember.getLoginType());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		log.info("Authentication Object = {}", SecurityContextHolder.getContext());
		String accessToken = jwtTokenProvider.createAccessToken(authentication, userOauthMember.getLoginType());
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
		saveRefreshToken(userOauthMember.getId(), refreshToken);

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
