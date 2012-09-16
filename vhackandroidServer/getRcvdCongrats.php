<?php 
include_once 'common.php';

$link = connectDB();

$user = getAuthenticatedUserOrDie($link, $_POST);

$userId = $user["userId"];
$sql = "SELECT * FROM users WHERE userId IN (SELECT fromUserId FROM congrats WHERE toUserId = '$userId')";
$users = queryDb($link, $sql);

dumpUserList($users);
?>