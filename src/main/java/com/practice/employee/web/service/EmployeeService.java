package com.practice.employee.web.service;

import com.practice.employee.domain.EmployeeDomain;
import com.practice.employee.domain.command.EmployeeCreateCommand;
import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.domain.port.EmployeeReadStore;
import com.practice.employee.domain.usecase.EmployeeUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EmployeeService implements EmployeeUseCase {
  private final EmployeeReadStore employeeReadStore;

  @Transactional(readOnly = true)
  @Override
  public PageResponse<EmployeeDomain> findEmployees(EmployeeReadCriteria criteria) {
    return employeeReadStore.findEmployees(criteria);
  }

  @Transactional(readOnly = true)
  @Override
  public List<EmployeeDomain> findEmployeeByName(String name) {
    return employeeReadStore.findEmployeeByName(name);
  }

  @Transactional
  @Override
  public void createEmployee(EmployeeCreateCommand command) {
    employeeReadStore.createEmployee(command);
  }
}
