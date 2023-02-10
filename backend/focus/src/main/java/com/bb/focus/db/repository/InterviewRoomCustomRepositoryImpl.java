package com.bb.focus.db.repository;

import com.bb.focus.api.response.ApplicantRes;
import com.bb.focus.api.response.EvaluatorRes;
import com.bb.focus.api.response.InterviewRoomRes;
import com.bb.focus.db.entity.applicant.QApplicant;
import com.bb.focus.db.entity.evaluator.QEvaluator;
import com.bb.focus.db.entity.helper.QApplicantInterviewRoom;
import com.bb.focus.db.entity.helper.QEvaluatorInterviewRoom;
import com.bb.focus.db.entity.interview.QInterviewRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InterviewRoomCustomRepositoryImpl implements InterviewRoomCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  QInterviewRoom qInterviewRoom = QInterviewRoom.interviewRoom;
  QEvaluator qEvaluator = QEvaluator.evaluator;
  QApplicant qApplicant = QApplicant.applicant;
  QEvaluatorInterviewRoom qEvaluatorInterviewRoom = QEvaluatorInterviewRoom.evaluatorInterviewRoom;
  QApplicantInterviewRoom qApplicantInterviewRoom = QApplicantInterviewRoom.applicantInterviewRoom;

  public List<EvaluatorRes> findEvaluatorsByInterviewId(Long interviewRoomId) {

    return jpaQueryFactory
        .select(Projections.constructor(EvaluatorRes.class,
            qEvaluator.id,
            qEvaluator.name,
            qEvaluator.code,
            qEvaluator.department,
            qEvaluator.position,
            qEvaluator.image,
            qEvaluator.userId,
            qEvaluator.tel,
            qEvaluator.email
        ))
        .from(qEvaluator)
        .innerJoin(qEvaluatorInterviewRoom).on(qEvaluatorInterviewRoom.evaluator.eq(qEvaluator))
        .innerJoin(qInterviewRoom).on(qEvaluatorInterviewRoom.interviewRoom.eq(qInterviewRoom))
        .where(eqInterviewRoomId(interviewRoomId))
        .orderBy(qEvaluator.code.asc())
        .fetch();
  }

  public List<ApplicantRes> findApplicantsByInterviewId(Long interviewRoomId) {

    return jpaQueryFactory
        .select(Projections.constructor(ApplicantRes.class,
            qApplicant.id,
            qApplicant.name,
            qApplicant.userId,
            qApplicant.code,
            qApplicant.image,
            qApplicant.tel,
            qApplicant.email
        ))
        .from(qApplicant)
        .innerJoin(qApplicantInterviewRoom).on(qApplicantInterviewRoom.applicant.eq(qApplicant))
        .innerJoin(qInterviewRoom).on(qApplicantInterviewRoom.interviewRoom.eq(qInterviewRoom))
        .where(eqInterviewRoomId(interviewRoomId))
        .orderBy(qApplicant.code.asc())
        .fetch();
  }

  public List<InterviewRoomRes> findInterviewRoomByEvaluatorId(Long evaluatorId) {
    return jpaQueryFactory
        .select(Projections.constructor(InterviewRoomRes.class,
            qInterviewRoom.id,
            qInterviewRoom.name,
            qInterviewRoom.startTime,
            qInterviewRoom.endTime,
            qInterviewRoom.duration,
            qInterviewRoom.date,
            qInterviewRoom.interviewRound,
            qInterviewRoom.processName
        ))
        .from(qInterviewRoom)
        .innerJoin(qEvaluatorInterviewRoom)
        .on(qEvaluatorInterviewRoom.interviewRoom.eq(qInterviewRoom))
        .innerJoin(qEvaluator).on(qEvaluatorInterviewRoom.evaluator.eq(qEvaluator))
        .where(eqEvaluatorId(evaluatorId),qInterviewRoom.startTime.gt(LocalDateTime.now()))
        .orderBy(qInterviewRoom.startTime.asc())
        .fetch();

  }

  @Override
  public List<InterviewRoomRes> findPastInterviewRoomByEvaluatorId(Long evaluatorId) {
    return jpaQueryFactory
        .select(Projections.constructor(InterviewRoomRes.class,
            qInterviewRoom.id,
            qInterviewRoom.name,
            qInterviewRoom.startTime,
            qInterviewRoom.endTime,
            qInterviewRoom.duration,
            qInterviewRoom.date,
            qInterviewRoom.interviewRound,
            qInterviewRoom.processName
        ))
        .from(qInterviewRoom)
        .innerJoin(qEvaluatorInterviewRoom)
        .on(qEvaluatorInterviewRoom.interviewRoom.eq(qInterviewRoom))
        .innerJoin(qEvaluator).on(qEvaluatorInterviewRoom.evaluator.eq(qEvaluator))
        .where(eqEvaluatorId(evaluatorId),qInterviewRoom.startTime.lt(LocalDateTime.now()))
        .orderBy(qInterviewRoom.startTime.asc())
        .fetch();
  }

  private BooleanExpression eqInterviewRoomId(Long interviewRoomId) {
    if (interviewRoomId.equals(null)) {
      return null;
    }
    return qInterviewRoom.id.eq(interviewRoomId);
  }

  private BooleanExpression eqEvaluatorId(Long evaluatorId) {
    if (evaluatorId.equals(null)) {
      return null;
    }
    return qEvaluator.id.eq(evaluatorId);
  }
}