package pt.isel.ls.Utils.Output;

import java.util.Random;

//This class is just to store the user command.
//This will be used when the user execute some query and the result is empty
class EmptyObject {
    private String query;
    private String message;

    private final String[] messages = {"Blame the stupid SQL", "Blame the idiots on Group 2", "Try again", "Just go take a break, and try later"};

    EmptyObject(String query){
        this.query = query;
        Random r = new Random();

        message = messages[r.nextInt(messages.length)];

    }
}
