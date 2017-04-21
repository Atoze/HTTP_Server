package jp.co.topgate.atoze.web;

/**
 * Created by atoze on 2017/04/12.
 */
class Main {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
