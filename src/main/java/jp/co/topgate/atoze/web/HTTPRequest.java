package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTPリクエストデータを読み込み、整理します.
 *
 * @author atoze
 */
class HTTPRequest {
    private String requestBody;
    private byte[] requestFile;
    private String requestHeader;

    private final int REQUEST_HEAD_BYTE_LIMIT = 1024;

    HTTPRequestLine request;
    Map<String, String> headerData = new HashMap<String, String>();
    Map<String, String> queryData = new HashMap<String, String>();

    private void addRequestData(String key, String value) {
        this.headerData.put(key, value);
    }

    /**
     * InputStreamより受け取ったHTTPリクエストを行ごとに分割し、保管します.
     * <p>
     * //* @param input HTTPリクエストのデータストリーム
     * //* @param host  HTTPホスト名
     *
     * @throws IOException ストリームデータ取得エラー
     */

    public void readRequest(InputStream is, String host) throws IOException {
        BufferedInputStream bi = new BufferedInputStream(is);
        bi.mark(REQUEST_HEAD_BYTE_LIMIT);
        BufferedReader br = new BufferedReader(new InputStreamReader(bi));

        StringBuilder text = new StringBuilder();
        String line = br.readLine();
        request = new HTTPRequestLine(line, host);

        while (line != null && !line.isEmpty()) {
            text.append(line);
            String[] headerLineData = line.split(":", 2);
            if (headerLineData.length == 2) {
                this.addRequestData(headerLineData[0].toUpperCase(), headerLineData[1].trim());
            }
            line = br.readLine();
        }
        this.requestHeader = text.toString();

        //RequestBody処理
        if (line == null || this.getHeaderParam("Content-Type") == null) {
            return;
        }
        if ("application/x-www-form-urlencoded".equals(this.getHeaderParam("Content-Type"))) {
            int num = Integer.parseInt(this.getHeaderParam("Content-Length"));
            char[] bodyText = new char[num];
            br.read(bodyText, 0, num);
            String[] queryData = String.valueOf(bodyText).split("&");
            System.out.println(queryData.length);
            for (int i = 0; i <= queryData.length - 1; i++) {
                String[] queryValue = queryData[i].split("=", 2);
                if (queryValue.length >= 2) {
                    this.queryData.put(queryValue[0], queryValue[1]);
                }
            }
            this.requestBody = String.valueOf(bodyText).trim();
        } else {
            bi.reset();
            int offset = 0;
            int bytesRead = 0;
            byte[] data = new byte[Integer.parseInt(this.getHeaderParam("Content-Length"))];
            bi.skip(requestHeader.getBytes().length + 1);
            while ((bytesRead = bi.read(data, offset, data.length - offset))
                    != -1) {
                offset += bytesRead;
                if (offset >= data.length) {
                    break;
                }
            }
            this.requestFile = data;
        }
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
    public String getMessageBody() {
        return this.requestBody;
    }

    /**
     * 要求するメッセージボディを返します.
     *
     * @return リクエストボディメッセージ
     */
    public byte[] getMessageFile() {
        return this.requestFile;
    }

    public String getParameter(String key) {
        return queryData.getOrDefault(key, null);
    }
}