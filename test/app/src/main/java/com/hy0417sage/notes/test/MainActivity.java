package com.hy0417sage.notes.test;

import static com.hy0417sage.notes.test.function.Camera.CAMERA_CODE;
import static com.hy0417sage.notes.test.function.Gallery.GALLERY_CODE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.hy0417sage.notes.test.database.Memo;
import com.hy0417sage.notes.test.database.MemoDB;
import com.hy0417sage.notes.test.function.Camera;
import com.hy0417sage.notes.test.function.Gallery;
import com.hy0417sage.notes.test.function.MemoManagement;
import com.hy0417sage.notes.test.function.PhotoLink;
import com.hy0417sage.notes.test.model.MemoModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Camera camera = new Camera();
    private final Gallery gallery = new Gallery();
    private final PhotoLink photoLink = new PhotoLink();
    private final MemoManagement memoManagement = new MemoManagement();

    private MemoDB memoDB = null;
    private MemoModel memoModel = null;
    public List<Uri> photoURIList = new ArrayList<Uri>();

    EditText editTextTitle, editTextText;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera.activity = new WeakReference<>(this);
        gallery.activity = new WeakReference<>(this);
        photoLink.activity = new WeakReference<>(this);
        memoManagement.activity = new WeakReference<>(this);

        memoDB = MemoDB.getInstance(this);
        memoModel = MemoModel.getInstance(this);
        context = getApplicationContext();

        editTextTitle = (EditText) findViewById(R.id.editTextTextPersonName);
        editTextText = (EditText) findViewById(R.id.editTextTextPersonName2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoDB.destroyInstance();
    }

    public void saveButton(View view) {
        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                Memo memo = new Memo();
                memo.title = editTextTitle.getText().toString();
                memo.text = editTextText.getText().toString();
                MemoDB.getInstance(context).memoDao().insertAll(memo);
            }
        }

        InsertRunnable insertRunnable = new InsertRunnable();
        Thread addThread = new Thread(insertRunnable);
        addThread.start();

        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivity(intent);
        finish();
    }

    public void cameraButton(View view) {
        camera.takeAPhoto();
    }

    public void galleryButton(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            gallery.getAPhoto();
        }else{
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_CODE);
        }
    }

    public void photoLinkButton(View view) {
//        startActivityResult.launch(new Intent(this, PhotoLink.class));
        photoLink.getAPhoto();
//        photoURIList.add(photoLink.getPhotoURI()); TODO 다이얼로그 uri 링크 예 클릭시 photoURIList 에 값 저장하기
    }

    @Override //권한 체크가 허용, 거부 되었는지 확인하는 함수
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "카메라 권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    camera.takeAPhoto();
                }
                break;
            case GALLERY_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "갤러리 접근이 거부 되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    gallery.getAPhoto();
                }
                break;
        }
    }

    @Override //실행 후 결과 값을 받아오는 함수
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == CAMERA_CODE) {
            photoURIList.add(camera.getPhotoURI());
        }
        if (requestCode == GALLERY_CODE) {
            Log.d("gallery", "데이터 가져왔는지 확인" + data.getData());
            photoURIList.add(data.getData());
        }
    }
}