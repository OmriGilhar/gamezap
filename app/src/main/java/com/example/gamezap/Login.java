package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gamezap.businessLogic.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private EditText login_ETX_email;
    private Animation shake;
    private EditText login_ETX_password;
    private Button login_BTN_signIn;
    private Button login_BTN_forgotPassword;
    private Button login_BTN_signUp;
    private TextView login_TXT_signInFailed;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private FirebaseUser user;
    private User gameZapUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
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
            login_ETX_email.setText(currentUser.getEmail());
        }
    }

    private void findViews() {
        login_ETX_email = findViewById(R.id.login_ETX_email);
        login_ETX_password = findViewById(R.id.login_ETX_password);
        login_BTN_signIn = findViewById(R.id.login_BTN_signIn);
        login_BTN_forgotPassword = findViewById(R.id.login_BTN_forgotPassword);
        login_BTN_signUp = findViewById(R.id.login_BTN_signUp);
        login_TXT_signInFailed = findViewById(R.id.login_TXT_signInFailed);
    }

    private void initViews() {
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        login_BTN_signIn.setOnClickListener(v -> signIn(login_ETX_email.getText().toString(),
                login_ETX_password.getText().toString()));
        login_BTN_forgotPassword.setOnClickListener(v -> forgotPassword());
        login_BTN_signUp.setOnClickListener(v -> singUp());
    }

    private void signIn(String email, String password) {
        if(checkValidEmail(email) && checkValidPassword(password)){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            login_TXT_signInFailed.setText("");
                            user = mAuth.getCurrentUser();
                            pullUserFromStorage(user.getUid());
//                            // Create intent for the Game Search Activity
//                            Intent gameSearchIntent = new Intent(LoginActivity.this, GameSearch.class);
//                            // MOCK moving a user to the next view
//                            gameSearchIntent.putExtra("userDetails", gameZapUser);
//                            // Start the new Game Search Activity
//                            LoginActivity.this.startActivity(gameSearchIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            login_TXT_signInFailed.setText("Login Failed\nCheck your password of email");
                            login_TXT_signInFailed.startAnimation(shake);
                        }
                    });
        }


    }

    private void pullUserFromStorage(String uid) {
        DocumentReference userRef = fireStore.collection("users").document(uid);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Create intent for the Game Search Activity
                    Intent gameSearchIntent = new Intent(Login.this, GameSearch.class);
                    // Moving a user to the next view
                    User gameZapUser = document.toObject(User.class);
                    gameZapUser.setUuid(uid);
                    gameSearchIntent.putExtra("userDetails", gameZapUser);
                    // Start the new Game Search Activity
                    Login.this.startActivity(gameSearchIntent);
                }
            } else {
                Log.println(Log.INFO, "Login:Error", "get failed with " + task.getException());
            }
        });
    }

    private boolean checkValidPassword(String password) {
        if(password == null || password.length() == 0){
            login_ETX_password.startAnimation(shake);
            login_ETX_password.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            login_ETX_password.setHintTextColor(getResources().getColor(R.color.red_warning_error));
            return false;
        }
        return true;
    }

    private boolean checkValidEmail(String email) {
        if(email == null || email.length() == 0) {
            login_ETX_email.startAnimation(shake);
            login_ETX_email.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            login_ETX_email.setHintTextColor(getResources().getColor(R.color.red_warning_error));
            return false;
        }
        return true;
    }

    private void forgotPassword() {
        // Do we need this functionality?
        Log.println(Log.INFO,"Login:Error", "forgot password clicked");
    }

    private void singUp() {
        Intent userIntent = new Intent(Login.this, SignUp.class);
        Login.this.startActivity(userIntent);
    }
}