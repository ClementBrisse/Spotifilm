package com.isep.spotifilm.activity;

import static com.spotify.sdk.android.auth.AuthorizationResponse.Type.TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.isep.spotifilm.R;
import com.isep.spotifilm.connectors.UserService;
import com.isep.spotifilm.object.User;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    private static final String CLIENT_ID = "e55720db87ae46669b49545b9004b061";
    private static final String REDIRECT_URI = "https://com.isep.spotifilm.activity/callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private," +
            "playlist-read-private,playlist-modify-public,playlist-modify-private";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);


        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }

    private void authenticateSpotify() {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        System.out.println("onActivityResult");

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            System.out.println("response : "+response.getType());

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
    private void waitForUserInfo() {
        System.out.println("waitForUserInfo");
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            Log.d("STARTING", "GOT USER INFORMATION");
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();
            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent newIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(newIntent);
    }
}