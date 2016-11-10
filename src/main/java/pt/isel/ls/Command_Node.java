package pt.isel.ls;


import pt.isel.ls.Commands.Command;

import java.util.LinkedList;

public class Command_Node {
    private String name;
    private LinkedList<Command_Node> children;
    private Command command;

    Command_Node(String name, LinkedList<Command_Node> list, Command cmd){
        this.name = name;
        this.children = list;
        this.command = cmd;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Command_Node> getChildren() {
        return children;
    }

    public Command getCommand() {
        return command;
    }
}
