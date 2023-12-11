package com.practice.employee.domain.command;

import java.time.LocalDate;
import java.util.List;

public record EmployeeCreateCommand(
  List<EmployeeCreateInfo> employeeCreateInfoList,
  String createdBy
) {
  /**
   * 직원 생성 정보 리스트를 파라미터로 받는 생성자
   *
   * @param employeeCreateInfoList 직원 생성 정보 리스트
   */
  public EmployeeCreateCommand(List<EmployeeCreateInfo> employeeCreateInfoList) {
    this(employeeCreateInfoList, "system");
  }

  public record EmployeeCreateInfo(
    String name,
    String email,
    String tel,
    LocalDate joined
  ) {
  }
}
