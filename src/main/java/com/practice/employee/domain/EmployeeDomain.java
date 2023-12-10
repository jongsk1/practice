package com.practice.employee.domain;

import java.time.LocalDate;

public record EmployeeDomain(
  long employeeId,
  String name,
  String email,
  String tel,
  LocalDate joined
) {
}
