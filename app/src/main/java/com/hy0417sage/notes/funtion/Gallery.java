package com.hy0417sage.notes.funtion;

import android.app.Activity;
import android.content.Intent;

import com.hy0417sage.notes.funtion.in.IGallery;

import java.lang.ref.WeakReference;

public class Gallery implements IGallery {

    public static final int GALLERY_CODE = 1_0000;

    public WeakReference<Activity> reference = new WeakReference<>(new Activity());

    @Override
    public void getPhotoAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), GALLERY_CODE);
    }
}
