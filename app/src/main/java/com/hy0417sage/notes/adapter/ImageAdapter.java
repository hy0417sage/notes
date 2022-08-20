package com.hy0417sage.notes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hy0417sage.notes.model.MemoModel;
import com.hy0417sage.notes.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private final List<MemoModel> memoDataList;
    private OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImageAdapter(Context context, List<MemoModel> memoDataList) {
        this.memoDataList = memoDataList;
        this.context = context;
    }

    public ImageAdapter(List<MemoModel> memoDataList) {
        this.memoDataList = memoDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemoModel memoData = memoDataList.get(position);
        Glide.with(context).load(memoData.getPictureUrl()).override(300, 300).into(holder.imageView);

        //메모 편집 및 작성 화면인 경우 cancel 아이콘은 표시해줍니다.
        if (memoData.getActivityDiscrimination().equals("CreateAndEdit")) {
            Glide.with(context).load(R.drawable.cancel).into(holder.deleteImgIcon);
        }
    }

    @Override
    public int getItemCount() {
        return memoDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ImageView deleteImgIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            deleteImgIcon = (ImageView) itemView.findViewById(R.id.icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }

    }
}
