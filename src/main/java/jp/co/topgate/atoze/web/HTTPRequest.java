package jp.co.topgate.atoze.web;

import java.io.BufferedInputStream;
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
    //private String requestBody;
    private byte[] requestBodyFile;
    private String requestBodyQuery;

    private String requestHeader;
    private String requestHeaderQuery;
    private String method;
    private String filePath;
    private String protocolVer;

    private String host;

    private Map<String, String> headerData = new HashMap<>();
    private Map<String, String> queryData = new HashMap<>();


    private final static String lineFeed = System.getProperty("line.separator");

    /**
     * InputStreamより受け取ったHTTPリクエストを行ごとに分割し、保管します.
     *
     * @param input HTTPリクエストのデータストリーム
     * @param host  HTTPホスト名
     * @throws IOException ストリームデータ取得エラー
     */

    public void readRequest(InputStream input, String host) throws IOException {
        this.host = host;
        BufferedInputStream bi = new BufferedInputStream(input);
        StringBuilder text = new StringBuilder();
        String line = readLine(bi);

        HTTPRequestLine httpRequestLine = new HTTPRequestLine();
        httpRequestLine.readRequestLine(line, host);

        method = httpRequestLine.getMethod();
        requestHeaderQuery = httpRequestLine.getHeadQuery();
        filePath = httpRequestLine.getFilePath();
        protocolVer = httpRequestLine.getProtocolVer();

        while (line != null && !line.isEmpty()) {
            text.append(line).append(lineFeed);
            String[] headerLineData = line.split(":", 2);
            if (headerLineData.length == 2) {
                this.headerData.put(headerLineData[0].toUpperCase(), headerLineData[1].trim());
            }
            line = readLine(bi);
        }
        this.requestHeader = text.toString();

        //RequestBody処理
        String contentType = getHeaderParam("Content-Type");
        if (line == null || contentType == null) {
            return;
        }
        int contentLength = Integer.parseInt(getHeaderParam("Content-Length"));
        HTTPRequestBody requestBody = new HTTPRequestBody(bi, contentType, contentLength);
        requestBodyQuery = requestBody.getBodyText();
        requestBodyFile = requestBody.getBodyFile();

        this.queryData = generateQueryMap();
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
     * 保管されたHTTPリクエストヘッダから、指定したキーに対応した値を取得します.
     *
     * @param key キー
     * @return 値
     */
    public String getHeaderParam(String key) {
        return headerData.getOrDefault(key.toUpperCase(), null);
    }


    /**
     * 要求するメッセージボディを返します.
     *
     * @return リクエストボディメッセージ
     */

    public String getRequestText() {
        return this.requestBodyQuery;
    }

    /**
     * 要求するメッセージボディを返します.
     *
     * @return リクエストボディメッセージ
     */
    public byte[] getRequestBodyFile() {
        return this.requestBodyFile;
    }


    public String getQueryParam(String key) {
        return queryData.getOrDefault(key, "");
    }

    public Map<String, String> getQuery() {
        return queryData;
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


    private Map<String, String> generateQueryMap() {
        String body;
        if (method.equals("GET")) body = requestHeaderQuery;
        else body = requestBodyQuery;

        Map<String, String> queryData = new HashMap<>();
        String[] data = body.split("&");
        for (int i = 0; i <= data.length - 1; i++) {
            String[] queryValue = data[i].split("=", 2);
            if (queryValue.length >= 2) {
                queryData.put(queryValue[0], queryValue[1]);
            }
        }
        return queryData;
    }

    //TODO 改行コードが"\r"のみの対応
    private String readLine(InputStream input) throws IOException {
        int num = 0;
        StringBuffer sb = new StringBuffer();
        boolean r = false;
        try {
            while ((num = input.read()) >= 0) {
                sb.append((char) num);
                String line = sb.toString();
                switch ((char) num) {
                    case '\r':
                        r = true;
                        break;
                    case '\n':
                        if (r) {
                            line = line.replace("\r", "");
                        }
                        line = line.replace("\n", "");
                        return line;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (sb.length() == 0) {
            return null;
        } else {
            return sb.toString();
        }
    }
}