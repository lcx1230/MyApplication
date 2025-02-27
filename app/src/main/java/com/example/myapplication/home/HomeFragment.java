package com.example.myapplication.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;

public class HomeFragment extends Fragment {

    /**
     * 定义一个接口，用于从 Fragment 向 Activity 传递数据。
     * 这是一个回调接口，Activity 需要实现这个接口，才能接收到 Fragment 发送的数据。
     */
    public interface OnDataPass {
        /**
         * 当数据需要传递时，Activity 会调用这个方法。
         *
         * @param data 要传递的数据，这里是一个字符串。
         */
        void onDataPass(String data);
    }

    /**
     * 定义一个 OnDataPass 类型的变量，用于保存 Activity 传递过来的回调接口实例。
     */
    OnDataPass dataPasser;

    /**
     * 当 Fragment 被附加到 Activity 上时，这个方法会被调用。
     * 我们在这里获取 Activity 传递过来的回调接口实例。
     *
     * @param context Fragment 所附加到的 Activity 的 Context。
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // 尝试将 Context 转换为 OnDataPass 接口类型。
            // 如果 Activity 实现了 OnDataPass 接口，那么转换会成功，否则会抛出 ClassCastException 异常。
            dataPasser = (OnDataPass) context;
        } catch (ClassCastException e) {
            // 如果 Activity 没有实现 OnDataPass 接口，则抛出异常，并提示 Activity 必须实现该接口。
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    /**
     * 当 Fragment 需要创建它的视图时，这个方法会被调用。
     * 我们在这里加载 Fragment 的布局文件。
     *
     * @param inflater           用于加载布局文件的 LayoutInflater。
     * @param container          Fragment 的父容器。
     * @param savedInstanceState 如果 Fragment 之前被销毁过，这里会保存之前的状态信息。
     * @return Fragment 的根视图。
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用 LayoutInflater 加载 fragment_home.xml 布局文件，并返回根视图。
        // R.layout.fragment_home 表示 fragment_home.xml 布局文件的资源 ID。
        // container 是 Fragment 的父容器，我们在这里传入它，是为了让系统知道这个 Fragment 应该放在哪里。
        // false 表示我们不希望将加载的布局文件附加到 container 上，因为系统会自动处理。
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * 在某个时刻传递数据到 Activity 的方法。
     * 当我们需要从 Fragment 向 Activity 传递数据时，可以调用这个方法。
     */
    public void sendData() {
        // 调用 Activity 实现的 onDataPass 方法，并将数据传递过去。
        dataPasser.onDataPass("Hello from HomeFragment");
    }
}