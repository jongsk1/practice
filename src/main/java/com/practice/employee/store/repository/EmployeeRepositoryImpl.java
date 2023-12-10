package com.practice.employee.store.repository;

import com.practice.employee.domain.EmployeeListDomain;
import com.practice.employee.domain.criteria.EmployeeReadCriteria;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.practice.employee.store.entity.QEmployee.employee;

@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryExt {
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<EmployeeListDomain> findEmployees(
    EmployeeReadCriteria criteria,
    Pageable pageable
  ) {
    var totalCount = queryFactory.select(employee.count())
      .from(employee)
      .fetchOne();

    var employeeListDomains = queryFactory.select(Projections.constructor(
        EmployeeListDomain.class,
        employee.employeeId,
        employee.name,
        employee.email,
        employee.tel,
        employee.joined
      ))
      .from(employee)
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetch();

    return new PageImpl<>(
      employeeListDomains,
      pageable,
      Objects.requireNonNull(totalCount)
    );
  }
}
