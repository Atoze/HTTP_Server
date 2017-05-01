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

    Map<String, String> headerData = new HashMap<String, String>();

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

    public void readRequest(InputStream is) throws IOException {
        BufferedInputStream bi = new BufferedInputStream(is);
        bi.mark(REQUEST_HEAD_BYTE_LIMIT);
        BufferedReader br = new BufferedReader(new InputStreamReader(bi));

        StringBuilder text = new StringBuilder();
        String line = br.readLine();
        text.append(line);

        while (line != null && !line.isEmpty()) {
            text.append(line);
            String[] headerLineData = line.split(":", 2);
            if (headerLineData.length == 2) {
                this.addRequestData(headerLineData[0].toUpperCase(), headerLineData[1].trim());
            }
            line = br.readLine();
            System.out.print(line);
        }
        this.requestHeader = text.toString();

        //RequestBody処理
        if (line != null) {
            if (getHeaderParam("Content-Type") == null) {
                return;
            }
            line = br.readLine();
            String[] contentTypeKey = this.getHeaderParam("Content-Type").split("/", 2);
            if ("text".equals(contentTypeKey[0])) {
                StringBuilder body = new StringBuilder();
                while (line != null && !line.isEmpty()) {
                    body.append(line);
                    line = br.readLine();
                }
                this.requestBody = body.toString().trim();
                return;
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
}