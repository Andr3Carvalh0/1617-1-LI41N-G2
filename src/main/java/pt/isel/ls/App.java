package pt.isel.ls;

public class App {
    public static void main(String[] args) {
        try {
            Routing r = new Routing(args);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
