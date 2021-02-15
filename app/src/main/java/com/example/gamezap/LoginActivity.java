package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.gamezap.businessLogic.User;

public class LoginActivity extends AppCompatActivity {

    private EditText login_ETX_email;
    private EditText login_ETX_password;
    private Button login_BTN_signIn;
    private Button login_BTN_forgotPassword;
    private Button login_BTN_signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();
    }

    private void findViews() {
        login_ETX_email = findViewById(R.id.login_ETX_email);
        login_ETX_password = findViewById(R.id.login_ETX_password);
        login_BTN_signIn = findViewById(R.id.login_BTN_signIn);
        login_BTN_forgotPassword = findViewById(R.id.login_BTN_forgotPassword);
        login_BTN_signUp = findViewById(R.id.login_BTN_signUp);
    }

    private void initViews() {
        login_BTN_signIn.setOnClickListener(v -> signIn());
        login_BTN_forgotPassword.setOnClickListener(v -> forgotPassword());
        login_BTN_signUp.setOnClickListener(v -> singUp());
    }

    private void signIn() {
        // For mocking only!!
        boolean userAuthenticated = true;

        // This is the way to get the Edit text text.
//        login_ETX_email.getText().toString();
//        login_ETX_password.getText().toString();

        if(userAuthenticated){
            // Create intent for the Game Search Activity
            Intent gameSearchIntent = new Intent(LoginActivity.this, GameSearch.class);
            // MOCK moving a user to the next view
            gameSearchIntent.putExtra("userDetails", new User("Test", "test@test.com", "kokobobo"));
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