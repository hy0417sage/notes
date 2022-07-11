package com.hy0417sage.notes.NotesAdapter;

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
import com.hy0417sage.notes.Interface;
import com.hy0417sage.notes.R;

import java.util.Arrays;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private final List<NotesData> list_data;

    public MainAdapter(Context context, List<NotesData> list_data) {
        this.list_data = list_data;
        this.context = context;
    }

    public MainAdapter(List<NotesData> list_data) {
        this.list_data = list_data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotesData notesData = list_data.get(position);

        holder.tempIndex = notesData.getTempIndex();
        holder.title.setText(notesData.getTitle()); //메모 제목
        holder.content.setText(notesData.getContent()); //메모 글
        holder.string_url = notesData.getStringUrlList().replace("[", "").replace("]", "");
        List<String> list = Arrays.asList(holder.string_url.split(","));
        Glide.with(context).load(list.get(0)).override(300, 300).into(holder.imageView); //첫번째 이미지를 썸네일로 보여줍니다.
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public Long tempIndex;
        public TextView title;
        public TextView content;
        public String string_url;
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
                    Intent intent = new Intent(context.getApplicationContext(), Interface.class);
                    intent.putExtra("nowIndex", tempIndex);
                    intent.putExtra("title", title.getText());
                    intent.putExtra("content", content.getText());
                    intent.putExtra("string_url", string_url);
                    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                }
            });

        }
    }


}