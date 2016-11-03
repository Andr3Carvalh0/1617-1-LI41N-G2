package pt.isel.ls.Utils;

import pt.isel.ls.Dtos.BaseDTO;
import java.util.HashMap;
import java.util.LinkedList;

public class CustomPrinter {

    public static void print(Object obj, HashMap<String, String> map){



    }

    private static void toJSON(Object obj) {
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
        for (Object e : (LinkedList) obj) {
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

        run(listJSON);
    }

    private static void toHTML(Object obj) {
        LinkedList<HashMap<String, String[]>> listHTML = new LinkedList<>();
        //HTML
        int j = 0;
        for (Object e : (LinkedList) obj) {
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

        run(listHTML);
    }


    private static void run(LinkedList<HashMap<String, String[]>> list){



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