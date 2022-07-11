package com.hy0417sage.notes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hy0417sage.notes.DataBase.DBHelper;
import com.hy0417sage.notes.NotesAdapter.MainAdapter;
import com.hy0417sage.notes.NotesAdapter.NotesData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 기능1. 메모리스트
 * **/

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final List<NotesData> memo_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.go_to_create_and_edit_button);
        fab.setOnClickListener(this);

        staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        mDBHelper = new DBHelper(this);
        mDBHelper.open();
        mDBHelper.create();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showDatabase();
    }

    //1. 로컬 영역에서 저장된 메모를 읽어 리스트 형태로 회면에 표시합니다.
    public void showDatabase() {
        Cursor iCursor = mDBHelper.sortColumn();
        memo_list.clear();

        while (iCursor.moveToNext()) {
            Long tempIndex = iCursor.getLong(iCursor.getColumnIndex("_id"));
            String temp_title = iCursor.getString(iCursor.getColumnIndex("title"));
            String temp_content = iCursor.getString(iCursor.getColumnIndex("content"));
            String temp_url = iCursor.getString(iCursor.getColumnIndex("url"));
            NotesData memo_data = new NotesData(tempIndex, temp_title, temp_content, temp_url);
            memo_list.add(memo_data);
        }

        //2. MainAdapter로 메모 데이터를 넘겨 이미지의 썸네일 제목 글의 일부를 보여줍니다.
        //리스트 메모 클릭시 상세화면 이동은 MainAdapter의 itemView.setOnClickListener로 데이터를 넘겨 구현하였습니다.
        adapter = new MainAdapter(getApplicationContext(), memo_list);
        recyclerView.setAdapter(adapter);
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
            startActivity(new Intent(getApplicationContext(), Interface.class));
        }
    }
}
