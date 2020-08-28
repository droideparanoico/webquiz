package org.hyperskill.webquiz.engine.repository;

import org.hyperskill.webquiz.engine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
