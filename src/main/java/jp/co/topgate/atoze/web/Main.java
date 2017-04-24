package jp.co.topgate.atoze.web;

/**
 * HTTPサーバーを実行します.
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
