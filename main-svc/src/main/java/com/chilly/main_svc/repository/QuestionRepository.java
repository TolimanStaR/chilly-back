package com.chilly.main_svc.repository;

import com.chilly.main_svc.model.Question;
import com.chilly.main_svc.model.QuizType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuizType(QuizType quizType);

    long deleteAllByQuizType(QuizType quizType);



}
