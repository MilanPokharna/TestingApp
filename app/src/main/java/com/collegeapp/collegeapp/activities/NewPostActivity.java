package com.collegeapp.collegeapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.CameraProfile;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.collegeapp.collegeapp.fragments.fragment_my_post.snakebar;

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
    DatabaseReference refe = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users");
    public FirebaseAuth mauth = FirebaseAuth.getInstance();
    public FirebaseUser user;
    Uri image;
    ProgressDialog progressDialog;
    int CAMERA_REQUEST = 1;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public static final int PICK_IMAGE = 2;
    String string;
    String mydate;
    StorageReference reference = FirebaseStorage.getInstance().getReference().child("images");
    @BindView(R.id.cardv)
    CardView cardv;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.newpostlayout)
    RelativeLayout newpostlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        myref = myref.child("root").child("twitter").child("posts");
        user = mauth.getCurrentUser();
        mydate = String.valueOf(System.currentTimeMillis());
        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(profileImage);
        imageRemoveButton.setVisibility(View.INVISIBLE);
        cardv.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
    }

    @OnClick({R.id.closeButton, R.id.postButton, R.id.imageRemoveButton, R.id.CameraIntent, R.id.ImageChooser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                finish();
                break;
            case R.id.postButton: {
                if (isNetworkConnected()) {
                    final String des = Description.getText().toString().trim();
                    if (!(TextUtils.isEmpty(des))) {
                        progressDialog.setMessage("Uploading Post");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        myref = myref.push();
                        string = myref.getKey().toString();

                        if (postImage.getDrawable() == null) {
                            myref.child("profileimage").setValue(user.getPhotoUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    myref.child("posttime").setValue(mydate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            myref.child("postimage").setValue("0");
                                            refe = refe.child(user.getUid());
                                            refe.child("value").setValue("1");
                                            refe.child("posts").child(string).setValue(string);
                                            myref.child("email").setValue(user.getEmail());
                                            myref.child("name").setValue(user.getDisplayName());
                                            myref.child("postdata").setValue(des);
                                            myref.child("userid").setValue(user.getUid());
                                            //Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                                            progressDialog.cancel();
                                            finish();
                                        }
                                    });
                                }
                            });
                        } else {
                            reference.child(string).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    myref.child("postimage").setValue(string);
                                    myref.child("profileimage").setValue(user.getPhotoUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            refe = refe.child(user.getUid());
                                            refe.child("value").setValue("1");
                                            refe.child("posts").child(string).setValue(string);
                                            myref.child("email").setValue(user.getEmail());
                                            myref.child("userid").setValue(user.getUid());
                                            myref.child("name").setValue(user.getDisplayName());
                                            myref.child("postdata").setValue(des);
                                            myref.child("posttime").setValue(mydate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();

                                                    progressDialog.cancel();
                                                    finish();
                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }

                    } else {
                        Snackbar snackbar1 = Snackbar.make(newpostlayout, "Can't Upload Empty Post", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        //Toast.makeText(this, "Can't Upload Empty Post", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar snackbar1 = Snackbar.make(newpostlayout, "No Internet Connection", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
                break;
            }

            case R.id.imageRemoveButton:
                postImage.setImageDrawable(null);
                postImage.setVisibility(View.GONE);
                cardv.setVisibility(View.GONE);
                imageRemoveButton.setVisibility(View.GONE);
                break;
            case R.id.CameraIntent: {
                if (check()) {
                    openCameraIntent();
                } else {
                    requestPermission();
                    Snackbar snackbar1 = Snackbar.make(newpostlayout, "No Permission to Access", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
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
                    Snackbar snackbar1 = Snackbar.make(newpostlayout, "No Permission to Access", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
            }
            break;
        }
    }

    private boolean check() {
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return (result == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission() {
        int requestCode;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, requestCode = 1);
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

        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode != RESULT_CANCELED)
        {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAPTURE_IMAGE) {
                cardv.setVisibility(View.VISIBLE);
                imageRemoveButton.setVisibility(View.VISIBLE);
                //Toast.makeText(this, "path : "+imageFilePath, Toast.LENGTH_SHORT).show();
                postImage.setImageURI(photoURI);
                image=photoURI;
            }
            else if ((requestCode == PICK_IMAGE) && (data != null)) {
                cardv.setVisibility(View.VISIBLE);
                image = data.getData();
                postImage.setImageURI(image);
                imageRemoveButton.setVisibility(View.VISIBLE);
            }
        } else {
            Snackbar snackbar1 = Snackbar.make(newpostlayout, "no image selected", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            //Toast.makeText(this, "no image selected", Toast.LENGTH_SHORT).show();
        }
    }
    public Uri photoURI;

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.collegeapp.collegeapp.FileProvider",
                        photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }
    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();

        return image;
    }


//    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            return true;
        } else
            return false;
    }
}
