package com.chilly.main_svc.repository;

import com.chilly.main_svc.containers.ContainersEnvironment;
import com.chilly.main_svc.model.Question;
import com.chilly.main_svc.model.QuizType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class QuestionRepositoryTest extends ContainersEnvironment {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    private Question baseQuestion1;

    @BeforeEach
    void setUp() {
        // Create test questions
        baseQuestion1 = Question.builder()
                .quizType(QuizType.BASE)
                .body("Base question 1?")
                .index(1)
                .build();

        Question baseQuestion2 = Question.builder()
                .quizType(QuizType.BASE)
                .body("Base question 2?")
                .index(2)
                .build();

        Question shortQuestion = Question.builder()
                .quizType(QuizType.SHORT)
                .body("Short question?")
                .index(1)
                .build();

        // Persist test questions
        entityManager.persist(baseQuestion1);
        entityManager.persist(baseQuestion2);
        entityManager.persist(shortQuestion);
        entityManager.flush();
    }

    @Test
    void findByQuizType_ShouldReturnQuestionsOfSpecifiedType() {
        // When
        List<Question> baseQuestions = questionRepository.findByQuizType(QuizType.BASE);
        List<Question> shortQuestions = questionRepository.findByQuizType(QuizType.SHORT);

        // Then
        assertThat(baseQuestions).hasSize(2);
        assertThat(shortQuestions).hasSize(1);

        assertThat(baseQuestions)
                .extracting(Question::getBody)
                .containsExactlyInAnyOrder("Base question 1?", "Base question 2?");

        assertThat(shortQuestions)
                .extracting(Question::getBody)
                .containsExactly("Short question?");
    }

    @Test
    void findByQuizType_ShouldReturnEmptyListWhenNoQuestionsOfType() {
        // Given
        questionRepository.deleteAll();
        entityManager.flush();

        // Create only SHORT quiz questions
        Question shortQuestion1 = Question.builder()
                .quizType(QuizType.SHORT)
                .body("New short question 1?")
                .index(1)
                .build();

        Question shortQuestion2 = Question.builder()
                .quizType(QuizType.SHORT)
                .body("New short question 2?")
                .index(2)
                .build();

        entityManager.persist(shortQuestion1);
        entityManager.persist(shortQuestion2);
        entityManager.flush();

        // When
        List<Question> baseQuestions = questionRepository.findByQuizType(QuizType.BASE);

        // Then
        assertThat(baseQuestions).isEmpty();
    }

    @Test
    void deleteAllByQuizType_ShouldDeleteQuestionsOfSpecifiedType() {
        // When
        long deletedCount = questionRepository.deleteAllByQuizType(QuizType.BASE);

        // Then
        assertThat(deletedCount).isEqualTo(2);

        List<Question> remainingQuestions = questionRepository.findAll();
        assertThat(remainingQuestions).hasSize(1);
        assertThat(remainingQuestions.get(0).getQuizType()).isEqualTo(QuizType.SHORT);
    }

    @Test
    void deleteAllByQuizType_ShouldReturnZeroWhenNoQuestionsOfTypeExist() {
        // Given
        questionRepository.deleteAll();
        entityManager.flush();

        Question shortQuestion1 = Question.builder()
                .quizType(QuizType.SHORT)
                .body("New short question?")
                .index(1)
                .build();

        entityManager.persist(shortQuestion1);
        entityManager.flush();

        // When
        long deletedCount = questionRepository.deleteAllByQuizType(QuizType.BASE);

        // Then
        assertThat(deletedCount).isEqualTo(0);

        List<Question> remainingQuestions = questionRepository.findAll();
        assertThat(remainingQuestions).hasSize(1);
        assertThat(remainingQuestions.get(0).getQuizType()).isEqualTo(QuizType.SHORT);
    }

    @Test
    void findByQuizType_ShouldWorkAfterDeletingAllQuestions() {
        // Given
        questionRepository.deleteAll();
        entityManager.flush();

        // When
        List<Question> baseQuestions = questionRepository.findByQuizType(QuizType.BASE);
        List<Question> shortQuestions = questionRepository.findByQuizType(QuizType.SHORT);

        // Then
        assertThat(baseQuestions).isEmpty();
        assertThat(shortQuestions).isEmpty();
    }

    @Test
    void repository_ShouldSupportBasicCrudOperations() {
        // Test count
        long count = questionRepository.count();
        assertEquals(3, count);

        // Test findById
        Question found = questionRepository.findById(baseQuestion1.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertEquals("Base question 1?", found.getBody());

        // Test save (update)
        found.setBody("Updated base question 1?");
        questionRepository.save(found);

        Question updated = questionRepository.findById(baseQuestion1.getId()).orElse(null);
        assertThat(updated).isNotNull();
        assertEquals("Updated base question 1?", updated.getBody());

        // Test delete
        questionRepository.delete(baseQuestion1);
        assertThat(questionRepository.findById(baseQuestion1.getId())).isEmpty();
        assertEquals(2, questionRepository.count());
    }
}
