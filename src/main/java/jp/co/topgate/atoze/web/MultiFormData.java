package jp.co.topgate.atoze.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atoze on 2017/05/16.
 */
class MultiFormData {

    private List<Map<String, String>> contentData = new ArrayList<>();
    private List<byte[]> fileByte = new ArrayList<>();

    public int pos = 0;

    MultiFormData(InputStream input, int length) throws IOException {
        readMulti(input, length);
    }

    private void readMulti(InputStream input, int length) throws IOException {
        BufferedInputStream bi = new BufferedInputStream(input);
        String line = readLine(bi);
        String separator = line;

        Map<String, String> contentData = readMultiBodyFile2(bi);
        byte[] file = readBodyFile(bi, length, separator);
        this.contentData.add(contentData);
        this.fileByte.add(file);
        readLine(bi);

        while (pos < length) {
            contentData = readMultiBodyFile2(bi);
            file = readBodyFile(bi, length, separator);
            this.contentData.add(contentData);
            this.fileByte.add(file);
            readLine(bi);
        }
    }
    /*

    public void getRequestFile(String value) {
        ContentType.getKey(value);

        for (int i = 0; i < this.contentData.size(); i++) {
            //Map<String, String> valueData=;
        }
        this.contentData.size();

    }*/

    public byte[] getByteData(int id) {
        return fileByte.get(id);
    }

    public String getParameter(int id, String key) {
        return (this.contentData.get(id)).get(key);
    }

    private Map<String, String> readMultiBodyFile2(InputStream input) throws IOException {
        Map<String, String> value = new HashMap<>();
        //StringBuilder text = new StringBuilder();
        String line = readLine(input);
        while (line != null && !line.isEmpty()) {
            //text.append(line);
            String[] values = line.split(":", 2);
            if (values.length >= 2) {
                value.put(values[0], values[1].trim());
            }
            line = readLine(input);
        }
        return value;
    }

    public byte[] readMultiBodyFile(InputStream input, int length) throws IOException {
        BufferedInputStream bi = new BufferedInputStream(input);
        StringBuilder text = new StringBuilder();
        String line = readLine(bi);
        String separator = line;

        while (line != null && !line.isEmpty()) {
            text.append(line);
            String[] multiFormData = line.split(":", 2);
            if (multiFormData.length == 2) {
                //multiForm.put(multiFormData[0], multiFormData[1].trim());
            }
            line = readLine(bi);
        }
        return readBodyFile(bi, length, separator);
    }

    private byte[] readBodyFile(InputStream input, int length, String boundary) throws IOException {
        List<Byte> buffer = new ArrayList<>();
        int separate = boundary.getBytes().length;
        int i = 0;
        int a;

        while (pos < length) {
            a = input.read();
            pos++;
            if (separate <= buffer.size()) {
                StringBuffer sb = new StringBuffer();
                byte[] separator = new byte[separate];
                for (int x = 0; x < separate; x++) {
                    separator[x] = buffer.get(buffer.size() + x - separate);
                    sb.append((char) separator[x]);
                }
                if (sb.toString().endsWith(boundary)) {
                    break;
                }
            }
            buffer.add((byte) a);
            i++;
        }
        if (i >= separate) i = i - separate;
        buffer = buffer.subList(0, i);

        byte[] fileByte = new byte[i];
        for (int size = 0; size < i; size++) {
            fileByte[size] = buffer.get(size);
        }
        return fileByte;
    }

    private String readLine(InputStream input) throws IOException {
        int num = 0;
        StringBuffer sb = new StringBuffer();
        boolean r = false;
        try {
            while ((num = input.read()) >= 0) {
                pos++;
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
