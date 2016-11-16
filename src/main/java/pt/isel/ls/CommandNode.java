package pt.isel.ls;


import pt.isel.ls.Commands.Command;

import java.util.LinkedList;

public class CommandNode {
    private String name;
    private LinkedList<CommandNode> children;
    private Command command;

    CommandNode(String name, LinkedList<CommandNode> list, Command cmd){
        this.name = name;
        this.children = list;
        this.command = cmd;
    }

    public String getName() {
        return name;
    }

    public LinkedList<CommandNode> getChildren() {
        return children;
    }

    public Command getCommand() {
        return command;
    }
}
