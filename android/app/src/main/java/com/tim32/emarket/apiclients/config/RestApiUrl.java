package com.tim32.emarket.apiclients.config;

public final class RestApiUrl {

    public static final String prodRoot = "http://34.89.182.11:6996";

    public static final String devRoot = "http://10.0.2.2:6996";

    public static final String root = prodRoot;

    public static final String companyImages = "/api/v1/images/company/";

    public static final String shopImages = "/api/v1/images/shop/";

    public static final String shopImage = root + "/api/v1/image/shop";

    public static final String productImages = "/api/v1/images/product/";

    public static final String productImage = root + "/api/v1/image/product";

    public static final String image = "/api/v1/image/";

    public static final String profileImages = "/api/v1/images/my";

    public static final String profileImage = root + "/api/v1/image/my";

    private RestApiUrl() {
    }
}
