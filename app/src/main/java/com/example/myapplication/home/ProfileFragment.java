package com.example.myapplication.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FunctionListAdapter;

import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.model.Function;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FunctionListAdapter adapter;
    private List<Function> functionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.rv_function_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        functionList = new ArrayList<>();
        functionList.add(new Function(R.drawable.service, "服务"));
        functionList.add(new Function(R.drawable.collection, "收藏"));
        functionList.add(new Function(R.drawable.emoji, "表情"));
        functionList.add(new Function(R.drawable.settings, "设置"));

        adapter = new FunctionListAdapter(functionList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
