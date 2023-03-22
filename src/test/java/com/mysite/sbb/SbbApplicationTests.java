package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionService questionService;

    @Test
    @DisplayName("질문 데이터 생성 및 저장")
    void testJpa_A() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
    }

    @Test
    @DisplayName("저장된 질문 데이터 개수 확인 및 데이터가 잘 저장되었는지")
    void testJpa_B() {
        List<Question> all = this.questionRepository.findAll();

        assertEquals(2, all.size());

        Question q = all.get(0);

        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("id로 해당 질문 조회")
    void testJpa_C() {
        Optional<Question> oq = this.questionRepository.findById(1);

        if (oq.isPresent()) {
            Question q = oq.get();

            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    @DisplayName("제목으로 해당 질문 조회")
    void testJpa_D() {
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");

        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("제목과 내용으로 해당 질문 조회")
    void testJpa_E() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");

        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("제목에 특정 문자열이 포함된 질문 조회")
    void testJpa_F() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");

        Question q = qList.get(0);

        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("질문 데이터 수정")
    void testJpa_G() {
        Optional<Question> oq = this.questionRepository.findById(1);

        assertTrue(oq.isPresent());

        Question q = oq.get();
        q.setSubject("수정된 제목");
        this.questionRepository.save(q);
    }

    @Test
    @DisplayName("질문 데이터 삭제")
    void testJpa_H() {
        assertEquals(2, this.questionRepository.count());

        Optional<Question> oq = this.questionRepository.findById(1);

        assertTrue(oq.isPresent());

        Question q = oq.get();
        this.questionRepository.delete(q);

        assertEquals(1, this.questionRepository.count());
    }

    @Test
    @DisplayName("답변 데이터 생성 및 저장")
    void testJpa_I() {
        Optional<Question> oq = this.questionRepository.findById(2);

        assertTrue(oq.isPresent());

        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q);
        a.setCreateDate(LocalDateTime.now());

        this.answerRepository.save(a);
    }

    @Test
    @DisplayName("id로 해당 답변 조회")
    void testJpa_J() {
        Optional<Answer> oa = this.answerRepository.findById(1);

        assertTrue(oa.isPresent());

        Answer a = oa.get();

        assertEquals(2, a.getQuestion().getId());
    }

    @Transactional
    @Test
    @DisplayName("질문에 연결된 답변 조회")
    void testJpa_K() {
        Optional<Question> oq = this.questionRepository.findById(2);

        assertTrue(oq.isPresent());

        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

    @Test
    @DisplayName("테스트 데이터 생성")
    void testJpa_L() {
        for(int i=1; i<=300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }
}
