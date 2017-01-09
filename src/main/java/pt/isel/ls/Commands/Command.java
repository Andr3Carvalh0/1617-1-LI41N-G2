package pt.isel.ls.Commands;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public abstract class Command {
    public abstract Object execute(HashMap<String, String> params, Connection con) throws Exception;
    public abstract String getMethod();
    public abstract String[] getPath();


    boolean validDate(String date){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = dateFormat.format(new Date());

        if(date == null)
            return true;

        String[] user = date.split("-|//.|/");
        String[] current = current_date.split("-");

        if(user.length != current.length){
            return false;
        }

        if(Integer.valueOf(user[2]) > Integer.valueOf(current[2])){
           return true;
        }

        if(Integer.valueOf(user[1]) > Integer.valueOf(current[1]) && Integer.valueOf(user[1]) > 0 && Integer.valueOf(user[1]) < 13){
            return true;
        }

        return Integer.valueOf(user[0]) > Integer.valueOf(current[0]) && Integer.valueOf(user[0]) > 0 && Integer.valueOf(user[0]) < 32;

    }
    String formatDate(String date) throws Exception {
        if(date == null)
            return null;

        String[] user = date.split("-|//.|/");
        String day;
        String month;
        String year;

        if(Integer.parseInt(user[0]) < 32 ){
            day = user[0];
            month = user[1];
            year = user[2];

        }else{
            day = user[2];
            month = user[1];
            year = user[0];
        }

        if(year.equals("") || month.equals("") || day.equals("")){
            throw new Exception("Invalid date format");
        }


        return year + "-" + month + "-" + day;
    }


}
