package com.hy0417sage.notes.fragment;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hy0417sage.notes.funtion.Camera;
import com.hy0417sage.notes.funtion.Gallery;
import com.hy0417sage.notes.funtion.ImageLink;
import com.hy0417sage.notes.funtion.Memo;
import com.hy0417sage.notes.model.MemoModel;
import com.hy0417sage.notes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 기능2. 메모 상세보기
 * **/

public class MemoDetailsFragment extends Fragment {

    private final MemoModel memoModel = MemoModel.getInstance();
    private final Memo memo = Memo.getInstance();

    public TextView textTitle, textText;
    public TextView imgCount;
    public List<MemoModel> memoList = new ArrayList<>();
    public LinearLayoutManager horizontalLayoutManager;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout memoDetails = (LinearLayout) inflater.inflate(R.layout.memo_details_view, container, false);
        setHasOptionsMenu(true);

        textTitle = (TextView) memoDetails.findViewById(R.id.text_title);
        textText = (TextView) memoDetails.findViewById(R.id.text_content);
        imgCount = (TextView) memoDetails.findViewById(R.id.img_count);

        horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView = (RecyclerView) memoDetails.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(horizontalLayoutManager);

        //1. 작성된 메모의 제목과 본문을 볼수 있습니다.
        textTitle.setText(memoModel.getTitle());
        textText.setText(memoModel.getText());
        textText.setMovementMethod(new ScrollingMovementMethod());

        return memoDetails;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_button:
                memo.editMemo(memoModel.getID(), memoModel.getTitle(), memoModel.getText());
                break;

            case R.id.delete_button:
                memo.deleteMemo(memoModel.getID());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
