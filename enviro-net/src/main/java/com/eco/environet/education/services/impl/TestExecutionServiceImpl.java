package com.eco.environet.education.services.impl;

import com.eco.environet.education.dto.SubmittedAnswer;
import com.eco.environet.education.dto.TestCompletionRequest;
import com.eco.environet.education.dto.TestCompletionResponse;
import com.eco.environet.education.dto.TestExecutionDto;
import com.eco.environet.education.model.Answer;
import com.eco.environet.education.model.Lecture;
import com.eco.environet.education.model.QuestionType;
import com.eco.environet.education.model.TestExecution;
import com.eco.environet.education.repository.LectureRepository;
import com.eco.environet.education.repository.QuestionRepository;
import com.eco.environet.education.repository.TestExecutionRepository;
import com.eco.environet.education.services.TestExecutionService;
import com.eco.environet.users.repository.UserRepository;
import com.eco.environet.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestExecutionServiceImpl implements TestExecutionService {

    private final TestExecutionRepository testExecutionRepository;
    private final QuestionRepository questionRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    @Override
    public TestExecutionDto findByFinishedAndUserId(Boolean finished, Long userId) {
        return Mapper.map(testExecutionRepository
                .findByFinishedAndUser_Id(finished, userId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Cannot find %s TestExecution with userId: %d", finished ? "finished" : "unfinished", userId))), TestExecutionDto.class);
    }

    @Override
    public List<TestExecutionDto> findAllByFinishedAndLectureId(Boolean finished, Long lectureId) {
        return Mapper.mapList(testExecutionRepository.findAllByFinishedAndLecture_Id(finished, lectureId), TestExecutionDto.class);
    }

    @Override
    public TestExecutionDto findByUserIdAndLectureId(Long userId, Long lectureId) {
        return Mapper.map(testExecutionRepository
                .findByUser_IdAndLecture_Id(userId, lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find execution with userId: " + userId + " and lectureId: " + lectureId)), TestExecutionDto.class);
    }

    @Override
    public TestExecutionDto create(Long lectureId, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Cannot find user with username: " + username));
        var lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new EntityNotFoundException("Cannot find lecture with id: " + lectureId));
//        if (testExecutionRepository.findByFinishedAndUser_Id(false, user.getId()).isPresent()) throw new IllegalArgumentException("User with username: " + username + " already has an active test");
        var existingExecution = testExecutionRepository.findByUser_IdAndLecture_Id(user.getId(), lectureId);
        if (existingExecution.isPresent()) {
            return Mapper.map(existingExecution.get(), TestExecutionDto.class);
        }
        return Mapper.map(testExecutionRepository.save(new TestExecution(user, lecture, 0, false)), TestExecutionDto.class);
    }

    @Override
    public List<TestExecutionDto> findAllByFinished(Boolean finished) {
        return Mapper.mapList(testExecutionRepository.findAllByFinished(finished), TestExecutionDto.class);
    }

    @Override
    public TestCompletionResponse finishTest(TestCompletionRequest testCompletionRequest, String username) {
        var lecture = lectureRepository.findById(testCompletionRequest.getLectureId()).orElseThrow(() -> new EntityNotFoundException("Cannot find lecture with id: " + testCompletionRequest.getLectureId()));
        var user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Cannot find user with username: " + username));
        var testExecution = testExecutionRepository.findByUser_IdAndLecture_Id(user.getId(), lecture.getId()).orElseThrow(() -> new IllegalArgumentException("User with id: " + user.getId() + "did not start test with lectureId: " + lecture.getId()));
        double points = calculatePoints(testCompletionRequest.getAnswers(), lecture);
        if (!testExecution.getFinished()) {
            testExecution.setFinished(true);
            testExecution.setPoints(points);
            testExecutionRepository.save(testExecution);
        }
        return new TestCompletionResponse(lecture.getId(), user.getId(), points);
    }

    private double calculatePoints(Set<SubmittedAnswer> answers, Lecture lecture) {
        double points = 0;
        for (SubmittedAnswer answer : answers) {
            var question = questionRepository.findById(answer.getQuestionId()).orElseThrow(() -> new EntityNotFoundException("Cannot find question with id: " + answer.getQuestionId()));
            if (question.getType() == QuestionType.FILL_IN) {
                var isFillInAnswerCorrect = question
                        .getAnswers()
                        .stream()
                        .findFirst()
                        .orElseThrow()
                        .getContent()
                        .equalsIgnoreCase(answer.getTextAnswer());
                if (!isFillInAnswerCorrect) continue;
            } else {
                var correctAnswerIds = question.getAnswers().stream().filter(Answer::getIsCorrect).map(Answer::getId).collect(Collectors.toSet());
                var isOfferedAnswerCorrect = correctAnswerIds.equals(answer.getAnswerIds());
                if (!isOfferedAnswerCorrect) continue;
            }
            switch (lecture.getDifficulty()) {
                case EASY -> points += 1;
                case MEDIUM -> points += 1.5;
                case HARD -> points += 2;
            }
        }
        return points;
    }
}
