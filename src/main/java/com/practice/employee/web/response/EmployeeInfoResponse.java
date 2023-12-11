package com.practice.employee.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.employee.domain.EmployeeDomain;
import java.time.LocalDate;

public record EmployeeInfoResponse(
  long employeeId,
  String name,
  String email,
  String tel,
  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate joined
) {
  /**
   * 직원 정보 도메인으로 response 생성
   */
  public static EmployeeInfoResponse toResponse(EmployeeDomain domain) {
    return new EmployeeInfoResponse(
      domain.employeeId(),
      domain.name(),
      domain.email(),
      domain.tel(),
      domain.joined()
    );
  }
}
