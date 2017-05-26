package jp.co.topgate.atoze.web;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTPリクエストデータを読み込み、整理します.
 *
 * @author atoze
 */
public class HTTPRequest {
    private byte[] bodyFile;
    private InputStream bodyInput;
    private String bodyText;
    private Map<String, String> bodyQuery;

    private String header;
    private Map<String, String> headerQuery;

    private final String method;
    private final String filePath;
    private final String protocolVer;
    private final String host;

    private Map<String, String> headerField = new HashMap<>();


    HTTPRequest(String method, String filePath, String protocolVer, String host) {
        this.method = method;
        this.filePath = filePath;
        this.protocolVer = protocolVer;
        this.host = host;
    }

    void setHeader(String header, Map<String, String> headerField) {
        this.header = header;
        this.headerField = headerField;
    }

    void setBody(String bodyText, byte[] bodyFile) {
        this.bodyText = bodyText;
        this.bodyFile = bodyFile;
    }

    void setBody(String bodyText, InputStream input) {
        this.bodyText = bodyText;
        this.bodyInput = input;
    }

    void setHeaderQuery(Map<String, String> query) {
        this.headerQuery = query;
    }

    void setBodyQuery(Map<String, String> query) {
        this.bodyQuery = query;
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
        return headerField.getOrDefault(key.toUpperCase(), null);
    }

    /**
     * 要求するメッセージボディを返します.
     *
     * @return リクエストボディメッセージ
     */
    public String getBodyText() {
        return this.bodyText;
    }


    /**
     * 要求するメッセージボディを返します.
     *
     * @return リクエストボディメッセージ
     */
    /*
    public byte[] getBodyFile() {
        return this.bodyFile;
    }
*/
    public InputStream getBodyInput() {
        return bodyInput;
    }

    public Map<String, String> getQuery() {
        return generateQueryMap();
    }

    public String getMethod() {
        return this.method;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getHost() {
        return this.host;
    }

    public String getProtocolVer() {
        return this.protocolVer;
    }

    @NotNull
    private Map<String, String> generateQueryMap() {
        if (method.equals("GET")) {
            if (headerQuery != null) return headerQuery;
            else return new HashMap<>();
        } else {
            if (bodyQuery != null) return bodyQuery;
            else return new HashMap<>();
        }
    }
}