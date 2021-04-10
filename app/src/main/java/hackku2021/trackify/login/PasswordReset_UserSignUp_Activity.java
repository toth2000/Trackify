package hackku2021.trackify.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import hackku2021.trackify.R;
import hackku2021.trackify.userDashboard.UserDashboardActivity;

public class PasswordReset_UserSignUp_Activity extends AppCompatActivity {

    private EditText editTextPasswordResetEmail, editTextCreateAccountName,
            editTextCreateAccountEmail, editTextCreateAccountPassword;

    private FrameLayout resetPasswordLayout, createAccountLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset__user_sign_up_);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.BLACK);

        editTextPasswordResetEmail = findViewById(R.id.editTextPasswordResetEmail);
        editTextCreateAccountEmail = findViewById(R.id.editTextCreateAccountEmail);
        editTextCreateAccountName = findViewById(R.id.editTextCreateAccountName);
        editTextCreateAccountPassword = findViewById(R.id.editTextTextCreateAccountPassword);

        resetPasswordLayout = findViewById(R.id.resetPasswordLayout);
        createAccountLayout = findViewById(R.id.createAccountLayout);

        Intent intent = getIntent();
        int state = intent.getIntExtra("state", -1);

        if(state==0)
        {
            createAccountLayout.setVisibility(View.VISIBLE);
        }
        else if(state==1)
        {
            resetPasswordLayout.setVisibility(View.VISIBLE);
        }
    }

    private void passwordRest(String email)
    {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PasswordReset_UserSignUp_Activity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                            Toast.makeText(PasswordReset_UserSignUp_Activity.this, "Account doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUser(String email, String password, String name)
    {
        final String nameToPass = name;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(PasswordReset_UserSignUp_Activity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            setUserName(nameToPass);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PasswordReset_UserSignUp_Activity.this, "Account creation failed, user might already exist.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void setUserName(String name)
    {
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                            Intent intent = new Intent(getApplicationContext(), UserDashboardActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                });
    }


    public void sendButtonMethod(View view)
    {
        String email = editTextPasswordResetEmail.getText().toString();
        if(email.isEmpty())
            Toast.makeText(this, "Please fill the above field first.", Toast.LENGTH_SHORT).show();
        else
            passwordRest(email);
    }

    public void createAccountActivityButtonMethod(View view)
    {
        String name = editTextCreateAccountName.getText().toString();
        String email = editTextCreateAccountEmail.getText().toString();
        String password = editTextCreateAccountPassword.getText().toString();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Please fill all the above fields first.", Toast.LENGTH_SHORT).show();
        else if(password.length()<6)
            Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
        else
            createUser(email, password, name);

    }

}