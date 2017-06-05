package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.RequestBodyParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTPリクエストデータを読み込み、整理します.
 *
 * @author atoze
 */
public class HTTPRequest {
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private InputStream bodyInput;

    private String header;
    private Map<String, String> headerQueryParam;
    private Map<String, String> bodyQueryParam;

    private final String method;
    private final String path;
    private final String protocolVer;
    private final String host;

    private Map<String, String> headerField = new HashMap<>();


    HTTPRequest(String method, String path, String protocolVer, String host) {
        this.method = method;
        this.path = path;
        this.protocolVer = protocolVer;
        this.host = host;
    }

    void setHeader(String header, Map<String, String> headerField) {
        this.header = header;
        this.headerField = headerField;
    }

    void setBody(InputStream input) {
        this.bodyInput = input;
    }

    void setHeaderQueryParam(Map<String, String> queryParam) {
        this.headerQueryParam = queryParam;
    }

    /**
     * HTTPリクエストヘッダを取得します.
     *
     * @return HTTPリクエストヘッダ
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * 保管されたHTTPリクエストヘッダから、指定したキーに対応した値を取得します.
     *
     * @param key キー
     * @return 値
     */
    public String getHeaderParam(String key) {
        return headerField.getOrDefault(key, null);
    }

    public Map<String, String> getHeaderField() {
        return headerField;
    }

    public String getContentType() {
        return headerField.get(CONTENT_TYPE_KEY);
    }

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getHost() {
        return this.host;
    }

    public String getProtocolVer() {
        return this.protocolVer;
    }

    public InputStream getBodyInput() {
        return bodyInput;
    }

    public String getBodyText() throws IOException, RequestBodyParseException {
        HTTPRequestBody body = new HTTPRequestBody(bodyInput, headerField);
        return body.getBodyText();
    }

    public byte[] getBodyFile() throws IOException, RequestBodyParseException {
        HTTPRequestBody body = new HTTPRequestBody(bodyInput, headerField);
        return body.getBodyFile();
    }

    public Map<String, String> getFormQueryParam() throws IOException, RequestBodyParseException {
        return generateQueryParam();
    }

    private Map<String, String> generateQueryParam() throws IOException, RequestBodyParseException {
        switch (method) {
            case "GET":
                return headerQueryParam;
            case "POST":
                HTTPRequestBody body = new HTTPRequestBody(bodyInput, headerField);
                bodyQueryParam = body.getQueryParam();
                return bodyQueryParam;
            default:
                return new HashMap<>();
        }
    }

    public Map<String, String> getUriQueryParam() {
        return headerQueryParam;
    }
}