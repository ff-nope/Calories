
<html>
<head>
<meta charset="UTF-8">

</head>
<body>


<h1>Read Me</h1>


<p>	Please go to <a href="https://github.com/ff-nope/Calories"> https://github.com/ff-nope/Calories  </a> for source code. </p>
<p>Below is the list of available REST services <br />
	
</p>
<hr>

<h2>API resources: </h2>
<p>
	<strong>/jpa03/api/v2/crud -</strong> <em>Root for the backend server. A http GET to this url will return an error message with a link to the readme (this) file.</em>
</p>
<BR>

<p>
	<strong>/jpa03/api/v2/crud/chk_user -</strong> <em>A http POST to this url with the first and last name in stringfied  JSON will return http code 200 if the   user exists, or 206 if non-existing. User object is then created. </em>
	<p  class="Ident">Sample:<BR>
    {<BR>
        "fname": "Isadora",<BR>
        "lname": "Duncan",<BR>
       }
  <BR>
</p>
<BR>


<p>
	<strong>/jpa03/api/v2/crud/rec_user -</strong> <em>A http POST to this url with the first name, last name and date of birth in stringfied  JSON will record a  new user, without items. </em>
	<p  class="Ident">Sample:<BR>
    {<BR>
        "fname": "Isadora",<BR>
        "lname": "Duncan",<BR>
        "dob":"1929/01/22"<BR>
       }
  <BR>
</p>
<BR>

<p>
	<strong>/jpa03/api/v2/crud/rec_item -</strong> <em>Receives http POST with user and item from view in JSON,  calls dao in model to persist item. Note the meal time in epoch format.</em>
	<p  class="Ident">Sample:<BR>
    {<BR>
        "fname": "Isadora",<BR>
        "lname": "Duncan",<BR>
        "dob":"1929/01/22"<BR>
        "memitem":"Apple Pie",<BR>
        "calorias":600,<BR>
        "quando":1431101164<BR>
       }
  <BR>
</p>
<BR>

<p>
	<strong>/jpa03/api/v2/crud/del_user -</strong> <em>Receives http POST and deletes user by name. Expects name in JSON, thusly -</em>
	<p  class="Ident">Sample:<BR>
    {<BR>
        "fname": "Isadora",<BR>
        "lname": "Duncan",<BR>
       } <BR>
        <em>Possible remaining items go as well, thanks to CascadeType set to ALL. Note that orphanremoval=true has no effect here.</em>
  <BR>
</p>
<BR>

<p>
	<strong>/jpa03/api/v2/crud/del_item -</strong> <em>Receives http POST and deletes an item by user name and item index. Expect 3 fields on JSON, like this -</em>
	<p  class="Ident">Sample:<BR>
    {<BR>
        "fname": "Isadora",<BR>
        "lname": "Duncan",<BR>
        "idxToKill":0<BR>
       } <BR>
        
  <BR>
</p>

</body>
</html>
