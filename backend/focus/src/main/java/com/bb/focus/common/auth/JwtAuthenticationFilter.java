package com.bb.focus.common.auth;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bb.focus.api.service.ApplicantService;
import com.bb.focus.api.service.CompanyAdminService;
import com.bb.focus.api.service.EvaluatorService;
import com.bb.focus.api.service.ServiceAdminService;
import com.bb.focus.common.util.JwtTokenUtil;
import com.bb.focus.common.util.ResponseBodyWriteUtil;
import com.bb.focus.db.entity.admin.ServiceAdmin;
import com.bb.focus.db.entity.applicant.Applicant;
import com.bb.focus.db.entity.company.CompanyAdmin;
import com.bb.focus.db.entity.evaluator.Evaluator;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

/**
 * 요청 헤더에 jwt 토큰이 있는 경우, 토큰 검증 및 인증 처리 로직 정의.
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  private CompanyAdminService companyAdminService;
  private ServiceAdminService serviceAdminService;
  private ApplicantService applicantService;
  private EvaluatorService evaluatorService;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
      CompanyAdminService companyAdminService, ServiceAdminService serviceAdminService,
      ApplicantService applicantService, EvaluatorService evaluatorService) {
    super(authenticationManager);
    this.companyAdminService = companyAdminService;
    this.serviceAdminService = serviceAdminService;
    this.applicantService = applicantService;
    this.evaluatorService = evaluatorService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    // Read the Authorization header, where the JWT Token should be
    String header = request.getHeader(JwtTokenUtil.HEADER_STRING);
    System.out.println("request : " + request);
    System.out.println("header : " + header);
    // If header does not contain BEARER or is null delegate to Spring impl and exit
    if (header == null || !header.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
      System.out.println("herE???? - jwtauthenticationfilter");
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // If header is present, try grab user principal from database and perform authorization
      Authentication authentication = getAuthentication(request);
      // jwt 토큰으로 부터 획득한 인증 정보(authentication) 설정.
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception ex) {
      ResponseBodyWriteUtil.sendError(request, response, ex);
      return;
    }

    filterChain.doFilter(request, response);
  }

  @Transactional(readOnly = true)
  public Authentication getAuthentication(HttpServletRequest request) throws Exception {
    String token = request.getHeader(JwtTokenUtil.HEADER_STRING);
    System.out.println("step1 token : " + token);
    // 요청 헤더에 Authorization 키값에 jwt 토큰이 포함된 경우에만, 토큰 검증 및 인증 처리 로직 실행.
    if (token != null) {
      // parse the token and validate it (decode)
      System.out.println("step2");
      JWTVerifier verifier = JwtTokenUtil.getVerifier();
      JwtTokenUtil.handleError(token);
      DecodedJWT decodedJWT = verifier.verify(token.replace(JwtTokenUtil.TOKEN_PREFIX, ""));
      String userId = decodedJWT.getSubject();
      System.out.println("step3 userId : " + userId);

      // Search in the DB if we find the user by token subject (username)
      // If so, then grab user details and create spring auth token using username, pass, authorities/roles
      if (userId != null) {
        // jwt 토큰에 포함된 계정 정보(userId) 통해 실제 디비에 해당 정보의 계정이 있는지 조회.
        System.out.println("step4");
        CompanyAdmin companyAdmin = companyAdminService.getCompanyAdminByUserId(userId);
        if (companyAdmin != null) {
          // 식별된 정상 유저인 경우, 요청 context 내에서 참조 가능한 인증 정보(jwtAuthentication) 생성.
          System.out.println("step5");
          User user = new User(companyAdmin.getUserId(), companyAdmin.getPwd(),
              companyAdmin.getUserRole());
          FocusUserDetails userDetails = new FocusUserDetails(user);
          UsernamePasswordAuthenticationToken jwtAuthentication = new UsernamePasswordAuthenticationToken(
              userId,
              null, userDetails.getAuthorities());
          jwtAuthentication.setDetails(userDetails);
          return jwtAuthentication;
        } else {
          ServiceAdmin serviceAdmin = serviceAdminService.getServiceAdminByUserId(userId);
          if (serviceAdmin != null) {
            System.out.println("step6 serviceAdmin : " + serviceAdmin.toString());
            User user = new User(serviceAdmin.getUserId(), serviceAdmin.getPwd(),
                serviceAdmin.getUserRole());
            FocusUserDetails userDetails = new FocusUserDetails(user);
            UsernamePasswordAuthenticationToken jwtAuthentication = new UsernamePasswordAuthenticationToken(
                userId,
                null, userDetails.getAuthorities());
            jwtAuthentication.setDetails(userDetails);
            return jwtAuthentication;
          } else {
            Applicant applicant = applicantService.getApplicantByUserId(userId);
            if (applicant != null) {
              System.out.println("step7");
              User user = new User(applicant.getUserId(), applicant.getPwd(),
                  applicant.getUserRole());
              FocusUserDetails userDetails = new FocusUserDetails(user);
              UsernamePasswordAuthenticationToken jwtAuthentication = new UsernamePasswordAuthenticationToken(
                  userId,
                  null, userDetails.getAuthorities());
              jwtAuthentication.setDetails(userDetails);
              return jwtAuthentication;
            } else {
              Evaluator evaluator = evaluatorService.getEvaluatorByUserId(userId);
              if (evaluator != null) {
                User user = new User(evaluator.getUserId(), evaluator.getPwd(),
                    evaluator.getUserRole());
                FocusUserDetails userDetails = new FocusUserDetails(user);
                UsernamePasswordAuthenticationToken jwtAuthentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null, userDetails.getAuthorities());
                jwtAuthentication.setDetails(userDetails);
                return jwtAuthentication;
              }

              System.out.println("step8");
            }
          }
        }
      }
      System.out.println("step6");
      return null;
    }
    return null;
  }
}
