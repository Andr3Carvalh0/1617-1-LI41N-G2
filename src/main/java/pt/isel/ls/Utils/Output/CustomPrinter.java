package pt.isel.ls.Utils.Output;

import pt.isel.ls.Dtos.BaseDTO;
import pt.isel.ls.Dtos.DtoWrapper;

import java.util.HashMap;
import java.util.LinkedList;

@SuppressWarnings("ConstantConditions")
public class CustomPrinter {

    private Converter converter = new Converter();
    private final String path = "./views/";
    private String[] executedCommand;

    public void print(Object obj, HashMap<String, String> map, String[] query) throws Exception {
        String file_type = map.get("accept");
        String file_location = map.get("file-name");
        executedCommand = query;

        if (file_type == null) {
            if (obj instanceof String) {
                run((String) obj, null);
            } else {
                toHTML(obj, file_location);
            }
        } else {
            if (file_type.contains("plain")) {
                toPlain(obj, file_location);
            } else if (file_type.contains("html")) {
                toHTML(obj, file_location);
            } else {
                toJSON(obj, file_location);
            }
        }
    }

    private void toPlain(Object obj, String file_location) {
        run(obj.toString(), file_location);
    }

    private void toJSON(Object obj, String file_location) throws Exception {
        String file;
        if(obj instanceof LinkedList){
            file = ((LinkedList)obj).size() == 0 ? "empty" : ((BaseDTO) (((LinkedList) obj).get(0))).getDTOName();
        }else {
            file = ((DtoWrapper) obj).getTemplate_Task() != null ? "template_detailed" : "checklist_detailed";
        }

        if(file.equals("empty")){
            obj = new EmptyObject(convertToString(executedCommand), executedCommand[1].replace("/", ""));
        }

        run(obj, file_location, false, CustomPrinter.class.getClassLoader().getResource(path + "json/" + file + ".json").getPath());
    }

    private void toHTML(Object obj, String file_location) throws Exception {
        String file;
        if(obj instanceof LinkedList){
            file = ((LinkedList)obj).size() == 0 ? "empty" : ((BaseDTO) (((LinkedList) obj).get(0))).getDTOName();
        }else {
            file = ((DtoWrapper) obj).getTemplate_Task() != null ? "template_detailed" : "checklist_detailed";
        }

        if(file.equals("empty")){
            obj = new EmptyObject(convertToString(executedCommand), executedCommand[1].replace("/", ""));
        }

        run(obj, file_location, true, CustomPrinter.class.getClassLoader().getResource(path + "html/" + file + ".html").getPath());
    }

    private void run(Object obj, String file_location, boolean isHTML, String template) {
        try {
            converter.compile(obj, isHTML, template);
            converter.commit(null, file_location);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Can't display the message");
        }
    }

    private void run(String text, String file_location) {
        try {
            converter.commit(text, file_location);
        } catch (Exception e) {
            System.out.println("Error: Can't display the message");
        }
    }

    private static String convertToString(String[] in){
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < in.length - 1; i++){
            sb.append(in[i]).append(" ");
        }
        sb.append(in[in.length -1]);

        return sb.toString();
    }
}