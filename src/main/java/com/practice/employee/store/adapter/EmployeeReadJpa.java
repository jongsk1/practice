package com.practice.employee.store.adapter;

import com.practice.employee.domain.EmployeeDomain;
import com.practice.employee.domain.page.PageResponse;
import com.practice.employee.domain.page.PageResponse.PageInfo;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.domain.port.EmployeeReadStore;
import com.practice.employee.store.entity.Employee;
import com.practice.employee.store.repository.EmployeeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class EmployeeReadJpa implements EmployeeReadStore {
  private final EmployeeRepository employeeRepository;

  @Transactional(readOnly = true)
  @Override
  public PageResponse<EmployeeDomain> findEmployees(EmployeeReadCriteria criteria) {
    var pageRequest = PageRequest.of(
      criteria.page() - 1,
      criteria.pageSize()
    );

    var page = employeeRepository.findEmployees(
      criteria,
      pageRequest
    );

    return new PageResponse<>(
      new PageInfo(
        criteria.page(),
        criteria.pageSize(),
        page.getTotalElements()
      ),
      page.getContent()
    );
  }

  @Transactional(readOnly = true)
  @Override
  public List<EmployeeDomain> findEmployeeByName(String name) {
    var foundEmployees = employeeRepository.findByName(name);

    return foundEmployees.stream()
      .map(Employee::toDomain)
      .toList();
  }
}
