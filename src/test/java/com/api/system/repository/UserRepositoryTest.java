package com.api.system.repository;

import com.api.system.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    User userOne;
    User userTwo;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.flush();
        userOne = new User("Erica", "female", 40);
        userTwo = new User("Eric", "male", 60);
        userRepository.save(userOne);
        userRepository.save(userTwo);
    }


    @Test
    @DisplayName("It should save the user to the database.")
    void save() {
        assertNotNull(userRepository);
        assertThat(userOne.getId()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("It should return the users list with size of 2.")
    void getAllUsers() {
        List<User> list = userRepository.findAll();

        assertNotNull(list);
        assertThat(list).isNotNull();
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("It should return the user by id.")
    void getUserById() {
        User newUser = userRepository.findById(userOne.getId()).get();

        assertNotNull(newUser);
        assertEquals("Erica", newUser.getName());
        assertEquals("female", newUser.getGender());
        assertEquals(40, newUser.getAge());

    }

    @Test
    @DisplayName("It should return user by name.")
    void getUserByName_Found() {
        User user = userRepository.findByName("Erica");
        assertThat(user.getId()).isEqualTo(userOne.getId());
        assertThat(user.getGender())
                .isEqualTo(userOne.getGender());
    }

    @Test
    @DisplayName("It should not return user by name.")
    void getUserByName_NotFound() {
        User u = userRepository.findByName("William");
        assertThat(u).isNull();
    }

    @Test
    @DisplayName("It should delete the existing user.")
    void deleteUserById() {
        int id = userOne.getId();

        userRepository.deleteById(id);

        List<User> list = userRepository.findAll();

        Optional<User> exitingUser = userRepository.findById(id);

        assertEquals(1, list.size());
        assertThat(exitingUser).isEmpty();

    }
}
