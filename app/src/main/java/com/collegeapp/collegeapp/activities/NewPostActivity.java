package com.collegeapp.collegeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
    public static final int PICK_IMAGE = 2;
    String string;
    StorageReference reference = FirebaseStorage.getInstance().getReference().child("images");
    @BindView(R.id.cardv)
    CardView cardv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        myref = myref.child("root").child("twitter").child("posts");
        user = mauth.getCurrentUser();
        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(profileImage);
        imageRemoveButton.setVisibility(View.INVISIBLE);
        cardv.setVisibility(View.GONE);
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
                    string = myref.getKey().toString();
                    myref.child("email").setValue(user.getEmail());
                    myref.child("name").setValue(user.getDisplayName());
                    myref.child("postdata").setValue(des);
                    final String mydate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
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
                        reference.child(string).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                myref.child("postimage").setValue(string);
                                progressDialog.cancel();
                                myref.child("profileimage").setValue(user.getPhotoUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.cancel();
                                        startActivity(new Intent(NewPostActivity.this, mainActivity.class));
                                    }
                                });
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

                } else {
                    Toast.makeText(this, "Can't Upload Empty Post", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.imageRemoveButton:
                postImage.setImageDrawable(null);
                postImage.setVisibility(View.GONE);
                cardv.setVisibility(View.GONE);
                imageRemoveButton.setVisibility(View.GONE);
                break;
            case R.id.CameraIntent: {
                if (check()) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    requestPermission();
                }
            }
            break;
            case R.id.ImageChooser: {
                if (check()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                } else {
                    requestPermission();
                }
            }
            break;
        }
    }

    private boolean check() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return (result == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED
        );
    }

    public void requestPermission() {
        int requestCode;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, INTERNET}, requestCode = 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, INTERNET},
                                                        1);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(NewPostActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            if ((requestCode == CAMERA_REQUEST) && (data != null)) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                cardv.setVisibility(View.VISIBLE);
                postImage.setVisibility(View.VISIBLE);
                imageRemoveButton.setVisibility(View.VISIBLE);
                image = getImageUri(getApplicationContext(), photo);
                postImage.setImageBitmap(photo);
            } else if ((requestCode == PICK_IMAGE) && (data != null)) {
                cardv.setVisibility(View.VISIBLE);
                image = data.getData();
                postImage.setImageURI(image);
                imageRemoveButton.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(this, "no image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
