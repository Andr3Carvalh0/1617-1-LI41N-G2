package pt.isel.ls.Utils.Output;

import pt.isel.ls.Dtos.BaseDTO;
import pt.isel.ls.Dtos.DtoWrapper;

import java.util.HashMap;
import java.util.LinkedList;

@SuppressWarnings("ConstantConditions")
public class CustomPrinter {

    private Converter converter = new Converter();
    private final String path = "./views/";
    private String executedCommand;

    public String print(Object obj, HashMap<String, String> map, String query) throws Exception {
        String file_type = map.get("accept");
        String file_location = map.get("file-name");
        executedCommand = query;

        if (file_type == null) {
            if (obj instanceof String) {
                return run((String) obj, null);
            } else {
                return toHTML(obj, file_location);
            }
        } else {
            if (file_type.contains("plain")) {
                return toPlain(obj, file_location);
            } else if (file_type.contains("html")) {
                return toHTML(obj, file_location);
            } else {
                return toJSON(obj, file_location);
            }
        }
    }

    private String toPlain(Object obj, String file_location) {
        return run(obj.toString(), file_location);
    }

    private String toJSON(Object obj, String file_location) throws Exception {
        String file;
        if(obj == null){
            file = "empty";
        }else {
            if (obj instanceof LinkedList) {
                file = ((LinkedList) obj).size() == 0 ? "empty" : ((BaseDTO) (((LinkedList) obj).get(0))).getDTOName();
            } else {
                file = ((DtoWrapper) obj).getTemplate_Task() != null ? "template_detailed" : "checklist_detailed";
            }
        }

        if (file.equals("empty")) {
            obj = new EmptyObject(executedCommand);
        }

        return run(obj, file_location, false, CustomPrinter.class.getClassLoader().getResource(path + "json/" + file + ".json").getPath());
    }

    private String toHTML(Object obj, String file_location) throws Exception {
        String file;

        if(obj == null){
            file = "empty";
        }else if(obj.equals("main")){
            file = "home";
        } else if(obj.equals("about")){
            file = "about";
        } else {
            if (obj instanceof LinkedList) {
                file = ((LinkedList) obj).size() == 0 ? "empty" : ((BaseDTO) (((LinkedList) obj).get(0))).getDTOName();
            } else {
                file = ((DtoWrapper) obj).getTemplate_Task() != null ? "template_detailed" : "checklist_detailed";
            }
        }

        if (file.equals("empty") || file.equals("home")) {
            obj = new EmptyObject(executedCommand);
        }
        return run(obj, file_location, true, CustomPrinter.class.getClassLoader().getResource(path + "html/" + file + ".html").getPath());
    }

    private String run(Object obj, String file_location, boolean isHTML, String template) {
        try {
            converter.compile(obj, isHTML, template);
            return converter.commit(null, file_location);
        } catch (Exception e) {
            return "Error: Can't display the message";
        }
    }

    private String run(String text, String file_location) {
        try {
            return converter.commit(text, file_location);
        } catch (Exception e) {
            return "Error: Can't display the message";
        }
    }
}