package jp.co.topgate.atoze.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atoze on 2017/05/24.
 */
public class ParseUtil {
    public static Map<String, String> parseQueryData(String body) {
        Map<String, String> queryData = new HashMap<>();
        if (body == null) {
            return null;
            //return queryData;
        }
        String[] data = body.split("&");
        for (int i = 0; i <= data.length - 1; i++) {
            String[] queryValue = data[i].split("=", 2);
            if (queryValue.length >= 2) {
                queryData.put(queryValue[0], queryValue[1]);
            }
        }
        return queryData;
    }

    public static String readLine(InputStream input) throws IOException {
        if (input == null) {
            return null;
        }
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
