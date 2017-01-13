"use strict"
const message_template = "You have to fill the following fields: "
const color_map = {}
color_map["red"] = "/images/f44336.png"
color_map["pink"] = "/images/e91e63.png"
color_map["purple"] = "/images/9c27b0.png"
color_map["indigo"] = "/images/3f51b5.png"
color_map["blue"] = "/images/2196f3.png"
color_map["cyan"] = "/images/00bcd4.png"
color_map["teal"] = "/images/009688.png"
color_map["green"] = "/images/4caf50.png"
color_map["yellow"] = "/images/ffeb3b.png"
color_map["amber"] = "/images/ffc107.png"
color_map["orange"] = "/images/ff9800.png"
color_map["deep-orange"] = "/images/ff5722.png"
color_map["brown"] = "/images/795548.png"
color_map["grey"] = "/images/9e9e9e.png"
color_map["blue-grey"] = "/images/607d8b.png"


let tags_to_checklists_links = {}

//The following functions handle everything related with UI, and view "validations"(Eg: if we dont have any task in the checklist we show a message alerting the user)
function prepareChecklist_Detailed(){
    prepareMaterialCSS()


    ajaxRequest("GET", "/tags",)
    .then(data =>{
        let resp = JSON.parse(data)
        let popup = document.getElementById("tag_menu")

        let value = '<option value="" disabled selected>Choose your option</option>'

        for(let i = 0; i < resp.entities.length; i++){
            tags_to_checklists_links[resp.entities[i].actions[0].fields[0].value] = resp.entities[i].actions[0]

             value += '<option data-icon="' + color_map[resp.entities[i].properties.color] + '"class="left circle" value="' + resp.entities[i].actions[0].fields[0].value + '">' + resp.entities[i].properties.name + '</option>'
        }

        popup.innerHTML = value

        prepareMaterialCSS()
        console.log("Done getting tags")
    })

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

    //if we dont have any checklist
    let body = document.getElementById("body_content")
    if(body.children.length == 0){
        switch (pageID){
            case 0:
                body.innerHTML = "<center><strong><p style='margin-top: 20%'>You don't have any checklists created.Click on <i class='material-icons'>mode_edit</i> to get started!</p></strong></center>"
                break
            case 1:
                body.innerHTML = "<center><strong><p style='margin-top: 20%'>You don't have any closed checklists!</p></strong></center>"
                break
            case 2:
                body.innerHTML = "<center><strong><p style='margin-top: 20%'>You don't have any checklists with a dueDate created.Click on <i class='material-icons'>mode_edit</i> to get started!</p></strong></center>"
                break
            case 3:
                body.innerHTML = "<center><strong><p style='margin-top: 20%'>You don't have any checklists that contains any task!</p></strong></center>"
                break
        }
    }
}

function prepareTag_Detailed(){
    prepareMaterialCSS()

    if(document.getElementById("checklists").children.length <= 2){
        document.getElementById("message_checklists").innerHTML = "<strong>There aren't any Checklists associated with this Tag.</strong>"
    }
}

function prepareView_Tag(){
    prepareMaterialCSS()

    let body = document.getElementById("body_content")
    if(body.children.length == 0){
        body.innerHTML = "<center><strong><p style='margin-top: 20%'>You don't have any tags created.Click on <i class='material-icons'> mode_edit</i>to get started!</p></strong></center>"
    }
}

function prepareView_Template(){
    prepareMaterialCSS()

    let body = document.getElementById("body_content")
    if(body.children.length == 0){
        body.innerHTML = "<center><strong><p style='margin-top: 20%'>You don't have any templates created.Click on <i class='material-icons'> mode_edit</i>to get started!</p></strong></center>"
    }
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

function validateAssociation_Tag(){
    let selected_tag = document.getElementById("tag_menu").value
    let tmp = window.location.href.split('/')
    let id = tmp[tmp.length -1]

    let data = tags_to_checklists_links[selected_tag].fields[0].name + "=" + tags_to_checklists_links[selected_tag].fields[0].value
    ajaxRequest(tags_to_checklists_links[selected_tag].method, tags_to_checklists_links[selected_tag].href.replace("{cid}", id), data)
    .then(window.location.reload(true))

    return false

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
        xmlhttp.setRequestHeader('accept', 'application/json')
        xmlhttp.send(data)
    })
    return promise
}
