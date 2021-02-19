package com.example.gamezap;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamezap.businessLogic.Adapter_User;
import com.example.gamezap.businessLogic.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SignUp extends AppCompatActivity {
    private EditText signUp_ETX_email;
    private EditText signUp_ETX_password;
    private EditText signUp_ETX_userName;
    private Button signUp_BTN_continue;
    private TextView signUp_TXT_signUpFailed;
    private Button signUp_BTN_signIn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private Animation shake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void findViews() {
        signUp_ETX_email = findViewById(R.id.signUp_ETX_email);
        signUp_ETX_password = findViewById(R.id.signUp_ETX_password);
        signUp_ETX_userName = findViewById(R.id.signUp_ETX_userName);
        signUp_TXT_signUpFailed = findViewById(R.id.signUp_TXT_signUpFailed);
        signUp_BTN_continue = findViewById(R.id.signUp_BTN_continue);
        signUp_BTN_signIn = findViewById(R.id.signUp_BTN_signIn);
    }

    private void initViews() {
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        signUp_BTN_continue.setOnClickListener(v -> createNewUser(
                signUp_ETX_email.getText().toString(),
                signUp_ETX_password.getText().toString(),
                signUp_ETX_userName.getText().toString()));
        signUp_BTN_signIn.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent userIntent = new Intent(SignUp.this, Login.class);
        SignUp.this.startActivity(userIntent);
    }

    private void createNewUser(String email, String password, String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Popup message
                        Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show();
                        // Create fresh user.
                        User user = new User(name, email, password);
                        // Add the uuid from firebase auth.
                        user.setUuid(mAuth.getCurrentUser().getUid());
                        // Create a new document in FireStore for this user in user storage.
                        DocumentReference docRef = fireStore.collection("users").document(user.getUuid());
                        // Using adapter to serialize the user from class to Map format.
                        Map<String, Object> userMap = Adapter_User.serializeUserFB(user);
                        // Feed FireStore storage with the fresh user details.
                        docRef.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Create intent for the Game Search Activity
                                Intent gameSearchIntent = new Intent(SignUp.this, GameSearch.class);
                                // MOCK moving a user to the next view
                                gameSearchIntent.putExtra("userDetails", user);
                                // Start the new Game Search Activity
                                SignUp.this.startActivity(gameSearchIntent);
                            }
                        });
                    } else {
                        // Manage exception scenarios
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            throwExceptionMessage(signUp_ETX_password, "Sign Up Failed\nPassword is too weak");
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            throwExceptionMessage(signUp_ETX_email, "Sign Up Failed\nEmail not formatted right");
                        } catch(FirebaseAuthUserCollisionException e) {
                            throwExceptionMessage(signUp_ETX_email, "Sign Up Failed\nEmail already exists");
                        } catch(Exception e) {
                            Log.println(Log.ERROR, "Sign-up", e.getMessage());
                        }
                    }
                });
    }

    private void throwExceptionMessage(EditText field, String exceptionMessage) {
        field.startAnimation(shake);
        field.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        field.setHintTextColor(getResources().getColor(R.color.red_warning_error));
        signUp_TXT_signUpFailed.setText(exceptionMessage);
        signUp_TXT_signUpFailed.startAnimation(shake);
    }


}