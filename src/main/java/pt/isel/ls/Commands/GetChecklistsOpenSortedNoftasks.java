package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.Checklist_Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class GetChecklistsOpenSortedNoftasks extends Command {
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String method = "GET";
    private final String[] path = {"", "checklists", "open", "sorted", "noftasks"};

    public class Wrapper{
        private Checklist check;
        private LinkedList<Checklist_Task> tasks;
        private int size;

        public Wrapper(Checklist checklist, LinkedList<Checklist_Task> task, int size){
            this.check = checklist;
            this.tasks = task;
            this.size = size;
        }

        public Checklist getCheck() {
            return check;
        }

        public LinkedList<Checklist_Task> getTasks() {
            return tasks;
        }

        public int getSize() {
            return size;
        }
    }

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "select * from checklist where Cl_closed = 0";
        String s2 = "select * from checklist_task where Cl_id = ? and Cl_Task_closed = 0";

        LinkedList<Checklist> checklists = new LinkedList<>();
        LinkedList<Wrapper> wrapper = new LinkedList<>();

        PreparedStatement ps = con.prepareStatement(s1);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String description = rs.getString(3);
            boolean closed = rs.getBoolean(4);

            String dueDate = (rs.getDate(5) != null) ? df.format(rs.getDate(5)) : null;
            int Tp_id = (rs.getString(6) == null) ? -1 : rs.getInt(6);
            checklists.add(new Checklist(id, name, description, closed, dueDate, Tp_id));
        }
        if(checklists.size() == 0){
            throw new Exception("No Checklists uncompleted");
        }

        for (Checklist c: checklists) {
            ps = con.prepareStatement(s2);
            ps.setString(1, c.getId() + "");
            rs = ps.executeQuery();
            LinkedList<Checklist_Task> tasks_per_check = new LinkedList<>();

            while (rs.next()){
                int Cl_Task_id = rs.getInt(1);
                int Cl_id = rs.getInt(2);
                int Cl_Task_index = rs.getInt(3);
                boolean Cl_Task_closed = rs.getBoolean(4);
                String Cl_Task_name = rs.getString(5);
                String Cl_Task_desc = rs.getString(6);
                String Cl_Task_duedate = (rs.getDate(7) != null) ? df.format(rs.getDate(7)) : null;

                tasks_per_check.add(new Checklist_Task(Cl_Task_id, Cl_id, Cl_Task_index, Cl_Task_closed, Cl_Task_name, Cl_Task_desc, Cl_Task_duedate));
            }
            wrapper.add(new Wrapper(c, tasks_per_check, tasks_per_check.size()));
        }

        checklists = new LinkedList<>();

        wrapper.sort((o1, o2) -> {
            if(o1.getSize() > o2.getSize()) return -1;
            if(o1.getSize() < o2.getSize()) return 1;
            return  0;
        });

        for (Wrapper w: wrapper) {checklists.add(w.getCheck());}

        return checklists;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
