package jp.co.topgate.atoze.web;

import java.util.HashMap;

/**
 * Created by atoze on 2017/04/13.
 */
class Status {
    private int statusCode;
    private String status;
    private String statusMessage;

    private String StatusParameter(int status) {
        final HashMap<Integer, String> content = new HashMap<Integer, String>() {
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
        if (content.containsKey(status)) {
            statusMessage = content.get(status);
            return status + " " + content.get(status);
        } else {
            return this.status + "Unknown Status";
        }
    }
    /*

    public static void setStatusCode(int i) {
        statuscode = i;
    }
    */

    public void setStatus(int i) {
        this.statusCode = i;
        //setStatusCode(i);
        this.status = StatusParameter(i);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public String getStatus() {
        return this.status;
    }

}
