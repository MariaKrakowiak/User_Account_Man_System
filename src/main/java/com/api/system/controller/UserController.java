package com.api.system.controller;

import com.api.system.entity.PageDTO;
import com.api.system.entity.PageSettings;
import com.api.system.entity.User;
import com.api.system.exception.UserAlreadyExistsException;
import com.api.system.exception.UserNotFoundException;
import com.api.system.mapper.PageToPageDTOMapper;
import com.api.system.response.ResponseHandler;
import com.api.system.service.UserService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    private final PageToPageDTOMapper<User> pageToPageDTOMapper = new PageToPageDTOMapper<>();

    @GetMapping("/{id}")
    @ApiOperation(value = "User id", notes = "Provide user details.",
            response = ResponseEntity.class)
    public ResponseEntity<Object> getUserDetails(@PathVariable("id") int id) throws UserNotFoundException {
        return ResponseHandler.responseBuilder("Requested user details are given here.",
                HttpStatus.OK, service.getUserDetails(id));
    }

    @GetMapping("/")
    public List<User> getAllUserDetails() {
        return service.getAllUsersDetails();
    }

    @PostMapping("/")
    public String createUserDetails(@RequestBody @Valid User user) throws UserAlreadyExistsException {
        service.createUserDetails(user);
        return "User created successfully.";
    }

    @PatchMapping("/{id}")
    public String updateUserDetails(@PathVariable("id") int id, @RequestBody @Valid User user) throws UserNotFoundException {
        service.updateUserDetails(id, user);
        return "User updated successfully.";
    }

    @DeleteMapping("/{id}")
    public String deleteUserDetails(@PathVariable("id") int id) throws UserNotFoundException {
        service.deleteUserDetails(id);
        return "User deleted successfully.";
    }

    @GetMapping("/page")
    public PageDTO<User> getAllUsersSorted(PageSettings pageSettings) {
        log.info("Request for user page received with data: {}", pageSettings);

        return pageToPageDTOMapper.pageToPageDTO(service.getAllUsersSorted(pageSettings));
    }
}
