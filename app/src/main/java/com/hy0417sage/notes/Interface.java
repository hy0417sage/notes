package com.hy0417sage.notes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hy0417sage.notes.DataBase.DBHelper;

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

public class Interface extends AppCompatActivity {

    public CreateAndEdit createAndEdit = new CreateAndEdit();
    public Details details = new Details();

    public long nowIndex;
    public String title, content;
    public String stringUrl;

    public DBHelper dbHelper;

    private static final int CAMERA_CODE = 10;
    private static final int GALLERY_CODE = 0;
    private Uri photoURI;
    private String currentPhotoPath;
    public List<Uri> url = new ArrayList<Uri>();

    public String linkData;
    public boolean img = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        dbHelper = new DBHelper(this);
        dbHelper.open();
        dbHelper.create();

        setting();
        requirePermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        deleteNullUrl();
    }

    //onFragmentChange 함수로 화면을 전환할 수 있도록 하였습니다.
    public void onFragmentChange(String choice_activity) {
        if (choice_activity.equals("CreateAndEdit")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, createAndEdit).commit();

        } else if (choice_activity.equals("Details")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, details).commit();
        }
    }

    //기능 2&3. 메모 셋팅
    public void setting() {
        Intent intent = getIntent();
        nowIndex = intent.getLongExtra("nowIndex", 0);

        if (nowIndex == 0) {
            //intent로 받아온 값이 없다면 바로 메모 편집 및 작성으로 넘어갑니다.
            onFragmentChange("CreateAndEdit");
        } else {
            /* intent로 받아온 값이 있는경우 전역변수 title, content, string_url에 각각의 값을 넣어주어
             * 메모 리스트 화면에서 선택한 메모의 값을 불러와 메모 상세보기 화면에 보여줍니다. */
            title = intent.getExtras().getString("title");
            content = intent.getExtras().getString("content");
            stringUrl = intent.getExtras().getString("string_url");

            String data = stringUrl.replace("[", "").replace("]", "").replaceAll(" ", "");
            List<String> url_data = Arrays.asList(data.split(","));

            //url값에 빈 값이 들어간 경우 삭제해 줍니다.
            for (int i = 0; i < url_data.size(); i++) {
                Uri u = Uri.parse(url_data.get(i));
                if (u != null) {
                    url.add(u);
                }
            }
            onFragmentChange("Details");
        }
    }

    //기능2. 메모 저장
    public void dataInsert() {
        title = createAndEdit.editTitle.getText().toString(); //create에 있는 에딧 가져와 저장
        content = createAndEdit.editContent.getText().toString();
        if (title.equals("") && content.equals("") && url.isEmpty()) {
            Toast.makeText(this, "입력한 내용이 없어 저장하지 않았어요.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            dbHelper.open();
            dbHelper.insertColumn(title, content, url.toString());
            onFragmentChange("Details");
        }
    }

    //기능3. 기존 메모 편집
    public void dataEdit() {
        title = createAndEdit.editTitle.getText().toString();
        content = createAndEdit.editContent.getText().toString();
        if (title.equals("") && content.equals("") && url.isEmpty()) {
            dbHelper.deleteColumn(nowIndex);
            Toast.makeText(this, "입력한 내용이 없어 저장하지 않았어요.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            dbHelper.updateColumn(nowIndex, title, content, url.toString());
            onFragmentChange("Details");
        }
    }

    //기능3. 새로 생성한 메모 편집
    public void firstDataEditDelete() {
        Cursor iCursor = dbHelper.selectColumns();
        iCursor.moveToLast();
        //새로 생성한 메모을 메모리스트를 거치치 않고 수정할 경우 로컬 영역에 저장된 마지막 nowIndex를 넣으줍니다.
        nowIndex = iCursor.getLong(iCursor.getColumnIndex("_id"));
    }

    //기능3. 메모 삭제
    public void dataDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("메모를 삭제 하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteColumn(nowIndex);
                        Toast.makeText(Interface.this, "메모를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
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

    //기능3. 이미지 링크
    public void linkDialog() {
        final EditText link = new EditText(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("이미지 링크를 입력해 주세요.").setView(link)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        linkData = link.getText().toString();
                        if (URLUtil.isValidUrl(linkData)) {
                            imageLink(linkData);
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

    //기능3. 이미지 링크 확인
    public void imageLink(String image_link){
        //링크 url를 겁사합니다.
        Thread thread = new Thread() {
            @Override
            public void run() {
                URLConnection connection = null;
                try {
                    connection = new URL(linkData).openConnection();
                    String contentType = connection.getHeaderField("Content-Type");
                    img = contentType.startsWith("image/");
                    if (img == true) {
                        url.add(Uri.parse(linkData));
                        Intent intent = new Intent(Interface.this, Interface.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } else {
                        //이미지를 가져올 수 없는 경우 Toast 메시지를 보내줍니다.
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "이미지 링크가 아닙니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    //기능3. 카메라 permission
    public void requirePermission() {
        int permission = ContextCompat.checkSelfPermission(Interface.this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    //기능3. permission 허용시 카메라 intent
    public void camera() {
        int permission = ContextCompat.checkSelfPermission(Interface.this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_GRANTED)
            dispatchTakePictureIntent();
    }

    //기능3. 카메라
    public void dispatchTakePictureIntent() {
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

    //기능3. 갤러리
    public void gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), GALLERY_CODE);
    }

    //이미지 null url 삭제
    public void deleteNullUrl() {
        for (int i = 0; i < url.size(); i++) {
            if (url.get(i).toString().equals("")) {
                url.remove(url.get(i));
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
            url.add(photoURI);
        }
    }

}