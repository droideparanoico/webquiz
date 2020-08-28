package org.hyperskill.webquiz.engine.controller;

import org.hyperskill.webquiz.engine.model.Quiz;
import org.hyperskill.webquiz.engine.dto.AnswerDto;
import org.hyperskill.webquiz.engine.dto.FeedbackDto;
import org.hyperskill.webquiz.engine.model.QuizCompleted;
import org.hyperskill.webquiz.engine.service.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @GetMapping
    public ResponseEntity<Page<Quiz>> getQuizzes(@RequestParam(defaultValue = "0", required = false) Integer page) {
        return ResponseEntity.ok(quizService.getAllQuizzes(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuiz(id));
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<FeedbackDto> solveQuiz(@PathVariable(value="id") Long id, @RequestBody AnswerDto answer){
        return ResponseEntity.ok(quizService.solveQuiz(id, answer));
    }

    @GetMapping(path = "/completed")
    public ResponseEntity<Page<QuizCompleted>> getCompletedQuestionForUser(@RequestParam(defaultValue = "0", required = false) Integer page) {
        return ResponseEntity.ok(quizService.getCompletedQuestionForUser(page));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return new ResponseEntity<>("Quiz deleted", HttpStatus.NO_CONTENT);
    }
}