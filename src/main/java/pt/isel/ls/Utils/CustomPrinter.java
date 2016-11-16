package pt.isel.ls.Utils;

import pt.isel.ls.Dtos.BaseDTO;
import pt.isel.ls.Dtos.DtoWrapper;

import java.util.HashMap;
import java.util.LinkedList;

public class CustomPrinter {

    private Converter converter = new Converter();
    private final String path = "./views/";

    public void print(Object obj, HashMap<String, String> map) throws Exception {
        String file_type = map.get("accept");
        String file_location = map.get("file-name");

        if (file_type == null) {
            if (obj instanceof LinkedList) {
                toHTML((LinkedList) obj, file_location);
            } else if (obj instanceof String) {
                run((String) obj, null);
            } else {
                toHTML((DtoWrapper) obj, file_location);
            }
        } else {
            if (file_type.contains("plain")) {
                toPlain(obj, file_location);
            } else if (file_type.contains("html")) {
                if (obj instanceof LinkedList) {
                    toHTML((LinkedList) obj, file_location);
                } else {
                    toHTML((DtoWrapper) obj, file_location);
                }
            } else {
                if (obj instanceof LinkedList) {
                    toJSON((LinkedList) obj, file_location);
                } else {
                    toJSON((DtoWrapper) obj, file_location);
                }
            }
        }
    }

    private void toPlain(Object obj, String file_location) {
        run(obj.toString(), file_location);
    }

    private void toJSON(DtoWrapper obj, String file_location) throws Exception {
        LinkedList<HashMap<String, String[]>> result = new LinkedList<>();
        HashMap<String, String[]> map = new HashMap<>();

        if (obj.getWrapperObjects().size() == 0) System.out.println("The query didnt return any object!");
        else {
            String types = "";
            int count = 0;
            if(obj.getChecklist() != null) { types += "\"" + "Checklist" + "\"," ; count++; }
            if(obj.getChecklist_Task() != null) { types += "\"" + "Checklist Task" + "\",";count++; }
            if(obj.getTemplate() != null) { types += "\"" + "Template" + "\",";count++; }
            if(obj.getTemplate_Task() != null){  types += "\"" + "Template Task" + "\",";count++; }

            if(count > 0){ types += "\"" + "Collections" + "\""; count++; }
            //Since we have more than 1 type, the prop_header will only have the count attribute


            for (Object e : obj.getWrapperObjects()) {

                LinkedList<HashMap<String, String[]>> listJSON = processWrapperToJSON(e);
                converter.compile(listJSON, false, Converter.class.getClassLoader().getResource(path +"wrapper_element.json").getPath());

                String[] arr = new String[1];
                arr[0] = converter.getMessage();
                map.put("table", arr);
                result.add(map);

                map = new HashMap<>();
            }
            //Remove the "," from the last one
            String tmp[] = result.getLast().get("table");
            String msg = tmp[tmp.length-1];
            msg = msg.substring(0, msg.length() - 2);
            result.getLast().remove("table");
            result.getLast().put("table", new String[]{msg});

            result.getFirst().put("prop_header", new String[]{"\"count\" : " + count + ""});
            result.getFirst().put("class_types", new String[]{types});

            run(result, file_location, false, Converter.class.getClassLoader().getResource(path +"template_wrapper.json").getPath());
        }
    }

    private void toHTML(DtoWrapper obj, String file_location) throws Exception {
        LinkedList<HashMap<String, String[]>> result = new LinkedList<>();
        HashMap<String, String[]> map = new HashMap<>();

        if (obj.getWrapperObjects().size() == 0) System.out.println("The query didnt return any object!");
        else {
            for (Object e : obj.getWrapperObjects()) {
                LinkedList<HashMap<String, String[]>> listHTML = processWrapperToHTML(e);
                converter.compile(listHTML, true, Converter.class.getClassLoader().getResource(path +"wrapper_element.html").getPath());

                String[] arr = new String[1];
                arr[0] = converter.getMessage();
                map.put("table", arr);
                result.add(map);

                map = new HashMap<>();
            }
            result.get(0).put("page_title", new String[]{"Results"});
            run(result, file_location, true, Converter.class.getClassLoader().getResource(path +"template_wrapper.html").getPath());
        }
    }

    private void toJSON(LinkedList obj, String file_location) {
        LinkedList<HashMap<String, String[]>> listJSON = new LinkedList<>();
        String class_types[];
        /*JSON
        *Since we know that the result was a linkedlist we add the Collection attribute
        */
        HashMap<String, String[]> map = new HashMap<>();
        if(obj.size() > 0) {
            class_types = new String[]{"\"" + ((BaseDTO) obj.getFirst()).getDTOName() + "\"", "\"Collections\""};
        }
        else {
            class_types = new String[]{"\"" + "\"", "\"Collections\""};
        }
        map.put("class_types", class_types);

        //Since we have more than 1 type, the prop_header will only have the count attribute
        String props[] = {"\"count\" : " + class_types.length + ""};
        map.put("prop_header", props);

        //listJSON.add(map);
        if(obj.size() > 0) {
            for (Object e : obj) {
                int numberOfProperties = ((BaseDTO) e).getPropertiesNames().length;

                props = new String[numberOfProperties];

                String name[] = {"\"" + ((BaseDTO) e).getDTOName() + "\""};
                map.put("class_name", name);

                for (int i = 0; i < numberOfProperties; i++) {

                    String begin = "\"" + ((BaseDTO) e).getPropertiesNames()[i] + "\" : ";

                    if (isInteger((((BaseDTO) e).getPropertiesValues()[i]), 10) || isBoolean((((BaseDTO) e).getPropertiesValues()[i]))) {
                        begin += ((BaseDTO) e).getPropertiesValues()[i];

                    } else {
                        begin += "\"" + ((BaseDTO) e).getPropertiesValues()[i] + "\"";

                    }

                    props[i] = begin;

                }
                map.put("prop_body", props);

                listJSON.add(map);
                map = new HashMap<>();
            }
        }
        else {
            listJSON.add(map);
        }
        run(listJSON, file_location, false, Converter.class.getClassLoader().getResource(path +"template_list.json").getPath());
    }

