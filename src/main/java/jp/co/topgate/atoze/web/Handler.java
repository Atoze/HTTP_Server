package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by atoze on 2017/05/01.
 */
public abstract class Handler {

    HTTPRequest request = new HTTPRequest();
    HTTPResponse response = new HTTPResponse();

    String HOST;

    public void request(InputStream input, String host) throws IOException {
        request.readRequest(input);
        HOST = host;
        //filePath=request.getFilePath();

        System.out.println("\nRequest incoming..." + Thread.currentThread().getName());
        System.out.println(request.getRequestHeader());
        System.out.println(request.getMessageBody());
    }

    public void response(OutputStream out) throws IOException {


    }

}