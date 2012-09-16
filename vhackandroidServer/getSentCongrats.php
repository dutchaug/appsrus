<?php 
$link = connectDB();

$user = getAuthenticatedUserOrDie($link, $_POST);

$userId = $user["userId"];
$sql = "SELECT * FROM users WHERE userId IN (SELECT toUserId FROM congrats WHERE fromUserId = '$userId')";
$users = queryDb($link, $sql);

dumpUserList($users);
?>