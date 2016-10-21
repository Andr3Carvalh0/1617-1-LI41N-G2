package pt.isel.ls;

import pt.isel.ls.Commands.Command;

public class App {
    public static void main(String[] args) {
        try {
            CommandParser cparser = new CommandParser(args);

            Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
            Command c = r.Route();
            System.out.println(r.run(c));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
