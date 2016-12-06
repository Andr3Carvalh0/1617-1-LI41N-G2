package pt.isel.ls.Utils.Output;

import pt.isel.ls.Dtos.BaseDTO;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Tag;
import pt.isel.ls.Utils.Output.Dummies.WrapperChecklistView;
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
                return toJSON(obj, file_location);
            }
        }
    }

    private String toPlain(Object obj, String file_location) {
        return run(obj.toString(), file_location);
    }

    private String toJSON(Object obj, String file_location) throws Exception {
        String file;
        if (obj == null) {
            file = "empty";
        } else {
            if (obj instanceof LinkedList) {
                file = ((LinkedList) obj).size() == 0 ? "empty" : ((BaseDTO) (((LinkedList) obj).get(0))).getDTOName();
            } else {
                if (((DtoWrapper) obj).getTemplate_Task() != null) {
                    file = (((DtoWrapper) obj).getTag() != null) ? "tag_detailed" : "template_detailed";

                } else {
                    file = "checklist_detailed";
                }
            }
        }

        if (file.equals("empty")) {
            obj = new WrapperServerError(executedCommand);
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

            if (query.contains("/checklists") && obj instanceof LinkedList) {
                int active;

                if (query.equals("/checklists")) {
                    active = 0;
                } else if (query.contains("closed")) {
                    active = 1;
                } else if (query.contains("duedate")) {
                    active = 2;
                } else {
                    active = 3;
                }
                obj = new WrapperChecklistView((LinkedList) obj, active);
            }
            else if(query.contains("/tags") && obj instanceof DtoWrapper) {
                String link = query.contains("checklists") ? "/tags/" + ((Tag) ((LinkedList) ((DtoWrapper) obj).getTag()).get(0)).getTg_id() : "/tags";
                obj = new WrapperTagsDetailed(link, (DtoWrapper) obj);

            }


            if (obj instanceof LinkedList) {
                String[] req = query.split("/");

                file = req[1];
            } else if (obj instanceof WrapperChecklistView) {
                file = "checklists";
            } else if (obj instanceof WrapperTagsDetailed) {
                file = "tag_detailed";
            }else {
                if (((DtoWrapper) obj).getTemplate_Task() != null) {
                    file = "template_detailed";

                } else {
                    file = (((DtoWrapper) obj).getTemplate() != null) ? "checklist_detailed" : "tag_detailed";

                }
            }
        }

        return run(obj, file_location, true, CustomPrinter.class.getClassLoader().getResource(path + "html/" + file + ".html").getPath());
    }

    private String run(Object obj, String file_location, boolean isHTML, String template) throws Exception {
        try {
            converter.compile(obj, isHTML, template);
            return converter.commit(null, file_location);
        } catch (Exception e) {
            throw new Error("Error: Can't display the message because there is a unknown marker");
        }
    }

    private String run(String text, String file_location) {
        try {
            return converter.commit(text, file_location);
        } catch (Exception e) {
            throw new Error("Error: Can't display the message because there is a unknown marker");
        }
    }
}