package com.api.system.service.impl;

import com.api.system.entity.PageSettings;
import com.api.system.entity.User;
import com.api.system.exception.UserAlreadyExistsException;
import com.api.system.exception.UserNotFoundException;
import com.api.system.repository.UserRepository;
import com.api.system.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public String createUserDetails(User user) throws UserAlreadyExistsException {
        User byName = repository.findByName(user.getName());
        if (byName == null) {
            repository.save(user);
        } else {
            throw new UserAlreadyExistsException("User with name: " + byName.getName() + " already exists.");
        }
        return "Success";
    }


    @Override
    public String updateUserDetails(int id, @Valid @RequestBody User user) throws UserNotFoundException {
        User existingUser = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found."));
        existingUser.setAge(user.getAge());
        existingUser.setGender(user.getGender());
        repository.save(existingUser);
        return "Success";
    }

    @Override
    public String deleteUserDetails(int id) throws UserNotFoundException {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            repository.deleteById(id);
            return "User with id: " + id + " has been deleted successfully.";
        } else {
            throw new UserNotFoundException("User not found with id: " + id + ".");
        }

    }

    @Override
    public User getUserDetails(int id) throws UserNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id: " + id + " does not exist.");
        } else {
            return repository.findById(id).get();
        }
    }

    @Override
    public List<User> getAllUsersDetails() {
        return repository.findAll();
    }


    @Override
    public Page<User> getAllUsersSorted(@NotNull PageSettings pageSetting) {
        new PageSettings();
        Sort userSort = pageSetting.buildSort();
        Pageable userPage = PageRequest.of(pageSetting.getPage(), pageSetting.getElementPerPage(), userSort);

        return repository.findAll(userPage);
    }
}
