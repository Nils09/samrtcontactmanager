package com.smart.dao;

import com.smart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReposetory extends JpaRepository<User,Integer> {
}
