package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.util.Scanner;

/**
 * クライアントクラス
 *
 * @author atoze
 */
public class ServerHandler {
    final static int PORT = 8080;
    private static final String START_NUM = "1";
    private static final String STOP_NUM = "2";
    private static final String END_NUM = "3";

    /**
     * メインメソッド
     */
    public static void main(String[] args) throws IOException {
        try {
            System.out.println("Starting up HTTP server...at PORT:" + PORT);
            Server server = new Server(PORT);
            //server.start();
            String choices;
            Scanner scan = new Scanner(System.in);
            do {
                System.out.println("--------------------");
                System.out.println(START_NUM + ": START");
                System.out.println(STOP_NUM + ": STOP");
                System.out.println(END_NUM + ": END");

                do {
                    System.out.print("please select :");
                    choices = scan.next();
                } while (isaBoolean(choices));

                String msg = controlServer(server, choices);
                if (msg != null) {
                    System.out.println(msg);
                } else {
                    choices = "";
                }
            } while (!choices.equals(END_NUM));
    } catch(IOException e) {
        e.printStackTrace();
    }

}

    private static boolean isaBoolean(String choices) {
        return !(choices.equals(START_NUM) || choices.equals(STOP_NUM) || choices.equals(END_NUM));
    }

    /**
     * サーバーを操作するメソッド
     *
     * @param choices 選択した文字
     * @return サーバーの状態をメッセージで返す
     * @throws IOException サーバークラスで発生した入出力エラー
     */
    public static String controlServer(Server server, String choices) throws IOException {
        if (server == null || choices == null) {
            return null;
        }
        String msg = "";
        switch (choices) {
            case START_NUM:
                switch (server.getState()) {
                    case TERMINATED:
                        server = new Server(PORT);

                    case NEW:
                        server.startServer();
                        msg = "start up http server..";
                        break;

                    case RUNNABLE:
                        msg = "http server is already running..";
                        break;

                    default:
                        server.endServer();
                }
                break;

            case STOP_NUM:
                switch (server.getState()) {
                    case NEW:

                    case TERMINATED:
                        msg = "http server is not running..";
                        break;

                    case RUNNABLE:
                        if (server.stopServer()) {
                            msg = "http server stops..";
                        } else {
                            msg = "wait a second, http server is returning a response..";
                        }
                        break;

                    default:
                        server.endServer();
                }
                break;

            case END_NUM:
                server.endServer();
                msg = "bye..";
                break;

            default:
                return null;
        }
        return msg;
    }
}