package com.bb.focus.db.repository;

import com.bb.focus.db.entity.applicant.Applicant;
import com.bb.focus.db.entity.applicant.QApplicant;
import com.bb.focus.db.entity.company.QCompanyAdmin;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplicantCustomRepositoryImpl implements ApplicantCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  QCompanyAdmin qCompanyAdmin = QCompanyAdmin.companyAdmin;
  QApplicant qApplicant = QApplicant.applicant;
//  private final ApplicantRepository applicantRepository;

  public List<Applicant> findAllApplicantsByCompanyAdminId(Long companyAdminId) {
    return jpaQueryFactory
        .selectFrom(qApplicant)
        .join(qApplicant.companyAdmin, qCompanyAdmin)
        .where(eqCompanyAdminId(companyAdminId))
        .fetch();
  }

  @Override
  public Applicant findApplicantByUserId(String userId) {
    Applicant applicant = jpaQueryFactory.select(qApplicant).from(qApplicant)
        .where(qApplicant.userId.eq(userId)).fetchOne();
    return applicant;
  }

  private BooleanExpression eqCompanyAdminId(Long companyAdminId) {
    if (companyAdminId.equals(null)) {
      return null;
    }
    return qCompanyAdmin.id.eq(companyAdminId);
  }
}
