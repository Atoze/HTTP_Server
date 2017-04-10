package jp.co.topgate.atoze.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 簡易HTTPサーバ
 *
 */
public class Server {

    //private static final int PORT = 8080;
    private static int PORT= 8080;

    public int getPort(int portnum) {
        this.PORT = portnum;
        return PORT;
    }


    public static void main(String[] args){

    }
}
