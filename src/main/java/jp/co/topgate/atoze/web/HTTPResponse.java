package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.Status;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTPレスポンスを生成し、出力します.
 *
 * @author atoze
 */
public class HTTPResponse {
    private String responseBodyText;
    private File responseBodyFile;
    private StringBuilder response = new StringBuilder();
    private Map<String, String> responseHeaders = new HashMap<>();

    /**
     * HTTPレスポンスボディを設定します.
     *
     * @param text テキスト
     */
    public void setResponseBody(String text) {
        this.responseBodyText = text;
    }

    /**
     * HTTPレスポンスボディにファイルを設定します.
     *
     * @param file ファイル
     */
    public void setResponseBody(File file) {
        this.responseBodyFile = file;
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
    public void writeTo(OutputStream out, int statusCode) {
        PrintWriter writer = new PrintWriter(out, true);
        Status status = new Status();
        status.setStatus(statusCode);
        this.response.append("HTTP/1.1 " + status.getStatus() + "\n");

        this.responseHeaders.forEach((key, value) -> {
            this.response.append(key + ": " + value + "\n");
        });

        if (this.responseBodyText != null) {
            this.response.append("\n").append(this.responseBodyText + "\n");
        }
        writer.println(this.response.toString());

        if (this.responseBodyFile != null) {
            try {
                BufferedInputStream bi
                        = new BufferedInputStream(new FileInputStream(this.responseBodyFile));

                for (int c = bi.read(); c >= 0; c = bi.read()) {
                    out.write(c);
                }
                bi.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * HTTPレスポンスを取得します.
     *
     * @return HTTPレスポンス
     */
    public String getResponse() {
        return response.toString();
    }

}