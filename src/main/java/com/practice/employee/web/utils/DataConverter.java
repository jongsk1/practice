package com.practice.employee.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.employee.domain.command.EmployeeCreateCommand;
import com.practice.employee.domain.command.EmployeeCreateCommand.EmployeeCreateInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataConverter {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T convertInputStreamToObject(
    InputStream inputStream,
    TypeReference<T> tTypeReference
  ) throws IOException {
    return objectMapper.readValue(
      inputStream,
      tTypeReference
    );
  }

  /**
   * 직원 정보 생성 payload로 직원 정보 생성 커맨드 생성
   *
   * @param content 직원 정보 생성 content
   */
  public static EmployeeCreateCommand makeEmployeeCreateCommandBy(String content) {
    /*
     * JsonArray만 유효한 json 형식으로 판단
     */
    try {
      objectMapper.readValue(
        content,
        new TypeReference<Map<String, String>>() {
        }
      );

      throw new RuntimeException("지원하지 않는 json 형식 입니다.");

    } catch (JsonProcessingException ignored) {
    }

    /*
     * JsonArray to EmployeeCreateCommand
     */
    try {
      var jsonArray = objectMapper.readValue(
        content,
        new TypeReference<List<Map<String, String>>>() {
        }
      );

      var employeeCreateInfoList = makeEmployeeCreateInfoListByJson(jsonArray);

      return new EmployeeCreateCommand(employeeCreateInfoList);

    } catch (JsonProcessingException ignored) {
    }

    var lines = Arrays.stream(content.split(System.lineSeparator()))
      .map(row -> row.split(","))
      .toList();

    var employeeCreateInfoList = makeEmployeeCreateInfoListByCsv(lines);

    return new EmployeeCreateCommand(employeeCreateInfoList);
  }

  public static List<EmployeeCreateInfo> makeEmployeeCreateInfoListByCsv(List<String[]> lines) {
    return lines.stream()
      .map(rowValues -> {
        if (rowValues.length != 4) {
          throw new RuntimeException("지원하지 않는 csv 형식 입니다.");
        }

        var name = DataValidator.checkName(rowValues[0]);
        var email = DataValidator.checkEmail(rowValues[1]);
        var tel = DataValidator.checkCsvTel(rowValues[2]);
        var joined = DataValidator.checkCsvJoined(rowValues[3]);

        return new EmployeeCreateInfo(
          name,
          email,
          tel,
          joined
        );
      })
      .toList();
  }

  public static List<EmployeeCreateInfo> makeEmployeeCreateInfoListByJson(List<Map<String, String>> maps) {
    return maps.stream()
      .map(employeeInfo -> {
        var name = DataValidator.checkName(employeeInfo.get("name"));
        var email = DataValidator.checkEmail(employeeInfo.get("email"));
        var tel = DataValidator.checkJsonTel(employeeInfo.get("tel"));
        var joined = DataValidator.checkJsonJoined(employeeInfo.get("joined"));

        return new EmployeeCreateInfo(
          name,
          email,
          tel,
          joined
        );
      })
      .toList();
  }
}
