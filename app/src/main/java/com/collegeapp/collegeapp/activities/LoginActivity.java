package com.collegeapp.collegeapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeapp.collegeapp.Manifest;
import com.collegeapp.collegeapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private GoogleApiClient mGoogleSignInClient;
    Button googleButton;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users");
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        progressDialog = new ProgressDialog(LoginActivity.this);
        open();

        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        googleButton = (Button) findViewById(R.id.signIn);
        googleButton.setVisibility(View.VISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi( Auth.GOOGLE_SIGN_IN_API,gso).build();
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickme();
    }
        });
    }
    public void clickme()
    {
        if (isNetworkConnected()) {
            progressDialog.setMessage("Logging you in");
            progressDialog.show();
            googleButton.setVisibility(View.GONE);
            signIn();
        }
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("Connectino Error ");
            dialog.setCancelable(false);
            dialog.setMessage("Unable to connect with the server.\n Check your Internet connection and try again." );
            dialog.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clickme();
                }
            }).show();
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void signIn()
    {

        mGoogleSignInClient.clearDefaultAccountAndReconnect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            progressDialog.dismiss();
            Intent intent = new Intent(LoginActivity.this,mainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        if(acct.getEmail().toString().endsWith("@technonjr.org")){
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                String a = user.getUid().toString();
                                mref.child(a).child("email").setValue(user.getEmail().toString());
                                mref.child(a).child("name").setValue(user.getDisplayName().toString());
                                mref.child(a).child("uid").setValue(user.getUid().toString());
                                updateUI(user);
                            } else {
                                updateUI(null);
                            }
                        }
                    });

        }
        else
        {
            Toast.makeText(this, "Login with Collage Id", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            googleButton.setVisibility(View.VISIBLE);

        }
    }


    public void open()
    {
        if(checkPermission())
        {
        }
        else
        {
            requestPermission();
        }
    }
    public Boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),INTERNET);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(),ACCESS_FINE_LOCATION);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(),STORAGE_SERVICE);
        return (result == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED
                && result4 == PackageManager.PERMISSION_GRANTED
                && result5 == PackageManager.PERMISSION_GRANTED ) ;
    }

    public  void requestPermission()
    {
        int requestCode;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,INTERNET},requestCode=1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale( WRITE_EXTERNAL_STORAGE )) {
                            showMessageOKCancel( "You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions( new String[]{ WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, INTERNET},
                                                        1 );
                                            }
                                        }
                                    } );
                            return;
                        }
                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder( LoginActivity.this )
                .setMessage( message )
                .setPositiveButton( "OK", okListener )
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
