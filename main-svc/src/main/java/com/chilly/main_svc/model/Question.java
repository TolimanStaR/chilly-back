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
    @SequenceGenerator(name = "question_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_sequence")
    @Column(name = "id")
    private Long id;

    @Enumerated
    @Column(name = "quiz_type", nullable = false)
    private QuizType quizType;

    @Column(name = "body")
    private String body;

    @Column(name = "index")
    private Integer index;

    @OneToMany(mappedBy = "question", orphanRemoval = true)
    @Builder.Default
    private Set<Answer> answers = new LinkedHashSet<>();

}
