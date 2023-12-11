package com.practice.employee.web.controller.integration;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.practice.employee.IntegrationTest;
import com.practice.employee.store.repository.EmployeeRepository;
import com.practice.employee.web.request.EmployeeCreateRequest;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

class EmployeeControllerIT extends IntegrationTest {
  private final String IDENTIFIER = "employee-api";
  @Autowired
  private EmployeeRepository employeeRepository;

  @DisplayName("직원 정보 리스트 조회 통합 테스트")
  @Test
  void findEmployees() throws Exception {
    mockMvc.perform(get(
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
            fieldWithPath("data[].joined").description("직원 입사일")
          )
          .tag(IDENTIFIER)
          .description("직원 정보 리스트 조회")
          .build())
      ));
  }

  @DisplayName("직원 이름으로 직원 정보 조회 통합 테스트")
  @Test
  void findEmployeeByName() throws Exception {
    mockMvc.perform(get(
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
            fieldWithPath("[].joined").description("직원 입사일")
          )
          .tag(IDENTIFIER)
          .description("직원 이름으로 직원 정보 조회")
          .build())
      ));
  }

  // todo OpenAPI v3 에서 requestParts 인식 못하는 부분 해결 필요
  @Transactional
  @DisplayName("csv 파일 내용으로 직원 정보를 생성 통합 테스트")
  @Test
  void createEmployeeByCsvFile() throws Exception {
    var employeeName1 = "김길동";
    var employeeName2 = "이길동";

    var content = """
      $EMPLOYEE_NAME_1,kildong.kim@clovf.com,01056785678,2023.12.01
      $EMPLOYEE_NAME_2,kildong.lee@clovf.com,01067896789,2023.11.01
      """.replace(
        "$EMPLOYEE_NAME_1",
        employeeName1
      )
      .replace(
        "$EMPLOYEE_NAME_2",
        employeeName2
      );

    var mockMultipartFile = new MockMultipartFile(
      "uploadFile",
      "create_employee.csv",
      "text/csv",
      content.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(multipart(BASE_PATH + "/upload").file(mockMultipartFile)
        .characterEncoding(StandardCharsets.UTF_8))
      .andExpect(status().isCreated())
      .andDo(print())
      .andDo(document(
        IDENTIFIER + "/{method-name}",
        requestParts(partWithName("uploadFile").description("csv 파일")),
        resource(ResourceSnippetParameters.builder()
          .tag(IDENTIFIER)
          .summary("csv 파일 내용으로 직원 직원 정보 생성")
          .description("csv 파일 내용으로 직원 직원 정보 생성<br/>OpenAPI v3 에서 requestParts 인식 못하는 부분 해결 필요")
          .build())
      ));

    var result1 = employeeRepository.findByName(employeeName1);

    assertThat(result1).isNotEmpty();

    var result2 = employeeRepository.findByName(employeeName2);

    assertThat(result2).isNotEmpty();
  }

  // todo OpenAPI v3 에서 requestParts 인식 못하는 부분 해결 필요
  @Transactional
  @DisplayName("json 파일 내용으로 직원 정보를 생성 통합 테스트")
  @Test
  void createEmployeeByJsonFile() throws Exception {
    var employeeName1 = "김길동";
    var employeeName2 = "이길동";

    var content = """
      [
        {
          "name":"$EMPLOYEE_NAME_1",
          "email":"kildong.kim@clovf.com",
          "tel":"010-5678-5678",
          "joined":"2023-12-01"
        },
        {
          "name":"$EMPLOYEE_NAME_2",
          "email":"kildong.lee@clovf.com",
          "tel":"010-6789-6789",
          "joined":"2023-11-01"
        }
      ]
      """.replace(
        "$EMPLOYEE_NAME_1",
        employeeName1
      )
      .replace(
        "$EMPLOYEE_NAME_2",
        employeeName2
      );

    var mockMultipartFile = new MockMultipartFile(
      "uploadFile",
      "create_employee.json",
      "application/json",
      content.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(multipart(BASE_PATH + "/upload").file(mockMultipartFile)
        .characterEncoding(StandardCharsets.UTF_8))
      .andExpect(status().isCreated())
      .andDo(print())
      .andDo(document(
        IDENTIFIER + "/{method-name}",
        requestParts(partWithName("uploadFile").description("json 파일")),
        resource(ResourceSnippetParameters.builder()
          .tag(IDENTIFIER)
          .summary("json 파일 내용으로 직원 직원 정보 생성")
          .description("json 파일 내용으로 직원 직원 정보 생성<br/>OpenAPI v3 에서 requestParts 인식 못하는 부분 해결 필요")
          .build())
      ));

    var result1 = employeeRepository.findByName(employeeName1);

    assertThat(result1).isNotEmpty();

    var result2 = employeeRepository.findByName(employeeName2);

    assertThat(result2).isNotEmpty();
  }

  @Transactional
  @DisplayName("csv 형식의 직원 정보 생성 request로 직원 정보 생성 통합 테스트")
  @Test
  void createEmployeeByCsv() throws Exception {
    var employeeName1 = "김길동";
    var employeeName2 = "이길동";

    var content = """
      $EMPLOYEE_NAME_1,kildong.kim@clovf.com,01056785678,2023.12.01
      $EMPLOYEE_NAME_2,kildong.lee@clovf.com,01067896789,2023.11.01
      """.replace(
        "$EMPLOYEE_NAME_1",
        employeeName1
      )
      .replace(
        "$EMPLOYEE_NAME_2",
        employeeName2
      );

    var request = new EmployeeCreateRequest(content);

    mockMvc.perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated())
      .andDo(print())
      .andDo(document(
        IDENTIFIER + "/{method-name}",
        resource(ResourceSnippetParameters.builder()
          .requestFields(fieldWithPath("content").description("csv 형식의 직원 생성 정보"))
          .tag(IDENTIFIER)
          .summary("직원 정보 생성")
          .description("csv 형식의 직원 정보 request로 직원 정보 생성")
          .build())
      ));

    var result1 = employeeRepository.findByName(employeeName1);

    assertThat(result1).isNotEmpty();

    var result2 = employeeRepository.findByName(employeeName2);

    assertThat(result2).isNotEmpty();
  }

  @Transactional
  @DisplayName("json 형식의 직원 정보 생성 request로 직원 정보 생성 통합 테스트")
  @Test
  void createEmployeeByJson() throws Exception {
    var employeeName1 = "김길동";
    var employeeName2 = "이길동";

    var content = """
      [
        {
          "name":"$EMPLOYEE_NAME_1",
          "email":"kildong.kim@clovf.com",
          "tel":"010-5678-5678",
          "joined":"2023-12-01"
        },
        {
          "name":"$EMPLOYEE_NAME_2",
          "email":"kildong.lee@clovf.com",
          "tel":"010-6789-6789",
          "joined":"2023-11-01"
        }
      ]
      """.replace(
        "$EMPLOYEE_NAME_1",
        employeeName1
      )
      .replace(
        "$EMPLOYEE_NAME_2",
        employeeName2
      );

    var request = new EmployeeCreateRequest(content);

    mockMvc.perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated())
      .andDo(print())
      .andDo(document(
        IDENTIFIER + "/{method-name}",
        resource(ResourceSnippetParameters.builder()
          .requestFields(fieldWithPath("content").description("json 형식의 직원 생성 정보"))
          .tag(IDENTIFIER)
          .summary("직원 정보 생성")
          .description("json 형식의 직원 정보 request로 직원 정보 생성")
          .build())
      ));

    var result1 = employeeRepository.findByName(employeeName1);

    assertThat(result1).isNotEmpty();

    var result2 = employeeRepository.findByName(employeeName2);

    assertThat(result2).isNotEmpty();
  }
}
