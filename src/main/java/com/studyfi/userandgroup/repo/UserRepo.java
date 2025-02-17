package com.studyfi.userandgroup.repo;

import com.studyfi.userandgroup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo  extends JpaRepository<User, Integer> {
}
