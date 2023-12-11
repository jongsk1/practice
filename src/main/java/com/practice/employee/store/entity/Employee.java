package com.practice.employee.store.entity;

import com.practice.employee.domain.EmployeeDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "employee")
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long employeeId;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(unique = true, length = 100, nullable = false)
  private String email;

  @Column(unique = true, length = 100, nullable = false)
  private String tel;

  @Column(nullable = false)
  private LocalDate joined;

  @Column(name = "created_by", length = 100, nullable = false)
  private String createdBy;

  @CreationTimestamp(source = SourceType.DB)
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "modified_by", length = 100, nullable = false)
  private String modifiedBy;

  @UpdateTimestamp(source = SourceType.DB)
  @Column(name = "modified_at", nullable = false)
  private LocalDateTime modifiedAt;

  @PrePersist
  public void prePersist() {
    this.modifiedBy = this.createdBy;
  }

  /**
   * 직원 정보 등록 생성자
   *
   * @param name      직원 이름
   * @param email     직원 이메일
   * @param tel       직원 연락처
   * @param joined    직원 입사일
   * @param createdBy 등록자
   */
  public Employee(
    String name,
    String email,
    String tel,
    LocalDate joined,
    String createdBy
  ) {
    this.name = name;
    this.email = email;
    this.tel = tel;
    this.joined = joined;
    this.createdBy = createdBy;
  }

  /**
   * 엔티티로 직원 정보 도메인 생성
   */
  public EmployeeDomain toDomain() {
    return new EmployeeDomain(
      this.employeeId,
      this.name,
      this.email,
      this.tel,
      this.joined
    );
  }
}
