package com.api.system.service;

import com.api.system.entity.User;
import com.api.system.exception.UserAlreadyExistsException;
import com.api.system.exception.UserNotFoundException;
import com.api.system.repository.UserRepository;
import com.api.system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    AutoCloseable autoCloseable;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
        user = new User(111, "Anna", "female", 50, new Date());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("Test for createUserDetails method.")
    public void givenUserDetails_whenCreateUserDetails_thenReturnSuccess() throws UserAlreadyExistsException {
        // given
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());
        given(userRepository.save(user)).willReturn(user);

        // when
        String answer = userService.createUserDetails(user);

        // then
        assertThat(answer).isEqualTo("Success");
    }

    @Test
    @DisplayName("Test for createUserDetails method which throws exception.")
    public void givenExistingName_whenCreateUserDetails_thenThrowsException() {
        // given
        given(userRepository.findByName(user.getName())).willReturn(user);

        // when
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUserDetails(user));

        // then
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Test for updateUserDetails method.")
    public void givenUserObject_whenUpdateUser_thenReturnSuccess() throws UserNotFoundException {

        // given
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));

        // when
        when(userRepository.save(any(User.class))).thenReturn(user);
        user.setGender("f");
        user.setAge(80);
        String s = userService.updateUserDetails(user.getId(), user);

        // then
        Assertions.assertEquals("Success", s);
    }


    @Test
    @DisplayName("Test for deleteUserDetails method.")
    public void givenUserId_whenDeleteUserDetails_thenNothing() throws UserNotFoundException {

        // given
        int id = 111;
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));


        // when
        userService.deleteUserDetails(id);

        // then
        Assertions.assertTrue(userService.getAllUsersDetails().isEmpty());
        verify(userRepository, times(1)).deleteById(id);

    }

    @Test
    @DisplayName("Test for getUserDetails method.")
    public void givenUserId_whenGetUserDetailsById_thenReturnUserDetails() throws UserNotFoundException {
        // given
        given(userRepository.findById(111)).willReturn(Optional.of(user));

        // when
        User u = userService.getUserDetails(user.getId());

        // then
        assertThat(u).isNotNull();
    }


    @Test
    @DisplayName("Test for getAllUsersDetails method.")
    public void givenUsersList_whenGetAllUsersDetails_thenReturnUserDetailsList() {

        // given
        User u = new User(1, "Bob", "male", 35, new Date());
        given(userRepository.findAll()).willReturn(List.of(user, u));

        // when
        List<User> allUsers = userService.getAllUsersDetails();

        // then
        assertThat(allUsers).isNotNull();
        assertThat(allUsers.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Test for getAllUsersDetails method (negative scenario).")
    public void givenUsersList_whenGetAllUsersDetails_thenReturnEmptyUserDetailsList() {

        // given
        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<User> allUsers = userService.getAllUsersDetails();

        // then
        assertThat(allUsers).isEmpty();
    }

    @Test
    @DisplayName("Test for deleteUserDetails method which throws exception.")
    public void givenUserId_whenDeleteUserDetails_thenThrowsException() {

        // given
        int id = 112;

        // when
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUserDetails(id));

        // then
        verify(userRepository, never()).deleteById(any());

    }

    @Test
    @DisplayName("Test for getUseDetails method which throws exception.")
    public void givenUserId_whenGetUserDetails_thenThrowsException() {

        // given
        int id = 112;

        // when
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserDetails(id));

        // then
        verify(userRepository, never()).getById(id);

    }


}
