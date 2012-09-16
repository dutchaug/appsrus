<?php 
include 'common.php';

$link = connectDB();

$user = getAuthenticatedUserOrDie($link, $_POST);

// Build the sql update query with the entries that are there

$entries = array("firstName", "lastName", "tagline", "day", "month", "year", "phoneModel", "osVersion", "c2dmToken");

$values="";
foreach ($entries as $key) {
	$value = $_POST[$key];
	if (isset($value) && strlen($value) > 0) {
		$values .= ", $key='".mysql_escape_string($_POST[$key])."'";
	}
}
if (strlen($values) > 0) {
	$values = substr($values, 1);
	$sql = "UPDATE users SET $values WHERE userId = '".$user["userId"]."'";
	queryDb($link, $sql);
	
	$sql = "SELECT * FROM users WHERE userId = '".$user["userId"]."'";
	$userSet = queryDb($link, $sql);
	$user = $userSet[0];
}

echo dumpUserInfo ($user);

?>