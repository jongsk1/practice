package com.practice.employee.domain.page;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponse<E> {
  private PageInfo pageInfo;
  List<E> data;

  public PageResponse(
    PageInfo pageInfo,
    List<E> data
  ) {
    this.pageInfo = pageInfo;
    this.data = data;
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class PageInfo {
    private int page;
    private int pageSize;
    private long totalCount;

    public PageInfo(
      int page,
      int pageSize,
      long totalCount
    ) {
      this.page = page;
      this.pageSize = pageSize;
      this.totalCount = totalCount;
    }
  }
}
