package com.practice.employee.domain.command;

import java.time.LocalDate;
import java.util.List;

public record EmployeeCreateCommand(
  List<EmployeeCreateInfo> employeeCreateInfoList,
  String createdBy
) {
  public record EmployeeCreateInfo(
    String name,
    String email,
    String tel,
    LocalDate joined
  ) {
  }
}
