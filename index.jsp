<%-- 
    Document   : index
    Created on : Apr 25, 2017, 2:22:30 PM
    Author     : mennna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/newcss.css">
        <title>Search</title>
    </head>
    <body  >
        <!--background="images/1.jpg"-->
         <img id="img" src="images/search.jpg" width="400" height="190" alt="search">
         
         <!--div class="button_box2"-->
         <!--form id="box" action="Engine" class="form-wrapper-2 cf" method="POST"-->
           <form id="box" action="Engine"  method="POST">   
            <input id="textbox" type="text" name="query"  placeholder="Search here..." />
            <!--value="Search here..."-->
            <!--input placeholder="Search here..." autocomplete ="off" required-->
            <!--input type="submit" value="Go" name="GO" /--!>
        </form>
              <!--/div-->
    </body>
</html>
