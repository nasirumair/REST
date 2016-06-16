<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

</head>
<style>
.mainDiv {
background-color:#ADD8E6;
width:auto;
} 

.dataDiv:hover{
	background-color:  #F9EDED;
}

#data{
    border-style: solid;
    border-width: 5px;
  /*   overflow-y:scroll;
    height:560px; */
}
</style>
<body >

<div class="container mainDiv">
  <input type="hidden" name="lat" id="lat" value="">
  <input type="hidden" name="lng" id="lng" value="">
  <input type="hidden" name="temp" id="temp" value="">
  
	<form>
	  <div class="form-group" style="display:none;">
	    <label for="id_no">User ID</label>
	    <input class="form-control" type="number" min="1" name="id_no" id="id_no" placeholder="User ID"/>
	  </div>
	  <div class="form-group">
	    <label for="userName">User Name</label>
		<input class="form-control" type="text" name="userName" id="userName" placeholder="User Name"/>
	  </div>
	   <div class="form-group">
	    <label for="textEntry">Text entry for above user</label>
		<input class="form-control" type="text" name="textEntry" id="textEntry" placeholder="Text Entry"/> 
	  </div>
	  <div class="form-group">
	    <label for="city">City/Postal Code</label>
		<input class="form-control" type="text" name="city" id="city" oninput="getWeather()" placeholder="City/Postal Code"/> 
	  </div>
	 
	  <div class="form-group">
	  	<input style="display:none;" class="btn btn-default" type="button" name="fetch_user" id="fetch_user" value="Get User Data" onclick="getSingle()">
		<input style="display:none;" class="btn btn-default" type="button" name="fetch_all_users" id="fetch_all_users" value="Get All User Data" onclick="getAll()">
		<input class="btn btn-default" type="button" name="post_text" id="post_text" value="Post Text Data" onclick="getText()">
		<input class="btn btn-default" type="button" name="fetch_all_text" id="fetch_all_text" value="Get All Text Data" onclick="getAllText()">
		<input style="display:none;" class="btn btn-default" type="button" name="fetch_lat_lng" id="fetch_lat_lng" value="Get LAT LANG" onclick="getWeather()">	  
	  	
	  </div>
	  	  <font color="red"><div id="cord"></div></font>
	  <div class="form-group">
	  	<h1><div id="title">Data will be displayed below</div></h1>
	  </div>
	  <div class="form-group">
	  	<h3><div id="data"></div></h3>
	  </div>
	  
<!-- 	  <button style="display:none;" type="submit" class="btn btn-default">Submit</button> -->
</form>
</div>
</body>
</html>

