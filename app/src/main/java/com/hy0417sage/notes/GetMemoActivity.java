package com.hy0417sage.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hy0417sage.notes.adapter.MemoAdapter;
import com.hy0417sage.notes.database.DBHelper;
import com.hy0417sage.notes.funtion.Memo;
import com.hy0417sage.notes.model.MemoModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.view.Menu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 기능1. 지금까지 작성한 모든 메모를 보여주는 액티비티 입니다.
 * 여기서 기존 메모를 수정할 것인지, 새로운 메모를 생성할 것인지 결정합니다.
 * **/

public class GetMemoActivity extends AppCompatActivity implements View.OnClickListener {

    private final List<MemoModel> memoDataList = new ArrayList<>();
    private final Memo memo = Memo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //툴바
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.go_to_create_and_edit_button); // 메모 수정 및 생성 버튼
        fab.setOnClickListener(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        memo.reference = new WeakReference<>(new Activity());
        getExistingMemo();

        RecyclerView.Adapter adapter = new MemoAdapter(this, memoDataList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //1. 로컬 영역에서 저장된 메모를 읽어 리스트 형태로 회면에 표시합니다.
    private void getExistingMemo() {
        memo.databaseHelper = new DBHelper(this);
        memo.openMemo();

        Cursor iCursor = memo.databaseHelper.sortColumn();
        memoDataList.clear();

        while (iCursor.moveToNext()) {
            Long tempIndex = iCursor.getLong(iCursor.getColumnIndex("_id"));
            String tempTitle = iCursor.getString(iCursor.getColumnIndex("title"));
            String tempContent = iCursor.getString(iCursor.getColumnIndex("content"));
            MemoModel memoData = MemoModel.getInstance();
            memoData.setMemoModel(tempIndex, tempTitle, tempContent);
            this.memoDataList.add(memoData);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.go_to_create_and_edit_button) {
            startActivity(new Intent(this, SetMemoActivity.class));
//            memo.closeMemo();
        }
    }
}
