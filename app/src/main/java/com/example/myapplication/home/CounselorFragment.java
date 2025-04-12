package com.example.myapplication.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CounselorAdapter;
import com.example.myapplication.dao.CounselorDAO;
import com.example.myapplication.model.Counselor;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CounselorAdapter;
import com.example.myapplication.dao.CounselorDAO;
import com.example.myapplication.dialogs.ReservationDialog;
import com.example.myapplication.model.Counselor;
import com.example.myapplication.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class CounselorFragment extends Fragment {

    private RecyclerView recyclerView;
    private CounselorAdapter adapter;
    private List<Counselor> counselorList;
    private List<Counselor> filteredList; // 筛选后的列表

    private TextView tvSpecialization, tvQualification, tvGender;
    private String selectedSpecialization = "不限";
    private String selectedQualification = "不限";
    private String selectedGender = "不限";

    private final String[] specializations = {"不限", "心理咨询", "儿童心理", "婚姻家庭", "职场心理", "学生心理"};
    private final String[] qualifications = {"不限", "国家二级", "国家三级", "国际认证", "心理学博士", "资深导师"};
    private final String[] genders = {"不限", "男", "女"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counselor, container, false);

        // 绑定筛选项
        tvSpecialization = view.findViewById(R.id.tvSpecialization);
        tvQualification = view.findViewById(R.id.tvQualification);
        tvGender = view.findViewById(R.id.tvGender);

        setupDropdown(tvSpecialization, specializations, "specialization");
        setupDropdown(tvQualification, qualifications, "qualification");
        setupDropdown(tvGender, genders, "gender");

        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 从数据库加载数据
        CounselorDAO dao = new CounselorDAO(getContext());
        counselorList = dao.getAllCounselorsWithCertificate();
        filteredList = new ArrayList<>(counselorList);

        // 绑定适配器
        adapter = new CounselorAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        // 监听点击 & 长按事件
        adapter.setOnCounselorClickListener(new CounselorAdapter.OnCounselorClickListener() {
            @Override
            public void onItemClick(Counselor counselor) {
                int userId= UserManager.getInstance().getUser().getUserId();
                Log.d("UserId", "onItemClick: "+userId);
                showReservationDialog(userId,  counselor.getCounselorId());// 普通用户：单击显示预约
            }

            @Override
            public void onItemLongClick(Counselor counselor) {
                Log.d("Counselor", "Certificate URL: " + counselor.getCertificateUrl());
                if (counselor.getCertificateUrl() == null || counselor.getCertificateUrl().isEmpty()) {
                    Toast.makeText(getContext(), "该咨询师暂无资格证书", Toast.LENGTH_SHORT).show();
                } else {
                    showCertificateDialog(counselor.getCertificateUrl());
                }
            }
        });

        return view;
    }

    /**
     * 显示预约对话框
     */
    private void showReservationDialog(int userId, int counselorId) {
        Log.d("UserId", "onItemClick: "+userId);
        ReservationDialog dialog = new ReservationDialog(getContext(),userId,counselorId);
        dialog.show();
    }

    /**
     * 显示资格证书
     */
    private void showCertificateDialog(String certificateUrl) {
        if (certificateUrl == null || certificateUrl.isEmpty()) {
            Toast.makeText(getContext(), "该咨询师暂无资格证书", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("资格证书");

        // 创建 ImageView 来显示证书
        ImageView imageView = new ImageView(getContext());
        Glide.with(getContext())
                .load(certificateUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Certificate load failed", e);
                        Log.d("GlideError", "onLoadFailed: "+certificateUrl);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("GlideSuccess", "Certificate loaded successfully");
                        return false;
                    }
                })
                .placeholder(R.drawable.emoji) // 默认占位图
                .error(R.drawable.emoji) // 加载失败
                .into(imageView);

        builder.setView(imageView);
        builder.setPositiveButton("关闭", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * 绑定筛选项的点击事件
     */
    private void setupDropdown(TextView textView, String[] items, String filterType) {
        textView.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(getContext(), v);
            for (String item : items) {
                popupMenu.getMenu().add(item);
            }
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                textView.setText(menuItem.getTitle());
                switch (filterType) {
                    case "specialization":
                        selectedSpecialization = menuItem.getTitle().toString();
                        break;
                    case "qualification":
                        selectedQualification = menuItem.getTitle().toString();
                        break;
                    case "gender":
                        selectedGender = menuItem.getTitle().toString();
                        break;
                }
                filterCounselors(); // 筛选数据
                return true;
            });
            popupMenu.show();
        });
    }

    /**
     * 根据筛选条件更新列表
     */
    private void filterCounselors() {
        filteredList.clear();
        for (Counselor counselor : counselorList) {
            boolean matchSpecialization = selectedSpecialization.equals("不限") || counselor.getSpecialization().equals(selectedSpecialization);
            boolean matchQualification = selectedQualification.equals("不限") || counselor.getQualifications().equals(selectedQualification);
            boolean matchGender = selectedGender.equals("不限") || counselor.getGender().equals(selectedGender);

            if (matchSpecialization && matchQualification && matchGender) {
                filteredList.add(counselor);
            }
        }
        adapter.notifyDataSetChanged(); // 刷新 RecyclerView
    }

}

