package app.com.relay.utils;

public class Constant {

    private static final String IP_ADDRESS = "41.226.11.252";
    private static final String URL = "http://" + IP_ADDRESS + ":11809";
    public static final String URL_USERS = URL + "/users/";
    public static final String URL_UPDATE_STORE = URL + "/users/update_store";
    public static final String URL_PRODUCTS = URL + "/products/";
    public static final String URL_STORES = URL + "/stores/";
    //public static final String URL_REVIEW_PROD = URL + "/product_reviews/product";
    public static final String URL_ORDER_STORE = URL + "/orders/store/";
    public static final String URL_IMAGE = URL + "/uploads/";
    public static final String URL_UPLOAD_IMAGE = URL_PRODUCTS+"upload";
    public static final String URL_GET_PRODUCT_REVIEWS = URL + "/product_reviews/product/";
    public static final String URL_ADD_PRODUCT_REVIEW = URL + "/product_reviews/add_review/product";
}
