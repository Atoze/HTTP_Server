package jp.co.topgate.atoze.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by atoze on 2017/04/12.
 */
public class HTTPHeader {
    private String headerText;

    public HTTPHeader(InputStream in) throws IOException {
        this.headerText = this.readRequestHeader(in);
        this.readRequestBodyLine(in);
    }

    private String readRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        StringBuilder header = new StringBuilder();

        while (line != null && !line.isEmpty()) {
            //System.out.println(line);
            header.append(line + "\r\n");
            line = br.readLine();
        }
        return header.toString();
    }

    private StringBuilder readRequestBodyLine(InputStream in){
        StringBuilder bodyText = new StringBuilder();
        return bodyText;
    }

    public String getHeaderText(){
        return this.headerText;
    }


}
