package com.collegeapp.collegeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import android.app.ProgressDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewPostActivity extends AppCompatActivity {

    @BindView(R.id.closeButton)
    ImageView closeButton;
    @BindView(R.id.postButton)
    TextView postButton;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;
    @BindView(R.id.Description)
    EditText Description;
    @BindView(R.id.postImage)
    ImageView postImage;
    @BindView(R.id.imageRemoveButton)
    ImageView imageRemoveButton;
    @BindView(R.id.CameraIntent)
    ImageButton CameraIntent;
    @BindView(R.id.ImageChooser)
    ImageButton ImageChooser;
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
    public FirebaseAuth mauth = FirebaseAuth.getInstance();
    public FirebaseUser user;
    Uri image;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        myref = myref.child("root").child("twitter").child("posts");
        user = mauth.getCurrentUser();
        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(profileImage);
        imageRemoveButton.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(this);
    }

    @OnClick({R.id.closeButton, R.id.postButton, R.id.imageRemoveButton, R.id.CameraIntent, R.id.ImageChooser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                startActivity(new Intent(NewPostActivity.this,mainActivity.class));
                break;
            case R.id.postButton:
            {
                progressDialog.setMessage("Uploading Post");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                String des = Description.getText().toString();
                if (!(TextUtils.isEmpty(des)))
                {

                    myref = myref.push();
                    myref.child("email").setValue(user.getEmail());
                    myref.child("name").setValue(user.getDisplayName());
                    myref.child("postdata").setValue(des);
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    myref.child("posttime").setValue(mydate);
                    if (postImage.getDrawable() == null)
                        myref.child("postimage").setValue("0");
                    else {
                        Toast.makeText(this, "image is not Uploaded", Toast.LENGTH_SHORT).show();
                        myref.child("postimage").setValue("0");
                    }
                    myref.child("profileimage").setValue(user.getPhotoUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.cancel();
                            startActivity(new Intent(NewPostActivity.this,mainActivity.class));
                        }
                    });
                }
            }
                break;
            case R.id.imageRemoveButton:
                postImage.setVisibility(View.GONE);
                imageRemoveButton.setVisibility(View.GONE);
                break;
            case R.id.CameraIntent:
                break;
            case R.id.ImageChooser:
                break;
        }
    }
}
