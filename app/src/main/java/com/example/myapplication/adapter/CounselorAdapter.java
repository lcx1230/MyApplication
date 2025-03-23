package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Counselor;

import java.util.List;

public class CounselorAdapter extends RecyclerView.Adapter<CounselorAdapter.CounselorViewHolder> {

    private final Context context;
    private final List<Counselor> counselorList;
    private OnCounselorClickListener onCounselorClickListener;


    // 接口回调，交互逻辑交给 Fragment
    public interface OnCounselorClickListener {
        void onItemClick(Counselor counselor);  // 预约
        void onItemLongClick(Counselor counselor); // 查看资格证书
    }

    public CounselorAdapter(Context context, List<Counselor> counselorList) {
        this.context = context;
        this.counselorList = counselorList;
    }

    public void setOnCounselorClickListener(OnCounselorClickListener listener) {
        this.onCounselorClickListener = listener;
    }

    @NonNull
    @Override
    public CounselorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_counselor, parent, false);
        return new CounselorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounselorViewHolder holder, int position) {
        Counselor counselor = counselorList.get(position);

        holder.counselorName.setText(counselor.getName());
        holder.counselorServiceType.setText("当面 / 视频");
        holder.counselorDetails.setText(counselor.getSpecialization() + " | " + counselor.getQualifications());

        // 加载头像
        Glide.with(context).load(counselor.getAvatarUrl()).placeholder(R.drawable.profile).into(holder.counselorAvatar);

        // 让 Fragment 处理点击事件
        holder.itemView.setOnClickListener(v -> {
            if (onCounselorClickListener != null) {
                onCounselorClickListener.onItemClick(counselor);
            }
        });

        // 让 Fragment 处理长按事件
        holder.itemView.setOnLongClickListener(v -> {
            if (onCounselorClickListener != null) {
                onCounselorClickListener.onItemLongClick(counselor);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return counselorList.size();
    }

    static class CounselorViewHolder extends RecyclerView.ViewHolder {
        ImageView counselorAvatar;
        TextView counselorName, counselorServiceType, counselorDetails;

        public CounselorViewHolder(@NonNull View itemView) {
            super(itemView);
            counselorAvatar = itemView.findViewById(R.id.counselorAvatar);
            counselorName = itemView.findViewById(R.id.counselorName);
            counselorServiceType = itemView.findViewById(R.id.counselorServiceType);
            counselorDetails = itemView.findViewById(R.id.counselorDetails);
        }
    }
}
