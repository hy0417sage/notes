package com.hy0417sage.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hy0417sage.notes.adapter.BaseAdapter;
import com.hy0417sage.notes.database.DBHelper;
import com.hy0417sage.notes.db.MemoDatabase;
import com.hy0417sage.notes.db.MemoEntity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 기능1. 메모리스트
 * **/

public class ShowAllMemoActivity extends AppCompatActivity implements View.OnClickListener {

    private MemoDatabase memoDatabase = null;

    public StaggeredGridLayoutManager staggeredGridLayoutManager;
    
    private List<MemoEntity> memoEntityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    private Context context = null;


    private DBHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.go_to_create_and_edit_button);
        fab.setOnClickListener(this);

        memoDatabase = MemoDatabase.getInstance(this);

        class InsertRunnable implements Runnable{

            @Override
            public void run(){

            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread thread = new Thread(insertRunnable);
        thread.start();

        staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//
//        databaseHelper = new DBHelper(this);
//        databaseHelper.open();
//        databaseHelper.create();

        showExistingMemo();

        //2. Memo Adapter로 메모 데이터를 넘겨 이미지의 썸네일 제목 글의 일부를 보여줍니다.
        //리스트 메모 클릭시 상세화면 이동은 Memo Adapter의 itemView.setOnClickListener로 데이터를 넘겨 구현하였습니다.
        adapter = new BaseAdapter(this, memoEntityList); //TODO :
        recyclerView.setAdapter(adapter);
        Log.d("ShowAllMemoActivity", "onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.d("ShowAllMemoActivity", "onResume()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MemoDatabase.destroyInstance();
        Log.d("FunctionActivity", "onDestroy()");
    }


    //1. 로컬 영역에서 저장된 메모를 읽어 리스트 형태로 회면에 표시합니다.
    public void showExistingMemo() {
        Cursor iCursor = databaseHelper.sortColumn();
//        memoDataList.clear();

//        if(memoDataList.isEmpty()) {
//            while (iCursor.moveToNext()) {
//                Long tempIndex = iCursor.getLong(iCursor.getColumnIndex("_id"));
//                String tempTitle = iCursor.getString(iCursor.getColumnIndex("title"));
//                String tempContent = iCursor.getString(iCursor.getColumnIndex("content"));
//                String tempUrl = iCursor.getString(iCursor.getColumnIndex("pictureUrl"));
//                MemoModel memoData = new MemoModel(tempIndex, tempTitle, tempContent, tempUrl);
//                this.memoDataList.add(memoData);
//            }
//        }else{
//
//        }


        // DB 생성
         memoDatabase = MemoDatabase.getInstance(this);

        // main thread에서 DB 접근 불가 => data 읽고 쓸 때 thread 사용하기
        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                try {
                    memoEntityList = memoDatabase.getInstance(context).memoDao().getAll();
                    adapter = new BaseAdapter(context, memoEntityList);
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e) {

                }
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

        Log.d("ShowAllMemoActivity", "showExistingMemo()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //4. 새 메모를 작성하기 위해 메모 작성 화면으로 이동합니다.
        if (v.getId() == R.id.go_to_create_and_edit_button) {
            startActivity(new Intent(this, FunctionActivity.class));
        }
    }
}
