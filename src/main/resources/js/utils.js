"use strict"

function prepareChecklist_Detailed(){
    $(document).ready(function() {
        $('select').material_select();
    });

    if(document.getElementById("ul_Tasks") != null){

        let Tasks = document.getElementById("ul_Tasks").children
        let numberChildrens = Tasks.length;
        let checklistID = window.location.pathname.split("/")[2]

        for(let i = 1; i < numberChildrens - 1; i++){
            let taskID = Tasks[i].getAttribute("id").split("_")[1]
            let state = Tasks[i].getAttribute("data")

            let form = Tasks[i].children[2]
            document.getElementById(form.getAttribute("id")).action = "/checklists/" + checklistID + "/tasks/" + taskID

            let checkboxID = form.getAttribute("id").split("_")[1]

            if((state === 'true')){
                document.getElementById('checkbox_' + checkboxID + '_state').innerHTML = "Closed"
                document.getElementById('checkbox_' + checkboxID).checked = true

            }else{
                document.getElementById('checkbox_' + checkboxID + '_state').innerHTML = "Open"
            }
        }
    }

    if(document.getElementById("ul_Tags").children.length <= 2){

        document.getElementById("message_tags").innerHTML = "<strong>There aren't any Tags associated with this Checklist.</strong>"
    }

    if(document.getElementById("ul_Tasks").children.length <= 2){

        document.getElementById("message_task").innerHTML = "<strong>There aren't any Tasks associated with this Checklist.</strong>"
    }
}


function prepareView_Checklist(){
    const currentPage = window.location.pathname
    const pages = ["/checklists", "/checklists/closed", "/checklists/open/sorted/duedate", "/checklists/open/sorted/noftasks"]

    let pageID = pages.indexOf(currentPage)

    if(pageID == -1)
        pageID++

    const id = "nav_" + pageID

    document.getElementById(id).className = "active";
}

function prepareTag_Detailed(){
    if(document.getElementById("checklists").children.length <= 2){
        document.getElementById("message_checklists").innerHTML = "<strong>There aren't any Checklists associated with this Template.</strong>"
    }
}

function prepareView_Tag(){
    $(document).ready(function() {
        $('select').material_select();
    });
}

function prepareTemplate_Detailed(){
    if(document.getElementById("tasks").children.length <= 2){
    document.getElementById("message_tasks").innerHTML = "<strong>There aren't any Tasks associated with this Template.</strong>"
    }
    if(document.getElementById("checklists").children.length <= 2){
    document.getElementById("message_checklists").innerHTML = "<strong>There aren't any Checklists associated with this Template.</strong>"
    }
}