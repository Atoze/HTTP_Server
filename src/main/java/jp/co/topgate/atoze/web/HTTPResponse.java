package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * HTTPレスポンスを生成し、出力します.
 *
 * @author atoze
 */
public class HTTPResponse {
    private final static String HEADER_KEY_VALUE_SEPARATOR = ":";
    private final static String LINE_FEED = System.getProperty("line.separator");
    private final static int FILE_WRITE_BUFFER = 1024;
    private InputStream responseBody;
    private String responseBodyText;

    private int statusCode;
    private String statusMessage;
    private final StringBuilder response = new StringBuilder();
    private final Map<String, String> responseHeaders = new HashMap<>();

    private HTTPResponse() {
    }

    public HTTPResponse(Status status) {
        this(status.getCode(), status.getMessage());
    }

    public HTTPResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    /**
     * HTTPレスポンスボディを設定します.
     *
     * @param input ストリーム型レスポンスボディ
     */
    public void setResponseBody(InputStream input) {
        responseBody = input;
    }

    /**
     * HTTPレスポンスボディを設定します.
     *
     * @param text テキスト
     */
    public void setResponseBody(String text) {
        responseBodyText = text;
    }

    /**
     * HTTPレスポンスに新たなレスポンスヘッダを追加します.
     *
     * @param type  キー
     * @param value 値
     */
    public void addResponseHeader(String type, String value) {
        this.responseHeaders.put(type, value);
    }

    /**
     * 生成したHTTPレスポンスを書き込みます.
     *
     * @param out 書き込み先データストリーム
     * @throws RuntimeException 書き込みエラー
     */
    void writeTo(OutputStream out) {
        PrintWriter writer = new PrintWriter(out, true);
        this.response.append(generateStatusLine(statusCode, statusMessage));
        this.response.append(generateResponseHeader(responseHeaders));
        writer.println(this.response.toString());
        if (responseBodyText != null) {
            writer.println(responseBodyText);

        } else if (responseBody != null) {
            final byte[] buffer = new byte[FILE_WRITE_BUFFER];
            int count = 0;
            int n = 0;
            try {
                while (-1 != (n = responseBody.read(buffer))) {
                    out.write(buffer, 0, n);
                    count += n;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * responseHeader内に挿入されたデータからHTTPレスポンスヘッダを生成します.
     */
    private String generateResponseHeader(Map<String, String> responseHeaders) {
        StringBuffer sb = new StringBuffer();
        Set<String> keys = responseHeaders.keySet();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.toArray(new String[0])[i];
            sb.append(key).append(HEADER_KEY_VALUE_SEPARATOR).append(responseHeaders.get(key)).append(LINE_FEED);
        }
        return sb.toString();
    }

    /**
     * HTTPステータスラインを生成します.
     */
    private String generateStatusLine(int statusCode, String statusMessage) {
        StringBuffer sb = new StringBuffer();
        sb.append(Server.SERVER_PROTOCOL).append(" ");
        sb.append(statusCode).append(" ").append(statusMessage).append(LINE_FEED);
        return sb.toString();
    }

    /**
     * HTTPレスポンスを取得します.
     *
     * @return HTTPレスポンス
     */
    @Override
    public String toString() {
        return response.toString();
    }
}