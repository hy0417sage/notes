package com.hy0417sage.notes;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
 * 기능3. 메모 편집 및 작성
 * **/

public class CreateOrModifyNotesFragment extends Fragment implements View.OnClickListener {

    public FunctionStorageActivity functionStorageActivity;
    private LinearLayout fragmentCreateAndEdit;
    public EditText editTitle, editContent;

    public LinearLayoutManager horizontalLayoutManager;
    private final List<NotesData> memoList = new ArrayList<>();
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Button addImgButton;

    @Override
    public void onDetach() {
        super.onDetach();
        functionStorageActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCreateAndEdit = (LinearLayout) inflater.inflate(R.layout.create_and_edit, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = fragmentCreateAndEdit.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        functionStorageActivity = (FunctionStorageActivity) getActivity();

        //1. 제목 입력란과 본문 입력란, 이미지 첨부란이 구분되어 있습니다.
        editTitle = (EditText) fragmentCreateAndEdit.findViewById(R.id.edit_title);
        editContent = (EditText) fragmentCreateAndEdit.findViewById(R.id.edit_content);
        editTitle.setText(functionStorageActivity.title);
        editContent.setText(functionStorageActivity.content);

        horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView = (RecyclerView) fragmentCreateAndEdit.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(horizontalLayoutManager);

        addImgButton = (Button) fragmentCreateAndEdit.findViewById(R.id.add_img_button);
        addImgButton.setOnClickListener(this);

        return fragmentCreateAndEdit;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        /* 2. 메모에 이미지를 실시간으로 첨부합니다. (생명주기 onResume 사용)
         * 실시간으로 첨부되고 있는 이미지를 ImageAdapter로 볼 수 있습니다. */
        memoList.clear();
        for (int i = 0; i < functionStorageActivity.url.size(); i++) {
            NotesData memoData = new NotesData(functionStorageActivity.url.get(i), "CreateAndEdit");
            memoList.add(memoData);
        }
        adapter = new ImageAdapter(getActivity().getApplicationContext(), memoList);

        // 3. 이미지를 삭제할 수 있습니다.
        ((ImageAdapter) adapter).setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(View v, int position) {
                functionStorageActivity.url.remove(memoList.get(position).getUrl());
                memoList.remove(memoList.get(position));
                adapter.notifyDataSetChanged(); //이미지가 삭제되는것을 실시간으로 보여줍니다.
                Toast.makeText(fragmentCreateAndEdit.getContext(), "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1); //첨부된 사진을 바로 볼수 있도록 스크롤포지션을 마지막으로 설정하였습니다.

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_and_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_button) {
            if (functionStorageActivity.nowIndex == 0) {
                functionStorageActivity.dataInsert();
            } else {
                functionStorageActivity.dataEdit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        /*2. 이미지 첨부란의 추가 버튼을 통해 이미지 첨부가 가능합니다.
         * 총 방법은 3 가지이며 선택 다이얼로그를 사용하여 첨부 방법을 선택할 수 있도록 하였습니다. */
        if (v.getId() == R.id.add_img_button) {
            final CharSequence[] items = {"@string/camera", "@sting/gallery", "@sting/link"};
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentCreateAndEdit.getContext());
            alertDialogBuilder.setTitle("@string/add_pic");
            alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (items[id].equals("@string/camera")) {
                        functionStorageActivity.requirePermission();
                        functionStorageActivity.camera();

                    } else if (items[id].equals("@sting/gallery")) {
                        functionStorageActivity.gallery();

                    } else if (items[id].equals("@sting/link")) {
                        functionStorageActivity.linkDialog();
                    }
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
