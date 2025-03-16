package com.example.myapplication.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.FunctionListAdapter;
import com.example.myapplication.model.Function;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.UserManager;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FunctionListAdapter adapter;
    private List<Function> functionList;
    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private View llUserInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.rv_function_list);
        ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
        tvUserName = view.findViewById(R.id.tv_user_name);
        llUserInfo = view.findViewById(R.id.ll_user_info);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        functionList = new ArrayList<>();
        functionList.add(new Function(R.drawable.service, "服务"));
        functionList.add(new Function(R.drawable.collection, "收藏"));
        functionList.add(new Function(R.drawable.emoji, "表情"));
        functionList.add(new Function(R.drawable.settings, "设置"));
        adapter = new FunctionListAdapter(functionList);
        recyclerView.setAdapter(adapter);

        loadUserProfile();

        llUserInfo.setOnClickListener(v -> startActivity(new Intent(getContext(), ProfileActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile(); // 🔥 确保每次返回时刷新数据
    }

    private void loadUserProfile() {
        User user = UserManager.getInstance().getUser();
        if (user != null) {
            tvUserName.setText(user.getName());
            if (!user.getAvatarUrl().isEmpty()) {
                Glide.with(this).load(Uri.parse(user.getAvatarUrl())).placeholder(R.drawable.icon).into(ivUserAvatar);
            } else {
                ivUserAvatar.setImageResource(R.drawable.icon);
            }
        }
    }
}
