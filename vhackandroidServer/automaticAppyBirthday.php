<?php 
// Sends Appy Birthday messages to anyone that has not been said yet by the Apps'R'Us account
// Apps'R'Us account has id 1
include 'common.php';
include 'gcm.php';

$link = connectDB();

// Get all possible targets
$sql = "SELECT * FROM users WHERE day = DAY(CURDATE()) AND month = MONTH(CURDATE()) AND userId != '1' AND userId NOT IN (
	SELECT toUserId FROM congrats
	WHERE fromUserId = '1' AND year = YEAR(CURDATE()))";

$users = queryDb($link, $sql);

foreach ($users as $user) {
	// targetUserId is required
	$targetId = $user["userId"];
	$gcmToken = $user["c2dmToken"];
	// Only once per year
	$sql = "INSERT INTO congrats (fromUSerId, toUserId, year) VALUES ('1', '$targetId', YEAR(CURDATE()))";
	queryDb($link, $sql);	
	sendGcm($gcmToken);
	echo date().": Saying Appy Birthday to ".$user["email"]."<br>\n";
}
?>