package com.collegeapp.collegeapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeapp.collegeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.useremail)
    TextInputEditText useremail;
    @BindView(R.id.domain)
    TextView domain;
    @BindView(R.id.signIn)
    Button signIn;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.signIn)
    public void onViewClicked() {
        String id = useremail.getText().toString();
        if (TextUtils.isEmpty(id))
        {
            Toast.makeText(this, "Please Enter Your ID", Toast.LENGTH_SHORT).show();
        }
        else
        {
            id = id +"@technonjr.org";
            sendSignInLinkToEmail(id);
        }
    }

    private void sendSignInLinkToEmail(String id) {
        ActionCodeSettings actionCodeSettings =
                 ActionCodeSettings.newBuilder()
                .setAndroidPackageName(
                        getPackageName(),
                        false, /* install if not available? */
                        null   /* minimum app version */)
                .setHandleCodeInApp(true)
                .setUrl("https://com.collegeapp.collegeapp/finishSignUp?cardId=1234")
                .build();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(id, actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Email is Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
