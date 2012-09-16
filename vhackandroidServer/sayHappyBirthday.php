<?php 
include 'common.php';
include 'gcm.php';

$link = connectDB();

$user = getAuthenticatedUserOrDie($link, $_POST);

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
$gcmToken = $target[0]["c2dmToken"];
// Only once per year
$sql = "SELECT * FROM congrats WHERE year = YEAR(CURDATE()) AND fromUserId = '".$user["userId"]."' AND toUserId = '$targetId'";
$target = queryDb($link, $sql);
if (count($target) > 0){
	dieWithError("You already sent birthday wishes to this person this year!");
}
// Enter it
$sql = "INSERT INTO congrats (fromUSerId, toUserId, year) VALUES ('".$user["userId"]."', '$targetId', YEAR(CURDATE()))";
queryDb($link, $sql);

sendGcm($gcmToken);

echo "{}";
?>