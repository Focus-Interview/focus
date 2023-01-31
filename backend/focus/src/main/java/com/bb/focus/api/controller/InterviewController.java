package com.bb.focus.api.controller;

import com.bb.focus.api.request.InterviewReq;
import com.bb.focus.api.service.InterviewService;
import com.bb.focus.common.model.response.BaseResponseBody;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interview/round")
public class InterviewController {

  private final InterviewService interviewService;

  @ApiOperation(value = "면접(N차) 생성", notes = "프로세스에 n차 면접을 생성한다. 평가지 매핑 포함")
  @PostMapping("/{process-id}")
  public ResponseEntity<?> createInterview(
      @ApiIgnore Authentication authentication,
      @PathVariable("process-id") Long processId,
      @RequestBody @ApiParam(value = "면접 생성 정보", required = true) List<InterviewReq> interviewReq) {

    for(InterviewReq interview : interviewReq){
      interviewService.createInterview(processId, interview);
    }
    return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
  }

}
