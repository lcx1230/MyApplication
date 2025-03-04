package com.example.myapplication.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CounselorAdapter;
import com.example.myapplication.model.Counselor;
import java.util.ArrayList;
import java.util.List;

public class CounselorFragment extends Fragment {

    private RecyclerView recyclerView;
    private CounselorAdapter adapter;
    private List<Counselor> counselorList;

    private TextView tvSpecialization, tvQualification, tvGender;

    private final String[] specializations = {"全部", "心理咨询", "儿童心理", "婚姻家庭", "职场心理", "学生心理"};
    private final String[] qualifications = {"全部", "国家二级", "国家三级", "国际认证", "心理学博士", "资深导师"};
    private final String[] genders = {"全部", "男", "女", "其他", "不限"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counselor, container, false);

        // 初始化筛选项
        tvSpecialization = view.findViewById(R.id.tvSpecialization);
        tvQualification = view.findViewById(R.id.tvQualification);
        tvGender = view.findViewById(R.id.tvGender);

        setupDropdown(tvSpecialization, specializations);
        setupDropdown(tvQualification, qualifications);
        setupDropdown(tvGender, genders);

        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 加载默认数据
        counselorList = getDefaultCounselors();
        adapter = new CounselorAdapter(getContext(), counselorList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    // 绑定下拉适配器
    private void setupDropdown(TextView textView, String[] items) {
        textView.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(getContext(), v);
            for (String item : items) {
                popupMenu.getMenu().add(item);
            }
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                textView.setText(menuItem.getTitle());
                return true;
            });
            popupMenu.show();
        });
    }

    // 默认数据（保证后续可动态加载数据库数据）
    private List<Counselor> getDefaultCounselors() {
        List<Counselor> list = new ArrayList<>();
        list.add(new Counselor(1, "李老师", "男", "国家二级心理咨询师", "心理咨询", "https://example.com/avatar1.jpg", "可预约"));
        list.add(new Counselor(2, "王教授", "女", "心理学博士", "儿童心理", "https://example.com/avatar2.jpg", "可预约"));
        list.add(new Counselor(3, "张医生", "男", "国际认证心理咨询师", "婚姻家庭", "https://example.com/avatar3.jpg", "可预约"));
        list.add(new Counselor(4, "张医生", "男", "国际认证心理咨询师", "婚姻家庭", "https://example.com/avatar3.jpg", "可预约"));
        list.add(new Counselor(5, "张医生", "男", "国际认证心理咨询师", "婚姻家庭", "https://example.com/avatar3.jpg", "可预约"));
        list.add(new Counselor(6, "张医生", "男", "国际认证心理咨询师", "婚姻家庭", "https://example.com/avatar3.jpg", "可预约"));
        list.add(new Counselor(7, "张医生", "男", "国际认证心理咨询师", "婚姻家庭", "https://example.com/avatar3.jpg", "可预约"));
        list.add(new Counselor(8, "张医生", "男", "国际认证心理咨询师", "婚姻家庭", "https://example.com/avatar3.jpg", "可预约"));
        return list;
    }
}
