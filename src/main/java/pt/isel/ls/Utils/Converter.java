package pt.isel.ls.Utils;

import java.util.*;
import java.io.*;

public class Converter {

    private LinkedList<String> message = new LinkedList<>();
    private boolean isHTML;

    private static final String lookFor_begin_HTML = "{{";
    private static final String lookFor_end_HTML = "}}";
    private static final String[] SUPPORTED_MARKERS_HTML = {"{{#FOR}}", "{{#FOR_M}}", "{{#END_M}}"};

    private static final String lookFor_begin_JSON = "<<";
    private static final String lookFor_end_JSON = ">>";
    private static final String[] SUPPORTED_MARKERS_JSON = {"<<#FOR>>", "<<#FOR_M>>", "<<#END_M>>"};


    //Reads file to memory
    private void allocate(String baseFile) throws Exception {
        Scanner io = null;
        try {
            io = new Scanner(new File(baseFile));

            while (io.hasNextLine()) {
                message.add(io.nextLine() + "\n");
            }

        } catch (FileNotFoundException e) {
            throw new Exception("Cannot read file!");
        } finally {
            if (io != null) {
                io.close();
            }
        }
    }

    public void compile(LinkedList<HashMap<String, String[]>> list, String outputName, boolean isHTML, String baseFile) throws Exception {
        //Make this the generic-ist way possible
        this.isHTML = isHTML;
        String[] marks = isHTML ? SUPPORTED_MARKERS_HTML : SUPPORTED_MARKERS_JSON;
        String marker_begin = isHTML ? lookFor_begin_HTML : lookFor_begin_JSON;
        String marker_end = isHTML ? lookFor_end_HTML : lookFor_end_JSON;

        allocate(baseFile);

        int[] l = {0};
        message = replaceFOR_M(list, l, message.size(), message, new LinkedList<>(), marks, marker_begin, marker_end, 0);

        l[0] = 0;
        message = replaceFOR(list, l, message.size(), message, new LinkedList<>(), marks[0], marker_begin, marker_end);

        l[0] = 0;
        message = replaceFlag(list, l, message.size(), message, new LinkedList<>(), marker_begin, marker_end);

        //Cleanup
        removeNotUsedMarkers(marks);

        commit(generateMessage(), outputName);

    }

    private String generateMessage() {
        String res = "";

        for (String line : message) {
            res += line;
        }

        return res;
    }

    // Saves the string message into the outputName file
    public void commit(String msg, String outputName) throws Exception {

        if (outputName == null) {
            System.out.println(msg);
        } else {
            try {
                File file = new File(outputName);
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.println(msg);
                writer.close();
            } catch (FileNotFoundException e) {
                throw new Exception("Cannot save file!");
            }
        }
    }

    private LinkedList<String> replaceFOR_M(LinkedList<HashMap<String, String[]>> list, int[] l, int r, LinkedList<String> msg, LinkedList<String> res, String marks[], String leftDelimiter, String rightDelimiter, int forID){
        if (l[0] >= r) {
            return res;
        }

        String line = msg.get(l[0]);
        LinkedList<String> tmp = new LinkedList<>();
        int numberLine2Delete = 0;

        if (msg.get(l[0]).contains(marks[1]) && msg.get(l[0]).contains(leftDelimiter + "" + forID + rightDelimiter)) {
            for (int k = l[0]+1;!message.get(k).contains(marks[2]) && !message.get(k).contains(leftDelimiter + "" + forID + rightDelimiter); k++, numberLine2Delete++) {
                line = message.get(k);

                //Remove the identifiers
                if(line.contains(marks[1])) {
                    line = line.replace(marks[1], "");
                    line = line.replace(leftDelimiter + "" + forID + rightDelimiter, "");
                }
                tmp.add(line);
            }
            int left[] = {0};
            tmp = replaceFOR_M(list, left, tmp.size(), tmp, new LinkedList<>(), marks, leftDelimiter, rightDelimiter, forID+1);

            LinkedList<String> tmp1 = new LinkedList<>();
            LinkedList<String> tmp2 = new LinkedList<>();
            int tmp1_size = 0;
            for (int k = 0; k < list.size(); k++) {
                LinkedList<HashMap<String, String[]>> map = new LinkedList<>();
                map.add(list.get(k));

                left[0] = 0;
                for(String text : replaceFOR(map, left, tmp.size(), tmp, new LinkedList<>(), marks[0], leftDelimiter, rightDelimiter)){
                    tmp1.add(text);
                }

                left[0] = tmp1_size * k;
                if(containsFlag(left[0], tmp1_size == 0 ? tmp1.size() : tmp1_size * (k+1),tmp1, leftDelimiter, rightDelimiter, marks[0])){
                    for(String text : replaceFlag(map, left, tmp1_size == 0 ? tmp1.size() : tmp1_size * (k+1), tmp1, new LinkedList<>(), leftDelimiter, rightDelimiter)){
                        tmp2.add(text);
                    }
                    if(tmp1_size == 0){
                        tmp1_size = tmp2.size();
                    }
                }
            }

            tmp = tmp2.size() == 0 ? tmp1 : tmp2;

            //Concatunate the list with values with the template
            LinkedList<String> proccessed = new LinkedList<>();
            for(int k = 0; k < l[0]; k++){
                proccessed.add(msg.get(k));
            }
            for (int k = 0; k < tmp.size(); k++) {
                proccessed.add(tmp.get(k));
            }

            //Remove the last ","
            if(!isHTML){
                proccessed.set(proccessed.size()-1, proccessed.getLast().substring(0, proccessed.getLast().length() -2)  + "\n");
            }

            for (int k = l[0] + numberLine2Delete+1; k < msg.size(); k++) {
                proccessed.add(msg.get(k));
            }

            msg = proccessed;
            return msg;
        } else {
            res.add(line);
        }

        ++l[0];
        return replaceFOR_M(list, l, r, msg, res, marks, leftDelimiter, rightDelimiter, forID);
    }

