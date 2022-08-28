package com.hy0417sage.notes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hy0417sage.notes.fragment.MemoDetailsFragment;
import com.hy0417sage.notes.fragment.MemoEditFragment;
import com.hy0417sage.notes.funtion.Camera;
import com.hy0417sage.notes.funtion.Gallery;
import com.hy0417sage.notes.funtion.ImageLink;
import com.hy0417sage.notes.funtion.Memo;

import java.lang.ref.WeakReference;

/**
 * 기능 2&3. 메모 상세보기와 편집 및 작성을 위한
 * 데이터와 기능이 있는 액티비티 *
 **/

public class SetMemoActivity extends AppCompatActivity {

    private final Memo memo = Memo.getInstance();
    private final Camera camera = new Camera();
    private final Gallery gallery = new Gallery();
    private final ImageLink imageLink = new ImageLink();

    public MemoEditFragment memoEditFragment = new MemoEditFragment();
    public MemoDetailsFragment memoDetailsFragment = new MemoDetailsFragment();

    public String title, content;
    public long nowIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        memo.reference = new WeakReference<>(new Activity());
        memo.openMemo();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMemoData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onFragmentChange(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void getMemoData() {
        Intent intent = getIntent();
        nowIndex = intent.getLongExtra("nowIndex", 0);
        if (nowIndex == 0) {
            onFragmentChange(memoEditFragment);
        } else {
            title = intent.getExtras().getString("title");
            content = intent.getExtras().getString("content");
            onFragmentChange(memoDetailsFragment);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(requestCode == RESULT_OK){
            if(PackageManager.PERMISSION_GRANTED == permission){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                Toast.makeText(this, "카메라 권한이 허용되었습니다.", Toast.LENGTH_LONG).show();
                camera.takePicture();
            }else{
                Toast.makeText(this, "카메라 권한이 거부되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //카메라와 갤러리로 가져온 uri를 저역변수 uri 리스트에 저장해 줍니다.
        if (resultCode == RESULT_OK) {
            if (requestCode == Camera.CAMERA_CODE) {
//                File file = new File(currentPhotoPath);
//                photoURI = Uri.fromFile(file);
            }
            if (requestCode == Gallery.GALLERY_CODE) {
//                photoURI = data.getData();
            }
//            imgUrlList.add(photoURI);
        }
    }

}