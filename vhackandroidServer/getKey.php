<?php 
include "common.php";

$email = $_POST["email"];

if (!isset($email)) {
	dieWithError ("Required parameters missing");
}

$link = connectDB();

// Generate a new token
do {
	$token = base64_encode(pack('N6', mt_rand(), mt_rand(), mt_rand(), mt_rand(), mt_rand(), mt_rand()));
	// Check that the referral does not exist
	$sql = "SELECT * FROM users WHERE authToken = '$token'";
	$row = queryDb($link, $sql);
}
while (count($row) > 0);
$sql = "INSERT INTO users (email, authToken) VALUES ('$email', '$token') ON DUPLICATE KEY UPDATE email='$email'";
queryDb($link, $sql);

echo "{ \"authKey\": \"$token\" }";
?>