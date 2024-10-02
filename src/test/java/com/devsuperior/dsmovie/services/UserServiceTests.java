package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private CustomUserUtil userUtil;

	private String validEmail, invalidEmail, nonExistingEmail;
	private UserEntity user;
	private List<UserDetailsProjection> userDetailsList;

	@BeforeEach
	public void setUp() throws Exception {
		validEmail = "maria@gmail.com";
		invalidEmail = "invalidEmail";
		nonExistingEmail = "nonExistingEmail";

		user = UserFactory.createUserEntity();
		userDetailsList = UserDetailsFactory.createCustomClientUser(validEmail);

		Mockito.when(repository.searchUserAndRolesByUsername(validEmail)).thenReturn(userDetailsList);
		Mockito.when(repository.searchUserAndRolesByUsername(invalidEmail))
				.thenReturn(new ArrayList<>());
		Mockito.when(repository.findByUsername(validEmail)).thenReturn(Optional.of(user));
	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(validEmail);
		UserDetails user = service.authenticated();
		Assertions.assertEquals(userDetailsList.get(0).getUsername(), user.getUsername());
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(invalidEmail);
		Assertions.assertThrows(UsernameNotFoundException.class, () -> service.authenticated());
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails user = service.loadUserByUsername(validEmail);
		Assertions.assertEquals(userDetailsList.get(0).getUsername(), user.getUsername());
		Assertions.assertEquals(userDetailsList.get(0).getPassword(), user.getPassword());
		Assertions.assertEquals(userDetailsList.get(0).getAuthority(), user.getAuthorities().iterator().next().getAuthority());
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(invalidEmail));
	}
}
