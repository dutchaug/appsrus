package nl.appsrus.vhack2012.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import nl.appsrus.vhack2012.api.AbcApi.ApiListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;


public class ApiRequest extends AsyncTask<String, Void, ApiResponse> {
	
	protected static final String API_BASE_URL = "http://api.plattysoft.com/vhackandroid/";
//	protected static final String API_BASE_URL = "http://192.178.10.62/~shalafi/abc/";

	public class JSOnResponse implements ApiResponse {

		private JSONObject mJsonObject;

		public JSOnResponse(JSONObject finalResult) {
			mJsonObject=finalResult;
		}

		public JSONObject getJSONObject() {
			return mJsonObject;
		}

	}

	public class ApiErrorResponse implements ApiResponse {

		private int mErrorCode;
		private String mErrorMessage;

		public ApiErrorResponse(Exception exception) {
			mErrorCode = 0;
			mErrorMessage = exception.getMessage();
		}

		public ApiErrorResponse(StatusLine statusLine) {
			mErrorCode = statusLine.getStatusCode();
			mErrorMessage = statusLine.getReasonPhrase();
		}

		public int getErrorCode() {
			return mErrorCode;
		}

		public String getErrorMessage() {
			return mErrorMessage;
		}
	}

	private static final int TIMEOUT_SOCKET = 5000;
	private static final int TIMEOUT_CONNECTION = 3000;

	private ApiListener mListener;

	public ApiRequest(ApiListener listener) {
		mListener = listener;
	}

	@Override
	protected ApiResponse doInBackground(String... params) {
		try {
			// prepare the GET request
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			// Get the default parameters, these ones always go into the URL
			String mUserToken = ApiFactory.getInstance().getAuthToken();
			if (mUserToken != null) {
				values.add(new BasicNameValuePair("authKey", mUserToken));
			}
			// The extra parameters can be either get or post
			// get the extra name value pairs
			for (int i=1; i<params.length; i+=2){
				values.add(new BasicNameValuePair(params[i], params[i+1]));
			}
			HttpRequestBase request = createRequest(values, params[0]);
			// Execute HTTP Request
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			int timeoutConnection = TIMEOUT_CONNECTION;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = TIMEOUT_SOCKET;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient httpClient = new DefaultHttpClient(httpParameters);

			Log.d("API Call", request.getURI().toString());
			
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			// Evaluate the response code
			switch (statusCode) {
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				ApiFactory.getInstance().setAuthToken(null);
				ApiFactory.getInstance().getKey(null);
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_NOT_FOUND:
			case HttpURLConnection.HTTP_INTERNAL_ERROR: {
				Log.e("ApiRequest",String.valueOf(statusCode)+request.getURI().toASCIIString());
				// Use a nicer error message
				return new ApiErrorResponse(response.getStatusLine());
			}
			case HttpURLConnection.HTTP_OK:{
				// Parse the result
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
				JSONTokener tokener = new JSONTokener(builder.toString());
				JSONObject finalResult = new JSONObject(tokener);                
				// Parse the response
				return new JSOnResponse(finalResult);
			}
			}
		}
		catch (IOException e) {
			return new ApiErrorResponse (e);
		}
		catch (Exception e) {
			return new ApiErrorResponse(e);
		}
		return new ApiErrorResponse(new Exception("Unknown Error"));
	}

	protected HttpRequestBase createRequest(List<NameValuePair> values, String path) {
		try {
			HttpPost httppost = new HttpPost(API_BASE_URL+path);
			HttpEntity encoded = new UrlEncodedFormEntity(values, "UTF-8");
			httppost.setEntity(encoded);
			return httppost;
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	@Override
	protected void onPostExecute(ApiResponse result) {
		super.onPostExecute(result);
		if (mListener == null) {
			return;
		}
		if (result instanceof ApiErrorResponse) {
			ApiErrorResponse error = (ApiErrorResponse) result;
			mListener.onError(error.getErrorCode(), error.getErrorMessage());
		}
		else {
			mListener.onSuccess(((JSOnResponse)result).getJSONObject());
		}
	}
}
