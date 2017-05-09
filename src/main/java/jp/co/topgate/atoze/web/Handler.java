package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by atoze on 2017/05/01.
 */
public abstract class Handler {

    HTTPRequest request;
    HTTPResponse response = new HTTPResponse();

    public void request(HTTPRequest request) throws IOException {
        System.out.println("\nRequest incoming..." + Thread.currentThread().getName());
        this.request = request;
        //request.readRequest(input);
        //filePath=request.getFilePath();
        //System.out.println(request.getRequestHeader());
        //System.out.println(request.getMessageBody());
    }

    public void response(OutputStream out) throws IOException {


    }

}