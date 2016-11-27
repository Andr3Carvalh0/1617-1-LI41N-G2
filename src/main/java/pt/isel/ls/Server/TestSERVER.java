package pt.isel.ls.Server;

public class TestSERVER {

    public static void main(String[] args) throws Exception {
        int port = 3000;

        HTTPServer server = new HTTPServer(port, null);
        server.run();
    }
}
