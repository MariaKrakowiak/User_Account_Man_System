package com.api.system.repository;

import com.api.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

    Optional<User> findById(int id);

    void deleteById(int id);

    List<User> findAll();
}
