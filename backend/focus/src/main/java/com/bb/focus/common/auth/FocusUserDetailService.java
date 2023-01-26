package com.bb.focus.common.auth;

import com.bb.focus.api.service.CompanyAdminService;
import com.bb.focus.api.service.ServiceAdminService;
import com.bb.focus.db.entity.admin.ServiceAdmin;
import com.bb.focus.db.entity.company.CompanyAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * 현재 액세스 토큰으로 부터 인증된 유저의 상세정보(활성화 여부, 만료, 롤 등) 관련 서비스 정의.
 */
@Component
public class FocusUserDetailService implements UserDetailsService {

  @Autowired
  CompanyAdminService companyAdminService;

  @Autowired
  ServiceAdminService serviceAdminService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    CompanyAdmin companyAdmin = companyAdminService.getCompanyAdminByUserId(username);
    if (companyAdmin == null) {
      ServiceAdmin serviceAdmin = serviceAdminService.getServiceAdminByUserId(username);
      if (serviceAdmin != null) {
        User user = new User(serviceAdmin.getUserId(), serviceAdmin.getPwd(),
            serviceAdmin.getUserRole());
        FocusUserDetails userDetails = new FocusUserDetails(user);
        return userDetails;
      }
    } else {
      User user = new User(companyAdmin.getUserId(), companyAdmin.getPwd(),
          companyAdmin.getUserRole());
      FocusUserDetails userDetails = new FocusUserDetails(user);
      return userDetails;
    }
    return null;
  }
}
