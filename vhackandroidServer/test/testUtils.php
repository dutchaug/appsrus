<?php 

function testApiCall($apiCall, $post_fields) {
	echo "Testing $apiCall with params:";
	print_r($post_fields);
	
	flush();
	// Try to retrieve a profile pic from someone that does not have it
	$ch = curl_init();

	curl_setopt($ch, CURLOPT_URL, "http://localhost/~shalafi/abc/".$apiCall);

	curl_setopt($ch, CURLOPT_HEADER, true);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($post_fields));
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_FRESH_CONNECT, true);
	curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

	$response = curl_exec($ch);

	curl_close($ch);

	echo $response."\n---------\n";
}

?>