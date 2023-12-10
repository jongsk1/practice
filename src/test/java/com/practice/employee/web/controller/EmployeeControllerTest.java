package com.practice.employee.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.practice.employee.UnitTest;
import com.practice.employee.domain.EmployeeListDomain;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.usecase.EmployeeUseCase;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EmployeeControllerTest extends UnitTest {
  @InjectMocks
  private EmployeeController employeeController;
  @Mock
  private EmployeeUseCase employeeUseCase;

  @BeforeEach
  void prepare() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
      .build();
  }

  @DisplayName("직원 정보 리스트를 조회하는 메소드는")
  @Nested
  class findEmployees {
    @Captor
    private ArgumentCaptor<EmployeeReadCriteria> captor;

    @DisplayName("page가 0 이하 데이터일 경우 에러를 반환한다")
    @Test
    void test1() throws Exception {
      var page = -1;
      var pageSize = 2;

      mockMvc.perform(get(
          BASE_PATH + "?page={page}&pageSize={pageSize}",
          page,
          pageSize
        ))
        .andDo(print())
        .andExpect(status().isBadRequest());

      verifyNoInteractions(employeeUseCase);
    }

    @DisplayName("pageSize가 0 이하 데이터일 경우 에러를 반환한다")
    @Test
    void test2() throws Exception {
      var page = 1;
      var pageSize = -2;

      mockMvc.perform(get(
          BASE_PATH + "?page={page}&pageSize={pageSize}",
          page,
          pageSize
        ))
        .andDo(print())
        .andExpect(status().isBadRequest());

      verifyNoInteractions(employeeUseCase);
    }

    @DisplayName("EmployeeUseCase 인터페이스의 메소드를 호출한다")
    @Test
    void test3() throws Exception {
      var page = 1;
      var pageSize = 2;
      var pageInfo = new PageResponse.PageInfo(page, pageSize, 1L);
      var domain = new EmployeeListDomain(
        1L,
        "김범수",
        "beomsu.kim@singer.com",
        "010-1234-1234",
        LocalDate.of(1999,
          4,
          1
        )
      );
      var pageResponse = mock(PageResponse.class);

      when(employeeUseCase.findEmployees(any(EmployeeReadCriteria.class))).thenReturn(pageResponse);
      when(pageResponse.getPageInfo()).thenReturn(pageInfo);
      when(pageResponse.getData()).thenReturn(List.of(domain));

      mockMvc.perform(get(
          BASE_PATH + "?page={page}&pageSize={pageSize}",
          page,
          pageSize
        ))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pageInfo").exists())
        .andExpect(jsonPath("$.pageInfo.page").value(pageInfo.getPage()))
        .andExpect(jsonPath("$.pageInfo.pageSize").value(pageInfo.getPageSize()))
        .andExpect(jsonPath("$.pageInfo.totalCount").value(pageInfo.getTotalCount()))
        .andExpect(jsonPath("$.data").isNotEmpty())
        .andReturn();

      verify(employeeUseCase).findEmployees(captor.capture());

      var criteria = captor.getValue();

      assertThat(criteria.page()).isEqualTo(page);
      assertThat(criteria.pageSize()).isEqualTo(pageSize);
    }
  }
}