package pt.isel.ls.Utils;

import pt.isel.ls.Dtos.BaseDTO;
import pt.isel.ls.Dtos.DtoWrapper;

import java.util.HashMap;
import java.util.LinkedList;

public class CustomPrinter {

    Converter converter = new Converter();
    private final String path = "./views/";

    public void print(Object obj, HashMap<String, String> map){
        String file_type = map.get("accept");
        String file_location = map.get("file-name");

        if(file_type == null){
            if(obj instanceof LinkedList){
                toHTML((LinkedList) obj, file_location);
            }else{
                toHTML((DtoWrapper)obj, file_location);
            }
        }else{
            if(file_type.contains("plain")){
                toPlain(obj, file_location);
            }else if(file_type.contains("html")){
                if(obj instanceof LinkedList){
                    toHTML((LinkedList) obj, file_location);
                }else{
                    toHTML((DtoWrapper)obj, file_location);
                }
            }else {
                if(obj instanceof LinkedList){
                    toJSON((LinkedList) obj, file_location);
                }else{
                    toJSON((DtoWrapper)obj, file_location);
                }
            }
        }
    }

    private void toPlain(Object obj, String file_location) {
        run(obj.toString(), file_location);
    }

    private void toJSON(DtoWrapper obj, String file_location) {}

    private void toJSON(LinkedList obj, String file_location) {
        LinkedList<HashMap<String, String[]>> listJSON = new LinkedList<>();
        //JSON
        //Since we know that the result was a linkedlist we add the Collection attribute
        HashMap<String, String[]> map = new HashMap<>();
        String class_types[] = {"\"" + ((BaseDTO) ((LinkedList) obj).getFirst()).getDTOName() + "\"", "\"Collections\""};
        map.put("class_types", class_types);

        //Since we have more than 1 type, the prop_header will only have the count attribute
        String props[] = {"\"count\" : " + class_types.length + ""};
        map.put("prop_header", props);

        //listJSON.add(map);
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

        run(listJSON, file_location,false, Converter.class.getClassLoader().getResource("./views/template_list.json").getPath());
    }

    private void toHTML(DtoWrapper obj, String file_location) {}

    private void toHTML(LinkedList obj, String file_location) {
        LinkedList<HashMap<String, String[]>> listHTML = new LinkedList<>();
        //HTML
        int j = 0;
        for (Object e : obj) {
            HashMap<String, String[]> map = new HashMap<>();
            String title[] = {"Result"};
            map.put("page_title", title);
            if(j == 0) {
                map.put("table_header", ((BaseDTO) e).getPropertiesNames());
            }
            map.put("table_value", ((BaseDTO) e).getPropertiesValues());

            listHTML.add(map);
            j++;
        }

        run(listHTML, file_location, true, Converter.class.getClassLoader().getResource("./views/template_list.html").getPath());
    }


    private void run(LinkedList<HashMap<String, String[]>> list, String file_location, boolean isHTML, String template){
        try {
            converter.compile(list, file_location, isHTML, template);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run(String text, String file_location){
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
        if (s == null) return false;
        if (s.isEmpty()) return false;

        return s.equals("true") || s.equals("false");
    }

}