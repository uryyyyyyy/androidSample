package com.github.uryyyyyyy.androidsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final String TAG = "DisplayMessageActivity";
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInAccount account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        LinearLayout layout = new LinearLayout(this);

        Button loginButton = new Button(this);
        loginButton.setText(message);
        loginButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        );
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        layout.addView(loginButton);

        Button closeButton = new Button(this);
        closeButton.setText("close");
        closeButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        );
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWithParam();
            }
        });
        layout.addView(closeButton);

        setContentView(layout);
    }

    private void closeWithParam() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("google.id", this.account.getId());
        bundle.putString("google.idToken", this.account.getIdToken());
        intent.putExtras(bundle);
        setResult(MainActivity.ACTIVITY_FLAG, intent);
        finish();
    }

    private void signIn() {
        Scope mScope = new Scope("https://www.googleapis.com/auth/urlshortener");
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(mScope)
                .requestEmail()
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "ConnectionCallbacks:onConnectionFailed");
                    }
                })
                .addScope(mScope)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "status: " + result.getStatus());
            Log.d(TAG, "result.isSuccess()" + result.isSuccess());
            if (result.isSuccess()) {
                Log.d(TAG, "result.isSuccess()" + result.isSuccess());
                // Google Sign In was successful, authenticate with Firebase
                this.account = result.getSignInAccount();
                Log.d(TAG, "accountId: " + this.account.getId());
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }
}
