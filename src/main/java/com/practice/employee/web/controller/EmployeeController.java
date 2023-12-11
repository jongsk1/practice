package com.practice.employee.web.controller;

import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.usecase.EmployeeUseCase;
import com.practice.employee.web.request.EmployeeReadRequest;
import com.practice.employee.web.response.EmployeeInfoResponse;
import com.practice.employee.web.utils.FileReader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/employee")
@RestController
public class EmployeeController {
  private final EmployeeUseCase employeeUseCase;

  @GetMapping
  public ResponseEntity<?> findEmployees(@Valid EmployeeReadRequest request) {
    log.info(
      "직원 정보 리스트 조회: {}",
      request
    );

    var criteria = request.toCriteria();

    var pageResponse = employeeUseCase.findEmployees(criteria);

    return ResponseEntity.ok()
      .body(new PageResponse<>(
        pageResponse.getPageInfo(),
        pageResponse.getData()
          .stream()
          .map(EmployeeInfoResponse::toResponse)
          .toList()
      ));
  }

  @GetMapping("/{name}")
  public ResponseEntity<?> findEmployeeByName(@PathVariable String name) {
    log.info(
      "직원 이름으로 직원 정보 조회: {} ",
      name
    );

    var employeeDomains = employeeUseCase.findEmployeeByName(name);

    return ResponseEntity.ok()
      .body(employeeDomains.stream()
        .map(EmployeeInfoResponse::toResponse)
        .toList());
  }

  @PostMapping(value = "/upload/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createEmployeeByCsvFile(@RequestPart("csvFile") MultipartFile csvFile) {
    var command = FileReader.csvToEmployeeCreateCommand(csvFile);

    log.info(
      "csv 파일 정보로 직원 정보 생성: {}",
      command
    );

    employeeUseCase.createEmployee(command);

    return ResponseEntity.status(HttpStatus.CREATED)
      .build();
  }

  @PostMapping(value = "/upload/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createEmployeeByJsonFile(@RequestPart("jsonFile") MultipartFile jsonFile) {
    var command = FileReader.jsonToEmployeeCreateCommand(jsonFile);

    log.info(
      "json 파일 정보로 직원 정보 생성: {}",
      command
    );

    employeeUseCase.createEmployee(command);

    return ResponseEntity.status(HttpStatus.CREATED)
      .build();
  }
}
