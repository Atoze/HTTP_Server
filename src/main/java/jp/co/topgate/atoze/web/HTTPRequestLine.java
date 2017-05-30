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
    private String path;
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

        path = uriQuerySplitter(urlDivider(uri, host));
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
     * 要求するリソースのパスを取得します.
     *
     * @return パス
     */
    String getPath() {
        return this.path;
    }


    /**
     * 要求するリソースのURIを取得します.
     *
     * @return URI
     */
    String getURI() {
        return this.uri;
    }

    /**
     * 絶対パスからホスト名を抜き、相対パスにします.
     *
     * @param uri
     * @param host
     * @return パス
     */
    @Contract("null, _ -> !null")
    private String urlDivider(String uri, String host) {
        if (uri == null) {
            return "";
        }
        if (uri.startsWith("http://" + host)) {
            return uri.substring(uri.indexOf(host) + host.length());
        }
        return uri;
    }

    /**
     * URIからパスとクエリデータを分割します.
     *
     * @param uri
     * @return パス
     */
    private String uriQuerySplitter(String uri) {
        if (uri == null) {
            return "";
        }

        String uriQuery[] = uri.split("\\?", 2);
        if (uriQuery[0] != uri) {
            this.query = ParseUtil.parseQueryData(uriQuery[1]);
        }
        return uriQuery[0];
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
