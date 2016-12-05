package pt.isel.ls.Server.Utils;


import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Tag;

import java.util.LinkedList;


public class WrapperTagsDetailed {
    private String link;
    LinkedList<Tag> Tag;
    LinkedList<Checklist> Checklist;


    public WrapperTagsDetailed(String link, DtoWrapper content){
        this.link = link;
        this.Tag = (LinkedList<pt.isel.ls.Dtos.Tag>) content.getTag();
        this.Checklist = (LinkedList<pt.isel.ls.Dtos.Checklist>) content.getChecklist();
    }

}
