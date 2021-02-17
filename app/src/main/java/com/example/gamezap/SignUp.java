package com.example.gamezap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText signUp_ETX_email;
    private EditText signUp_ETX_password;
    private EditText signUp_ETX_userName;
    private Button signUp_BTN_continue;
    private Button signUp_BTN_forgotPassword;
    private Button signUp_BTN_signIn;

    private static final String TAG = "EmailPassword";
    //declare_auth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        signUp_ETX_email = findViewById(R.id.signUp_ETX_email);
        signUp_ETX_password = findViewById(R.id.signUp_ETX_password);
        signUp_ETX_userName = findViewById(R.id.signUp_ETX_userName);
        signUp_BTN_continue = findViewById(R.id.signUp_BTN_continue);
        signUp_BTN_signIn = findViewById(R.id.signUp_BTN_signIn);
    }

    private void initViews() {
        signUp_BTN_continue.setOnClickListener(v -> createNewUser(
                signUp_ETX_email.getText().toString(),
                signUp_ETX_password.getText().toString(),
                signUp_ETX_userName.getText().toString()));
        signUp_BTN_signIn.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent userIntent = new Intent(SignUp.this, LoginActivity.class);
        SignUp.this.startActivity(userIntent);
    }

    private void createNewUser(String email, String password, String name){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUp.this, "You now have an account!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.\n Make sure your details are correct",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }





}