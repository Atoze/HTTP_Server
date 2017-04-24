package jp.co.topgate.atoze.web;

import java.util.HashSet;
import java.util.Set;

/**
 * 初期要求行(Initial Request Line)を受け取り、
 * メソッド名、要求するリソースのパス、HTTPプロトコルバージョンの決められた3要素を取得します.
 *
 * @author atoze
 */

class HTTPRequestLine {
    private final static Set<String> methods = new HashSet<>();

    static {
        methods.add("GET");
        methods.add("POST");
        methods.add("HEAD");
        methods.add("OPTIONS");
        methods.add("PUT");
        methods.add("DELETE");
        methods.add("TRACE");
    }

    private String headMethod;
    private String filePath;
    private String protocol;

    private final int REQUEST_HEADER_VALUE = 3;

    HTTPRequestLine(String line) {
        this.readRequestHeader(line);
    }

    private void readRequestHeader(String line) {
        if (line == null) {
            return;
        }
        String headerLines[] = line.split(" ");
        if (headerLines.length == this.REQUEST_HEADER_VALUE) {
            this.headMethod = rightMethod(headerLines[0]);
            this.filePath = headerLines[1];
            if (headerLines[2].startsWith("HTTP/")) {
                this.protocol = headerLines[2];
            }
        }
    }
    /**
     * 要求したメソッド名が存在しているか確認します.
     * メソッドが存在しない場合は取得しません.
     *
     * @return メソッド名
    */
    private String rightMethod(String method) {
        if (methods.contains(method)) {
            return method;
        }
        return null;
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
