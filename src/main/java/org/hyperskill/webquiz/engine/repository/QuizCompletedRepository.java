package org.hyperskill.webquiz.engine.repository;

import org.hyperskill.webquiz.engine.model.QuizCompleted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizCompletedRepository extends JpaRepository<QuizCompleted, Long> {
    Page<QuizCompleted> findByUserEmail(String email, Pageable pageable);
}