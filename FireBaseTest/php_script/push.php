<?php
require '../includes/db_connection.php';
?>
<?php 
	function send_notification ($tokens, $message)
	{
		$url = 'https://fcm.googleapis.com/fcm/send';
		$fields = array(
			 'registration_ids' => $tokens,
			 'data' => $message
			);
		$headers = array(
			'Authorization:key = AAAAHA5oF9c:APA91bEBH_5OIY8uBS_b2L-w1sEOhNkEvuLq4N_5Y_S49QS_5Htjd0wKr9-K7xoMoRKoRWfkOHDZdB9ezb8sHAjDdoCXWH3UP40JHSJPR0USFISB7hE1t0ojd9BxMptjdA3g7yT6cpK_',
			'Content-Type: application/json'
			);
	   $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);           
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
	}
	
	$sql = " Select Token From reg_users";
	$result = mysqli_query($connection,$sql);
	$tokens = array();
	if(mysqli_num_rows($result) > 0 ){
		while ($row = mysqli_fetch_assoc($result)) {
			$tokens[] = $row["Token"];
		}
	}
	mysqli_close($connection);
	$message = array("message" => " FCM PUSH NOTIFICATION TEST MESSAGE");
	$message_status = send_notification($tokens, $message);
	echo $message_status;
 ?>