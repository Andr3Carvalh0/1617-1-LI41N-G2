package pt.isel.ls.Utils.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/*
*
* Future improvements:
*  - Make it modular
*  - Instead of declaring the head on every template.Make a template for template
*
 */

class Converter {
    private LinkedList<String> message = new LinkedList<>();
    private static final String privateKey = "self";

    private boolean isHTML;

    //Cache for the objects fields.
    private HashMap<String, HashMap<String, Field>> cache = new HashMap<>();

    //Cache the files that we use as template
    private HashMap<String, LinkedList<String>> file_cache = new HashMap<>();

    private static final String lookFor_begin_HTML = "{{";
    private static final String lookFor_end_HTML = "}}";
    private static final String[] SUPPORTED_MARKERS_HTML = {"{{#FOR}}", "{{#END}}", "{{#NOT_NULL}}", "{{#REPLACE_NULL}}", "{{#COUNT}}", "{{#REMOVE_ON_LAST}}"};

    private static final String lookFor_begin_JSON = "<<";
    private static final String lookFor_end_JSON = ">>";
    private static final String[] SUPPORTED_MARKERS_JSON = {"<<#FOR>>", "<<#END>>", "<<#NOT_NULL>>", "<<#REPLACE_NULL>>", "<<#COUNT>>", "<<#REMOVE_ON_LAST>>"};

    private void allocate(String baseFile) throws Exception {
        Scanner io = null;
        try {
            message = new LinkedList<>();

            if (isPresentInCache(baseFile, file_cache)) {
                message = file_cache.get(baseFile);
            } else {
                io = new Scanner(new File(baseFile));

                while (io.hasNextLine()) {
                    message.add(io.nextLine() + "\n");
                }

                file_cache.put(baseFile, message);
            }
        } catch (FileNotFoundException e) {
            throw new Exception("Cannot read the template file: " + baseFile);
        } finally {
            if (io != null) {
                io.close();
            }
        }
    }

    void compile(Object obj, boolean isHTML, String baseFile) throws Exception {
        this.isHTML = isHTML;

        //Make this the generic-ist way possible
        String[] marks = isHTML ? SUPPORTED_MARKERS_HTML : SUPPORTED_MARKERS_JSON;
        String marker_begin = isHTML ? lookFor_begin_HTML : lookFor_begin_JSON;
        String marker_end = isHTML ? lookFor_end_HTML : lookFor_end_JSON;

        allocate(baseFile);

        message = replaceFOR(obj, message, marks, marker_begin, marker_end);

        message = replaceFlag(obj, message, marks, marker_begin, marker_end, false);

        //Cleanup
        removeNotUsedMarkers(marker_begin);
    }

    String commit(String msg, String outputName) throws Exception {
        if (msg == null) msg = generateMessage();

        if (outputName == null) {
            return msg;
        } else {
            try {
                saveToFile(msg, outputName);
                return "Saved!";
            } catch (FileNotFoundException e) {
                throw new Exception("Cannot save file!");
            }
        }
    }

