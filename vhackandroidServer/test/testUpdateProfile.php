<?php 
include 'testUtils.php';

testApiCall("updateProfile.php", array ("email" => "sla.shalafi@gmail.com"));

testApiCall("updateProfile.php", array ("authKey" => "123456"));

testApiCall("updateProfile.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym"));

testApiCall("updateProfile.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym",
		"birthday" => "2011-11-11",
		"c2dmToken" => "123456"));

testApiCall("updateProfile.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym",
		"birthday" => "2000-11-12", 
		"something" => "something"));

testApiCall("updateProfile.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym",
		"birthday" => "1976-11-18",
		"firstName" => "Raoul",
		"lastName" => "Portavales"));

testApiCall("updateProfile.php", array ("authKey" => "FVLy736ft3g4lIo0bPpS0h3OVlEdxuym",
		"firstName" => "Raul",
		"lastName" => "Portales",
		"c2dmToken" => "654321"));
?>