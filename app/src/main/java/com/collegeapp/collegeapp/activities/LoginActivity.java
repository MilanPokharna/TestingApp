package com.collegeapp.collegeapp.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeapp.collegeapp.R;

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
            id = id.concat("@technonjr.org");

        }
    }
}
