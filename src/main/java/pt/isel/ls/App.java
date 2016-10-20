package pt.isel.ls;

import pt.isel.ls.Commands.Command;

public class App {
    public static void main(String[] args) {
        try {
            Routing r = new Routing(args);
            Command c = r.Route();
            r.run(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
