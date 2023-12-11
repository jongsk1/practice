package com.practice.employee.web.request;

import com.practice.employee.domain.command.EmployeeCreateCommand;
import com.practice.employee.web.utils.DataConverter;
import jakarta.validation.constraints.NotBlank;

public record EmployeeCreateRequest(
  @NotBlank String content
) {
  /**
   * 직원 정보 생성 커맨드로 변환
   */
  public EmployeeCreateCommand toCommand() {
    return DataConverter.makeEmployeeCreateCommandBy(content);
  }
}
