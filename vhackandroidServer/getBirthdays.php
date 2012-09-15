<?php 
include 'common.php';

$link = connectDB();

$sql = "SELECT * FROM users WHERE day = DAY(CURDATE()) AND month = MONTH(CURDATE())";
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