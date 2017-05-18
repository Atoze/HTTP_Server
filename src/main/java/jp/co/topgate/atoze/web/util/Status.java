package jp.co.topgate.atoze.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ステータスコードを受け取り、それに対応したステータスメッセージを返します.
 *
 * @author atoze
 */
public class Status {
    private int statusCode;
    private String status;
    private String statusMessage;

    private static final Map<Integer, String> STATUS = new HashMap<Integer, String>() {
        {
            put(200, "OK");

            put(304, "NotModified");

            put(400, "Bad Request");
            put(403, "Forbidden");
            put(404, "Not Found");
            put(405, "Method Not Allowed");
            put(408, "Request Timeout");
            put(411, "Length Required");

            put(500, "Internal Server Error");
            put(503, "Service Unavailable");
            put(504, "Gateway Timeout");
            put(505, "HTTP Version Not Supported");
        }
    };

    private String getStatusParameter(int statusCode) {
        if (STATUS.containsKey(statusCode)) {
            statusMessage = STATUS.get(statusCode);
            return statusCode + " " + STATUS.get(statusCode);
        } else {
            return this.statusCode + " Unknown Status";
        }
    }

    /**
     * ステータス値を設定します.
     *
     * @param i ステータスコード
     */
    public void setStatus(int i) {
        this.statusCode = i;
        this.status = getStatusParameter(i);
    }

    /**
     * 現在設定されているステータスのコードを取得します.
     *
     * @return コード
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * 現在設定されているステータスのメッセージを取得します.
     *
     * @return メッセージ
     */
    public String getStatusMessage() {
        return this.statusMessage;
    }

    /**
     * 現在設定されているステータスを取得します.
     *
     * @return ステータス
     */
    public String getStatus() {
        return this.status;
    }

}
