package com.hy0417sage.notes.test.function;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.hy0417sage.notes.test.function.in.IPhotoLink;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class PhotoLink implements IPhotoLink {

    public static final int PHOTO_LINK_CODE = 9_0000;
    public WeakReference<Activity> activity;
    private Uri photoURI;

    @Override
    public void getAPhoto() {
        inputPhotoLinkDialog();
    }

    public Uri getPhotoURI() {
        return photoURI;
    }

    private void inputPhotoLinkDialog() {
        final EditText setPhotoLink = new EditText(activity.get());
        AlertDialog.Builder alert = new AlertDialog.Builder(activity.get());

        alert.setTitle("이미지 링크를 입력해 주세요.").setView(setPhotoLink)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String photoLink = setPhotoLink.getText().toString();
                        getResultPhoto(photoLink);
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        alert.show();
    }

    private void getResultPhoto(String photoLink){
        final boolean[] isImage = {false};
        Thread thread = new Thread() {
            @Override
            public void run() {
                URLConnection connection = null;
                try {
                    connection = new URL(photoLink).openConnection(); //URLUtil.isValidUrl(photoLink) 아닐 경우, catch
                    String contentType = connection.getHeaderField("Content-Type");
                    if (URLUtil.isValidUrl(photoLink) && contentType.startsWith("image/")) {
                        photoURI = Uri.parse(photoLink);
                        isImage[0] = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        if(!isImage[0]){
            Toast.makeText(activity.get().getApplicationContext(), "이미지 링크가 아닙니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
        }
    }
}
