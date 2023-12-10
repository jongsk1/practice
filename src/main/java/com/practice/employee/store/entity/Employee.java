package com.practice.employee.store.entity;

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

  @Column(length = 100, nullable = false)
  private String email;

  @Column(length = 100, nullable = false)
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
}
