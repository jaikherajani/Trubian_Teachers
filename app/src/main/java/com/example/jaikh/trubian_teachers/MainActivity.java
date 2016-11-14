package com.example.jaikh.trubian_teachers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.File;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private SignInButton mGoogleButton;
    private static final int RC_SIGN_IN = 1;
    public GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "SIGN_IN";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private String user_name;
    private boolean status;
    int i =0;
    private static final int PERMS_REQUEST_CODE=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mGoogleButton = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleButton.setColorScheme(SignInButton.COLOR_AUTO);

        if(hasPermissions()){
            makeFolder();
        }
        else{
            requestPerms();
        }

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    System.out.println("User is signed in as "+ firebaseAuth.getCurrentUser());
                }
                else
                {
                    System.out.println("User is signed out");
                }
            }
        };
        //if(mAuth.getCurrentUser() != null)
        //startActivity(new Intent(MainActivity.this,MainPage.class));

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Sign-In with Google failed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                signIn();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private boolean hasPermissions(){

        int res =0;
        String[] permissions =new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for(String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if(!(res== PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions =new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMS_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            makeFolder();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void makeFolder(){
        File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"TrubianAppDownloads");
        if(!file.exists()){
            Boolean ff = file.mkdir();
            if(ff){
                Toast.makeText(MainActivity.this,"Thanks for allowing permissions",Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(MainActivity.this,"Permissions have been denied. ",Toast.LENGTH_LONG).show();

            }}
        else{
            Toast.makeText(MainActivity.this,"Welcome Back",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                user_name = account.getDisplayName();
                isUserRegistered();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void isUserRegistered() {
        AsyncTask<Void,Void,Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                Firebase studentsRef = new Firebase("https://trubian-6f4e4.firebaseio.com/teachers/"+user_name);
                studentsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> values = (Map<String,String>) dataSnapshot.getValue();
                        Log.d(TAG, "Value is: " + values);
                        if(values==null)
                        {
                            status = true;
                            System.out.println("Values " + values);
                            System.out.println("Status " + status);
                            Proceed();
                        }
                        else
                        {
                            status = false;
                            System.out.println("Values " + values);
                            System.out.println("Status " + status);
                            Proceed();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(MainActivity.this, "Error : "+firebaseError, Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        };
        task.execute();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        //status = isUserRegistered();
        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        Proceed();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    public void Proceed()
    {
        i=++i;
        if(i==2) {
            progressDialog.dismiss();
            if (status) {
                System.out.println("Status received is " + status);
                startActivity(new Intent(MainActivity.this, Register.class));
            } else {
                startActivity(new Intent(MainActivity.this, MainPage.class));
            }
        }
    }

}
