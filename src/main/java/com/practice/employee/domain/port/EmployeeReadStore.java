package com.practice.employee.domain.port;

import com.practice.employee.domain.EmployeeDomain;
import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import java.util.List;

public interface EmployeeReadStore {
  /**
   * 직원 정보 리스트 조회
   *
   * @param criteria 직원 조회 criteria
   */
  PageResponse<EmployeeDomain> findEmployees(EmployeeReadCriteria criteria);

  /**
   * 이름에 해당하는 직원 정보 리스트 조회
   *
   * @param name 직원 이름
   */
  List<EmployeeDomain> findEmployeeByName(String name);
}
