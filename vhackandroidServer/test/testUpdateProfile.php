<?php 
include 'testUtils.php';

testApiCall("updateProfile.php", array ("email" => "sla.shalafi@gmail.com"));

testApiCall("updateProfile.php", array ("authKey" => "1111"));

testApiCall("updateProfile.php", array ("authKey" => "1111"));

testApiCall("updateProfile.php", array ("authKey" => "1111",
		"birthday" => "2011-11-11",
		"c2dmToken" => "123456"));

testApiCall("updateProfile.php", array ("authKey" => "1111",
		"birthday" => "2000-11-12", 
		"something" => "something"));

testApiCall("updateProfile.php", array ("authKey" => "1111",
		"birthday" => "1976-11-18",
		"firstName" => "Raoul",
		"lastName" => "Portavales"));

testApiCall("updateProfile.php", array ("authKey" => "1111",
		"firstName" => "Raul",
		"lastName" => "Portales",
		"c2dmToken" => "654321"));
?>