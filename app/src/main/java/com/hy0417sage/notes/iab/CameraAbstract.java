package com.hy0417sage.notes.iab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hy0417sage.notes.FunctionActivity;
import com.hy0417sage.notes.interafce.CameraInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CameraAbstract implements CameraInterface{

    Context context;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void runCamera() {
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(??? , new String[]{Manifest.permission.CAMERA}, 0);
        }else if(permission == PackageManager.PERMISSION_GRANTED){
            takePictureIntent();
        }
    }

    @Override
    public void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ignored) {
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.hy0417sage.notes.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    @Override
    public void createImageFile() {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
