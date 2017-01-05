package pt.isel.ls.Utils.Output;

import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Tag;
import pt.isel.ls.Utils.Output.Dummies.WrapperJsonError;
import pt.isel.ls.Utils.Output.Dummies.WrapperServerError;
import pt.isel.ls.Utils.Output.Dummies.WrapperTagsDetailed;

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
                return toHTML(obj, file_location, query);
            }
        } else {
            if (file_type.contains("plain")) {
                return toPlain(obj, file_location);
            } else if (file_type.contains("html")) {
                return toHTML(obj, file_location, query);
            } else {
                return toJSON(obj, file_location, query);
            }
        }
    }

    private String toPlain(Object obj, String file_location) throws Exception {
        return run(obj.toString(), file_location);
    }

    private String toJSON(Object obj, String file_location, String query) throws Exception {
        String file;
        if (obj == null) {
            file = "empty";

        } else {
            if (obj instanceof LinkedList) {
                if(((LinkedList) obj).size() == 0){
                    file = "empty";
                }
                else{
                    String[] req = query.split("/");
                    file = req[1];
                }
            } else {
                if (((DtoWrapper) obj).getTemplate_Task() != null) {
                    file = "template_detailed";

                } else {
                    file = (((DtoWrapper) obj).getTemplate() != null) ? "checklist_detailed" : "tag_detailed";

                }
            }
        }

        if(file.equals("empty")){
            obj = new WrapperJsonError(executedCommand);
        }

        return run(obj, file_location, false, CustomPrinter.class.getClassLoader().getResource(path + "json/" + file + ".json").getPath());
    }

    private String toHTML(Object obj, String file_location, String query) throws Exception {
        String file;

        if(obj == null){
            file = "server_error";
            obj = new WrapperServerError(executedCommand);
        } else if (obj.equals("not_found")) {
            file = obj.toString();
        }
        else if (query.equals("/")) {
            file = "home";
        } else if (obj.equals("/about")) {
            file = "about";
        } else {

            if(query.contains("/tags") && obj instanceof DtoWrapper) {
                String link = query.contains("checklists") ? "/tags/" + ((Tag) ((LinkedList) ((DtoWrapper) obj).getTag()).get(0)).getTg_id() : "/tags";
                obj = new WrapperTagsDetailed(link, (DtoWrapper) obj);

            }

            if (obj instanceof LinkedList) {
                String[] req = query.split("/");

                file = req[1];
            }else if (obj instanceof WrapperTagsDetailed) {
                file = "tag_detailed";
            }else {
                if (((DtoWrapper) obj).getTemplate_Task() != null) {
                    file = "template_detailed";

                } else {
                    file = (((DtoWrapper) obj).getTemplate() != null) ? "checklist_detailed" : "tag_detailed";

                }
            }
        }

        System.out.println("OLA CARALHO!");
        System.out.println(CustomPrinter.class.getClassLoader().getPath());
        
        return run(obj, file_location, true, CustomPrinter.class.getClassLoader().getResource(path + "html/" + file + ".html").getPath());
    }

    private String run(Object obj, String file_location, boolean isHTML, String template) throws Exception {
        try {
            converter.compile(obj, isHTML, template);
            return converter.commit(null, file_location);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String run(String text, String file_location) throws Exception {
        try {
            return converter.commit(text, file_location);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}