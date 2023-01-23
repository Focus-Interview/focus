package com.bb.focus.api.service;

import com.bb.focus.api.request.CompanyAdminRegisterPostReq;
import com.bb.focus.db.entity.company.CompanyAdmin;

public interface CompanyAdminService {
    CompanyAdmin createCompanyAdmin(CompanyAdminRegisterPostReq userRegisterInfo);

    CompanyAdmin getCompanyAdminByUserId(String userId);
}
