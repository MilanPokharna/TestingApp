package com.collegeapp.collegeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    int CAMERA_REQUEST = 1;
    StorageReference reference = FirebaseStorage.getInstance().getReference().child("images");

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
                startActivity(new Intent(NewPostActivity.this, mainActivity.class));
                break;
            case R.id.postButton: {

                String des = Description.getText().toString();
                if (!(TextUtils.isEmpty(des))) {
                    progressDialog.setMessage("Uploading Post");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    myref = myref.push();
                    myref.child("email").setValue(user.getEmail());
                    myref.child("name").setValue(user.getDisplayName());
                    myref.child("postdata").setValue(des);
                    final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    myref.child("posttime").setValue(mydate);
                    if (postImage.getDrawable() == null) {
                        Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        myref.child("postimage").setValue("0");
                        myref.child("profileimage").setValue(user.getPhotoUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.cancel();
                                startActivity(new Intent(NewPostActivity.this, mainActivity.class));
                            }
                        });
                    } else {
                        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(NewPostActivity.this, taskSnapshot.getTask().getResult().toString(), Toast.LENGTH_LONG).show();
                                progressDialog.cancel();
                               myref.child("profileimage").setValue(user.getPhotoUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       progressDialog.cancel();
                                       startActivity(new Intent(NewPostActivity.this, mainActivity.class));
                                   }
                               });
                            }
                        });
                    }

                }
                else
                {
                    Toast.makeText(this, "Can't Upload Empty Post", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.imageRemoveButton:
                postImage.setVisibility(View.GONE);
                imageRemoveButton.setVisibility(View.GONE);
                break;
            case R.id.CameraIntent: {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            break;
            case R.id.ImageChooser:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            postImage.setVisibility(View.VISIBLE);
            image = getImageUri(getApplicationContext(), photo);
            postImage.setImageBitmap(photo);
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
