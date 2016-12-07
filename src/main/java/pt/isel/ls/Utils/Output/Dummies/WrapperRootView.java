package pt.isel.ls.Utils.Output.Dummies;

import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.Tag;
import pt.isel.ls.Dtos.Template;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class WrapperRootView {

    private LinkedList<Checklist> Checklist = new LinkedList<>();
    private LinkedList<Template> Template = new LinkedList<>();
    private LinkedList<Tag> Tag = new LinkedList<>();
    private String message_tags = "", message_checklists = "", message_templates = "";

    public WrapperRootView(LinkedList<Checklist> checklists, LinkedList<Template> templates, LinkedList<Tag> tags) {
        this.Checklist = checklists;

        this.Template = templates;

        this.Tag = tags;

        if (checklists.size() == 0) {
            message_checklists = "There arent any checklists in the Database!";
        }

        if (templates.size() == 0) {
            message_templates = "There arent any templates in the Database!";
        }

        if (tags.size() == 0) {
            message_tags = "There arent any checklists in the Database!";
        }


    }


}