    private void toHTML(LinkedList obj, String file_location) {
        LinkedList<HashMap<String, String[]>> listHTML = new LinkedList<>();
        //HTML
        HashMap<String, String[]> map = new HashMap<>();
        String title[] = {"Result"};
        map.put("page_title", title);

        if(obj.size() > 0) {
            map.put("table_header", ((BaseDTO) obj.get(0)).getPropertiesNames());

            for (Object e : obj) {

                map.put("table_value", ((BaseDTO) e).getPropertiesValues());
                listHTML.add(map);
                map = new HashMap<>();
            }
        } else {
            listHTML.add(map);
        }
        run(listHTML, file_location, true, Converter.class.getClassLoader().getResource(path + "template_list.html").getPath());
    }

    private void run(LinkedList<HashMap<String, String[]>> list, String file_location, boolean isHTML, String template) {
        try {
            converter.compile(list, isHTML, template);
            converter.commit(converter.getMessage(), file_location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run(String text, String file_location) {
        try {
            converter.commit(text, file_location);
        } catch (Exception e) {
            System.out.println();
        }
    }

    private static boolean isInteger(String s, int radix) {
        if (s == null) return false;

        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    private static boolean isBoolean(String s) {
        return s != null && (s.equals("true") || s.equals("false"));

    }

    private LinkedList<HashMap<String, String[]>> processWrapperToHTML(Object obj) {
        LinkedList<HashMap<String, String[]>> listHTML = new LinkedList<>();

        HashMap<String, String[]> map = new HashMap<>();

        if (obj instanceof LinkedList) {
            map.put("table_header", ((BaseDTO) ((LinkedList) obj).get(0)).getPropertiesNames());
            map.put("table_name", new String[]{((BaseDTO) ((LinkedList) obj).get(0)).getDTOName()});

            for (Object e : (LinkedList) obj) {

                map.put("table_value", ((BaseDTO) e).getPropertiesValues());
                listHTML.add(map);
                map = new HashMap<>();
            }
        } else {
            map.put("table_name", new String[]{((BaseDTO) obj).getDTOName()});
            map.put("table_header", ((BaseDTO) obj).getPropertiesNames());
            map.put("table_value", ((BaseDTO) obj).getPropertiesValues());
            listHTML.add(map);
        }
        return listHTML;
    }

    private LinkedList<HashMap<String, String[]>> processWrapperToJSON(Object obj) {
        LinkedList<HashMap<String, String[]>> listJSON = new LinkedList<>();
        String props[];
        HashMap<String, String[]> map = new HashMap<>();
        if (obj instanceof LinkedList) {


            for (Object e :(LinkedList)obj) {
                int numberOfProperties = ((BaseDTO) e).getPropertiesNames().length;
                props = new String[numberOfProperties];

                String name[] = {"\"" + ((BaseDTO) e).getDTOName() + "\""};
                map.put("class_name", name);

                for (int i = 0; i < numberOfProperties; i++) {

                    String begin = "\"" + ((BaseDTO) e).getPropertiesNames()[i] + "\" : ";

                    if (isInteger((((BaseDTO) e).getPropertiesValues()[i]), 10) || isBoolean((((BaseDTO) e).getPropertiesValues()[i]))) {
                        begin += ((BaseDTO) e).getPropertiesValues()[i];

                    } else {
                        begin += "\"" + ((BaseDTO) e).getPropertiesValues()[i] + "\"";

                    }

                    props[i] = begin;

                }
                map.put("prop_body", props);

                listJSON.add(map);
                map = new HashMap<>();
            }
        }else{
            int numberOfProperties = ((BaseDTO) obj).getPropertiesNames().length;
            props = new String[numberOfProperties];

            String name[] = {"\"" + ((BaseDTO) obj).getDTOName() + "\""};
            map.put("class_name", name);

            for (int i = 0; i < numberOfProperties; i++) {

                String begin = "\"" + ((BaseDTO) obj).getPropertiesNames()[i] + "\" : ";

                if (isInteger((((BaseDTO) obj).getPropertiesValues()[i]), 10) || isBoolean((((BaseDTO) obj).getPropertiesValues()[i]))) {
                    begin += ((BaseDTO) obj).getPropertiesValues()[i];

                } else {
                    begin += "\"" + ((BaseDTO) obj).getPropertiesValues()[i] + "\"";

                }

                props[i] = begin;

            }
            map.put("prop_body", props);

            listJSON.add(map);
        }
        return listJSON;
    }
}