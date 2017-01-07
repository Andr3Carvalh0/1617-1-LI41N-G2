package pt.isel.ls;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Utils.Output.CustomPrinter;
import java.util.Scanner;

public class App {
    private static CustomPrinter cPrinter = new CustomPrinter();

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                enterInteractiveMode();
            } else if (args.length >= 2 && args.length <= 4) {
                run(args);
            } else throw new Exception("Invalid number of arguments.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enterInteractiveMode() throws Exception {
        System.out.println("Interactive mode engaged");
        Scanner io = new Scanner(System.in);
        while (true) {
            String[] input = io.nextLine().split(" ");
            if (input.length >= 2 && input.length <= 4) {
                if (input[0].equals("EXIT")) break;
                run(input);
            }
        }
    }

    private static void run(String[] input) throws Exception {
        CommandParser cparser = new CommandParser(input);
        Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
        Command c = r.Route();
        try {
            Object obj = r.run(c);

            if (c.getMethod().equals("GET")) {
                System.out.println(cPrinter.print(obj, cparser.getHeaders(), getQuery(input)));
            } else {
                System.out.println(obj);
            }
        } catch (Exception e) {
            System.out.println(cPrinter.print(null, cparser.getHeaders(), getQuery(input)));
        }
    }

    private static String getQuery(String in[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < in.length - 1; i++) {
            sb.append(in[i]).append(" ");
        }
        sb.append(in[in.length - 1]);

        return sb.toString();
    }

}
