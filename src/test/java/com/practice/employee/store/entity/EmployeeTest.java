package com.practice.employee.store.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class EmployeeTest {
  @DisplayName("엔티티로 직원 정보 도메인 생성")
  @Test
  void toDomain() {
    var entity = new Employee(
      "김길동",
      "kildong.kim@clovf.com",
      "010-5678-5678",
      LocalDate.of(
        2023,
        12,
        1
      ),
      "system"
    );

    ReflectionTestUtils.setField(
      entity,
      "employeeId",
      5L
    );

    var domain = entity.toDomain();

    assertThat(entity).usingRecursiveComparison()
      .ignoringFields(
        "createdBy",
        "createdAt",
        "modifiedBy",
        "modifiedAt"
      )
      .isEqualTo(domain);
  }
}