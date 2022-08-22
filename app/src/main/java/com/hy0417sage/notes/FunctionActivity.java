package com.hy0417sage.notes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.hy0417sage.notes.database.DBHelper;
import com.hy0417sage.notes.db.MemoDatabase;
import com.hy0417sage.notes.db.MemoEntity;
import com.hy0417sage.notes.fragment.MemoContentFragment;
import com.hy0417sage.notes.fragment.MemoCreateOrModifyFragment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 기능 2&3. 메모 상세보기와 편집 및 작성을 위한
 * 데이터와 기능이 있는 액티비티 *
 **/

public class FunctionActivity extends AppCompatActivity {

    public DBHelper databaseHelper;
    public MemoCreateOrModifyFragment memoCreateOrModifyFragment = new MemoCreateOrModifyFragment();
    public MemoContentFragment memoContentFragment = new MemoContentFragment();
    public String title, content, stringUrl, linkData;
    public List<Uri> imgUrlList = new ArrayList<Uri>();
    public long nowIndex;
    public boolean isImg = false;

    private static final int CAMERA_CODE = 10;
    private static final int GALLERY_CODE = 0;
    private String currentPhotoPath;
    private Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        databaseHelper = new DBHelper(this);
        databaseHelper.open();
        databaseHelper.create();


        Log.d("FunctionActivity", "onResume()");
    }

    // onStart : 액티비티가 화면에 표시되기 직전에 호출, 화면에 진입할 때마다 실행
    @Override
    public void onStart() {
        super.onStart();
        settingMemo();
        //toRunCamera();
        Log.d("FunctionActivity", "onStart()");
    }

    // onResume : 잠시 액티비티가 일시정지 되었다가 돌아오는 경우 호출, 액티비티가 재개되었을 때 실행
    @Override
    public void onResume() {
        super.onResume();
        deleteNullUrl();
        Log.d("FunctionActivity", "onResume()");
    }

    // onPause : 방해되는 이벤트가 발생하면 호출, 실행할 필요가 없는 기능들을 일시정지
    @Override
    public void onPause() {
        super.onPause();
        Log.d("FunctionActivity", "onPause()");
    }

    // onStop : 액티비티가 사용자에게 더이상 보이지 않으면 호출
    @Override
    public void onStop() {
        super.onStop();
        Log.d("FunctionActivity", "onStop()");
    }

    // onRestart : 홈으로 나갔다가 다시 돌아오거나 다른 액티비티로 갔다가 뒤로 가기 버튼을 통해서 돌아오는 경우 호출
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("FunctionActivity", "onRestart()");
    }

    // onDestroy : 앱을 종료하는 경우 호출
    @Override
    public void onDestroy() {
        super.onDestroy();
        MemoDatabase.destroyInstance();
        Log.d("FunctionActivity", "onDestroy()");
    }

    //onFragmentChange 함수로 화면을 전환할 수 있도록 하였습니다.
    public void onFragmentChange(Fragment fragment) {
        Log.d("FunctionActivity", "onFragmentChange() : " + fragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    //기능 2&3. 메모 셋팅
    public void settingMemo() {
        Log.d("FunctionActivity", "settingMemo()");
        Intent intent = getIntent();
        nowIndex = intent.getLongExtra("nowIndex", 0);

        if (nowIndex == 0) {
            //intent로 받아온 값이 없다면 바로 메모 편집 및 작성으로 넘어갑니다.
            onFragmentChange(memoCreateOrModifyFragment);
        } else {
            /* intent로 받아온 값이 있는경우 전역변수 title, content, string_url에 각각의 값을 넣어주어
             * 메모 리스트 화면에서 선택한 메모의 값을 불러와 메모 상세보기 화면에 보여줍니다. */
            title = intent.getExtras().getString("title");
            content = intent.getExtras().getString("content");
            stringUrl = intent.getExtras().getString("stringUrl");

            String data = stringUrl.replace("[", "").replace("]", "").replaceAll(" ", "");
            List<String> urlData = Arrays.asList(data.split(","));

            //url값에 빈 값이 들어간 경우 삭제해 줍니다.
            for (int i = 0; i < urlData.size(); i++) {
                Uri u = Uri.parse(urlData.get(i));
                if (u != null) {
                    imgUrlList.add(u);
                }
            }
            onFragmentChange(memoContentFragment);
        }
    }
    private Context context;

    //기능2. 메모 저장
    public void saveTheMemo() {
        Log.d("FunctionActivity", "saveTheMemo()");
        title = memoCreateOrModifyFragment.editTitle.getText().toString(); //create에 있는 에딧 가져와 저장
        content = memoCreateOrModifyFragment.editContent.getText().toString();
        if (title.equals("") && content.equals("") && imgUrlList.isEmpty()) {
            Toast.makeText(this, "입력한 내용이 없어 저장하지 않았어요.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
//            databaseHelper.open();
//            databaseHelper.insertColumn(title, content, imgUrlList.toString());
//            onFragmentChange(memoContentFragment);

            class InsertRunnable implements Runnable{
                @Override
                public void run(){
                    context = getApplicationContext();
                    MemoEntity memoEntity = new MemoEntity(title, content, imgUrlList.toString());
                    MemoDatabase.getInstance(context).memoDao().insertMemo(memoEntity);
                }
            }

            InsertRunnable insertRunnable = new InsertRunnable();
            Thread addThread = new Thread(insertRunnable);
            addThread.start();
            onFragmentChange(memoContentFragment);
        }
    }

    //기능3. 기존 메모 편집
    public void editMemo() {
        Log.d("FunctionActivity", "editMemo()");
        title = memoCreateOrModifyFragment.editTitle.getText().toString();
        content = memoCreateOrModifyFragment.editContent.getText().toString();
        if (title.equals("") && content.equals("") && imgUrlList.isEmpty()) {
            databaseHelper.deleteColumn(nowIndex);
            Toast.makeText(this, "입력한 내용이 없어 저장하지 않았어요.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            databaseHelper.updateColumn(nowIndex, title, content, imgUrlList.toString());
            onFragmentChange(memoContentFragment);
        }
    }

    //기능3. 새로 생성한 메모 편집
    public void newMemoEdit() {
        Log.d("FunctionActivity", "newMemoEdit()");
        Cursor iCursor = databaseHelper.selectColumns();
        iCursor.moveToLast();
        //새로 생성한 메모을 메모리스트를 거치치 않고 수정할 경우 로컬 영역에 저장된 마지막 nowIndex를 넣으줍니다.
        nowIndex = iCursor.getLong(iCursor.getColumnIndex("_id"));
    }

    //기능3. 메모 삭제
    public void deleteMemo() {
        Log.d("FunctionActivity", "deleteMemo()");
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("메모를 삭제 하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteColumn(nowIndex);
                        Toast.makeText(FunctionActivity.this, "메모를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    //이미지 링크 입력 다이얼로그
    public void inputImgLinkDialog() {
        Log.d("FunctionActivity", "inputImgLinkDialog()");
        final EditText link = new EditText(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("이미지 링크를 입력해 주세요.").setView(link)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        linkData = link.getText().toString();
                        checkImgLink();
                        if (isImg) {
                            addImgUrl(); //url 가 맞을 경우
                        }else{
                            Toast.makeText(getApplicationContext(), "이미지 링크가 아닙니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    //기능3-1. 이미지 링크인지 확인합니다.
    public void checkImgLink(){
        Log.d("FunctionActivity", "checkImgLink()");
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
                }
            }
        };
        thread.start();
    }


    //기능3-2. 이미지 링크인 경우 photoUrlList에 url을 추가합니다.
    public void addImgUrl(){
        Log.d("FunctionActivity", "addImgUrl()");
        if (isImg) {
            imgUrlList.add(Uri.parse(linkData));
            Intent intent = new Intent(FunctionActivity.this, FunctionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            //이미지를 가져올 수 없는 경우 Toast 메시지를 보내줍니다.
            Looper.prepare();
            Toast.makeText(getApplicationContext(), "이미지 링크가 아닙니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
            Looper.loop();
        }
        isImg = false;
    }

    
    //기능3. 카메라 실행
    public void toRunCamera() {
        Log.d("FunctionActivity", "toRunCamera()");
        int permission = ContextCompat.checkSelfPermission(FunctionActivity.this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }else if(permission == PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent();
        }
    }


    //기능3. 카메라 이미지 파일 생성
    public void dispatchTakePictureIntent() {
        Log.d("FunctionActivity", "dispatchTakePictureIntent()");
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

    private File createImageFile() throws IOException {
        Log.d("FunctionActivity", "createImageFile()");
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

    //기능3. 갤러리를 보여줍니다.
    public void showPhotoAlbum() {
        Log.d("FunctionActivity", "showPhotoAlbum()");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), GALLERY_CODE);
    }

    //이미지 null url 삭제
    public void deleteNullUrl() {
        Log.d("FunctionActivity", "deleteNullUrl()");
        for (int i = 0; i < imgUrlList.size(); i++) {
            if (imgUrlList.get(i).toString().equals("")) {
                imgUrlList.remove(imgUrlList.get(i));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //카메라와 갤러리로 가져온 uri를 저역변수 uri 리스트에 저장해 줍니다.
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                photoURI = data.getData();
            }
            if (requestCode == CAMERA_CODE) {
                File file = new File(currentPhotoPath);
                photoURI = Uri.fromFile(file);
            }
            imgUrlList.add(photoURI);
        }
    }

}