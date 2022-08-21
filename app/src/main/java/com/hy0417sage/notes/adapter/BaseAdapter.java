package com.hy0417sage.notes.adapter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hy0417sage.notes.FunctionActivity;
import com.hy0417sage.notes.R;
import com.hy0417sage.notes.db.MemoEntity;
import com.hy0417sage.notes.model.MemoModel;

import java.util.Arrays;
import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    private Context context;
    private List<MemoEntity> memoList;

    public BaseAdapter(Context context, List<MemoEntity> memoList) {
        this.memoList = memoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.note, parent, false);
        BaseAdapter.ViewHolder viewHolder = new BaseAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MemoEntity memoEntity = memoList.get(position);

        holder.tempIndex = memoEntity.getTempIndex();
        holder.title.setText(memoEntity.getTitle()); //메모 제목
        holder.content.setText(memoEntity.getContent()); //메모 글
        holder.stringUrl = memoEntity.getStringUrlList().replace("[", "").replace("]", "");
        List<String> pictureUrlList = Arrays.asList(holder.stringUrl.split(","));
        Glide.with(context).load(pictureUrlList.get(0)).override(300, 300).into(holder.imageView); //첫번째 이미지를 썸네일로 보여줍니다.
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Long tempIndex;
        public TextView title;
        public TextView content;
        public String stringUrl;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            //3. 리스트의 메모를 선택하면 메모 상세보기 화면으로 이동합니다.
            // 메모 상세보기 화면에서 해당 메모의 내용을 보여주기 위해서 Intent를 사용하여 값을 전달해 줍니다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), FunctionActivity.class);
                    intent.putExtra("nowIndex", tempIndex);
                    intent.putExtra("title", title.getText());
                    intent.putExtra("content", content.getText());
                    intent.putExtra("stringUrl", stringUrl);
                    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
}
