package com.practice.employee.web.utils;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.practice.employee.domain.command.EmployeeCreateCommand;
import com.practice.employee.domain.command.EmployeeCreateCommand.EmployeeCreateInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileReader {
  private static final String CSV_CONTENT_TYPE = "text/csv";
  private static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
  private static final String TEL_PATTERN = "^\\d{3}-\\d{3,4}-\\d{4}$";

  public static EmployeeCreateCommand csvToEmployeeCreateCommand(MultipartFile csvFile) {
    if (!CSV_CONTENT_TYPE.equals(csvFile.getContentType())) {
      throw new RuntimeException("csv 파일만 업로드 가능합니다.");
    }

    try (CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(
      csvFile.getInputStream(),
      StandardCharsets.UTF_8
    )))) {
      var lines = csvReader.readAll();

      var employeeCreateInfoList = lines.stream()
        .map(rowValues -> {
          if (rowValues.length != 4) {
            throw new RuntimeException("지원하지 않는 csv 형식 입니다.");
          }

          var name = checkName(rowValues[0]);
          var email = checkEmail(rowValues[1]);
          var tel = checkCsvTel(rowValues[2]);
          var joined = checkCsvJoined(rowValues[3]);

          return new EmployeeCreateInfo(
            name,
            email,
            tel,
            joined
          );
        })
        .toList();

      return new EmployeeCreateCommand(
        employeeCreateInfoList,
        "system"
      );
    } catch (IOException | CsvException e) {
      log.error(
        "csv 파일을 읽는 중 오류가 발생했습니다.",
        e
      );

      throw new RuntimeException("csv 파일을 읽는 중 오류가 발생했습니다.");
    } catch (DateTimeParseException e) {
      log.error(
        "직원 입사일 오류",
        e
      );

      throw new RuntimeException("지원하지 않는 직원 입사일 형식 입니다.");
    }
  }

  private static String checkName(String name) {
    if (StringUtils.isBlank(name)) {
      throw new RuntimeException("직원 이름을 입력해 주세요.");
    }

    return trimAll(name);
  }

  private static String checkEmail(String email) {
    if (StringUtils.isBlank(email)) {
      throw new RuntimeException("직원 이메일을 입력해 주세요.");
    }

    var trimValue = trimAll(email);

    if (!Pattern.matches(
      EMAIL_PATTERN,
      trimValue
    )) {
      throw new RuntimeException("이메일 형식이 아닙니다.");
    }

    return trimValue;
  }

  private static String checkCsvTel(String tel) {
    if (StringUtils.isBlank(tel)) {
      throw new RuntimeException("직원 연락처를 입력해 주세요.");
    }

    var trimValue = trimAll(tel);

    if (trimValue.length() != 11) {
      throw new RuntimeException("휴대전화번호만 입력 가능합니다.");
    }

    return trimValue.substring(
      0,
      3
    ) + "-" + trimValue.substring(
      3,
      7
    ) + "-" + trimValue.substring(7);
  }

  private static LocalDate checkCsvJoined(String joined) {
    if (StringUtils.isBlank(joined)) {
      throw new RuntimeException("직원 입사일을 입력해 주세요.");
    }

    var trimValue = trimAll(joined);

    return LocalDate.parse(
      trimValue,
      DateTimeFormatter.ofPattern("yyyy.MM.dd")
    );
  }

  private static String trimAll(String value) {
    return value.replaceAll(
      "\\p{Z}",
      ""
    );
  }
}
