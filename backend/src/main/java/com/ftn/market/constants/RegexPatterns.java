package com.ftn.market.constants;

public class RegexPatterns {

  public static final String EMAIL =
      "^([\\w-]+(?:[\\.+][\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$";

  private RegexPatterns() {
    super();
  }
}
