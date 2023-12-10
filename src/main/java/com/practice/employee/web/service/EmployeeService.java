package com.practice.employee.web.service;

import com.practice.employee.domain.EmployeeListDomain;
import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.domain.port.EmployeeReadStore;
import com.practice.employee.domain.usecase.EmployeeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EmployeeService implements EmployeeUseCase {
  private final EmployeeReadStore employeeReadStore;

  @Transactional(readOnly = true)
  @Override
  public PageResponse<EmployeeListDomain> findEmployees(EmployeeReadCriteria criteria) {
    return employeeReadStore.findEmployees(criteria);
  }
}
