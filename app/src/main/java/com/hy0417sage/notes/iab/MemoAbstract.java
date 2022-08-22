package com.hy0417sage.notes.iab;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.hy0417sage.notes.database.DBHelper;
import com.hy0417sage.notes.interafce.MemoInterface;
import com.hy0417sage.notes.model.MemoModel;
import com.hy0417sage.notes.view.MemoDetailsView;
import com.hy0417sage.notes.view.MemoEditView;

public abstract class MemoAbstract implements MemoInterface{

    public Context context;
    public DBHelper databaseHelper;
    public MemoModel memoModel = new MemoModel();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void showAllMemo() {

    }

    @Override
    public void showDetailMemo() {
        long nowIndex = memoModel.getNewIndex();
        if(nowIndex == 0) {
        } else {
            memoModel.setTitle(title);
            memoModel.setContent(content);
        }
    }

    @Override
    public void saveEditMemo() {
        long nowIndex = memoModel.getNewIndex();
        if (memoModel.getTitle().isEmpty() && memoModel.getContent().isEmpty()) {
            databaseHelper.deleteColumn(nowIndex);
            Toast.makeText(context, "입력한 내용이 없어 저장하지 않았어요.", Toast.LENGTH_SHORT).show();
        } else {
            if(nowIndex == 0){ //true
                databaseHelper.open();
                databaseHelper.insertColumn(title, content);
            }else{
                databaseHelper.updateColumn(nowIndex, title, content);
            }
        }
    }

    @Override
    public void editMemo() {
        Cursor cursor = databaseHelper.selectColumns();
        cursor.moveToLast();
        nowIndex = cursor.getLong(cursor.getColumnIndex("_id"));
    }

    @Override
    public void deleteMemo() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("메모를 삭제 하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteColumn(nowIndex);
                        Toast.makeText(context, "메모를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }
}
