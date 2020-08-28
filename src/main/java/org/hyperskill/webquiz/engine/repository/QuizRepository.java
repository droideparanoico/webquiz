package org.hyperskill.webquiz.engine.repository;

import org.hyperskill.webquiz.engine.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}