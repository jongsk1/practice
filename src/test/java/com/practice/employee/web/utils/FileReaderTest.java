package com.practice.employee.web.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileReaderTest {
  @DisplayName("csv 파일 정보로 직원 정보 생성 커맨드를 생성하는 메소드는")
  @Nested
  class csvToEmployeeCreateCommand {
    private MockMultipartFile mockMultipartFile;

    @DisplayName("csv 파일이 아닐경우 에러를 반환한다")
    @Test
    void test1() {
      var content = """
        [
          {
            "name":"김길동",
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

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("csv 파일만 업로드 가능합니다.");
    }

    @DisplayName("csv 형식이 다를경우 에러를 반환한다")
    @Test
    void test2() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.lee@clovf.com,01067896789,2023.11.01
        박길동,01078907890,2023.10.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("지원하지 않는 csv 형식 입니다.");
    }

    @DisplayName("직원 이름이 없을 경우 에러를 반환한다")
    @Test
    void test3() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        ,kildong.lee@clovf.com,01067896789,2023.11.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 이름을 입력해 주세요.");
    }

    @DisplayName("직원 이메일이 없을 경우 에러를 반환한다")
    @Test
    void test4() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,,01067896789,2023.11.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 이메일을 입력해 주세요.");
    }

    @DisplayName("직원 이메일 형식이 다를 경우 에러를 반환한다")
    @Test
    void test5() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.kimclovf.com,01067896789,2023.11.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("이메일 형식이 아닙니다.");
    }

    @DisplayName("직원 연락처가 없을 경우 에러를 반환한다")
    @Test
    void test6() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.kim@clovf.com,,2023.11.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 연락처를 입력해 주세요.");
    }

    @DisplayName("직원 연락처가 휴대전화번호(11자리)가 아닐 경우 에러를 반환한다")
    @Test
    void test7() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.kim@clovf.com,0212341234,2023.11.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("휴대전화번호(11자리)만 입력 가능합니다.");
    }

    @DisplayName("직원 입사일이 없을 경우 에러를 반환한다")
    @Test
    void test8() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.kim@clovf.com,01067896789,
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 입사일을 입력해 주세요.");
    }

    @DisplayName("직원 입사일이 yyyy.MM.dd 형식이 아닐 경우 에러를 반환한다")
    @Test
    void test9() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.kim@clovf.com,01067896789,2023-11-01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.csvToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 입사일은 yyyy.MM.dd만 입력 가능 합니다.");
    }

    @DisplayName("csv 내용으로 직원 생성 커맨드를 생성한다")
    @Test
    void test10() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.kim@clovf.com,01067896789,2023.11.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "csvFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var command = FileReader.csvToEmployeeCreateCommand(mockMultipartFile);

      assertThat(command).isNotNull();
      assertThat(command.employeeCreateInfoList()).isNotEmpty();
      assertThat(command.employeeCreateInfoList()
        .size()).isEqualTo(2);
      assertThat(command.createdBy()).isEqualTo("system");
    }
  }

  @DisplayName("json 파일 정보로 직원 정보 생성 커맨드를 생성하는 메소드는")
  @Nested
  class jsonToEmployeeCreateCommand {
    private MockMultipartFile mockMultipartFile;

    @DisplayName("json 파일이 아닐경우 에러를 반환한다")
    @Test
    void test1() {
      var content = """
        김길동,kildong.kim@clovf.com,01056785678,2023.12.01
        이길동,kildong.kim@clovf.com,01067896789,2023.11.01
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.csv",
        "text/csv",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(RuntimeException.class, () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile));

      assertThat(exception.getMessage()).isEqualTo("json 파일만 업로드 가능합니다.");
    }

    @DisplayName("json 형식이 다를경우 에러를 반환한다")
    @Test
    void test2() {
      var content = """
        {
          "name":"김길동",
          "email":"kildong.kim@clovf.com",
          "tel":"010-5678-5678",
          "joined":"2023-12-01"
        }
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("지원하지 않는 json 형식 입니다.");
    }

    @DisplayName("직원 이름이 없을 경우 에러를 반환한다")
    @Test
    void test3() {
      var content = """
        [
          {
            "name":"",
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

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 이름을 입력해 주세요.");
    }

    @DisplayName("직원 이메일이 없을 경우 에러를 반환한다")
    @Test
    void test4() {
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
            "email":"",
            "tel":"010-6789-6789",
            "joined":"2023-11-01"
          }
        ]
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 이메일을 입력해 주세요.");
    }

    @DisplayName("직원 이메일 형식이 다를 경우 에러를 반환한다")
    @Test
    void test5() {
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
            "email":"kildong.leeaclovf.com",
            "tel":"010-6789-6789",
            "joined":"2023-11-01"
          }
        ]
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("이메일 형식이 아닙니다.");
    }

    @DisplayName("직원 연락처가 없을 경우 에러를 반환한다")
    @Test
    void test6() {
      var content = """
        [
          {
            "name":"홍길동",
            "email":"kildong.kim@clovf.com",
            "tel":"",
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

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 연락처를 입력해 주세요.");
    }

    @DisplayName("직원 연락처 형식이 다를 경우 에러를 반환한다")
    @Test
    void test7() {
      var content = """
        [
          {
            "name":"홍길동",
            "email":"kildong.kim@clovf.com",
            "tel":"01056785678",
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

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("휴대전화번호 형식(xxx-xxxx-xxxx)이 아닙니다.");
    }

    @DisplayName("직원 입사일이 없을 경우 에러를 반환한다")
    @Test
    void test8() {
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
            "joined":""
          }
        ]
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 입사일을 입력해 주세요.");
    }

    @DisplayName("직원 입사일이 yyyy-MM-dd 형식이 아닐 경우 에러를 반환한다")
    @Test
    void test9() {
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
            "joined":"2023.11.01"
          }
        ]
        """;

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var exception = assertThrows(
        RuntimeException.class,
        () -> FileReader.jsonToEmployeeCreateCommand(mockMultipartFile)
      );

      assertThat(exception.getMessage()).isEqualTo("직원 입사일은 yyyy-MM-dd만 입력 가능 합니다.");
    }

    @DisplayName("json 내용으로 직원 생성 커맨드를 생성한다")
    @Test
    void test10() {
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

      this.mockMultipartFile = new MockMultipartFile(
        "jsonFile",
        "create_employee.json",
        "application/json",
        content.getBytes(StandardCharsets.UTF_8)
      );

      var command = FileReader.jsonToEmployeeCreateCommand(mockMultipartFile);

      assertThat(command).isNotNull();
      assertThat(command.employeeCreateInfoList()).isNotEmpty();
      assertThat(command.employeeCreateInfoList()
        .size()).isEqualTo(2);
      assertThat(command.createdBy()).isEqualTo("system");
    }
  }
}