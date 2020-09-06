package com.ftn.market.helper;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

public final class PaginationHelper {

  public static final String HTTP_HEADER_PAGINATION_TOTALPAGES = "X-Pagination-TotalPages";
  public static final String HTTP_HEADER_PAGINATION_TOTALELEMENTS = "X-Pagination-TotalElements";
  public static final String HTTP_HEADER_PAGINATION_NUMBER = "X-Pagination-Number";
  public static final String HTTP_HEADER_PAGINATION_NUMBEROFELEMENTS = "X-Pagination-NumberOfElements";
  public static final String HTTP_HEADER_PAGINATION_SIZE = "X-Pagination-Size";
  public static final String HTTP_HEADER_PAGINATION_FIRST = "X-Pagination-First";
  public static final String HTTP_HEADER_PAGINATION_LAST = "X-Pagination-Last";

  public static void addPaginationHeaders(final HttpServletResponse response, final Page<?> page) {
    response.addIntHeader(HTTP_HEADER_PAGINATION_TOTALPAGES, page.getTotalPages());
    response.addHeader(HTTP_HEADER_PAGINATION_TOTALELEMENTS, Long.toString(page.getTotalElements()));
    response.addIntHeader(HTTP_HEADER_PAGINATION_NUMBER, page.getNumber());
    response.addIntHeader(HTTP_HEADER_PAGINATION_NUMBEROFELEMENTS, page.getNumberOfElements());
    response.addIntHeader(HTTP_HEADER_PAGINATION_SIZE, page.getSize());
    response.addHeader(HTTP_HEADER_PAGINATION_FIRST, Boolean.toString(page.isFirst()));
    response.addHeader(HTTP_HEADER_PAGINATION_LAST, Boolean.toString(page.isLast()));
  }

  private PaginationHelper() {
    super();
  }
}
