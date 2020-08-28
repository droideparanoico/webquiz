package org.hyperskill.webquiz.engine.service;

import org.hyperskill.webquiz.engine.model.Quiz;
import org.hyperskill.webquiz.engine.dto.AnswerDto;
import org.hyperskill.webquiz.engine.dto.FeedbackDto;
import org.hyperskill.webquiz.engine.model.QuizCompleted;
import org.hyperskill.webquiz.engine.repository.QuizCompletedRepository;
import org.hyperskill.webquiz.engine.repository.QuizRepository;
import org.hyperskill.webquiz.engine.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizCompletedRepository quizCompletedRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, QuizCompletedRepository quizCompletedRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.quizCompletedRepository = quizCompletedRepository;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Quiz not found!")
    static class QuizNotFoundException extends RuntimeException {
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You are not the creator of the quiz!")
    static class QuizWrongUserException extends RuntimeException {
    }

    public Quiz createQuiz(Quiz quiz) {
        quiz.setUser(userRepository.findByEmail(getUserMail()));
        return quizRepository.save(quiz);
    }

    public Quiz getQuiz(Long id) {
        return quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);
    }

    public Page<Quiz> getAllQuizzes(Integer page) {
        return quizRepository.findAll(PageRequest.of(page, 10));
    }

    public FeedbackDto solveQuiz(Long id, AnswerDto answer) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);
        if (new HashSet<>(quiz.getAnswer()).equals(new HashSet<>(answer.getAnswer()))) {
            QuizCompleted quizCompleted = new QuizCompleted();
            quizCompleted.setQuestionId(quiz.getId());
            quizCompleted.setCompletedAt(LocalDateTime.now());
            quizCompleted.setUser(userRepository.findByEmail(getUserMail()));
            quizCompletedRepository.save(quizCompleted);
            return new FeedbackDto(true, "Congratulations, you're right!");
        } else {
            return new FeedbackDto(false, "Wrong answer! Please, try again.");
        }
    }

    public Page<QuizCompleted> getCompletedQuestionForUser(Integer page) {
        return quizCompletedRepository.findByUserEmail(getUserMail(), PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "completedAt")));
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);

        if (quiz.getUser().getEmail().equalsIgnoreCase(getUserMail())) {
            quizRepository.deleteById(id);
        } else {
            throw new QuizWrongUserException();
        }
    }

    private String getUserMail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}