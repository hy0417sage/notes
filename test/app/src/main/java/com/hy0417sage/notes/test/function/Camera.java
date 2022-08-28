package com.hy0417sage.notes.test.function;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hy0417sage.notes.test.function.in.ICamera;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera implements ICamera {

    public static final int CAMERA_CODE = 1_0000;
    public WeakReference<Activity> activity;
    private Uri photoURI;

    @Override
    public void takeAPhoto() {
        int permission = ContextCompat.checkSelfPermission(activity.get(), Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity.get(), new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
        }else {
            dispatchTakePictureIntent();
        }
    }

    public Uri getPhotoURI() {
        return photoURI;
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.get().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(activity.get().getApplicationContext(), "사진을 가지고 올 수 없습니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(activity.get(),
                        "com.hy0417sage.notes.myapplication.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.get().startActivityForResult(intent, CAMERA_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }
}
