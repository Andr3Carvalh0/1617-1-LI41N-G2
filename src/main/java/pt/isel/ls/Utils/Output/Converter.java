package pt.isel.ls.Utils.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

@SuppressWarnings("ConstantConditions")
class Converter {

    private LinkedList<String> message = new LinkedList<>();
    private boolean isHTML;
    private static final String theme = "bootstrap.min.css";
    private static final String privateKey = "self";

    private static final String lookFor_begin_HTML = "{{";
    private static final String lookFor_end_HTML = "}}";
    private static final String[] SUPPORTED_MARKERS_HTML = {"{{#FOR}}", "{{#END}}"};

    private static final String lookFor_begin_JSON = "<<";
    private static final String lookFor_end_JSON = ">>";
    private static final String[] SUPPORTED_MARKERS_JSON = {"<<#FOR>>", "<<#END>>"};

    //Cache for the objects fields.
    private HashMap<String, HashMap<String, Field>> cache = new HashMap<>();

    //Cache the files that we use as template
    private HashMap<String, LinkedList<String>> file_cache = new HashMap<>();

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
            e.printStackTrace();
            // throw new Exception("Cannot read file!");
        } finally {
            if (io != null) {
                io.close();
            }
        }
    }

    void compile(Object obj, boolean isHTML, String baseFile) throws Exception {
        //Make this the generic-ist way possible
        this.isHTML = isHTML;
        String[] marks = isHTML ? SUPPORTED_MARKERS_HTML : SUPPORTED_MARKERS_JSON;
        String marker_begin = isHTML ? lookFor_begin_HTML : lookFor_begin_JSON;
        String marker_end = isHTML ? lookFor_end_HTML : lookFor_end_JSON;

        allocate(baseFile);

        message = replaceFOR(obj, message, marks, marker_begin, marker_end);

        message = replaceFlag(obj, message, marker_begin, marker_end);

        //Cleanup
        removeNotUsedMarkers(marker_begin);
    }

    void commit(String msg, String outputName) throws Exception {
        if (msg == null) msg = generateMessage();

        if (outputName == null) {
            System.out.println(msg);
        } else {
            try {
                saveToFile(msg, outputName);
                //Copy the css file
                if (isHTML) {
                    allocate(Converter.class.getClassLoader().getResource("./views/" + theme).getPath());
                    String out = outputName.substring(0, outputName.lastIndexOf("/") + 1);
                    saveToFile(generateMessage(), out + theme);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Cannot save file!");
            }
        }
    }

    private void saveToFile(String msg, String outputName) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(outputName);
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println(msg);
        writer.close();
    }

    private LinkedList<String> replaceFOR(Object obj, LinkedList<String> msg, String marks[], String leftDelimiter, String rightDelimiter) {
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
                    applyObjectValues((LinkedList) obj, leftDelimiter, rightDelimiter, result, iterateBody);
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

                        applyObjectValues((LinkedList) value, leftDelimiter, rightDelimiter, result, iterateBody);

                        if (!isHTML) {
                            result.set(result.size() - 1, result.getLast().replace(",", ""));
                        }

                    } catch (IllegalAccessException e) {
                        System.out.println("Error: Cant find field");
                    }
                }
            } else {
                result.add(line);
            }
        }
        return result;
    }

    private LinkedList<String> replaceFlag(Object obj, LinkedList<String> msg, String leftDelimiter, String rightDelimiter) {
        LinkedList<String> result = new LinkedList<>();
        for (String line : msg) {
            if (line.contains(leftDelimiter) && line.contains(rightDelimiter)) {
                String key = line.substring(line.indexOf(leftDelimiter) + 2, line.indexOf(rightDelimiter));
                try {
                    if (!isPresentInCache(obj.getClass().getName(), cache)) {
                        addToCache(obj);
                    }

                    HashMap<String, Field> currentOBJ = cache.get(obj.getClass().getName());
                    Field field = currentOBJ.get(key);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (isHTML) {
                        result.add(line.replace(leftDelimiter + key + rightDelimiter, value == null ? "" : value.toString()));
                    } else {
                        if (value != null) {
                            result.add(line.replace(leftDelimiter + key + rightDelimiter, value.toString()));
                        } else {
                            if (result.getLast().contains(",")) {
                                result.set(result.size() - 1, result.getLast().replace(",\n", "\n"));
                            }
                        }
                    }
                } catch (IllegalAccessException e1) {
                    result.add(line);
                }
            } else {
                result.add(line);
            }
        }
        return result;
    }

    private void applyObjectValues(LinkedList obj, String leftDelimiter, String rightDelimiter, LinkedList<String> result, LinkedList<String> iterateBody) {
        for (Object o : obj) {
            LinkedList<String> tmp = replaceFlag(o, iterateBody, leftDelimiter, rightDelimiter);
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
}