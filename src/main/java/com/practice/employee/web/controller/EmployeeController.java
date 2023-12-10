package com.practice.employee.web.controller;

import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.usecase.EmployeeUseCase;
import com.practice.employee.web.request.EmployeeReadRequest;
import com.practice.employee.web.response.EmployeeInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
