package org.iklesic.hcshare.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_name)
    EditText registerName;
    @BindView(R.id.register_email)
    EditText registerEmail;
    @BindView(R.id.register_password)
    EditText registerPassword;
    @BindView(R.id.register_register)
    Button registerRegister;
    @BindView(R.id.register_login)
    Button registerLogin;

    private FirebaseAuth auth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    @OnClick(R.id.register_register)
    public void onRegisterClicked() {
        String email = registerEmail.getText().toString();
        String name = registerName.getText().toString();
        String password = registerPassword.getText().toString();

        if (email.length() != 0 && name.length() != 0 && password.length() != 0) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        User user = new User();

                        if (task.getResult() != null) {
                            user.setId(task.getResult().getUser().getUid());
                            user.setEmail(email);
                            user.setName(name);
                        }

                        database.collection("users").document(user.getId()).set(user);

                        Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.regiser_data_error), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.register_login)
    public void onLoginClickListener() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
