package com.hy0417sage.notes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hy0417sage.notes.NotesAdapter.ImageAdapter;
import com.hy0417sage.notes.NotesAdapter.NotesData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 기능2. 메모 상세보기
 * **/

public class NotesContentFragment extends Fragment {

    public FunctionStorageActivity functionStorageActivity;

    public TextView textTitle;
    public TextView textContent;
    public TextView imgCount;

    public List<NotesData> memoList = new ArrayList<>();
    public LinearLayoutManager horizontalLayoutManager;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout fragmentDetails = (LinearLayout) inflater.inflate(R.layout.details, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = fragmentDetails.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        functionStorageActivity = (FunctionStorageActivity) getActivity();

        textTitle = (TextView) fragmentDetails.findViewById(R.id.text_title);
        textContent = (TextView) fragmentDetails.findViewById(R.id.text_content);
        imgCount = (TextView) fragmentDetails.findViewById(R.id.img_count);

        horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView = (RecyclerView) fragmentDetails.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(horizontalLayoutManager);

        //1. 작성된 메모의 제목과 본문을 볼수 있습니다.
        textTitle.setText(functionStorageActivity.title);
        textContent.setText(functionStorageActivity.content);
        textContent.setMovementMethod(new ScrollingMovementMethod());

        /* 2. 메모에 첨부되어있는 이미지를 볼 수 있습니다.
         * CreateAndEdit을 통해 메모에 첨부되어있는 이미지를 ImageAdapter로 볼 수 있습니다. */
        memoList.clear();
        for (int i = 0; i < functionStorageActivity.imgUrlList.size(); i++) {
            NotesData memo_data = new NotesData(functionStorageActivity.imgUrlList.get(i), "Details");
            memoList.add(memo_data);
        }

        adapter = new ImageAdapter(getActivity().getApplicationContext(), memoList);
        recyclerView.setAdapter(adapter);

        return fragmentDetails;
    }

    public void init(){

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        imgCount.setText("사진 개수 : " + functionStorageActivity.imgUrlList.size() + "개");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /* 메모를 저장하자마자 메모 리스트를 통하지 않고 메모를 수정, 삭제하기 위해
             * 로컬영역의 마지막에 저장되어있는 데이터를 불러옵니다. */

            //3. 상단 메뉴를 통해 메모 내용을 편집할 수 있습니다.
            case R.id.edit_button:
                if (functionStorageActivity.nowIndex == 0) {
                    functionStorageActivity.newMemoEdit();
                }
                functionStorageActivity.onFragmentChange(functionStorageActivity.createOrModifyNotesFragment);
                break;

            //3. 상단 메뉴를 통해 메모 내용을 삭제할 수 있습니다.
            case R.id.delete_button:
                if (functionStorageActivity.nowIndex == 0) {
                    functionStorageActivity.newMemoEdit();
                    functionStorageActivity.databaseHelper.deleteColumn(functionStorageActivity.nowIndex);
                } else {
                    functionStorageActivity.deleteMemo();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
