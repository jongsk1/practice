package com.practice.employee.web.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.employee.UnitTest;
import com.practice.employee.domain.command.EmployeeCreateCommand.EmployeeCreateInfo;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DataConverterTest extends UnitTest {
  @DisplayName("직원 정보 생성 request 내용으로 직원 정보 생성 커맨드를 생성하는 메소드는")
  @Nested
  class makeEmployeeCreateCommandBy {
    @DisplayName("JsonObject 형식일 경우 에러를 반환한다")
    @Test
    void test1() {
      var content = """
        {
            "name":"홍길동",
            "email":"kildong.kim@clovf.com",
            "tel":"010-5678-5678",
            "joined":"2023-12-01"
          }
        """;

      var exception = assertThrows(
        RuntimeException.class,
        () -> DataConverter.makeEmployeeCreateCommandBy(content)
      );

      assertThat(exception.getMessage()).isEqualTo("지원하지 않는 json 형식 입니다.");
    }

    @DisplayName("JsonArray 형식 내용으로 직원 정보 생성 커맨드를 생성한다")
    @Test
    void test2() throws JsonProcessingException {
      var content = """
        [
          {
            "name":"홍길동",
            "email":"kildong.kim@clovf.com",
            "tel":"010-5678-5678",
            "joined":"2023-12-01"
          },
          {
            "name":"이길동",
            "email":"kildong.lee@clovf.com",
            "tel":"010-6789-6789",
            "joined":"2023-11-01"
          }
        ]
        """;

      var command = DataConverter.makeEmployeeCreateCommandBy(content);

      var employeeCreateInfoList = objectMapper.readValue(
        content,
        new TypeReference<List<EmployeeCreateInfo>>() {
        }
      );

      assertThat(command.employeeCreateInfoList()).usingRecursiveComparison()
        .isEqualTo(employeeCreateInfoList);
    }

    @DisplayName("csv 형식 내용으로 직원 정보 생성 커맨드를 생성한다")
    @Test
    void test3() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.lee@clovf.com,01067896789,2023.11.01
        """;

      var command = DataConverter.makeEmployeeCreateCommandBy(content);

      var lines = Arrays.stream(content.split(System.lineSeparator()))
        .map(row -> row.split(","))
        .toList();

      var employeeCreateInfoList = DataConverter.makeEmployeeCreateInfoListByCsv(lines);

      assertThat(command.employeeCreateInfoList()).usingRecursiveComparison()
        .isEqualTo(employeeCreateInfoList);
    }
  }
}