    private boolean containsFlag(int left, int size, LinkedList<String> tmp,String leftDelimiter,String rightDelimiter, String FORMark) {
        for (int i = left; i < size; i++) {
            if(tmp.get(i).contains(leftDelimiter) && tmp.get(i).contains(rightDelimiter) && !tmp.get(i).contains(FORMark)) return true;
        }
        return false;
    }

    private LinkedList<String> replaceFlag(LinkedList<HashMap<String, String[]>> list, int[] l, int r, LinkedList<String> msg, LinkedList<String> res, String leftDelimiter, String rightDelimiter) {
        if (l[0] >= r) {
            return res;
        }
        String line = msg.get(l[0]);
        if(line.contains(leftDelimiter) && line.contains(rightDelimiter)){
             String key = line.substring(line.indexOf(leftDelimiter) + 2, line.indexOf(rightDelimiter));

            String tmp[];
            if ((tmp = list.get(0).get(key)) != null) {
                res.add(line.replace(leftDelimiter + key + rightDelimiter, tmp[0]));
            }
        } else {
            res.add(line);
        }

        ++l[0];
        return replaceFlag(list, l, r, msg, res, leftDelimiter, rightDelimiter);
    }

    private LinkedList<String> replaceFOR(LinkedList<HashMap<String, String[]>> list, int[] l, int r, LinkedList<String> msg, LinkedList<String> res, String mark, String leftDelimiter, String rightDelimiter) {
        if (l[0] >= r) {
            return res;
        }

        String line = msg.get(l[0]);

        if (msg.get(l[0]).contains(mark)) {
            line = line.replace(mark, "");

            String key = line.substring(line.indexOf(leftDelimiter) + 2, line.indexOf(rightDelimiter));

            for (int j = 0; j < list.size(); j++) {
                String tmp[];

                if ((tmp = list.get(j).get(key)) != null) {
                    for (int k = 0; k < tmp.length; k++) {
                        String aux = line;

                        aux = (!isHTML && k != tmp.length-1) ? aux.substring(0, aux.length()-1) + ",\n" : aux;

                        if(aux.contains(leftDelimiter + key + rightDelimiter)){
                            aux = aux.replace(leftDelimiter + key + rightDelimiter, tmp[k] + "");
                        }
                        res.add(aux);
                    }
                }
            }
        } else {
            res.add(line);
        }

        ++l[0];
        return replaceFOR(list, l, r, msg, res, mark, leftDelimiter, rightDelimiter);
    }

    //Remove not used marks
    private void removeNotUsedMarkers(String[] marks) {

        for (int i = 0; i < message.size(); i++) {
            for (String mark : marks) {
                if (message.get(i).contains(mark)) {
                    message.set(i, message.get(i).replace(mark, ""));
                }
            }
        }
    }
}