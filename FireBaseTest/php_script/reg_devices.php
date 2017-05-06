<?php require("includes/db_connection.php");?>
<?php
        if (isset($_POST["Token"])) {
		
		   $_uv_Token=$_POST["Token"];
		   $q="INSERT INTO reg_users (Token) VALUES ( '$_uv_Token') "
              ." ON DUPLICATE KEY UPDATE Token = '$_uv_Token';";
              
      mysqli_query($connection,$q) or die(mysqli_error($connection));
      mysqli_close($connection);
	}else{
	    $q="INSERT INTO reg_users (Token) VALUES ( 'tut') "
              ." ON DUPLICATE KEY UPDATE Token = 'tut';";
              
      mysqli_query($connection,$q) or die(mysqli_error($connection));
      mysqli_close($connection);
	}


?>