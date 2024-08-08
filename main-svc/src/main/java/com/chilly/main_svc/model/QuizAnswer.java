package com.chilly.main_svc.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "quiz_answer")
@Table(name = "quiz_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswer {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "answer_id")
    private Answer answer;

}
