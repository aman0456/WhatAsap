/**
 * Sample javascript file. Read the contents and understand them, 
 * then modify this file for your use case.
 */
onLoad1();
function onLoad() {
	$("#usersTable").DataTable().ajax.url("AllConversations").load();
}
s
function onLoad1(){
	var myTable;
	$(document).ready(function() {
		var allUsers = "<table id=\"usersTable\" class=\"display\">" + 
		"        <thead>" + 
		"        <tr> <th>UId</th> <th>Timestamp</th> <th>num_msgs</th> </tr>" + 
		"        </thead>" + 
		"    </table>";
		document.getElementById("search").insertBefore
		$('#content').html(allUsers);
		
		myTable = $("#usersTable").DataTable({
	        columns: [{data:"uid"}, {data:"last_timestamp"}, {data:"num_msgs"}]
	    });
		$("#search").autocomplete({
			source: "AutoCompleteUser",
			select: function( event, ui ) {
		        showDetails(ui.item.value);
		}});
		var other_id;
	    $('#usersTable tbody').on( 'click', 'tr', function () {
	        if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        }
	        else {
	            myTable.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	        }
	        
	        other_id= myTable.row(this).data()["uid"];
	        showDetails(other_id);
//	        $('#content').html(myTable.row(this).data()["uid"]);
	            
	    } );
	    
	    //load div contents asynchronously, with a call back function
//	    alert("Page loaded. Click to load div contents.");
//		$("#content").load("content.html", function(response){
//			//callback function
//			alert("Div loaded. Size of content: " + response.length + " characters.");
//		});
		onLoad();
		document.getElementById("create").text = "Create Conversation";
		$("#create").click(function(){
			document.getElementById("create").text = "";
			$('#content').html("<a id=\"createText\">user</a> <input id=\"newField1\" type=\"text\" name = \"other_id\"> \n " +
	        		"<button id=\"createButton\" onclick=\"createConv(document.getElementById(\'newField1\').value)\">Send</button>\n");
	        $("#newField1").autocomplete({
				source: "AutoCompleteUser",
				select: function( event, ui ) {
			        createConv(ui.item.value);
			}});
	    });
		
		
	});

}

function showDetails(other_id)  {
	var str = "<table id=\"convDetail\" class=\"display\">" + 
	"        <thead>" + 
	"        <tr> <th>Post Id</th> <th>Thread Id</th> <th>UId</th> <th>Timestamp</th> <th>Text</th> </tr>" + 
	"        </thead>" + 
	"    </table>" +
	"<br>" +
	"message <input id=\"newField\" type=\"text\" name = \"msg\"> \n" + 
	"<button onclick=\"newMessage('" + other_id + "')\">Send</button>\n";
    $('#content').html(str);
    
    var myTable1 = $("#convDetail").DataTable({
        columns: [{data:"post_id"}, {data:"thread_id"}, {data:"uid"}, {data:"timestamp"}, {data:"text"}]
    });
    myTable1.ajax.url("ConversationDetail?other_id=" + other_id).load();
}

function newMessage(other_id){
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {  
		if  (this.readyState ==  4){
			if (this.status == 200) {
				var myobj1 = JSON.parse(this.responseText);
				//console.log(myobj);
				myobj = myobj1.status;	
				console.log(myobj);
				if  (!myobj) {
					alert("No message sent\n"+ myobj1.message);
				}
		
//					console.log("a;lsfdj");
					var myTable1 = $("#convDetail").DataTable();
				    myTable1.ajax.url("ConversationDetail?other_id=" + other_id).load();
				    document.getElementById("newField").value = "";
				
			}
			else {
				alert("Message can't be sent");
			}	
		}
//	    if (this.readyState == 4 && this.status == 200) {
//	    	
//	    }   
//	    else if((this.readyState == 4 && this.status == 403)||(this.readyState == 4 && this.status == 404)){
//			alert("Message can't be sent");
//		}
	}
	xhttp.open("GET", "NewMessage?other_id="+ other_id + "&msg=" + document.getElementById("newField").value, true);
	xhttp.send();
//	document.location.reload();
	
	return true;
}

function createConv(other_id) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    	var myObj1 = JSON.parse(this.responseText);
       	//console.log(myObj);
       	var myObj = myObj1.status;
       	//console.log(myObj);
       	if (myObj){
       		alert("Conversation successfully created.");
       	}
       	else {
       		
       		alert("Conversation creation unsuccessfull.\n"+ myObj1.message);
       	}
       	onLoad();
       	document.getElementById("newField1").value = "";
    	//var myTable = document.getElementById("sta");
//	      var myTable1 = $("#convDetail").DataTable();
//	      myTable1.ajax.url("ConversationDetail?other_id=" + document.forms["newMess"]["other_id"].value).load();
    	}
	}
	xhttp.open("GET", "CreateConversation?other_id="+ other_id, true);
	xhttp.send();
	document.getElementById("create").text = "Create Conversation";
	$("#createText").remove();
	$("#createButton").remove();
	$("#newField1").remove();
//	document.location.reload();
	
	return true;
}

