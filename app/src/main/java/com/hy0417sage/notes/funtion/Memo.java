package com.hy0417sage.notes.funtion;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.hy0417sage.notes.GetMemoActivity;
import com.hy0417sage.notes.database.DBHelper;
import com.hy0417sage.notes.funtion.in.IMemo;
import com.hy0417sage.notes.model.MemoModel;

import java.lang.ref.WeakReference;

public class Memo implements IMemo {

    MemoModel memoModel;

    public static final Memo INSTANCE = new Memo();
    public static Memo getInstance(){
        return INSTANCE;
    }
    public WeakReference<Activity> reference;
    public DBHelper databaseHelper;

    private Memo(){
    }

    public void openMemo(){
        databaseHelper.open();
        databaseHelper.create();
    }

    public void closeMemo(){
        databaseHelper.close();
    }

    @Override
    public void saveMemo(long id, String title, String text) {
        if (title.isEmpty() && text.isEmpty()) {
            Toast.makeText(reference.get(), "입력한 내용이 없어 저장하지 않았어요.", Toast.LENGTH_SHORT).show();
        } else {
            memoModel = MemoModel.getInstance();
            memoModel.setMemoModel(id, title, text);
            databaseHelper.insertColumn(memoModel.getTitle(), memoModel.getText());
        }
    }

    @Override
    public void editMemo(long id, String title, String text) {
        if (title.isEmpty() && text.isEmpty()) {
            Toast.makeText(reference.get(), "입력한 내용이 없어 저장하지 않았어요.", Toast.LENGTH_SHORT).show();
        } else {
            memoModel = MemoModel.getInstance();
            memoModel.setMemoModel(id, title, text);
            databaseHelper.updateColumn(memoModel.getID(), memoModel.getTitle(), memoModel.getText());
        }
    }

    @Override
    public void deleteMemo(long id) {
        final long index = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHelper.deleteColumn(index);
                Toast.makeText(reference.get().getApplicationContext(), "메모를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create();
    }
}
