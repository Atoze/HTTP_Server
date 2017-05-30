package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.StatusBadRequestException;
import jp.co.topgate.atoze.web.util.ParseUtil;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * HTTPリクエストのジェネラルラインのデータを読み取ります.
 */
class HTTPRequestLine {
    private String method;
    private String uri;
    private String filePath;
    private Map<String, String> query;
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

    HTTPRequestLine(InputStream input, String host) throws IOException, StatusBadRequestException {
        String line = ParseUtil.readLine(input);
        readRequest(line, host);
    }

    HTTPRequestLine(String line, String host) throws IOException, StatusBadRequestException {
        readRequest(line, host);
    }

    /**
     * HTTPジェネラルヘッダを読み込み、整理します.
     *
     * @param line
     * @param host
     */
    private void readRequest(String line, String host) throws IOException, StatusBadRequestException {
        if (line == null) {
            return;
        }
        parse(line);

        filePath = uriQuerySplitter(urlDivider(uri, host));
        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }
        checkIsValidMethod(method);
        protocolVer = checkProtocolVer(protocolVer);
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
     * 要求するリソースのURIを取得します.
     *
     * @return ファイルパス
     */
    String getURI() {
        return this.uri;
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
    @Contract("null, _ -> !null")
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
            this.query = ParseUtil.parseQueryData(urlQuery[1]);
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

    private String checkProtocolVer(String protocol) throws StatusBadRequestException {
        if (protocol != null) {
            if (protocol.startsWith("HTTP/"))
                return protocol.substring(protocol.indexOf("HTTP/") + "HTTP/".length());
        }
        throw new StatusBadRequestException("プロトコルが正しく書かれていません");
    }

    /**
     * 要求するクエリ値を取得します.
     *
     * @return クエリ値
     */
    public Map<String, String> getQuery() {
        return this.query;
    }

    private void parse(String line) throws IOException {
        if (line == null) {
            return;
        }
        String headerLines[] = line.split(" ");
        if (headerLines.length == REQUEST_HEADER_VALUE) {
            method = headerLines[0];
            uri = URLDecoder.decode(headerLines[1], "UTF-8");
            protocolVer = headerLines[2];
        }
    }

    /**
     * 要求したメソッド名が存在しているか確認します.
     */
    private void checkIsValidMethod(String method) throws StatusBadRequestException {
        if (!METHODS.contains(method)) {
            throw new StatusBadRequestException("有効なメソッドではありません");
        }
    }
}
