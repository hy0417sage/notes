package com.hy0417sage.notes.funtion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hy0417sage.notes.funtion.in.ICamera;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera implements ICamera {

    public WeakReference<Activity> reference = new WeakReference<>(new Activity());
    public static final int CAMERA_CODE = 1_0000000;

    @Override
    public void takePicture() {
        int permission = ContextCompat.checkSelfPermission(reference.get(), Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(reference.get(), new String[]{Manifest.permission.CAMERA}, 0);
        } else if (permission == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        }
    }

    //기능3. 카메라 이미지 파일 생성
    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ignored) {
//            }
//            if (photoFile != null) {
//                photoURI = FileProvider.getUriForFile(this,
//                        "com.hy0417sage.notes.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, CAMERA_CODE);
//            }
//        }
    }

//    private File createImageFile() throws IOException {
//        Log.d("FunctionActivity", "createImageFile()");
//        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,
//                ".jpg",
//                storageDir
//        );
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
}
