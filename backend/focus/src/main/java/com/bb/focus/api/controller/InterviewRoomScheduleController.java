package com.bb.focus.api.controller;

import com.bb.focus.api.request.InterviewRoomReq;
import com.bb.focus.api.service.EvaluationService;
import com.bb.focus.api.service.InterviewRoomService;
import com.bb.focus.common.model.response.BaseResponseBody;
import com.bb.focus.db.repository.InterviewRoomRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Path;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "면접실 : 면접 일정 API", tags = {"InterviewRoom Schedule"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/interview/schedule")
public class InterviewRoomScheduleController {

  private final InterviewRoomService interviewRoomService;
  private final EvaluationService evaluationService;

  @ApiOperation(value = "면접 일정 생성")
  @PostMapping("")
  public ResponseEntity<?> createInterviewRoom(
      @ApiIgnore Authentication authentication,
      @RequestBody @ApiParam(value = "면접 일정 생성 정보", required = true) InterviewRoomReq interviewRoomReq) {

    Long interviewRoomId = interviewRoomService.createInterviewRoom(interviewRoomReq);

    Long interviewId = interviewRoomReq.getInterviewId();
    Long[] evaluatorIds = interviewRoomReq.getEvaluators();
    Long[] applicantIds = interviewRoomReq.getApplicants();

    //지원자-평가자 테이블에 데이터 넣기
    for(int e = 0, elen = evaluatorIds.length; e < elen; e++) {
      for (int a = 0, alen = applicantIds.length; a < alen; a++) {
        evaluationService.createApplicantEvaluator(interviewId, interviewRoomId, evaluatorIds[e], applicantIds[a]);
      }
    }

    return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
  }



}
