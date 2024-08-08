package com.chilly.main_svc.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity(name = "answer")
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "body")
    private String body;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Answer answer = (Answer) o;
        return id != null && Objects.equals(id, answer.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
