package pt.isel.ls.Dtos;

public class DtoWrapper {
    private Object Checklist;
    private Object Checklist_Task;
    private Object Template;
    private Object Template_Task;


    public Object getChecklist() {
        return Checklist;
    }

    public void setChecklist(Object checklist) {
        Checklist = checklist;
    }

    public Object getChecklist_Task() {
        return Checklist_Task;
    }

    public void setChecklist_Task(Object checklist_Task) {
        Checklist_Task = checklist_Task;
    }

    public Object getTemplate() {
        return Template;
    }

    public void setTemplate(Object template) {
        Template = template;
    }

    public Object getTemplate_Task() {
        return Template_Task;
    }

    public void setTemplate_Task(Object template_Task) {
        Template_Task = template_Task;
    }

    @Override
    public String toString(){
        String res = "";

        if(Checklist != null){
            res += "Checklist Information:\n" + Checklist.toString();
        }
        if(Checklist_Task != null){
            res += "Checklist's Tasks Information:\n" + Checklist_Task.toString();
        }
        if(Template != null){
            res += "Template Information:\n" + Template.toString();
        }
        if(Template_Task != null){
            res += "Template's Tasks Information:\n" + Template_Task.toString();
        }

        return res;
    }
}
