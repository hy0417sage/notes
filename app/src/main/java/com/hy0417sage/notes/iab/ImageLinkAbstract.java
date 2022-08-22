package com.hy0417sage.notes.iab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.hy0417sage.notes.FunctionActivity;
import com.hy0417sage.notes.interafce.ImageLinkInterface;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public abstract class ImageLinkAbstract implements ImageLinkInterface {


    Context context;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }


    @Override
    public void inputImageLinkDialog() {
        final EditText link = new EditText(context);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("이미지 링크를 입력해 주세요.").setView(link)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addImgUrl();
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        alert.show();
    }

    @Override
    public void checkImageLink() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                URLConnection connection = null;
                try {
                    connection = new URL(linkData).openConnection();
                    String contentType = connection.getHeaderField("Content-Type");
                    isImg = contentType.startsWith("image/");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "이미지 링크가 아닙니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                }
            }
        };
        thread.start();
    }

    @Override
    public void addImageLink() {
        Log.d("FunctionActivity", "addImgUrl()");
        if (isImg) {
            linkData = link.getText().toString();
            imgUrlList.add(Uri.parse(linkData));
            Intent intent = new Intent(context, FunctionActivity.class); //TODO : FunctionActivity 가 들어갈 자리 아님!
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            //이미지를 가져올 수 없는 경우 Toast 메시지를 보내줍니다.
            Toast.makeText(context, "이미지 링크가 아닙니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
        }
        isImg = false;
    }
}
