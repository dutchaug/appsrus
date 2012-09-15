<?php 
include 'testUtils.php';

testApiCall("sayHappyBirthday.php", array ());

testApiCall("sayHappyBirthday.php", array ("authKey" => "1111111"));

testApiCall("sayHappyBirthday.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym",
		"targetUserId" => "4"));

testApiCall("sayHappyBirthday.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym",
		"targetUserId" => "10"));

testApiCall("sayHappyBirthday.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym",
		"targetUserId" => "10"));
?>