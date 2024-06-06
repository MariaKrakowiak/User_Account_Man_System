package com.api.system.service;


import com.api.system.entity.PageSettings;
import com.api.system.entity.User;
import com.api.system.exception.UserAlreadyExistsException;
import com.api.system.exception.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    String createUserDetails(User user) throws UserAlreadyExistsException;

    String updateUserDetails(int id, User user) throws UserNotFoundException;

    String deleteUserDetails(int id) throws UserNotFoundException;

    User getUserDetails(int id) throws UserNotFoundException;

    List<User> getAllUsersDetails();

    Page<User> getAllUsersSorted(@NotNull PageSettings pageSetting);

}