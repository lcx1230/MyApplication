package com.example.myapplication.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;

public class ReadingFragment extends Fragment {

    public ReadingFragment() {
        // 默认的构造函数
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载布局文件
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }
}
