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

public class CreateAndEdit extends Fragment implements View.OnClickListener {

    Interface activity;
    private LinearLayout Fragment_CreateAndEdit;
    EditText edit_title, edit_content;

    LinearLayoutManager horizonalLayoutManager;
    private final List<NotesData> memo_list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Button add_img_button;

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fragment_CreateAndEdit = (LinearLayout) inflater.inflate(R.layout.create_and_edit, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = Fragment_CreateAndEdit.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        activity = (Interface) getActivity();

        //1. 제목 입력란과 본문 입력란, 이미지 첨부란이 구분되어 있습니다.
        edit_title = (EditText) Fragment_CreateAndEdit.findViewById(R.id.edit_title);
        edit_content = (EditText) Fragment_CreateAndEdit.findViewById(R.id.edit_content);
        edit_title.setText(activity.title);
        edit_content.setText(activity.content);

        horizonalLayoutManager
                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView = (RecyclerView) Fragment_CreateAndEdit.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(horizonalLayoutManager);

        add_img_button = (Button) Fragment_CreateAndEdit.findViewById(R.id.add_img_button);
        add_img_button.setOnClickListener(this);

        return Fragment_CreateAndEdit;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        /* 2. 메모에 이미지를 실시간으로 첨부합니다. (생명주기 onResume 사용)
         * 실시간으로 첨부되고 있는 이미지를 ImageAdapter로 볼 수 있습니다. */
        memo_list.clear();
        for (int i = 0; i < activity.url.size(); i++) {
            NotesData memo_data = new NotesData(activity.url.get(i), "CreateAndEdit");
            memo_list.add(memo_data);
        }
        adapter = new ImageAdapter(getActivity().getApplicationContext(), memo_list);

        // 3. 이미지를 삭제할 수 있습니다.
        ((ImageAdapter) adapter).setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(View v, int position) {
                activity.url.remove(memo_list.get(position).getUrl());
                memo_list.remove(memo_list.get(position));
                adapter.notifyDataSetChanged(); //이미지가 삭제되는것을 실시간으로 보여줍니다.
                Toast.makeText(Fragment_CreateAndEdit.getContext(), "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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
            if (activity.nowIndex == 0) {
                activity.data_insert();
            } else {
                activity.data_edit();
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Fragment_CreateAndEdit.getContext());
            alertDialogBuilder.setTitle("@string/add_pic");
            alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (items[id].equals("@string/camera")) {
                        activity.requirePermission();
                        activity.camera();

                    } else if (items[id].equals("@sting/gallery")) {
                        activity.gallery();

                    } else if (items[id].equals("@sting/link")) {
                        activity.link_dialog();
                    }
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
