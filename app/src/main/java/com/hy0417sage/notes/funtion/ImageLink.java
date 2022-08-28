package com.hy0417sage.notes.funtion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.hy0417sage.notes.SetMemoActivity;
import com.hy0417sage.notes.funtion.in.IImageLink;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ImageLink implements IImageLink {

    public WeakReference<Activity> reference = new WeakReference<>(new Activity());
    private boolean isImg = false;

    @Override
    public void inputLinkDialog(String linkUri) {
//        final String uri = linkUri;
//        AlertDialog.Builder alert = new AlertDialog.Builder(reference.get());
//        alert.setTitle("이미지 링크를 입력해 주세요.").setView(???)
//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        checkImgLink(uri);
//                    }
//                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//        alert.show();
    }

    @Override
    public void addImageLink(List<Uri> linkUriList, String linkUri) {
        checkImgLink(linkUri);
//        if(isImg) {
//            linkUriList.add(Uri.parse(linkUri));
//            Intent intent = new Intent(reference.get(), SetMemoActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//        }
    }

    private void checkImgLink(String linkUri){
        final String uri = linkUri;
        Thread thread = new Thread() {
            @Override
            public void run() {
                URLConnection connection = null;
                try {
                    connection = new URL(uri).openConnection();
                    String contentType = connection.getHeaderField("Content-Type");
                    isImg = contentType.startsWith("image/");
                } catch (IOException e) {
                    Toast.makeText(reference.get(), "이미지 링크가 아닙니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

}
