package com.practice.employee.domain.usecase;

import com.practice.employee.domain.EmployeeDomain;
import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;

public interface EmployeeUseCase {
  /**
   * 직원 정보 리스트 조회
   *
   * @param criteria 직원 조회 criteria
   */
  PageResponse<EmployeeDomain> findEmployees(EmployeeReadCriteria criteria);
}
