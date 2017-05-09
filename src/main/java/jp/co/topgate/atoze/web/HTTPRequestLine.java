package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by atoze on 2017/05/01.
 */
public class HTTPRequestLine {
    private String method;
    private String filePath;
    private String fileQuery;
    private String protocolVer;

    private final static Set<String> METHODS = new HashSet<>();

    static {
        METHODS.add("GET");
        METHODS.add("POST");
        METHODS.add("HEAD");
        METHODS.add("OPTIONS");
        METHODS.add("PUT");
        METHODS.add("DELETE");
        METHODS.add("TRACE");
    }

    private final int REQUEST_HEADER_VALUE = 3;

    HTTPRequestLine(String line, String host) throws IOException {
        this.readRequestLine(line, host);
    }

    /**
     * HTTPジェネラルヘッダを読み込み、整理します.
     *
     * @param line
     * @param host
     */
    private void readRequestLine(String line, String host) throws IOException {
        this.readRequestHeader(line);

        this.filePath = uriQuerySplitter(urlDivider(this.filePath, host));
        if (this.filePath.endsWith("/")) {
            this.filePath += "index.html";
        }
        this.protocolVer = ProtocolVer(this.protocolVer);
    }

    /**
     * 要求するHTTPメソッドを取得します.
     * ,
     *
     * @return メソッド名
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * 要求するリソースのローカルパスを取得します.
     *
     * @return ファイルパス
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * 絶対パスからホスト名を抜き、相対パスにします.
     *
     * @param filePath
     * @param host
     * @return ファイルパス
     */
    private String urlDivider(String filePath, String host) {
        if (filePath == null) {
            return "";
        }
        if (filePath.startsWith("http://" + host)) {
            return filePath.substring(filePath.indexOf(host) + host.length());
        }
        return filePath;
    }

    /**
     * ファイルパスとクエリデータを分割します.
     *
     * @param filePath
     * @return ファイルパス
     */
    private String uriQuerySplitter(String filePath) {
        if (filePath == null) {
            return "";
        }

        String urlQuery[] = filePath.split("\\?", 2);
        if (urlQuery[0] != filePath) {
            this.fileQuery = urlQuery[1];
        }
        return urlQuery[0];
    }

    /**
     * HTTPプロトコルのバージョンの値を取得します.
     *
     * @return HTTPプロトコルバージョンの値
     */
    public String getProtocolVer() {
        return this.protocolVer;
    }

    private String ProtocolVer(String protocol) {
        if (protocol != null) {
            return protocol.substring(protocol.indexOf("HTTP/") + "HTTP/".length());
        } else {
            return null;
        }
    }

    /**
     * 要求するクエリ値を取得します.
     *
     * @return クエリ値
     */
    public String getFileQuery() {
        return this.fileQuery;
    }

    private void readRequestHeader(String line) throws IOException {
        if (line == null) {
            return;
        }
        String headerLines[] = line.split(" ");
        if (headerLines.length == this.REQUEST_HEADER_VALUE) {
            if (isValidMethod(headerLines[0])) {
                this.method = headerLines[0];
            }
            this.filePath = URLDecoder.decode(headerLines[1], "UTF-8");
            if (headerLines[2].startsWith("HTTP/")) {
                this.protocolVer = headerLines[2];
            }
        }
    }

    /**
     * 要求したメソッド名が存在しているか確認します.
     *
     * @return 有効なメソッド名であるか
     */
    private boolean isValidMethod(String method) {
        if (METHODS.contains(method)) {
            return true;
        }
        return false;
    }
}
