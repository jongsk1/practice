package com.practice.employee.web.controller.integration;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.practice.employee.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

class EmployeeControllerIT extends IntegrationTest {
  private final String IDENTIFIER = "employee-api";

  @DisplayName("직원 정보 리스트 조회 통합 테스트")
  @Test
  void findEmployees() throws Exception {
    mockMvc.perform(RestDocumentationRequestBuilders.get(
        BASE_PATH + "?page={page}&pageSize={pageSize}",
        1,
        2
      ))
      .andExpect(status().isOk())
      .andDo(print())
      .andDo(document(
        IDENTIFIER + "/{method-name}",
        resource(ResourceSnippetParameters.builder()
          .queryParameters(
            parameterWithName("page").description("페이지"),
            parameterWithName("pageSize").description("페이지 사이즈")
          )
          .responseFields(
            fieldWithPath("pageInfo").description("페이지 정보"),
            fieldWithPath("pageInfo.page").description("페이지"),
            fieldWithPath("pageInfo.pageSize").description("페이지 사이즈"),
            fieldWithPath("pageInfo.totalCount").description("직원 정보 전체 개수"),
            fieldWithPath("data").description("직원 정보 리스트"),
            fieldWithPath("data[].employeeId").description("직원 정보 PK"),
            fieldWithPath("data[].name").description("직원 이름"),
            fieldWithPath("data[].email").description("직원 이메일"),
            fieldWithPath("data[].tel").description("직원 연락처"),
            fieldWithPath("data[].joined").description("직원 합류 날짜")
          )
          .tag(IDENTIFIER)
          .description("직원 정보 리스트 조회")
          .build())
      ));
  }

  @DisplayName("직원 이름으로 직원 정보 조회 통합 테스트")
  @Test
  void findEmployeeByName() throws Exception {
    mockMvc.perform(RestDocumentationRequestBuilders.get(
        BASE_PATH + "/{name}",
        "박효신"
      ))
      .andExpect(status().isOk())
      .andDo(print())
      .andDo(document(
        IDENTIFIER + "/{method-name}",
        resource(ResourceSnippetParameters.builder()
          .pathParameters(parameterWithName("name").description("직원 이름"))
          .responseFields(
            fieldWithPath("[].employeeId").description("직원 정보 PK"),
            fieldWithPath("[].name").description("직원 이름"),
            fieldWithPath("[].email").description("직원 이메일"),
            fieldWithPath("[].tel").description("직원 연락처"),
            fieldWithPath("[].joined").description("직원 합류 날짜")
          )
          .tag(IDENTIFIER)
          .description("직원 이름으로 직원 정보 조회")
          .build())
      ));
  }
}
