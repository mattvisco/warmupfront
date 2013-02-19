package com.example.warmupfront;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.*;


@SuppressLint("NewApi")
public class MainActivity extends Activity {

	 public final static String EXTRA_MESSAGE = "com.example.warmupfront.MESSAGE";
	 TextView main_message;
	 String message;
	 public final static int SUCCESS=1;
	 public final static int ERR_BAD_CREDENTIALS=-1;
	 public final static int ERR_USER_EXISTS=-2;
	 public final static int ERR_BAD_USERNAME=-3;
	 public final static int ERR_BAD_PASSWORD=-4;
	 
	 int err, count;
	 String user, pass;
	 
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_message = (TextView) findViewById(R.id.main_message);
        message="Please Enter Your Credentials Below";
        main_message.setText(message);    
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Login button */
    public void Login(View view) {
    	EditText tmpUser = (EditText) findViewById(R.id.username);
    	EditText tmpPass = (EditText) findViewById(R.id.password);
    	user=tmpUser.getText().toString();
    	pass=tmpPass.getText().toString();
    	RequestParams params = new RequestParams();
		params.put("user", user);
	    params.put("password", pass);
    	JsonHttpResponseHandler handle =new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject res) {
                // Pull out the error code and count
                try {
					err=res.getInt("errCode");
					if(err==1) {
						count=res.getInt("count");
					}
				} catch (JSONException e) {
					// Catch block
					e.printStackTrace();
				} 
                LoginUp();
            }
        };
		BackendFetch.post("/users/login", params, handle);	
    }
    	
    /** Called when the JSON handler successful parses response for Login post */
    public void LoginUp(){
    	Intent intent = new Intent(this, WelcomeScreen.class);
    	main_message = (TextView) findViewById(R.id.main_message);
    	if (err==SUCCESS) {
        	message = "Welcome "+ user + " you have logged in " + Integer.toString(count)+ " times!";
        	intent.putExtra(EXTRA_MESSAGE, message);
        	startActivity(intent);
    	} else if(err==ERR_BAD_CREDENTIALS) {
    		message = "Invalid username and password combination. Please try again.";
    		main_message = (TextView) findViewById(R.id.main_message);
    		main_message.setText(message);
    	}	
    }
    
    /** Called when the user clicks the Add button */
    public void Add(View view) {
    	EditText tmpUser = (EditText) findViewById(R.id.username);
    	EditText tmpPass = (EditText) findViewById(R.id.password);
    	user=tmpUser.getText().toString();
    	pass=tmpPass.getText().toString();
    	RequestParams params = new RequestParams();
		params.put("user", user);
	    params.put("password", pass);
	    JsonHttpResponseHandler handle =new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject res) {
                // Pull out the error code and count
                try {
					err=res.getInt("errCode");
					if(err==1) {
						count=res.getInt("count");
					}
				} catch (JSONException e) {
					// Catch block
					e.printStackTrace();
				} 
                AddUp();
            }
        };
		BackendFetch.post("/users/add", params, handle);
    }
    
    /** Called when the JSON handler successful parses response for add post */
    public void AddUp() {
    	Intent intent = new Intent(this, WelcomeScreen.class);
    	main_message = (TextView) findViewById(R.id.main_message);
    	if (err==SUCCESS) {
        	String message = "Welcome " + user + " you have logged in " + Integer.toString(count)+ " times!";
        	intent.putExtra(EXTRA_MESSAGE, message);
        	startActivity(intent);
    	} else if(err==ERR_BAD_USERNAME) {
    		message = "The user name should be non-empty and at most 128 characters long. Please try again.";
    		main_message.setText(message);
    	} else if (err==ERR_BAD_PASSWORD) {
    		message = "The password should be at most 128 characters long. Please try again.";
    		main_message.setText(message);
    	} else if (err==ERR_USER_EXISTS) {
    		message = "This user name already exists. Please try again.";
    		main_message.setText(message);
    	} 
    }
	
    /** Used to asynchronously post to Backend */
	private static class BackendFetch {
		  private static final String BASE_URL = "http://stark-citadel-8257.herokuapp.com";

		  private static AsyncHttpClient client = new AsyncHttpClient();

		  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		      client.post(getAbsoluteUrl(url), params, responseHandler);
		  }
		  public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		      client.post(getAbsoluteUrl(url), responseHandler);
		  }
		  private static String getAbsoluteUrl(String relativeUrl) {
		      return BASE_URL + relativeUrl;
		  }
		}
    
}
