package com.hy0417sage.notes.iab;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.hy0417sage.notes.interafce.GalleryInterface;

public abstract class GalleryAbstract implements GalleryInterface {

    abstract Intent createIntent(@NonNull Context context);

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void showGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        createIntent(Intent.createChooser(intent, "이미지를 선택하세요."), GALLERY_CODE);
    }
}
