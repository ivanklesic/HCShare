package org.iklesic.hcshare.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.iklesic.hcshare.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    public static final String PASSWORD_KEY = "login_password";
    public static final String EMAIL_KEY = "login_email";

    @BindView(R.id.login_email)
    EditText loginEmail;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_login)
    Button loginLogin;
    @BindView(R.id.login_register)
    Button loginRegister;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        loginEmail.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(EMAIL_KEY, ""));
        loginPassword.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(PASSWORD_KEY, ""));
    }


    @OnClick(R.id.login_login)
    public void onLoginClicked() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (email.length() != 0 && password.length() != 0) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(EMAIL_KEY, email).apply();
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(PASSWORD_KEY, password).apply();

                        Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.login_fialed), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.regiser_data_error), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.login_register)
    public void onRegisterClicked() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
