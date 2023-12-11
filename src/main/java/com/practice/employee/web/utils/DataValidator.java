package com.practice.employee.web.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataValidator {
  private static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
  private static final String TEL_PATTERN = "^\\d{3}-\\d{4}-\\d{4}$";

  public static String checkName(String name) {
    if (StringUtils.isBlank(name)) {
      throw new RuntimeException("직원 이름을 입력해 주세요.");
    }

    return trimAll(name);
  }

  public static String checkEmail(String email) {
    if (StringUtils.isBlank(email)) {
      throw new RuntimeException("직원 이메일을 입력해 주세요.");
    }

    var trimValue = trimAll(email);

    if (!Pattern.matches(
      EMAIL_PATTERN,
      trimValue
    )) {
      throw new RuntimeException("이메일 형식이 아닙니다.");
    }

    return trimValue;
  }

  public static String checkCsvTel(String tel) {
    if (StringUtils.isBlank(tel)) {
      throw new RuntimeException("직원 연락처를 입력해 주세요.");
    }

    var trimValue = trimAll(tel);

    if (trimValue.length() != 11) {
      throw new RuntimeException("휴대전화번호(11자리)만 입력 가능합니다.");
    }

    return trimValue.substring(
      0,
      3
    ) + "-" + trimValue.substring(
      3,
      7
    ) + "-" + trimValue.substring(7);
  }

  public static LocalDate checkCsvJoined(String joined) {
    if (StringUtils.isBlank(joined)) {
      throw new RuntimeException("직원 입사일을 입력해 주세요.");
    }

    var trimValue = trimAll(joined);

    return LocalDate.parse(
      trimValue,
      DateTimeFormatter.ofPattern("yyyy.MM.dd")
    );
  }

  public static String checkJsonTel(String tel) {
    if (StringUtils.isBlank(tel)) {
      throw new RuntimeException("직원 연락처를 입력해 주세요.");
    }

    var trimValue = trimAll(tel);

    if (!Pattern.matches(
      TEL_PATTERN,
      trimValue
    )) {
      throw new RuntimeException("휴대전화번호 형식(xxx-xxxx-xxxx)이 아닙니다.");
    }

    return trimValue;
  }

  public static LocalDate checkJsonJoined(String joined) {
    if (StringUtils.isBlank(joined)) {
      throw new RuntimeException("직원 입사일을 입력해 주세요.");
    }

    var trimValue = trimAll(joined);

    return LocalDate.parse(
      trimValue,
      DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );
  }

  public static String trimAll(String value) {
    return value.replaceAll(
      "\\p{Z}",
      ""
    );
  }
}