<script language="javascript" type="text/javascript">
	var request = null;
	
	function getText() {
		createRequest();
		document.getElementById('data').innerHTML = "";
		var text = document.getElementById("textEntry").value;
		var userName = document.getElementById("userName").value;
		var city = document.getElementById("city").value;
		if(text == null || text == "" || text=="undefined"){
			alert("Please enter a value for the Text");
			window.setTimeout(function () { document.getElementById('text').focus(); }, 0); 
			return;
		}
		if(userName == null || userName == "" || userName=="undefined"){
			alert("Please enter a value for the User Name");
			window.setTimeout(function () { document.getElementById('userName').focus(); }, 0); 
			return;
		}
		if(city == null || city == "" || city=="undefined"){
			alert("Please enter a value for the City");
			window.setTimeout(function () { document.getElementById('city').focus(); }, 0); 
			return;
		}
		
		document.getElementById('title').innerHTML = "Displaying Single Text for: "+userName;
		var lat = document.getElementById('lat').value; 
		var lng = document.getElementById('lng').value; 
		var temp = document.getElementById('temp').value; 
		var url = "https://localhost:8443/UserManagement/rest/UserService/users/setText";
		request.onreadystatechange = handleTextResponse;
		request.open("PUT", encodeURI(url), true);
		request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		request.send("text="+text+"&user="+userName+"&lat="+lat+"&lng="+lng+"&temp="+temp);
		
	}
	
	function getWeather(){

		createRequest();
	    var city = document.getElementById("city").value;
		var APPID="779a48b483954296ac3181105161406";
		var url = "https://api.apixu.com/v1/current.json?key="+APPID+"&q="+city;
		//var url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&APPID="+APPID;
		request.onreadystatechange = handleWeatherResponse;
		request.responseType = 'json';
		request.open("GET", encodeURI(url), true);
		request.send(null);
    }
	
	function getReplyText(replyText, id, text) {
		createRequest();
		var userName = document.getElementById("userName").value;
		var url = "https://localhost:8443/UserManagement/rest/UserService/users/setReplyText";
		var x = request.onreadystatechange = function(){
			if (request.readyState == 4 && request.status == 200) {
				var txtDocument = request.responseText;
				document.getElementById('replyBlock'+id).innerHTML = "&emsp;&emsp;&emsp;&emsp;&#8627;&emsp;"+txtDocument;
			}
			if (request.readyState == 4 && request.status != 200) {
				document.getElementById('data').innerHTML = "No text data was found";
			}
		};
		request.open("POST", encodeURI(url), true);
		request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		request.send("text="+text+"&user="+userName+"&reply="+replyText);
		
	}
	
	function getAllText() {
		createRequest();
		document.getElementById('data').innerHTML = "";
		var userName = document.getElementById("userName").value;
		if(userName == null || userName == "" || userName=="undefined"){
			alert("Please enter a value for the User Name");
			window.setTimeout(function () { document.getElementById('userName').focus(); }, 0); 
			return;
		}
		document.getElementById('title').innerHTML = "Displaying All Text for: "+userName;
		
		var url = "https://localhost:8443/UserManagement/rest/UserService/users/getAll/"+userName;
		request.onreadystatechange = handleAllTextResponse;
		request.open("GET", encodeURI(url), true);
		request.send(null);
		
	}
	
	function handleTextResponse() {
		if (request.readyState == 4 && request.status == 200) {
			var txtDocument = request.responseText;
			var textArray= txtDocument.split(",");
 			document.getElementById('data').innerHTML = "&emsp;&emsp;<b>Text</b>: "+textArray[0]+"&emsp;&emsp;<b>Lat</b>: "+textArray[1]+"  <b>Lng</b>: "+textArray[2]+"  <b>Temp</b>: "+textArray[3]+"&deg; C";
		}
		if (request.readyState == 4 && request.status != 200) {
			document.getElementById('data').innerHTML = "No text data was found";
		}

	}
	
	function getSingle() {
		createRequest();
	    document.getElementById('data').innerHTML = "";
		var playerid = document.getElementById("id_no").value;
		if(playerid == null || playerid == "" || playerid=="undefined" || isNaN(playerid)){
			alert("Please Enter a number value for the ID field");
			window.setTimeout(function () { document.getElementById('id_no').focus(); }, 0); 
			return;
		}
		var id = eval(playerid);
		
		document.getElementById('title').innerHTML = "Displaying Single User for ID: "+id;
		
		var url = "https://localhost:8443/UserManagement/rest/UserService/users/"+ id;
		request.onreadystatechange = handleResponse;
		request.open("GET", encodeURI(url), true);
		request.send(null);
		
	}
	
		
	function getAll() {
		createRequest();
		document.getElementById('data').innerHTML = "";
		
		document.getElementById('title').innerHTML = "Displaying All USERS";
		
		
		var url = "https://localhost:8443/UserManagement/rest/UserService/users";
		request.onreadystatechange = handleResponse;
		request.open("GET", encodeURI(url), true);
		request.send(null);
		
	}
	
	function createRequest() {
	
		try {
			request = new XMLHttpRequest();
		} catch (trymicrosoft) {
			try {
				request = new ActiveXObject("MsXML2.XMLHTTP");
			} catch (othermicrosoft) {
				try {
					request = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (failed) {
					request = null;
				}
			}
		}

		if (request == null)
			alert("Error creating request object!");
			
			
	}
	
	function handleWeatherResponse() {
		if (request.readyState == 4 && request.status == 200) {
			var lat = request.response.location.lat;
			var lng = request.response.location.lon;
			var temp = request.response.current.temp_c;
			document.getElementById('cord').innerHTML = "Latitude: "+lat+" and Longitude: "+lng+" and Temperature: "+temp+"&deg; C";
			document.getElementById('lat').value = lat;
			document.getElementById('lng').value = lng;
			document.getElementById('temp').value = temp;

		}
		
		if (request.readyState == 4 && request.status != 200) {
			document.getElementById('data').innerHTML = "No weather data was found";
		}

	}

	function handleResponse() {
	
		if (request.readyState == 4 && request.status == 200) {
			var xmlDocument = request.responseXML;
 			var fnames = xmlDocument.getElementsByTagName("name");
			var professions = xmlDocument.getElementsByTagName("profession");
			var ids = xmlDocument.getElementsByTagName("id");
			if(fnames.length==0 || professions.length==0 || ids.length==0){
 				document.getElementById('data').innerHTML = "No text data was found";
 			}
 			for (var i = 0; i < fnames.length; i++) {
				var fname = fnames[i].childNodes[0].nodeValue;
				var profession = professions[i].childNodes[0].nodeValue;
				var id = ids[i].childNodes[0].nodeValue;
				document.getElementById('data').innerHTML += id + " " + fname + " "+ profession + "<br>";
			}
			
		}
		if (request.readyState == 4 && request.status != 200) {
			document.getElementById('data').innerHTML = "No data was found";
		}

	}
	
	function handleAllTextResponse() {
	
		if (request.readyState == 4 && request.status == 200) {
			var xmlDocument = request.responseXML;
 			var texts = xmlDocument.getElementsByTagName("text");
 			var times = xmlDocument.getElementsByTagName("time");
 			var dates = xmlDocument.getElementsByTagName("date");
 			var lats = 	xmlDocument.getElementsByTagName("lat");
 			var lngs= xmlDocument.getElementsByTagName("lng");
 			var temps = xmlDocument.getElementsByTagName("temp");
 			var replies = xmlDocument.getElementsByTagName("reply");
 			var users = xmlDocument.getElementsByTagName("userName");
 			if(texts.length==0){
 				document.getElementById('data').innerHTML = "No text data was found";
 			}
 			for (var i = 0; i < texts.length; i++) {
				var text = texts[i].childNodes[0].nodeValue;
				var time = times[i].childNodes[0].nodeValue;
				var date = dates[i].childNodes[0].nodeValue;
				var lat = lats[i].childNodes[0].nodeValue;
				var lng = lngs[i].childNodes[0].nodeValue;
				var temp = temps[i].childNodes[0].nodeValue;
				var user = users[i].childNodes[0].nodeValue;
				
				if(replies[i].childNodes[0] != null){
					var reply = replies[i].childNodes[0].nodeValue;
				}
				
				document.getElementById('data').innerHTML += "<div class='dataDiv' id="+i+" onclick='reply(id)'><b>["+i+"]</b>: <span id=\"mes"+i+"\">"+text+ "</span>  <b>Time</b>: "+time+"  <b>Date</b>: "+date+"  <b>Lat</b>: "+lat+"  <b>Lng</b>: "+lng+"  <b>Temp</b>: "+temp+"<input style=\"float: right;\" class=\"btn btn-default\" type=\"button\" name=\"del_text"+i+"\" id=\"del_text"+i+"\" value=\"Delete\" onclick=\"delText('"+text+"','"+user+"','"+time+"','"+date+"')\"></div><br>";
				 if(reply != null && reply != "" && replies[i].childNodes[0] != null){
					var iDiv = document.createElement('div');
					iDiv.id = 'replyBlock'+i;
					iDiv.className = 'replyBlock'+i;
					iDiv.innerHTML = "&emsp;&emsp;&emsp;&emsp;&#8627;&emsp;"+reply;	
					document.getElementById(i).appendChild(iDiv);
				} 
				
 			}
		}
		if (request.readyState == 4 && request.status != 200) {
				document.getElementById('data').innerHTML = "No text data was found";
		
		}

	}
	
	function delText(text, user, time, date){
	
	document.getElementById('data').innerHTML = "";
	createRequest();
	var url = "https://localhost:8443/UserManagement/rest/UserService/users/delText";
	request.onreadystatechange = handleAllTextResponse;
	request.open("PUT", encodeURI(url), true);
	request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	request.send("text="+text+"&user="+user+"&time="+time+"&date="+date);
	
	}
	
	function reply(id) {
		var text=document.getElementById("mes"+id).innerHTML;
		var iDiv = document.createElement('div');
		iDiv.id = 'replyBlock'+id;
		iDiv.className = 'replyBlock'+id;
		document.getElementById(id).appendChild(iDiv);
		var reply = prompt("What would you like to reply to '"+text+"'  with?", "");
		if(reply == null || reply == "null"){
				return;
		}else{
				getReplyText(reply, id, text);
		}
		
	}
</script>



