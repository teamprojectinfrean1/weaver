package com.task.weaver.domain.user.service.Impl;

import com.task.weaver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	/**
	 * Spring Security 유저 인증 처리 과정 중 유저 객체 만드는 과정
	 * ❕ 보통 UserDetails를 따로 만들어서 사용하지만 UserDetails 인터페이스를 구현한 User라는 클래스를 시큐리티가 제공해주긴함
	 * 그럴려면 Entity를 Member라고 바꿔야함 (User 겹쳐서 사용 못함)
	 * @param id the username identifying the user whose data is required.
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

		return userRepository.findByUserId(id)
			.orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));
	}


	/**
	 * Security 권한 관리
	 * 현재 서비스는 프로젝트마다 권한 관리를 해줘야하기 때문에 패스
	 */
	// private UserDetails createUserDetails(User user) {
	//     List<SimpleGrantedAuthority> grantedAuthorities = user.getRoleList()
	//         .stream()
	//         .map(authority -> new SimpleGrantedAuthority(authority))
	//         .collect(Collectors.toList());
	//
	//     return new User(user.getUserId(),
	//         user.getPassword(),
	//         grantedAuthorities);
	// }
}
