package com.hy0417sage.notes.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.hy0417sage.notes.SetMemoActivity;
import com.hy0417sage.notes.adapter.ImageAdapter;
import com.hy0417sage.notes.funtion.Camera;
import com.hy0417sage.notes.funtion.Gallery;
import com.hy0417sage.notes.funtion.ImageLink;
import com.hy0417sage.notes.funtion.Memo;
import com.hy0417sage.notes.model.MemoModel;
import com.hy0417sage.notes.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 기능3. 메모 편집 및 작성
 * **/

public class MemoEditFragment extends Fragment implements View.OnClickListener {

    private final Memo memo = Memo.getInstance();
    private final Camera camera = new Camera();
    private final Gallery gallery = new Gallery();
    private final ImageLink imageLink = new ImageLink();

    public SetMemoActivity functionStorageActivity;
    public EditText editTitle, editContent;
    public LinearLayoutManager horizontalLayoutManager;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Button addImgButton;

    private final List<MemoModel> memoList = new ArrayList<>();
    private LinearLayout fragmentCreateAndEdit;

    @Override
    public void onDetach() {
        super.onDetach();
        functionStorageActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCreateAndEdit = (LinearLayout) inflater.inflate(R.layout.memo_edit_view, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = fragmentCreateAndEdit.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        functionStorageActivity = (SetMemoActivity) getActivity();

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
//        memoList.clear();
//        for (int i = 0; i < functionStorageActivity.imgUrlList.size(); i++) {
//            MemoModel memoData = new MemoModel(functionStorageActivity.imgUrlList.get(i), "CreateAndEdit");
//            memoList.add(memoData);
//        }
//        adapter = new ImageAdapter(getActivity().getApplicationContext(), memoList);
//
//        // 3. 이미지를 삭제할 수 있습니다.
//        ((ImageAdapter) adapter).setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onItemClick(View v, int position) {
//                functionStorageActivity.imgUrlList.remove(memoList.get(position).getPictureUrl());
//                memoList.remove(memoList.get(position));
//                adapter.notifyDataSetChanged(); //이미지가 삭제되는것을 실시간으로 보여줍니다.
//                Toast.makeText(fragmentCreateAndEdit.getContext(), "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        recyclerView.setAdapter(adapter);
//        recyclerView.scrollToPosition(adapter.getItemCount() - 1); //첨부된 사진을 바로 볼수 있도록 스크롤포지션을 마지막으로 설정하였습니다.

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_and_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_button) {
            memo.saveMemo(0, editTitle.getText().toString(), editContent.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_img_button) {
            final CharSequence[] items = {getResources().getString(R.string.camera), getResources().getString(R.string.gallery), getResources().getString(R.string.link)};
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentCreateAndEdit.getContext());
            alertDialogBuilder.setTitle(getResources().getString(R.string.add_pic));
            alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    switch (id) {
                        case 1:
                            camera.takePicture();
                            break;
                        case 2:
                            gallery.getPhotoAlbum();
                            break;
                        case 3:
//                            imageLink.inputLinkDialog();
//                            imageLink.addImageLink();
                    }
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
