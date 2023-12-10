package com.practice.employee.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.employee.domain.EmployeeListDomain;
import java.time.LocalDate;

public record EmployeeListResponse(
  long employeeId,
  String name,
  String email,
  String tel,
  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate joined
) {
  /**
   * 직원 리스트 도메인을 response로 변환
   */
  public static EmployeeListResponse toResponse(EmployeeListDomain domain) {
    return new EmployeeListResponse(domain.employeeId(),
      domain.name(),
      domain.email(),
      domain.tel(),
      domain.joined()
    );
  }
}
