package com.practice.employee.web.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.employee.domain.EmployeeDomain;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmployeeInfoResponseTest {
  @DisplayName("직원 정보 도메인으로 response 생성")
  @Test
  void toResponse() {
    var domain = new EmployeeDomain(
      2L,
      "나얼",
      "naul.yu@singer.com",
      "010-2345-2345",
      LocalDate.of(
        2001,
        6,
        1
      )
    );

    var response = EmployeeInfoResponse.toResponse(domain);

    assertThat(response).usingRecursiveComparison()
      .isEqualTo(domain);
  }
}