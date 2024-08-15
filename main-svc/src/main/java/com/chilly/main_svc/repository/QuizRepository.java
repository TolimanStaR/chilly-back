package com.chilly.main_svc.repository;

import com.chilly.main_svc.model.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<QuizAnswer, Long> {

}
