package com.practice.employee.store.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.practice.employee.domain.EmployeeDomain;
import com.practice.employee.domain.command.EmployeeCreateCommand;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.UnitTest;
import com.practice.employee.store.entity.Employee;
import com.practice.employee.store.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class EmployeeReadJpaTest extends UnitTest {
  @InjectMocks
  private EmployeeReadJpa employeeReadJpa;
  @Mock
  private EmployeeRepository employeeRepository;

  @DisplayName("직원 정보 리스트를 조회하는 메소드는")
  @Nested
  class findEmployees {
    @Captor
    private ArgumentCaptor<PageRequest> captor;
    private EmployeeReadCriteria criteria;
    private Page page;
    private EmployeeDomain domain1;
    private EmployeeDomain domain2;
    private EmployeeDomain domain3;
    private EmployeeDomain domain4;
    private List<EmployeeDomain> employeeListDomain;

    @BeforeEach
    void prepare() {
      this.criteria = new EmployeeReadCriteria(
        1,
        50
      );

      this.page = mock(Page.class);

      this.domain1 = new EmployeeDomain(
        1,
        "김범수",
        "beomsu.kim@singer.com",
        "010-1234-1234",
        LocalDate.now()
      );

      this.domain2 = new EmployeeDomain(
        2,
        "유나얼",
        "naul.yu@singer.com",
        "010-2345-2345",
        LocalDate.now()
      );

      this.domain3 = new EmployeeDomain(
        3,
        "박효신",
        "hyoshin.park@singer.com",
        "010-3456-3456",
        LocalDate.now()
      );

      this.domain4 = new EmployeeDomain(
        4,
        "이수",
        "su.lee@singer.com",
        "010-4567-4567",
        LocalDate.now()
      );

      this.employeeListDomain = List.of(
        this.domain1,
        this.domain2,
        this.domain3,
        this.domain4
      );
    }

    @DisplayName("criteria의 페이지 정보로 PageRequest 객체를 생성한다")
    @Test
    void test1() {
      when(employeeRepository.findEmployees(
        any(EmployeeReadCriteria.class),
        any(Pageable.class)
      )).thenReturn(page);

      when(page.getTotalElements()).thenReturn((long) employeeListDomain.size());

      when(page.getContent()).thenReturn(employeeListDomain);

      employeeReadJpa.findEmployees(criteria);

      verify(employeeRepository).findEmployees(
        eq(criteria),
        captor.capture()
      );

      var pageRequest = captor.getValue();

      assertThat(pageRequest.getPageNumber()).isEqualTo(criteria.page() - 1);
      assertThat(pageRequest.getPageSize()).isEqualTo(criteria.pageSize());
    }

    @DisplayName("criteria와 PageRequest로 직원 정보 리스트를 조회하는 repository 메소드를 호출한다")
    @Test
    void test2() {
      when(employeeRepository.findEmployees(
        any(EmployeeReadCriteria.class),
        any(Pageable.class)
      )).thenReturn(page);

      when(page.getTotalElements()).thenReturn((long) employeeListDomain.size());

      when(page.getContent()).thenReturn(employeeListDomain);

      var result = employeeReadJpa.findEmployees(criteria);

      verify(employeeRepository).findEmployees(
        eq(criteria),
        captor.capture()
      );

      var pageRequest = captor.getValue();

      assertThat(pageRequest.getPageNumber()).isEqualTo(criteria.page() - 1);
      assertThat(pageRequest.getPageSize()).isEqualTo(criteria.pageSize());

      assertThat(result).isNotNull();
      assertThat(result.getPageInfo()).isNotNull();

      var pageInfo = result.getPageInfo();

      assertThat(pageInfo.getPage()).isEqualTo(criteria.page());
      assertThat(pageInfo.getPageSize()).isEqualTo(criteria.pageSize());
      assertThat(pageInfo.getTotalCount()).isEqualTo(employeeListDomain.size());

      assertThat(result.getData()).isEqualTo(employeeListDomain);
    }
  }

  @DisplayName("직원 이름으로 직원 정보를 조회하는 메소드는")
  @Nested
  class findEmployeeByName {
    @DisplayName("이름을 파라미터로 받는 EmployeeRepository 인터페이스의 메소드를 호출한다")
    @Test
    void test() {
      var name = "직원이름";
      var entity = mock(Employee.class);

      when(employeeRepository.findByName(anyString())).thenReturn(List.of(entity));

      var result = employeeReadJpa.findEmployeeByName(name);

      verify(employeeRepository).findByName(eq(name));

      assertThat(result).isNotEmpty();
    }
  }

  @DisplayName("직원 정보를 생성하는 메소드는")
  @Nested
  class createEmployee {
    @Captor
    private ArgumentCaptor<List<Employee>> captor;
    private EmployeeCreateCommand command;

    @BeforeEach
    void prepare() {
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
      this.command = new EmployeeCreateCommand(
        List.of(
          employeeCreateInfo1,
          employeeCreateInfo2
        ),
        "system"
      );
    }

    @DisplayName("직원 생성 커맨드로 엔티티를 생성하고 EmployeeRepository 인터페이스의 직원 정보 생성 메소드를 호출한다")
    @Test
    void test() {
      employeeReadJpa.createEmployee(command);

      verify(employeeRepository).saveAll(captor.capture());

      var entities = captor.getValue();

      assertThat(entities).isNotEmpty();

      assertThat(entities.size()).isEqualTo(command.employeeCreateInfoList()
        .size());

      assertThat(entities.get(0)).usingRecursiveComparison()
        .ignoringFields(
          "employeeId",
          "createdBy",
          "createdAt",
          "modifiedBy",
          "modifiedAt"
        )
        .isEqualTo(command.employeeCreateInfoList()
          .get(0));

      assertThat(entities.get(0).getCreatedBy()).isEqualTo(command.createdBy());

      assertThat(entities.get(1)).usingRecursiveComparison()
        .ignoringFields(
          "employeeId",
          "createdBy",
          "createdAt",
          "modifiedBy",
          "modifiedAt"
        )
        .isEqualTo(command.employeeCreateInfoList()
          .get(1));

      assertThat(entities.get(1).getCreatedBy()).isEqualTo(command.createdBy());
    }
  }
}