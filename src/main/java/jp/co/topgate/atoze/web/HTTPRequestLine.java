package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

/**
 * HTTPリクエストのジェネラルラインのデータを読み取ります.
 */
class HTTPRequestLine {
    private String method;
    private String filePath;
    private String headQuery;
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

    HTTPRequestLine() {
    }

    /**
     * HTTPジェネラルヘッダを読み込み、整理します.
     *
     * @param line
     * @param host
     */
    void readRequestLine(String line, String host) throws IOException {
        if (line == null) {
            return;
        }
        this.readRequestHeader(line);

        this.filePath = uriQuerySplitter(urlDivider(this.filePath, host));
        if (this.filePath.endsWith("/")) {
            this.filePath += "index.html";
        }
        protocolVer = ProtocolVer(protocolVer);
    }

    /**
     * 要求するHTTPメソッドを取得します.
     *
     * @return メソッド名
     */
    String getMethod() {
        return this.method;
    }

    /**
     * 要求するリソースのローカルパスを取得します.
     *
     * @return ファイルパス
     */
    String getFilePath() {
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
            this.headQuery = urlQuery[1];
        }
        return urlQuery[0];
    }

    /**
     * HTTPプロトコルのバージョンの値を取得します.
     *
     * @return HTTPプロトコルバージョンの値
     */
    String getProtocolVer() {
        return this.protocolVer;
    }

    private String ProtocolVer(String protocol) {
        if (protocol != null) {
            if (protocol.startsWith("HTTP/"))
                return protocol.substring(protocol.indexOf("HTTP/") + "HTTP/".length());
        }
        return null;
    }

    /**
     * 要求するクエリ値を取得します.
     *
     * @return クエリ値
     */
    public String getHeadQuery() {
        return this.headQuery;
    }

    private void readRequestHeader(String line) throws IOException {
        if (line == null) {
            return;
        }
        String headerLines[] = line.split(" ");
        if (headerLines.length == REQUEST_HEADER_VALUE) {
            if (isValidMethod(headerLines[0])) {
                method = headerLines[0];
            }
            this.filePath = URLDecoder.decode(headerLines[1], "UTF-8");
            if (headerLines[2].startsWith("HTTP/")) {
                protocolVer = headerLines[2];
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
