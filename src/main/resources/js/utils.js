"use strict"
const message_template = "You have to fill the following fields: "

//The following functions handle everything related with UI, and view "validations"(Eg: if we dont have any task in the checklist we show a message alerting the user)
function prepareChecklist_Detailed(){
    prepareMaterialCSS()

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
    prepareMaterialCSS()

    const currentPage = window.location.pathname
    const pages = ["/checklists", "/checklists/closed", "/checklists/open/sorted/duedate", "/checklists/open/sorted/noftasks"]

    let pageID = pages.indexOf(currentPage)

    if(pageID == -1)
        pageID++

    const id = "nav_" + pageID

    document.getElementById(id).className = "active";
}

function prepareTag_Detailed(){
    prepareMaterialCSS()

    if(document.getElementById("checklists").children.length <= 2){
        document.getElementById("message_checklists").innerHTML = "<strong>There aren't any Checklists associated with this Tag.</strong>"
    }
}

function prepareView_Tag(){
    prepareMaterialCSS()
}

function prepareView_Template(){
    prepareMaterialCSS()
}

function prepareTemplate_Detailed(){
    prepareMaterialCSS()

    if(document.getElementById("tasks").children.length <= 2){
        document.getElementById("message_tasks").innerHTML = "<strong>There aren't any Tasks associated with this Template.</strong>"
    }
    if(document.getElementById("checklists").children.length <= 2){
        document.getElementById("message_checklists").innerHTML = "<strong>There aren't any Checklists associated with this Template.</strong>"
    }
}

function prepareMaterialCSS(){
    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

    $('.datepicker').pickadate({min: new Date(), format: 'dd-mm-yyyy', container: 'body'})

    $(document).ready(function() {
        $('select').material_select();
    });
}

//Validations
//The following functions validate the data from the submission forms
function validateSubmission_Checklist(){
    let message = message_template
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
        Materialize.toast(message, 4000)
        return false
    }

    form.submit()

}

function validateSubmission_Template(){
    let message = message_template
    let hasMessage = false

    let form =  document.getElementById("template_submit")

    let name = document.getElementById("template_name").value
    let description = document.getElementById("template_description").value

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
        Materialize.toast(message, 4000)
        return false
    }

    form.submit()
}

function validateSubmission_Tag(){
    let message = message_template
    let hasMessage = false

    let form =  document.getElementById("tag_submit")

    let name = document.getElementById("tag_name").value
    let color = document.getElementById("tag_color").value

    console.log(color)

    if(!name || 0 === name.length){
        message += "Name"

        hasMessage = true
    }

    if(!color || 0 === color.length){
        if(hasMessage)
            message += ", "

        message += "Color"
        hasMessage = true
    }

    if(hasMessage){
        Materialize.toast(message, 4000)
        return false
    }

    form.submit()

}

function validateSubmission_Task(){
    let message = message_template
    let hasMessage = false

    let form =  document.getElementById("task_submit")

    let name = document.getElementById("task_name").value
    let description = document.getElementById("task_description").value
    let duedate = document.getElementById("task_duedate").value

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
        Materialize.toast(message, 4000)
        return false
    }

    form.submit()
}



//Deletions
function deleteChecklist(id){

}

function deleteTemplate(id){

}

function deleteTag(id){

}

//Checklists related
function deleteTagChecklist(tag, checklist){

}

function deleteTaskChecklist(task, checklist){

}

//Templates related
function deleteChecklistTemplate(checklist, template){

}

function deleteTaskTemplate(task, template){
    
}

//Tags related
function deleteChecklistTag(checklist, tag){

}

//AJAX - Make request to the server.Used in the deletions
function ajaxRequest(meth, path, data) {
    const promise = new Promise((resolve, reject) => {
        const xmlhttp = new XMLHttpRequest()
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
                if (xmlhttp.status == 200) {
                    resolve(xmlhttp.responseText)
                }
                else {
                    reject(new Error(xmlhttp.statusText))
                }
            }
        }    
        xmlhttp.open(meth, path, true)
        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
        xmlhttp.send(data)
    })
    return promise
}
