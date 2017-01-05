"use strict"

function prepareChecklist_Detailed(){
    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

    $(document).ready(function() {
        $('select').material_select()
    });

    $('.datepicker').pickadate({min: new Date()})

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
    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

    $('.datepicker').pickadate({min: new Date(), dateFormat: 'yy-mm-dd' })

    const currentPage = window.location.pathname
    const pages = ["/checklists", "/checklists/closed", "/checklists/open/sorted/duedate", "/checklists/open/sorted/noftasks"]

    let pageID = pages.indexOf(currentPage)

    if(pageID == -1)
        pageID++

    const id = "nav_" + pageID

    document.getElementById(id).className = "active";
}

function prepareTag_Detailed(){

    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

    if(document.getElementById("checklists").children.length <= 2){
        document.getElementById("message_checklists").innerHTML = "<strong>There aren't any Checklists associated with this Tag.</strong>"
    }
}

function prepareView_Tag(){
    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

    $(document).ready(function() {
        $('select').material_select();
    });
}

function prepareView_Template(){
    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

    $(document).ready(function() {
        $('select').material_select();
    });
}

function prepareTemplate_Detailed(){
    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

    $('.datepicker').pickadate({min: new Date(), dateFormat: 'yy-mm-dd'})

    if(document.getElementById("tasks").children.length <= 2){
        document.getElementById("message_tasks").innerHTML = "<strong>There aren't any Tasks associated with this Template.</strong>"
    }
    if(document.getElementById("checklists").children.length <= 2){
        document.getElementById("message_checklists").innerHTML = "<strong>There aren't any Checklists associated with this Template.</strong>"
    }
}




function validateSubmission_Checklist(){
    let message = "You have to fill the following fields: "
    let hasMessage = false

    let form =  document.getElementById("checklist_submit")

    let name = document.getElementById("checklist_name").value
    let description = document.getElementById("checklist_description").value
    let duedate = document.getElementById("checklist_duedate").value

    if(!name || 0 === name.length){
        message += "Name"

        hasMessage = true
    }

    if(!description || 0 === description.length){
        if(hasMessage)
            message += ", "

        message += "Description"
        hasMessage = true
    }

    if(hasMessage){
        alert(message)
        return false
    }

    return false
//    form.submit()

}

function validateSubmission_Template(){
    return false

}

function validateSubmission_Tag(){
    return false

}

function validateSubmission_Task(){
    return false
}