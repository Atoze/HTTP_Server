package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.app.board.IndexHandler;
import jp.co.topgate.atoze.web.exception.BadRequestException;
import jp.co.topgate.atoze.web.exception.InternalServerErrorException;
import jp.co.topgate.atoze.web.exception.ProtocolException;
import jp.co.topgate.atoze.web.util.Status;

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
    static final String SERVER_PROTOCOL = "HTTP/1.1";

    public static final String BOARD_APP_DIRECTORY = "/program/board/";

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
        HTTPResponse response = null;
        try {
            InputStream input = socket.getInputStream();
            output = socket.getOutputStream();
            HTTPRequest httpRequest = HTTPRequestParser.parse(input, HOST_NAME + ":" + PORT);
            System.out.println(httpRequest.getHeader());
            checkValidRequest(httpRequest);

            HTTPHandler handler = null;
            String path = httpRequest.getPath();

            if (path.startsWith(BOARD_APP_DIRECTORY)) {
                handler = new IndexHandler(httpRequest);
            } else {
                handler = new StaticHandler(httpRequest);
            }
            response = handler.generateResponse();

            if (response == null) {
                throw new InternalServerErrorException("生成したレスポンスが空です");
            }
        } catch (BadRequestException e) {
            response = new HTTPResponse(Status.BAD_REQUEST);
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            response = new HTTPResponse(Status.HTTP_VERSION_NOT_SUPPORTED);
            throw new RuntimeException(e);
        } catch (InternalServerErrorException e) {
            response = new HTTPResponse(Status.INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        } catch (Exception e) {
            response = new HTTPResponse(Status.INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        } finally {
            if (response != null && output != null) {
                response.writeTo(output);
            }
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

    private void checkValidRequest(HTTPRequest request) throws BadRequestException, ProtocolException {
        String protocolVer = request.getProtocolVer();
        if (!SUPPORTED_PROTOCOL_VERSION.contains(protocolVer)) {
            throw new ProtocolException();
        }
        if (protocolVer.equals("1.1") && !request.getHost().equals(request.getHeaderParam("Host"))) {
            throw new BadRequestException("Hostが指定されていないか間違っています");
        }
    }
}

