import jp.co.topgate.atoze.web.Server;
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
public class ServerTest {
    private String endo;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        System.out.println("start up http server...");

        ServerSocket serverSocket = null;
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader br = null;

        try {
            serverSocket = new ServerSocket(server.getPort(8080));
            while (true) {
                //ソケットを開く
                socket = serverSocket.accept();
                System.out.println("request incoming...");

                // リクエスト
                br = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                String str;
                while (!(str = br.readLine()).equals("")) {
                    System.out.println(str);
                }

                // レスポンス
                writer = new PrintWriter(socket.getOutputStream(), true);
                StringBuilder builder = new StringBuilder();//文字列生成

                builder.append("HTTP/1.1 ");
                builder.append("200 OK").append("\n");
                builder.append("Content-Type: text/html").append("\n");
                builder.append("\n");
                builder.append("<html><head><title>Hello world!</title></head><body><h1>Hello world!</h1>Hi!</body></html>");

                System.out.println("responce...");
                System.out.println(builder.toString() + "\n");
                writer.println(builder.toString());

                System.out.println("Bad Request");

                socket.close();
            }

        }finally {

            if (serverSocket != null) {
                serverSocket.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }

}
