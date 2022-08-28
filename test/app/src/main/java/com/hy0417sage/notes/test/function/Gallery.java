package com.hy0417sage.notes.test.function;

import android.app.Activity;
import android.content.Intent;
import com.hy0417sage.notes.test.function.in.IGallery;

import java.lang.ref.WeakReference;

public class Gallery implements IGallery {

    public static final int GALLERY_CODE = 5_0000;
    public WeakReference<Activity> activity;

    @Override
    public void getAPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //컨텐트를 가져오는 내장 액티비티
        intent.setType("image/*");
        activity.get().startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), GALLERY_CODE);
    }

}
