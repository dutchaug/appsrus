<?php 
include 'common.php';

// You need to be authenticated
$authKey = $_POST["authKey"];

if (!isset ($authKey)) {
	dieWithError("Missing params");
}
$link = connectDB();

$sql = "SELECT * FROM users WHERE authToken = '".mysql_escape_string($authKey)."'";
$user = queryDb($link, $sql);

if (count($user) == 0){
	dieWithError("Error authenticating", "401");
}

// targetUserId is required
$targetId = $_POST["targetUserId"];
if (! isset($targetId) || ! is_numeric($targetId)) {
	dieWithError("Incorrect params");
}
// It has to be his birthday
$sql = "SELECT * FROM users WHERE userId = '$targetId' AND day = DAY(CURDATE()) AND month = MONTH(CURDATE())";
$target = queryDb($link, $sql);
if (count($target) == 0){
	dieWithError("Today is not the birthday of this person");
}
// Only once per year
$sql = "SELECT * FROM congrats WHERE year = YEAR(CURDATE()) AND fromUserId = '".$user[0]["userId"]."' AND toUserId = '$targetId'";
$target = queryDb($link, $sql);
if (count($target) > 0){
	dieWithError("You already sent birthday wishes to this person this year!");
}
// Enter it
$sql = "INSERT INTO congrats (fromUSerId, toUserId, year) VALUES ('".$user[0]["userId"]."', '$targetId', YEAR(CURDATE()))";
queryDb($link, $sql);

echo "{}";
// // Send a push notification
// // Replace with real BROWSER API key from Google APIs
// $apiKey = "6462992600";
// $theRealKey = "AIzaSyDaRlIfHKSt9Ujqzg3Bnw_5TsTIvPAeeyk";
// // Replace with real client registration IDs
// $registrationIDs = array( "123", "456" );

// // Message to be sent
// $message = "x";

// // Set POST variables
// $url = 'https://android.googleapis.com/gcm/send';

// $fields = array(
// 		'registration_ids'  => $registrationIDs,
// 		'data'              => array( "message" => $message ),
// );

// $headers = array(
// 		'Authorization: key=' . $apiKey,
// 		'Content-Type: application/json'
// );

// // Open connection
// $ch = curl_init();

// // Set the url, number of POST vars, POST data
// curl_setopt( $ch, CURLOPT_URL, $url );

// curl_setopt( $ch, CURLOPT_POST, true );
// curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers);
// curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );

// curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $fields ) );

// // Execute post
// $result = curl_exec($ch);

// // Close connection
// curl_close($ch);

// echo $result;
?>