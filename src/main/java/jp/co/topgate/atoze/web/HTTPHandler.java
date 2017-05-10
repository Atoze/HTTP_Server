package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by atoze on 2017/05/01.
 */
public abstract class HTTPHandler {

    HTTPRequest request;
    HTTPResponse response = new HTTPResponse();

    public void request(HTTPRequest request) throws IOException {
        System.out.println("\nRequest incoming..." + Thread.currentThread().getName());
        this.request = request;
    }

    public void response(OutputStream out) throws IOException {


    }

}