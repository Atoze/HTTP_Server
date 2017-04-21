package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTPリクエストデータを保持します.
 *
 * @author atoze
 */
class HTTPRequest {
    private String requestHeader;
    private String method;
    private String filePath;
    private String fileQuery;
    private String protocolVer;

    Map<String, String> headerData = new HashMap<String, String>();

    private void addRequestData(String key, String value) {
        this.headerData.put(key, value);
    }

    /**
     * InputStreamより受け取ったHTTPリクエストを行ごとに分割し、保管します.
     *
     * @param host HTTPホスト名
     */
    public void readRequestHeader(InputStream input, String host) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = br.readLine();

        this.readRequestLine(line, host);

        StringBuilder text = new StringBuilder();

        while (true) {
            while (line != null && !line.isEmpty()) {
                text.append(line).append("\n");
                String[] headerLineData = line.split(":", 2);
                if (headerLineData.length == 2) {
                    this.addRequestData(headerLineData[0].toUpperCase(), headerLineData[1].trim());
                }
                line = br.readLine();
            }



            if (!br.ready()) {
                break;
            }
        }

        this.requestHeader = text.toString();
    }

    private void readRequestLine(String line, String host) {
        HTTPRequestLine header = new HTTPRequestLine(line);
        this.method = header.getMethod();
        this.filePath = uriQuerySplitter(urlDivider(null, host));
        if (this.filePath.endsWith("/")) {
            this.filePath += "index.html";
        }
        this.protocolVer = ProtocolVer(header.getProtocol());
    }

    /**
     * HTTPリクエストヘッダを取得します.
     *
     * @return HTTPリクエストヘッダ
     */
    public String getRequestHeader() {
        return this.requestHeader;
    }

    /**
     * 要求するHTTPメソッドを取得します.
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

        String pattern = "http*.://";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(filePath);

        if (m.find()) {
            if (filePath.startsWith(m.group() + host)) {
                return filePath.substring(filePath.indexOf(host) + host.length());
            }
        }
        return filePath;
    }

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
     * 保管されたHTTPリクエストヘッダから、指定したキーに対応した値を取得します.
     *
     * @return 値
     */
    public String getHeaderParam(String value) {
        return headerData.getOrDefault(value.toUpperCase(), null);
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
        //if("HTTP/".startsWith(protocol)){
        if (protocol != null) {
            return protocol.substring(protocol.indexOf("HTTP/") + "HTTP/".length());
        } else {
            return null;
        }
        //}
    }

    /**
     * 要求するクエリ値を取得します.
     *
     * @return クエリ値
     */
    public String getFileQuery() {
        return this.fileQuery;
    }
}