    private void saveToFile(String msg, String outputName) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(outputName);
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println(msg);
        writer.close();
    }

    private LinkedList<String> replaceFOR(Object obj, LinkedList<String> msg, String marks[], String leftDelimiter, String rightDelimiter) throws Exception {
        LinkedList<String> result = new LinkedList<>();
        String line;
        for (int i = 0; i < msg.size(); i++) {
            line = msg.get(i);

            if (line.contains(marks[0])) {
                if (!isHTML) {
                    if (result.getLast().contains("}")) {
                        result.set(result.size() - 1, result.getLast().replace("\n", ",\n"));
                    }
                }
                //Remove the FOR tag and obtain the object name in which we will iterate
                line = line.replace(marks[0], "");
                String object = line.replace(leftDelimiter, "").replace(rightDelimiter, "").replace("\n", "").replace(" ", "");

                LinkedList<String> iterateBody = new LinkedList<>();

                i++;
                while (!(line = msg.get(i)).contains(marks[1])) {
                    iterateBody.add(line);
                    i++;
                }

                //Check if the object we will iterate is self
                //If so we will iterate over the object we receive in the pars
                //else we will iterate over a property that the object we receive in the pars has
                if (object.equals(privateKey)) {
                    applyObjectValues((LinkedList) obj, marks, leftDelimiter, rightDelimiter, result, iterateBody);
                    if (!isHTML) {
                        result.set(result.size() - 1, result.getLast().replace(",", ""));
                    }
                } else {
                    try {
                        if (!isPresentInCache(obj.getClass().getName(), cache)) {
                            addToCache(obj);
                        }

                        HashMap<String, Field> currentOBJ = cache.get(obj.getClass().getName());
                        Field field = currentOBJ.get(object);
                        field.setAccessible(true);
                        Object value = field.get(obj);

                        applyObjectValues((LinkedList) value, marks, leftDelimiter, rightDelimiter, result, iterateBody);

                        if (!isHTML) {
                            result.set(result.size() - 1, result.getLast().replace(",", ""));
                        }

                    } catch (IllegalAccessException e) {
                        throw new Exception("Error: the field named: " + object + " doesn't exist!");
                    }
                }
            } else {
                result.add(line);
            }
        }
        return result;
    }

    /*
    *
    * obj : Object -> the object which we will get the properties values to populate the msg.
    * msg : LinkedList -> the body of text that we will analyze and replace with values from obj.
    * marks : String[] -> we use different marks for JSON and HTML, so instead of trying to tell if its a HTML or
    *                     JSON we just received the marks to use.
    * leftDelimiter : String  -> the same case as marks.
    * rightDelimiter : String -> the same case as marks.
    * isLastElement : boolean -> we use this, so that we can implement the "REMOVE_ON_LAST", which removes all lines
    *                               that begin with "#REMOVE_ON_LAST", if the we are using a list and we are populating
    *                               the last element of such list.
    */
    private LinkedList<String> replaceFlag(Object obj, LinkedList<String> msg, String marks[], String leftDelimiter, String rightDelimiter, boolean isLastElement) throws IllegalAccessException {
        LinkedList<String> result = new LinkedList<>();
        boolean toRemove = false;
        boolean toReplace = false;

        for (String line : msg) {
            //SPECIAL CASE : #COUNT
            if (line.contains(marks[4])) {
                line = line.replace(marks[4], "");
                String count_for_object = line.substring(line.indexOf(leftDelimiter) + 2, line.indexOf(rightDelimiter));

                if (count_for_object.equals(privateKey)) {
                    result.add(line.replace(leftDelimiter + count_for_object + rightDelimiter, ((LinkedList) obj).size() + ""));
                } else {
                    if (!isPresentInCache(obj.getClass().getName(), cache)) {
                        addToCache(obj);
                    }
                    HashMap<String, Field> currentOBJ = cache.get(obj.getClass().getName());
                    Field field = currentOBJ.get(count_for_object);
                    field.setAccessible(true);
                    try {
                        Object value = field.get(obj);
                        result.add(line.replace(leftDelimiter + count_for_object + rightDelimiter, ((LinkedList) value).size() + ""));
                    } catch (IllegalAccessException e) {
                        result.add(line);
                    }
                }

            } else {
                //#NOT_NULL
                if (line.contains(marks[2])) {
                    toRemove = true;
                    line = line.replace(marks[2], "");
                }
                //#REPLACE_NULL
                if (line.contains(marks[3])) {
                    toReplace = true;
                    line = line.replace(marks[3], "");
                }


                while (line.contains(leftDelimiter) && line.contains(rightDelimiter)) {
                    //CASE REMOVE_ON_LAST
                    if (line.contains(marks[5])) {
                        if (isLastElement) {
                            line = "";
                        } else {
                            line = line.replace(marks[5], "");
                        }
                    }

                    if (line.contains(leftDelimiter) && line.contains(rightDelimiter)) {
                        String key = line.substring(line.indexOf(leftDelimiter) + 2, line.indexOf(rightDelimiter));

                        if (!isPresentInCache(obj.getClass().getName(), cache)) {
                            addToCache(obj);
                        }
                        HashMap<String, Field> currentOBJ = cache.get(obj.getClass().getName());
                        Field field = currentOBJ.get(key);
                        field.setAccessible(true);
                        Object value = field.get(obj);

                        if (toRemove) {
                            if (value == null || isNumericNull(value, -1)) {
                                //We dont add to our message, but if the message is json, we have
                                // to check if the previous line contains "," and if so remove it
                                if (!isHTML) {
                                    if (result.getLast().contains(",")) {
                                        result.set(result.size() - 1, result.getLast().replace(",\n", "\n"));
                                    }
                                }
                                line = "";
                            } else {
                                line = line.replace(leftDelimiter + key + rightDelimiter, value.toString());
                            }
                            toRemove = false;

                        } else if (toReplace) {
                            if (value == null || isNumericNull(value, -1)) {
                                line = line.replace(leftDelimiter + key + rightDelimiter, "");

                            } else {
                                line = line.replace(leftDelimiter + key + rightDelimiter, value.toString());
                            }

                            toReplace = false;
                        } else {
                            if (value != null) {
                                line = line.replace(leftDelimiter + key + rightDelimiter, value.toString());
                            } else {
                                line = line.replace(leftDelimiter + key + rightDelimiter, "");
                            }
                        }

                    }
                }
                result.add(line);
            }
        }
        return result;
    }

    private void applyObjectValues(LinkedList obj, String marks[], String leftDelimiter, String rightDelimiter, LinkedList<String> result, LinkedList<String> iterateBody) {
        LinkedList<String> tmp;

        for (int i = 0; i < obj.size(); i++) {
            Object o = obj.get(i);
            try {
                boolean last = i == obj.size() - 1;
                tmp = replaceFlag(o, iterateBody, marks, leftDelimiter, rightDelimiter, last);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
            result.addAll(tmp);
        }
    }

    private void addToCache(Object obj) {
        HashMap<String, Field> objFields = new HashMap<>();

        for (Field f : obj.getClass().getDeclaredFields()) {
            objFields.put(f.getName(), f);
        }

        cache.put(obj.getClass().getName(), objFields);
    }

    private boolean isPresentInCache(String obj, HashMap<String, ?> map) {
        return (map.get(obj) != null);
    }

    private void removeNotUsedMarkers(String mark) {

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i).contains(mark)) {
                message.set(i, "");
            }
        }
    }

    private String generateMessage() {
        String res = "";

        for (String line : message) {
            res += line;
        }

        return res;
    }

    private boolean isNumericNull(Object obj, int definitionOfNull) {
        if (obj instanceof Integer) {
            if (((Integer) obj) == definitionOfNull) {
                return true;
            }
        }
        return false;
    }
}