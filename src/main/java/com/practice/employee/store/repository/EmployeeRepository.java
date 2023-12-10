package com.practice.employee.store.repository;

import com.practice.employee.domain.EmployeeDomain;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.practice.employee.store.entity.Employee;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryExt {
  List<Employee> findByName(String name);
}

interface EmployeeRepositoryExt {
  /**
   * 직원 정보 리스트 조회
   *
   * @param criteria 직원 조회 criteria
   * @param pageable 페이지 정보
   */
  Page<EmployeeDomain> findEmployees(
    EmployeeReadCriteria criteria,
    Pageable pageable
  );
}
