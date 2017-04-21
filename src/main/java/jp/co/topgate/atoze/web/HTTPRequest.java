package jp.co.topgate.atoze.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTPリクエストデータを保持します.
 * @author atoze
 */
class HTTPRequest {
    private String headerText;
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
     * またRequestLineクラスにRequestLineを送り、受け取った３要素のローカルパスとHTTPプロトコルバージョンを分析します.
     * @param host HTTPホスト+ドメイン名
     */
    public void readRequestText(InputStream input, String host) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = br.readLine();

        HTTPRequestLine header = new HTTPRequestLine(line);

        StringBuilder text = new StringBuilder();
        while (line != null && !line.isEmpty()) {
            text.append(line).append("\n");
            String[] headerLineData = line.split(":", 2);
            if (headerLineData.length == 2) {
                this.addRequestData(headerLineData[0].toUpperCase(), headerLineData[1].trim());
            }
            line = br.readLine();
        }

        this.headerText = text.toString();
        this.method = header.getMethod();
        this.filePath = uriQuerySplitter(urlDivider(header.getFilePath(), host));
        this.protocolVer = ProtocolVer(header.getProtocol());
    }

    /**
     * HTTPリクエストヘッダを取得します.
     * @return HTTPリクエストヘッダ
     */
    public String getHeaderText() {
        return this.headerText;
    }

    /**
     * 要求するHTTPメソッドを取得します.
     * @return メソッド名
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * 要求するリソースのローカルパスを取得します.
     * @return ファイルパス
     */
    public String getFilePath() {
        return this.filePath;
    }

    private String urlDivider(String filePath, String host) {
        if (filePath == null) {
            return null;
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
            return null;
        }

        String urlQuery[] = filePath.split("\\?", 2);
        if (urlQuery[0] != filePath) {
            this.fileQuery = urlQuery[1];
        }
        return urlQuery[0];
    }

    /**
     * 保管されたHTTPリクエストヘッダから、指定したキーに対応した値を取得します.
     * @return 値
     */
    public String getRequestValue(String value) {
        return headerData.getOrDefault(value, null);
    }

    /**
     * HTTPプロトコルのバージョンの値を取得します.
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
     * @return クエリ値
     */
    public String getFileQuery() {
        return this.fileQuery;
    }
}
