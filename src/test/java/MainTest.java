import jp.co.topgate.atoze.web.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by atoze on 2017/04/13.
 */
class MainTest {
    final static int PORT = 8080;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Starting up HTTP server...at PORT:" + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Server(socket, PORT).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try{
                if(serverSocket !=null) {
                    serverSocket.close();
                }
            }catch (Throwable e){
                throw new RuntimeException(e);
            }
        }
    }
}
