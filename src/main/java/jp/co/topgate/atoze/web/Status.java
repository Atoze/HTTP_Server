package jp.co.topgate.atoze.web;

import java.util.HashMap;

/**
 * Created by atoze on 2017/04/13.
 */
class Status {
    private static Integer status;
    private static String statusMessage;

    //public Status() {
    //}

    private static String StatusParameter(int status) {
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
            return status + " " + content.get(status);
        } else {
            return "Unknown Error";
        }
    }

    public static void setStatusCode(int i) {
        status = i;
    }

    public static void setStatus(int i){
        statusMessage = StatusParameter(i);
    }

    public static String getStatus() {
        setStatus(status);
        return statusMessage;
    }

}
