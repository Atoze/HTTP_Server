package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTPレスポンスを生成し、出力します.
 *
 * @author atoze
 */
class HTTPResponse {
    private String requestBodyText;
    private File requestBodyFile;
    private StringBuilder response = new StringBuilder();
    private Map<String, String> responseHeaders = new HashMap<>();

    /**
     * HTTPレスポンスボディを設定します.
     *
     * @param text テキスト
     */
    public void setResponseBody(String text) {
        this.requestBodyText = text;
    }

    /**
     * HTTPレスポンスボディにファイルを設定します.
     *
     * @param file ファイル
     */
    public void setResponseBody(File file) {
        this.requestBodyFile = file;
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
     * @param out    書き込み先データストリーム
     * @param status ステータスクラス
     * @throws IOException 書き込みエラー
     */
    public void writeTo(OutputStream out, Status status) throws IOException {
        PrintWriter writer = new PrintWriter(out, true);

        this.response.append("HTTP/1.1 " + status.getStatus() + "\n");

        this.responseHeaders.forEach((key, value) -> {
            this.response.append(key + ": " + value + "\n");
        });

        if (this.requestBodyText != null) {
            this.response.append("\n").append(this.requestBodyText + "\n");
        }
        writer.println(this.response.toString());

        if (this.requestBodyFile != null) {
            BufferedInputStream bi
                    = new BufferedInputStream(new FileInputStream(this.requestBodyFile));
            try {
                for (int c = bi.read(); c >= 0; c = bi.read()) {
                    out.write(c);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (bi != null) {
                    bi.close();
                }
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