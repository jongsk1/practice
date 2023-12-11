package com.practice.employee.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.practice.employee.UnitTest;
import com.practice.employee.domain.EmployeeDomain;
import com.practice.employee.domain.command.EmployeeCreateCommand;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.port.EmployeeReadStore;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class EmployeeServiceTest extends UnitTest {
  @InjectMocks
  private EmployeeService employeeService;
  @Mock
  private EmployeeReadStore employeeReadStore;

  @DisplayName("criteria로 직원 정보 리스트를 조회하는 메소드는")
  @Nested
  class findEmployees {
    @DisplayName("EmployeeReadStore 인터페이스의 메소드를 호출한다")
    @Test
    void test() {
      var criteria = new EmployeeReadCriteria(
        1,
        2
      );
      var pageResponse = mock(PageResponse.class);

      when(employeeReadStore.findEmployees(any(EmployeeReadCriteria.class))).thenReturn(pageResponse);

      var result = employeeService.findEmployees(criteria);

      verify(employeeReadStore).findEmployees(eq(criteria));

      assertThat(result).isEqualTo(pageResponse);
    }
  }

  @DisplayName("직원 이름으로 직원 정보를 조회하는 메소드는")
  @Nested
  class findEmployeeByName {
    @DisplayName("EmployeeReadStore 인터페이스의 메소드를 호출한다")
    @Test
    void test() {
      var name = "이수";
      var domain = new EmployeeDomain(
        4L,
        name,
        "su.lee@singer.com",
        "010-4567-4567",
        LocalDate.of(
          2000,
          3,
          1
        )
      );

      when(employeeReadStore.findEmployeeByName(anyString())).thenReturn(List.of(domain));

      var result = employeeService.findEmployeeByName(name);

      verify(employeeReadStore).findEmployeeByName(eq(name));

      assertThat(result).isEqualTo(List.of(domain));
    }
  }

  @DisplayName("직원 정보를 생성하는 메소드는")
  @Nested
  class createEmployee {
    @DisplayName("직원 생성 커맨드를 파라미터로 받는 EmployeeReadStore 인터페이스의 메소드를 호출한다")
    @Test
    void test() {
      var command = mock(EmployeeCreateCommand.class);

      employeeService.createEmployee(command);

      verify(employeeReadStore).createEmployee(eq(command));
    }
  }
}