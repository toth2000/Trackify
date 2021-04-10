package hackku2021.trackify.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import hackku2021.trackify.R;
import hackku2021.trackify.userDashboard.UserDashboardActivity;

public class LoginActivity extends AppCompatActivity {

    /* View Variable */
    private TextView textViewCreateAccount, textViewForgotPassword;
    private Button buttonLogin;
    private ImageButton imageButtonPhoneLogin, imageButtonGoogleLogin;
    private EditText editTextEmail, editTextPassword;

    /* Non-View Variable */
    private FirebaseAuth mAuth;
    private   int RC_SIGN_IN = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
            intentCall();


        /* Action Bar Adjustment section*/

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.BLACK);


        /* Button and textView initialization section */

        textViewCreateAccount = findViewById(R.id.loginActivityTextViewCreateAccount);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        imageButtonPhoneLogin = findViewById(R.id.imageButtonPhoneLogin);
        imageButtonGoogleLogin = findViewById(R.id.imageButtonGoogleLogin);
        editTextEmail = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);

        /* Variable Initialization section */

        mAuth = FirebaseAuth.getInstance();

        /* Function Call section */

        initializeButton();
    }

    /* initialize all button/textView/imageView on Click functionality */
    private void initializeButton()
    {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMethod();
            }
        });

        imageButtonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLoginButtonMethod();
            }
        });

        imageButtonPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneLoginButtonMethod();
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpPasswordResetIntentCall(1);
            }
        });

        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpPasswordResetIntentCall(0);
            }
        });

    }


    private void loginMethod()
    {
        String emailString = editTextEmail.getText().toString();
        String passwordString = editTextPassword.getText().toString();

        if(!emailString.isEmpty() && !passwordString.isEmpty())
            emailLogin(emailString, passwordString);
        else
            Toast.makeText(this, "Email and Password can't be empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                intentCall();
                Log.i("Login Success", "successfull");
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                Log.i("Login Success", "Failure");
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    void emailLogin(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            intentCall();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid credential, try again.", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                        }
                    }
                });

    }

    void intentCall()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); Intent intent;

        assert user != null;
       /* if(user.getDisplayName() == null || user.getEmail() == null)
        {
            intent = new Intent(getApplicationContext(), UserInfoActivity.class);
            intent.putExtra("value", 0);
        }

        */
      //  else
        {
            intent = new Intent(getApplicationContext(), UserDashboardActivity.class);
        }

        startActivity(intent);
        finish();
    }

    public void googleLoginButtonMethod()
    {
        List<AuthUI.IdpConfig> googleProvider = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityCall(googleProvider);

    }

    public void phoneLoginButtonMethod()
    {
        List<AuthUI.IdpConfig> phoneProvider = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        startActivityCall(phoneProvider);
    }

    void startActivityCall(List<AuthUI.IdpConfig> Provider)
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Provider)
                        .setTheme(R.style.Theme_AppCompat)
                        .build(),
                RC_SIGN_IN);
    }


    void signUpPasswordResetIntentCall(int i)
    {
        Intent intent = new Intent(getApplicationContext(), PasswordReset_UserSignUp_Activity.class);
        intent.putExtra("state", i);
        startActivity(intent);
    }

}