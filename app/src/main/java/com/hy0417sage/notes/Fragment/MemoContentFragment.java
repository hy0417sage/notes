package com.hy0417sage.notes.Fragment;

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

import com.hy0417sage.notes.FunctionActivity;
import com.hy0417sage.notes.Adapter.ImageAdapter;
import com.hy0417sage.notes.DataClass.MemoData;
import com.hy0417sage.notes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 기능2. 메모 상세보기
 * **/

public class MemoContentFragment extends Fragment {

    public FunctionActivity functionActivity;
    public TextView textTitle;
    public TextView textContent;
    public TextView imgCount;
    public List<MemoData> memoList = new ArrayList<>();
    public LinearLayoutManager horizontalLayoutManager;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout memoContent = (LinearLayout) inflater.inflate(R.layout.memo_content_view, container, false);
        setHasOptionsMenu(true);

        Toolbar toolbar = memoContent.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        functionActivity = (FunctionActivity) getActivity();

        textTitle = (TextView) memoContent.findViewById(R.id.text_title);
        textContent = (TextView) memoContent.findViewById(R.id.text_content);
        imgCount = (TextView) memoContent.findViewById(R.id.img_count);

        horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView = (RecyclerView) memoContent.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(horizontalLayoutManager);

        //1. 작성된 메모의 제목과 본문을 볼수 있습니다.
        textTitle.setText(functionActivity.title);
        textContent.setText(functionActivity.content);
        textContent.setMovementMethod(new ScrollingMovementMethod());

        /* 2. 메모에 첨부되어있는 이미지를 볼 수 있습니다.
         * CreateAndEdit을 통해 메모에 첨부되어있는 이미지를 ImageAdapter로 볼 수 있습니다. */
        memoList.clear();
        for (int i = 0; i < functionActivity.imgUrlList.size(); i++) {
            MemoData memo_data = new MemoData(functionActivity.imgUrlList.get(i), "Details");
            memoList.add(memo_data);
        }

        adapter = new ImageAdapter(getActivity().getApplicationContext(), memoList);
        recyclerView.setAdapter(adapter);

        return memoContent;
    }

    public void init(){

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        imgCount.setText("사진 개수 : " + functionActivity.imgUrlList.size() + "개");
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
                if (functionActivity.nowIndex == 0) {
                    functionActivity.newMemoEdit();
                }
                functionActivity.onFragmentChange(functionActivity.memoCreateOrModifyFragment);
                break;

            //3. 상단 메뉴를 통해 메모 내용을 삭제할 수 있습니다.
            case R.id.delete_button:
                if (functionActivity.nowIndex == 0) {
                    functionActivity.newMemoEdit();
                    functionActivity.databaseHelper.deleteColumn(functionActivity.nowIndex);
                } else {
                    functionActivity.deleteMemo();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
