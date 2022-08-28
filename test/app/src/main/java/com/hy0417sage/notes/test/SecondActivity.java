package com.hy0417sage.notes.test;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hy0417sage.notes.test.database.Memo;
import com.hy0417sage.notes.test.database.MemoDB;

import java.util.List;


public class SecondActivity extends AppCompatActivity {

    Context context;
    private MemoDB musicDB = null;
    TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_second);
        musicDB = MemoDB.getInstance(this);


        class InsertRunnable implements Runnable{

            @Override
            public void run() {
                textView = (TextView) findViewById(R.id.textView);
                Log.d("SECOND", "여기를 보시오 : " + MemoDB.getInstance(context).memoDao().getAll());
                List<Memo> music= MemoDB.getInstance(context).memoDao().getAll();
                textView.setText(music.toString());
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread thread = new Thread(insertRunnable);
        thread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoDB.destroyInstance();
    }
}
