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


    public boolean validDate(String date){
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String current_date = dateFormat.format(new Date());


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
    public String formatDate(String date) throws Exception {
        String[] user = date.split("-|//.|/");
        String day = "";
        String month = "";
        String year = "";

        for (int i = 0; i < 3; i++) {
            if(Integer.valueOf(user[i]) > 999){
                year = user[i];
            }

            //Handle day
            if(Integer.valueOf(user[i]) > 0 && Integer.valueOf(user[i]) < 32){
                day = user[i];
            }

            //Handle month
            if(Integer.valueOf(user[i]) < 12 && Integer.valueOf(user[i]) > 0){
                if(month.equals("")){
                    month = user[i];
                }else{
                    day = user[i];
                }
            }

        }

        if(year.equals("") || month.equals("") || day.equals("")){
            throw new Exception("Invalide date format");
        }


        return year + "-" + month + "-" + day;
    }


}
