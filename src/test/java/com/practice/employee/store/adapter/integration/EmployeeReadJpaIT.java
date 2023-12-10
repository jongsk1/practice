package com.practice.employee.store.adapter.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.employee.IntegrationTest;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.store.adapter.EmployeeReadJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
