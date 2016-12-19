package pt.isel.ls.Utils.Output.Dummies;


import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Tag;
import java.util.LinkedList;

public class WrapperTagsDetailed {
    private String link;
    private String message;
    private int id;
    private Object Tag;
    private Object Checklist;


    public WrapperTagsDetailed(String link, DtoWrapper content){
        this.link = link;
        this.message = link.equals("/tags") ? "Detailed" : "";
        this.id = (((Tag)((LinkedList) content.getTag()).get(0)).getTg_id());
        this.Tag = content.getTag();
        this.Checklist = content.getChecklist();
    }

}
