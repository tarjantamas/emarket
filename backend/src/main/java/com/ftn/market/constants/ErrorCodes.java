package com.ftn.market.constants;

public final class ErrorCodes {

  // general
  public static final String DTO_VALIDATION_FAILED = "G001";
  public static final String BAD_CREDENTIALS = "G002";
  public static final String UNAUTHORIZED = "G003";

  // user
  public static final String USER_ALREADY_EXISTS_BY_EMAIL = "U001";
  public static final String USER_NOT_FOUND_1 = "U002.1";

  // company
  public static final String COMPANY_NOT_FOUND_1 = "C001.1";
  public static final String COMPANY_NOT_FOUND_2 = "C001.2";
  public static final String COMPANY_NOT_FOUND_3 = "C001.3";
  public static final String COMPANY_NOT_FOUND_4 = "C001.4";
  public static final String COMPANY_NOT_FOUND_5 = "C001.5";
  public static final String COMPANY_NOT_FOUND_6 = "C001.6";
  public static final String COMPANY_NOT_FOUND_7 = "C001.7";
  public static final String COMPANY_NOT_FOUND_8 = "C001.8";
  public static final String COMPANY_NOT_FOUND_9 = "C001.9";
  public static final String COMPANY_NOT_FOUND_10 = "C001.10";
  public static final String COMPANY_UPDATE_REQUESTER_NOT_OWNER = "C002";

  // shop
  public static final String SHOP_CREATE_REQUESTER_NOT_COMPANY_OWNER = "S001";
  public static final String SHOP_NOT_FOUND_1 = "S002.1";
  public static final String SHOP_NOT_FOUND_2 = "S002.2";
  public static final String SHOP_NOT_FOUND_3 = "S002.3";
  public static final String SHOP_NOT_FOUND_4 = "S002.4";
  public static final String SHOP_NOT_FOUND_5 = "S002.5";
  public static final String SHOP_NOT_FOUND_6 = "S002.6";
  public static final String SHOP_NOT_FOUND_7 = "S002.7";
  public static final String SHOP_NOT_FOUND_8 = "S002.8";
  public static final String SHOP_NOT_FOUND_9 = "S002.9";
  public static final String SHOP_NOT_FOUND_10 = "S002.10";
  public static final String SHOP_UPDATE_REQUESTER_NOT_COMPANY_OWNER = "S003";
  public static final String SHOP_DELETE_REQUESTER_NOT_COMPANY_OWNER = "S004";

  // product
  public static final String PRODUCT_CREATE_REQUESTER_NOT_COMPANY_OWNER = "P001";
  public static final String PRODUCT_NOT_FOUND_1 = "P002.1";
  public static final String PRODUCT_NOT_FOUND_2 = "P002.2";
  public static final String PRODUCT_NOT_FOUND_3 = "P002.3";
  public static final String PRODUCT_NOT_FOUND_4 = "P002.4";
  public static final String PRODUCT_NOT_FOUND_5 = "P002.5";
  public static final String PRODUCT_NOT_FOUND_6 = "P002.6";
  public static final String PRODUCT_SEARCH_PARAMS_INVALID = "P003";
  public static final String PRODUCT_DELETE_REQUESTER_NOT_COMPANY_OWNER = "P004";
  public static final String PRODUCT_UPDATE_REQUESTER_NOT_COMPANY_OWNER = "P005";

  // favorite
  public static final String FAVORITE_SHOP_ALREADY_EXISTS = "F001";
  public static final String SHOP_NOT_FAVORITE = "F002";

  // subscription
  public static final String SUBSCRIPTION_COMPANY_ALREADY_EXISTS = "SU001";
  public static final String COMPANY_NOT_SUBSCRIBED = "SU002";

  // image
  public static final String IMAGE_UPLOAD_NOT_COMPANY_OWNER_1 = "I001.1";
  public static final String IMAGE_UPLOAD_NOT_COMPANY_OWNER_2 = "I001.2";
  public static final String IMAGE_UPLOAD_NOT_COMPANY_OWNER_3 = "I001.3";
  public static final String IMAGE_NOT_FOUND_1 = "I002.1";
  public static final String IMAGE_NOT_FOUND_2 = "I002.2";
  public static final String IMAGE_DELETE_REQUESTER_NOT_COMPANY_OWNER = "I003";
  public static final String IMAGE_DELETE_REQUESTER_NOT_IMAGE_OWNER = "I004";

  // setting
  public static final String SETTING_NOT_FOUND_1 = "SE001.1";
  public static final String SETTING_NOT_FOUND_2 = "SE001.2";
  public static final String SETTING_NOT_FOUND_3 = "SE001.3";
  public static final String SETTING_NOT_FOUND_4 = "SE001.4";
  public static final String SETTING_NOT_FOUND_5 = "SE001.5";
}
