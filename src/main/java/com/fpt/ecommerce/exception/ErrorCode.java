package com.fpt.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // --- 9999: SYSTEM ERROR ---
    UNCATEGORIZED_EXCEPTION(9999, "error.uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- 1xxx: COMMON & AUTHENTICATION ---
    INVALID_KEY(1001, "error.validation.failed", HttpStatus.BAD_REQUEST), // Lỗi mặc định cho @Valid
    RESOURCE_NOT_FOUND(1002, "error.resource.not_found", HttpStatus.NOT_FOUND),

    // Auth
    USER_EXISTED(1003, "error.user.existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "error.user.not_existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005, "error.user.unauthenticated", HttpStatus.UNAUTHORIZED), // 401: Chưa đăng nhập
    UNAUTHORIZED(1006, "error.user.unauthorized", HttpStatus.FORBIDDEN),       // 403: Không có quyền
    EMAIL_EXISTED(1007, "error.email.existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1008, "error.role.not_found", HttpStatus.NOT_FOUND),
    // Lỗi email chung

    // Validation User
    DOB_REQUIRED(1008, "DOB_REQUIRED", HttpStatus.BAD_REQUEST),
    USER_MUST_BE_16_YEARS_OLD(1009, "error.user.must_be_16", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1010, "error.password.invalid", HttpStatus.BAD_REQUEST),

    // --- 2xxx: PRODUCT & CATEGORY ---
    PRODUCT_NOT_FOUND(2001, "error.product.not_found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(2002, "error.category.not_found", HttpStatus.NOT_FOUND),

    // --- 3xxx: SUPPLIER & INVENTORY ---
    SUPPLIER_NOT_FOUND(3001, "error.supplier.not_found", HttpStatus.NOT_FOUND),
    SUPPLIER_NAME_REQUIRED(3008, "error.supplier.name_required", HttpStatus.BAD_REQUEST),
    SUPPLIER_EMAIL_EXISTED(3002, "error.supplier.email.existed", HttpStatus.BAD_REQUEST),
    SUPPLIER_PHONE_EXISTED(3003, "error.supplier.phone.existed", HttpStatus.BAD_REQUEST),
    SUPPLIER_HAS_INVOICES(3004, "error.supplier.has.invoices", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(3005, "error.email.required", HttpStatus.BAD_REQUEST),
    CONTACT_PERSON_REQUIRED(3006, "error.contact.person.required", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(3007, "error.email.format.invalid", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_FORMAT(3009, "error.phone.format.invalid", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String messageKey, HttpStatusCode statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String messageKey;
    private final HttpStatusCode statusCode;
}