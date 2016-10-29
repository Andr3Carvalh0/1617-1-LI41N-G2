package pt.isel.ls.Commands;

import java.util.HashMap;
import java.util.LinkedList;

public interface GetCommand {
    LinkedList<HashMap<String, String[]>> prepareForTransformartion(Boolean isHTML, Object obj);

}
