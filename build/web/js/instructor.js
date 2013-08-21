function getHome(name) {
    var div = "<h1> WELCOME " + name + "!</h1>";
    div += "<a class=\"ilink\" href=\"javascript:addAsgnFormInstr();\">Add Assignment</br>";
    div += "<a class=\"ilink\" href=\"javascript:setGradeForm();\">Change Grade</a></br>";
    div += "<a class=\"ilink\" href=\"javascript:autoGradeForm();\">Assign Grade</a></br>";
    document.getElementById("centerpane").innerHTML = div;
}

function autoGrade() {
    var assgn_id = document.forms["autograde"]["assgn_id"].value;
    var ct_A = document.forms["autograde"]["ct_A"].value;
    var ct_B = document.forms["autograde"]["ct_B"].value;
    var ct_C = document.forms["autograde"]["ct_C"].value;
    var ct_D = document.forms["autograde"]["ct_D"].value;
    if(ct_A == "" ||  ct_A == null) {
        alert("A field is blank");
        return false;
    }
    else if(ct_B == "" ||  ct_B == null) {
        alert("B field is blank");
        return false;
    }else if(ct_C == "" ||  ct_C == null) {
        alert("C field is blank");
        return false;
    }else if(ct_D == "" ||  ct_D == null) {
        alert("D field is blank");
        return false;
    }
    else {
        var xmlhttp;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else  {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.open("GET","autoGrade?asgn_ID="+assgn_id+"&coA="+ct_A+"&coB="+ct_B+"&coC="+ct_C+"&coD="+ct_D,false);
        xmlhttp.send();
        alert(xmlhttp.responseText);      
    }
    return true;
}

function autoGradeForm() {
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else  {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
            document.getElementById("centerpane").innerHTML = xmlhttp.responseText;
        }
    }
    
    xmlhttp.open("GET","autoGradeForm",true);
    xmlhttp.send();
}


function addAsgnFormInstr() {
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else  {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
            document.getElementById("centerpane").innerHTML=xmlhttp.responseText;
        }
    }
    xmlhttp.open("GET", "addAsgnFormInstr", true);
    xmlhttp.send();
}

function insertAsgnInstr(asgn_ID) {
    var topic = document.forms["assignment"]["topicHeading"].value;
    var mode = document.forms["assignment"]["AssignmentMode"].value;
    var numQ = document.forms["assignment"]["quesNum"].value;
    var date = document.forms["assignment"]["SubmissionDate"].value;
    var hr = document.forms["assignment"]["SubmissionTimeHrs"].value;
    var min = document.forms["assignment"]["SubmissionTimeMin"].value;
    if(topic == "" ||  topic == null) {
        alert("topic field is blank");
        return false;
    } else if(mode == "" ||  mode == null) {
        alert("mode field is blank");
        return false;
    } else if(numQ == "" ||  numQ == null) {
        alert("Number of Question field is blank");
        return false;
    } else if(date == "" ||  date == null) {
        alert("Deadline field is blank");
        return false;
    } else if(hr == "" ||  hr == null) {
        alert("hr field is blank");
        return false;
    } else if(min == "" ||  min == null) {
        alert("min field is blank");
        return false;
    } else {
        var xmlhttp;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else  {
           
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        var arg = "insertAsgnInstr?assgn_ID="+asgn_ID+"&topic="+topic+"&mode="
        +mode+"&numQ="+numQ+"&date="+date+"&hr="+hr+"&min="+min;
        xmlhttp.open("GET",arg, false);
        xmlhttp.send();
        document.getElementById("centerpane").innerHTML=xmlhttp.responseText;
    }
    return true; 
}

function insertQuesI(assgn_ID,num_ques,date,hour,minute,assgn_mode,topic){
    for(var i=1; i<=num_ques; i++){
        var question = document.forms["Add"]["Question"+i].value;
        var answer = document.forms["Add"]["Answer"+i].value;
        var marks = document.forms["Add"]["marks"+i].value;
        var database = document.forms["Add"]["database"+i].value;
        if(question == "" ||  question == null) {
            alert("topic field is blank");
            return false;
        } else if(answer == "" ||  answer == null) {
            alert("answer field is blank");
            return false;
        } else if(marks == "" ||  marks == null) {
            alert("marks field is blank");
            return false;
        } else if(database == "" ||  database == null) {
            alert("database field is blank");
            return false;
        } else{
            continue;
        }
    }
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else  {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    var parameters = "assgn_ID="+assgn_ID+"&num_ques="+num_ques+"&date="+date
    +"&hour="+hour+"&minute="+minute+"&assgn_mode="+assgn_mode+"&topic="+topic;
    for(i=1; i<=num_ques; i++){
        var question1 = document.forms["Add"]["Question"+i].value;
        parameters += "&Question"+i+"="+question1;
        var answer1 = document.forms["Add"]["Answer"+i].value;
        parameters += "&Answer"+i+"="+answer1;
        var marks1 = document.forms["Add"]["marks"+i].value;
        parameters += "&marks"+i+"="+marks1;
        var database1 = document.forms["Add"]["database"+i].value;
        parameters += "&database"+i+"="+database1;
    }
    xmlhttp.open("get", "insertQuesInstr?"+parameters, false);
    xmlhttp.send();
    alert("successfully added!");
    location.reload();
    return true;
}

function setGradeForm() {
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else  {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
            if(xmlhttp.responseText == "") {
                alert("No gradable assignment");
            } else {
                document.getElementById("centerpane").innerHTML = xmlhttp.responseText;
            }
        }
    }
    xmlhttp.open("GET","setGradeForm",true);
    xmlhttp.send();
}