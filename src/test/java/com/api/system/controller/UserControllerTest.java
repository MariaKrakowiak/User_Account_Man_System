package com.api.system.controller;

import com.api.system.entity.User;
import com.api.system.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    static User userOne;
    static User userTwo;
    List<User> userList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        userOne = new User(1, "Anna", "female", 50, new Date());
        userTwo = new User(2, "John", "male", 30, new Date());
        userList.add(userOne);
        userList.add(userTwo);
    }

    @Test
    void shouldFetchOneUserDetailsById() throws Exception {
        when(userService.getUserDetails(1)).thenReturn(userOne);
        this.mockMvc.perform(get("/user/" + 1))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldFetchAllUserDetails() throws Exception {
        when(userService.getAllUsersDetails()).thenReturn(userList);
        this.mockMvc.perform(get("/user/"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldCreateUserDetails() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userOne);

        when(userService.createUserDetails(userOne)).thenReturn("Success");
        this.mockMvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUserDetails() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userOne);

        when(userService.updateUserDetails(1, userOne))
                .thenReturn("User updated successfully.");
        this.mockMvc.perform(patch("/user/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserDetails() throws Exception {
        when(userService.deleteUserDetails(1))
                .thenReturn("User deleted successfully.");
        this.mockMvc.perform(delete("/user/" + 1))
                .andDo(print()).andExpect(status().isOk());

    }
}

