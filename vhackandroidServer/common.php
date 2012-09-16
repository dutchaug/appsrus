<?php 

function connectDB () {
	$link = mysql_connect("localhost",
			"vhackandroid",
			"appsRus");
	if (!$link) {
		die("Could not connect to database");
	}
	mysql_select_db ("vhackandroid");
	return $link;
}

function queryDb ($link, $sql) {
	$res = mysql_query($sql, $link) or dieWithError(mysql_error($link));
	$result = array();
	while ($row = mysql_fetch_assoc($res)){
		$result[] = $row;
	}
	return $result;
}

function dieWithError($errorMessage = "", $errorCode = "400") {
        header("HTTP/1.0 $errorCode $errorMessage");
        die ($errorMessage);
}

function dumpUserInfo ($user) 
{
	return '{"userId" : "'.$user["userId"].'", '.
			'"firstName" : "'.$user["firstName"].'", '.
			'"lastName" : "'.$user["lastName"].'", '.
			'"tagline": "'.$user["tagline"].'", '.
			'"phoneModel": "'.$user["phoneModel"].'", '.
			'"osVersion": "'.$user["osVersion"].'", '.
			'"day": "'.$user["day"].'", '.
			'"month": "'.$user["month"].'", '.
			'"year": "'.$user["year"].'", '.
			'"gravatarUrl": "'.md5(strtolower(trim($user["email"]))).'" }';
}

function dumpUserList($users) {
	$result = "";
	foreach ($users as $user) {
		$result .= ", ".dumpUserInfo ($user);
	}
	if (strlen($result) > 0){
		$result = substr($result, 1);
	}
	echo "{ \"users\" : [ $result ] }";
}

function getAuthenticatedUserOrDie ($link, $array) {
	$authKey = $array["authKey"];
	if (!isset ($authKey)) {
		dieWithError("Missing params", "400");
	}
	$sql = "SELECT * FROM users WHERE authToken = '".mysql_escape_string($authKey)."'";
	$user = queryDb($link, $sql);

	if (count($user) == 0){
		dieWithError("Error authenticating", "401");
	}
	return $user[0];
}
?>