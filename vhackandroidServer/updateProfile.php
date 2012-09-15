<?php 
include 'common.php';

// You need to be authenticated
$authKey = $_POST["authKey"];

if (!isset ($authKey)) {
	dieWithError("Missing params", "400");
}
$link = connectDB();

$sql = "SELECT * FROM users WHERE authToken = '".mysql_escape_string($authKey)."'";
$user = queryDb($link, $sql);

if (count($user) == 0){
	dieWithError("Error authenticating", "401");
}

// Build the sql update query with the entries that are there

$entries = array("firstName", "lastName", "tagline", "birthday", "c2dmToken");

foreach ($entries as $key) {
	if (isset($_POST[$key])) {
		$values .= ", $key='".mysql_escape_string($_POST[$key])."'";
	}
}
if (strlen($values) > 0) {
	$values = substr($values, 1);
	$sql = "UPDATE users SET $values WHERE userId = '".$user[0]["userId"]."'";
	queryDb($link, $sql);
	
	$sql = "UPDATE users SET month=MONTH(birthday), day=DAY(birthday) WHERE userId = '".$user[0]["userId"]."'";
	queryDb($link, $sql);
	
	$sql = "SELECT * FROM users WHERE authToken = '".mysql_escape_string($authKey)."'";
	$user = queryDb($link, $sql);
}

echo dumpUserInfo ($user[0]);

?>