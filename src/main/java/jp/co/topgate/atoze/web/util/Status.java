package jp.co.topgate.atoze.web.util;

import org.jetbrains.annotations.Contract;

/**
 * Created by atoze on 2017/06/01.
 */
public enum Status {
    OK(200, "OK"),
    SEE_OTHER(300, "See Other"),

    NOT_MODIFIED(304, "Not Modified"),

    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

    private int statusCode;
    private String statusMessage;

    Status(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    @Contract(pure = true)
    public int getCode() {
        return statusCode;
    }

    @Contract(pure = true)
    public String getMessage() {
        return statusMessage;
    }
}