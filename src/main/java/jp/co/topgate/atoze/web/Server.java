package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.app.forum.ForumAppHandler;
import jp.co.topgate.atoze.web.exception.StatusBadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * HTTPリクエストに応じた処理を行います.
 *
 * @author atoze
 */
public class Server extends Thread {
    private final Socket socket;
    private final int PORT;
    private static final String HOST_NAME = "localhost";
    static final String ROOT_DIRECTORY = "./src/main/resources";

    private final static Set<String> SUPPORTED_PROTOCOL_VERSION = new HashSet<>();

    static {
        SUPPORTED_PROTOCOL_VERSION.add("1.0");
        SUPPORTED_PROTOCOL_VERSION.add("1.1");
    }

    public Server(Socket socket, int PORT) {
        this.PORT = PORT;
        this.socket = socket;
    }

    /**
     * HTTPリクエストに応じた処理を行います.
     */

    @Override
    public void run() {
        System.out.println("\nRequest incoming..." + Thread.currentThread().getName());
        OutputStream output = null;
        try {
            InputStream input = socket.getInputStream();
            output = socket.getOutputStream();
            HTTPRequest httpRequest = HTTPRequestParser.parse(input, HOST_NAME + ":" + PORT);
            System.out.println(httpRequest.getHeader());
            System.out.println(httpRequest.getBodyText());
            checkValidRequest(httpRequest);

            HTTPHandler handler;
            String filePath = httpRequest.getFilePath();

            if (filePath.startsWith(URLPattern.PROGRAM_BOARD.getURL())) {
                handler = new ForumAppHandler(httpRequest);
            } else {
                handler = new StaticHandler(httpRequest);
            }
            handler.writeResponse(output);

        } catch (StatusBadRequestException e) {
            if (output != null) {
                HTTPResponse response = new HTTPResponse(400);
                response.writeTo(output);
            }
        } catch (StatusProtocolException e) {
            if (output != null) {
                HTTPResponse response = new HTTPResponse(505);
                response.writeTo(output);
            }
        } catch (NullPointerException | IOException e) {
            if (output != null) {
                HTTPResponse response = new HTTPResponse(500);
                response.writeTo(output);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Disconnected" + Thread.currentThread().getName());
        }
    }

    private void checkValidRequest(HTTPRequest request) throws StatusBadRequestException, StatusProtocolException {
        String protocolVer = request.getProtocolVer();
        if (!SUPPORTED_PROTOCOL_VERSION.contains(protocolVer)) throw new StatusProtocolException();
        if (protocolVer.equals("1.1") && !request.getHeaderParam("Host").equals(request.getHost()))
            throw new StatusBadRequestException();
    }
}

class StatusProtocolException extends Exception {
    StatusProtocolException() {
        super();
    }
}