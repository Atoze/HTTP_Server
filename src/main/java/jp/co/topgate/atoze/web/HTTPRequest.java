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
    private byte[] requestFile;
    private String requestText;
    private String requestHeader;

    private String method;
    private String headQuery;
    private String filePath;
    private String protocolVer;

    private String host;

    private Map<String, String> headerData = new HashMap<>();
    private Map<String, String> queryData = new HashMap<>();

    private String headerQuery;
    private String query;
    private String bodyQuery;

    private final static String lineFeed = System.getProperty("line.separator");

    /**
     * InputStreamより受け取ったHTTPリクエストを行ごとに分割し、保管します.
     * <p>
     * //* @param input HTTPリクエストのデータストリーム
     * //* @param host  HTTPホスト名
     *
     * @throws IOException ストリームデータ取得エラー
     */

    public void readRequest(InputStream is, String host) throws IOException {
        this.host = host;
        BufferedInputStream bi = new BufferedInputStream(is);
        StringBuilder text = new StringBuilder();
        String line = readLine(bi);
        HTTPRequestLine httpRequestLine = new HTTPRequestLine(line, host);
        method = httpRequestLine.getMethod();
        //headQuery = httpRequestLine.getHeadQuery();
        headerQuery = httpRequestLine.getHeadQuery();//TODO
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
        requestText = requestBody.getBodyText();
        //bodyQuery = requestBody.getQuery();
        //queryData = requestBody.getQueryData();

        requestFile = requestBody.getBodyFile();
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
        return this.requestText;
    }

    /**
     * 要求するメッセージボディを返します.
     *
     * @return リクエストボディメッセージ
     */
    public byte[] getRequestFile() {
        return this.requestFile;
    }

    public String getParameter(String key) {
        String body = "";
        if ("GET".equals(method))body=headerQuery;
        if ("POST".equals(method))body=requestText;

        return readQueryData(body).getOrDefault(key, "");
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

    public String getHeadQuery() {
        return this.headQuery;
    }

    private Map<String, String> readQueryData(String body) {
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