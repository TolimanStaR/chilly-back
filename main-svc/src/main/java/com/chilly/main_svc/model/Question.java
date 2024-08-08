package com.chilly.main_svc.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "question")
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @Column(name = "id")
    private Long id;

    @Enumerated
    @Column(name = "quiz_type", nullable = false)
    private QuizType quizType;

    @Column(name = "body")
    private String body;

    @OneToMany(mappedBy = "question", orphanRemoval = true)
    private Set<Answer> answers = new LinkedHashSet<>();

}
