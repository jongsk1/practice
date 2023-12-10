package com.practice.employee.web.request;

import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import jakarta.validation.constraints.Positive;

public record EmployeeReadRequest(
  @Positive int page,
  @Positive int pageSize
) {
  /**
   * 직원 기본정보 전체 조회 criteria 생성
   */
  public EmployeeReadCriteria toCriteria() {
    return new EmployeeReadCriteria(
      page,
      pageSize
    );
  }
}
