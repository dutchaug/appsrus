<?php 
include 'common.php';

$link = connectDB();

$authKey = $_POST["authKey"];

if (isset ($authKey)) {
	$sql = "SELECT * FROM users WHERE authToken = '".mysql_escape_string($authKey)."'";
	$user = queryDb($link, $sql);
	if (count($user) == 0){
		dieWithError("Error authenticating", "401");
	}
	$userId = $user[0]["userId"];
	$extra = " AND userId NOT IN (
		SELECT toUserId FROM congrats 
		WHERE fromUserId = '$userId' AND year = YEAR(CURDATE()))";
}

$sql = "SELECT * FROM users WHERE day = DAY(CURDATE()) AND month = MONTH(CURDATE()) $extra";
$users = queryDb($link, $sql);

$result = "";
foreach ($users as $user) { 
	$result .= ", ".dumpUserInfo ($user);
}
if (strlen($result) > 0){
	$result = substr($result, 1);
}
echo "{ \"users\" : [ $result ] }";

?>