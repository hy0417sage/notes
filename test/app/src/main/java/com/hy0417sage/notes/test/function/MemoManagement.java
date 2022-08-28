package com.hy0417sage.notes.test.function;

import android.app.Activity;
import android.content.Intent;

import com.hy0417sage.notes.test.database.Memo;
import com.hy0417sage.notes.test.database.MemoDB;
import com.hy0417sage.notes.test.function.in.IMemoManagement;
import com.hy0417sage.notes.test.model.MemoModel;
import com.hy0417sage.notes.test.SecondActivity;

import java.lang.ref.WeakReference;

public class MemoManagement implements IMemoManagement {

    public WeakReference<Activity> activity;
    public MemoModel memoModel;

    @Override
    public void saveMemo() {
        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                Memo memo = new Memo();
                memo.title = memoModel.getTitle();
                memo.text = memoModel.getText();
                MemoDB.getInstance(activity.get()).memoDao().insertAll(memo);
            }
        }

        InsertRunnable insertRunnable = new InsertRunnable();
        Thread addThread = new Thread(insertRunnable);
        addThread.start();
        Intent intent = new Intent(activity.get(), SecondActivity.class);
        activity.get().startActivity(intent);
        activity.get().finish();
    }

    @Override
    public void editMemo() {

    }

    @Override
    public void deleteMemo() {

    }
}
