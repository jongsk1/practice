package com.practice.employee.web.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.practice.employee.domain.command.EmployeeCreateCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileReader {
  private static final String CSV_CONTENT_TYPE = "text/csv";
  private static final String JSON_CONTENT_TYPE = "application/json";

  /**
   * 업로드 파일 정보로 직원 생성 커맨드 생성
   *
   * @param uploadFile 업로드 파일
   */
  public static EmployeeCreateCommand fileToEmployeeCreateCommand(MultipartFile uploadFile) {
    if (CSV_CONTENT_TYPE.equals(uploadFile.getContentType())) {
      return csvToEmployeeCreateCommand(uploadFile);
    }

    if (JSON_CONTENT_TYPE.equals(uploadFile.getContentType())) {
      return jsonToEmployeeCreateCommand(uploadFile);
    }

    throw new RuntimeException("csv 또는 json 파일만 업로드 가능합니다.");
  }

  /**
   * csv 파일 내용으로로 직원 생성 커맨드 생성
   *
   * @param csvFile csv MultipartFile
   */
  private static EmployeeCreateCommand csvToEmployeeCreateCommand(MultipartFile csvFile) {
    try (CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(
      csvFile.getInputStream(),
      StandardCharsets.UTF_8
    )))) {
      var lines = csvReader.readAll();

      var employeeCreateInfoList = DataConverter.makeEmployeeCreateInfoListByCsv(lines);

      return new EmployeeCreateCommand(employeeCreateInfoList);

    } catch (DateTimeParseException e) {
      throw new RuntimeException("직원 입사일은 yyyy.MM.dd만 입력 가능 합니다.");

    } catch (IOException | CsvException e) {
      throw new RuntimeException("csv 파일을 읽는 중 오류가 발생했습니다.");
    }
  }

  /**
   * json 파일 내용으로 직원 생성 커맨드 생성
   *
   * @param jsonFile json MultipartFile
   */
  private static EmployeeCreateCommand jsonToEmployeeCreateCommand(MultipartFile jsonFile) {
    try {
      var employeeInfoMaps = DataConverter.convertInputStreamToObject(
        jsonFile.getInputStream(),
        new TypeReference<List<Map<String, String>>>() {
        }
      );

      var employeeCreateInfoList = DataConverter.makeEmployeeCreateInfoListByJson(employeeInfoMaps);

      return new EmployeeCreateCommand(employeeCreateInfoList);

    } catch (DatabindException e) {
      throw new RuntimeException("지원하지 않는 json 형식 입니다.");

    } catch (DateTimeParseException e) {
      throw new RuntimeException("직원 입사일은 yyyy-MM-dd만 입력 가능 합니다.");

    } catch (IOException e) {
      throw new RuntimeException("json 파일을 읽는 중 오류가 발생했습니다.");
    }
  }
}
