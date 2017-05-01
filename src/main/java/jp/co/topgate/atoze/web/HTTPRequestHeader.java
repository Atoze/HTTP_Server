package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

/**
 * 初期要求行(Initial Request Line)を受け取り、
 * メソッド名、要求するリソースのパス、HTTPプロトコルバージョンの決められた3要素を取得します.
 *
 * @author atoze
 */

class HTTPRequestHeader {
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

    private String headMethod = "";
    private String filePath;
    private String fileQuery;
    private String protocol;


    private final int REQUEST_HEADER_VALUE = 3;

    HTTPRequestHeader(String line) throws IOException {
        this.readRequestHeader(line);
    }

    private void readRequestHeader(String line) throws IOException {
        if (line == null) {
            return;
        }
        String headerLines[] = line.split(" ");
        if (headerLines.length == this.REQUEST_HEADER_VALUE) {
            if (isValidMethod(headerLines[0])) {
                this.headMethod = headerLines[0];
            }
            this.filePath = URLDecoder.decode(headerLines[1], "UTF-8");
            //this.filePath = uriQuerySplitter(urlDivider(this.filePath, host));


            if (headerLines[2].startsWith("HTTP/")) {
                this.protocol = headerLines[2];
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

    /**
     * 要求するHTTPメソッドを取得します.
     *
     * @return メソッド名
     */
    public String getMethod() {
        return this.headMethod;
    }

    /**
     * 要求するリソースのパスを取得します.
     *
     * @return リソースのパス
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * HTTPプロトコルのバージョンを取得します.
     *
     * @return HTTPプロトコルバージョン
     */
    public String getProtocol() {
        return this.protocol;
    }
}
