package com.practice.employee.store.adapter.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.employee.IntegrationTest;
import com.practice.employee.domain.command.EmployeeCreateCommand;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.store.adapter.EmployeeReadJpa;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class EmployeeReadJpaIT extends IntegrationTest {
  @Autowired
  private EmployeeReadJpa employeeReadJpa;

  @DisplayName("직원 정보 리스트 조회 store 통합 테스트")
  @Test
  void findEmployees() {
    var criteria = new EmployeeReadCriteria(
      1,
      2
    );

    var result = employeeReadJpa.findEmployees(criteria);

    var pageInfo = result.getPageInfo();

    assertThat(pageInfo.getPage()).isEqualTo(criteria.page());
    assertThat(pageInfo.getPageSize()).isEqualTo(criteria.pageSize());

    assertThat(result.getData()).isNotEmpty();
    assertThat(result.getData()
      .size()).isEqualTo(criteria.pageSize());
  }

  @DisplayName("이름에 해당하는 직원 정보 조회 store 통합 테스트")
  @Test
  void findEmployeeByName() {
    var name = "박효신";

    var result = employeeReadJpa.findEmployeeByName(name);

    assertThat(result).isNotEmpty();
    assertThat(result.size()).isOne();
    assertThat(result.get(0)
      .name()).isEqualTo(name);
  }

  @Transactional
  @DisplayName("직원 생성 커맨드로 직원 정보 생성 store 통합 테스트")
  @Test
  void createEmployee() {
    var employeeCreateInfo1 = new EmployeeCreateCommand.EmployeeCreateInfo(
      "김길동",
      "kildong.kim@clovf.com",
      "010-5678-5678",
      LocalDate.of(
        2023,
        12,
        1
      )
    );
    var employeeCreateInfo2 = new EmployeeCreateCommand.EmployeeCreateInfo(
      "이길동",
      "kildong.lee@clovf.com",
      "010-6789-6789",
      LocalDate.of(
        2023,
        11,
        1
      )
    );
    var command = new EmployeeCreateCommand(
      List.of(
        employeeCreateInfo1,
        employeeCreateInfo2
      ),
      "system"
    );

    employeeReadJpa.createEmployee(command);

    var created1 = employeeReadJpa.findEmployeeByName(employeeCreateInfo1.name());

    assertThat(created1).isNotEmpty();
    assertThat(created1.size()).isOne();
    assertThat(created1.get(0)).usingRecursiveComparison()
      .ignoringFields("employeeId")
      .isEqualTo(employeeCreateInfo1);

    var created2 = employeeReadJpa.findEmployeeByName(employeeCreateInfo2.name());

    assertThat(created2).isNotEmpty();
    assertThat(created2.size()).isOne();
    assertThat(created2.get(0)).usingRecursiveComparison()
      .ignoringFields("employeeId")
      .isEqualTo(employeeCreateInfo2);
  }
}
