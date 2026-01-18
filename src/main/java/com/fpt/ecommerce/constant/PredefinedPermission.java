package com.fpt.ecommerce.constant;

public class PredefinedPermission {
    // Role Names
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_STAFF = "STAFF";
    public static final String ROLE_USER = "USER";

    // User Permissions
    public static final String MANAGE_USERS = "MANAGE_USERS";
    public static final String VIEW_PROFILE = "VIEW_PROFILE";

    // Product Permissions
    public static final String VIEW_PRODUCT = "VIEW_PRODUCT";
    public static final String CREATE_PRODUCT = "CREATE_PRODUCT";
    public static final String UPDATE_PRODUCT = "UPDATE_PRODUCT";
    public static final String DELETE_PRODUCT = "DELETE_PRODUCT";

    // --- SUPPLIER MANAGEMENT ---
    public static final String VIEW_SUPPLIER = "VIEW_SUPPLIER";
    public static final String CREATE_SUPPLIER = "CREATE_SUPPLIER";
    public static final String UPDATE_SUPPLIER = "UPDATE_SUPPLIER";
    public static final String DELETE_SUPPLIER = "DELETE_SUPPLIER";

    private PredefinedPermission() {}
}