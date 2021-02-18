package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamezap.businessLogic.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText login_ETX_email;
    private EditText login_ETX_password;
    private Button login_BTN_signIn;
    private Button login_BTN_forgotPassword;
    private Button login_BTN_signUp;

    private static final String TAG = "EmailPassword";
    //declare_auth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViews();
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void findViews() {
        login_ETX_email = findViewById(R.id.login_ETX_email);
        login_ETX_password = findViewById(R.id.login_ETX_password);
        login_BTN_signIn = findViewById(R.id.login_BTN_signIn);
        login_BTN_forgotPassword = findViewById(R.id.login_BTN_forgotPassword);
        login_BTN_signUp = findViewById(R.id.login_BTN_signUp);
    }

    private void initViews() {
        login_BTN_signIn.setOnClickListener(v -> signIn(login_ETX_email.getText().toString(),
                login_ETX_password.getText().toString()));
        login_BTN_forgotPassword.setOnClickListener(v -> forgotPassword());
        login_BTN_signUp.setOnClickListener(v -> singUp());
    }

    private void signIn(String email, String password) {
        // For mocking only!!
        boolean userAuthenticated = true;

        // This is the way to get the Edit text text.
//        login_ETX_email.getText().toString();
//        login_ETX_password.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });

        Log.d("member", "signInWithEmail:"+mAuth.getCurrentUser().getEmail().toString());

        //TODO: Check how to use the database and link users to their data
        if(mAuth.getCurrentUser() != null){
            // Create intent for the Game Search Activity
            Intent gameSearchIntent = new Intent(LoginActivity.this, GameSearch.class);
            // MOCK moving a user to the next view
            gameSearchIntent.putExtra("userDetails", new User("Test", mAuth.getCurrentUser().getEmail().toString(), "kokobobo"));
            // Start the new Game Search Activity
            LoginActivity.this.startActivity(gameSearchIntent);
        }

    }

    private void forgotPassword() {
        // Do we need this functionality?
        Log.println(Log.INFO,"asdasd", "forgot password clicked");
    }

    private void singUp() {
        Intent userIntent = new Intent(LoginActivity.this, SignUp.class);
        LoginActivity.this.startActivity(userIntent);
    }
}