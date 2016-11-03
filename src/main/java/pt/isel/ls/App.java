package pt.isel.ls;

import pt.isel.ls.Commands.Command;

import java.util.Scanner;

//java -cp build/classes/Printer; vendor/Printer/* pt.isel.ls.App
public class App {
    public static void main(String[] args) {
        Scanner io;
        try {
            if(args.length == 0){
                enterInteractiveMode();
            }
            else if (args.length >= 2 && args.length <= 4){
                CommandParser cparser = new CommandParser(args);
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();


                System.out.println(r.run(c));
            } else throw new Exception("Invalid number of arguments.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void enterInteractiveMode() throws Exception {
        System.out.println("Interactive mode engaged");
        Scanner io = new Scanner(System.in);
        while(true) {
            String[] input = io.nextLine().split(" ");
            if (input.length >= 2 && input.length <= 4) {
                if (input[0].equals("EXIT")) break;
                CommandParser cparser = new CommandParser(input);
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();
                System.out.println(r.run(c));
            }
        }
    }
}
