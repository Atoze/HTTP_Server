package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.Status;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * HTTPResponseにContent-Typeが容易になるHTTPResponse拡張クラス
 */
public class ExtendedHTTPResponse extends HTTPResponse {
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CHARSET_KEY = "; charset=";

    public ExtendedHTTPResponse(Status status) {
        super(status);
    }

    public ExtendedHTTPResponse(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }

    public void setResponseBody(File file, String contentType) throws FileNotFoundException {
        setResponseBody(new FileInputStream(file));
        setContentType(contentType);
    }

    public void setResponseBody(File file, String contentType, String charset) throws FileNotFoundException {
        setResponseBody(new FileInputStream(file));
        setContentType(contentType, charset);
    }

    public void setContentType(String contentType) {
        addResponseHeader(CONTENT_TYPE_KEY, contentType);
    }

    public void setContentType(String contentType, String charset) {
        addResponseHeader(CONTENT_TYPE_KEY, createHeaderWithCharset(contentType, charset));
    }

    @NotNull
    private String createHeaderWithCharset(String contentType, String charset) {
        StringBuffer sb = new StringBuffer();
        sb.append(contentType);
        sb.append(CHARSET_KEY);
        sb.append(charset);
        return sb.toString();
    }
}
