package com.example.myapplication.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.myapplication.R;
import com.example.myapplication.adapter.HomeArticleAdapter;
import com.example.myapplication.adapter.ImagePagerAdapter;
import com.example.myapplication.model.HomeArticle;
import com.example.myapplication.utils.WebScraperHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    public interface OnDataPass {
        void onDataPass(String data);
    }

    private OnDataPass dataPasser;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private ImageView[] dots;
    private int currentPage = 1; // 真实的第一张图片索引
    private final int[] realImageResIds = {R.drawable.home_01, R.drawable.home_02, R.drawable.home_03, R.drawable.home_04, R.drawable.home_05};
    private int[] extendedImageResIds; // 额外的伪无限轮播图片数组
    private final Handler handler = new Handler();
    private Timer timer;
    private RecyclerView recyclerView;
    private HomeArticleAdapter articleAdapter;
    private List<HomeArticle> articleList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataPasser = (OnDataPass) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " 必须实现 OnDataPass 接口");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // 绑定 "即时预约" 卡片
        View appointmentCard = view.findViewById(R.id.psy_reserve);
        // 绑定 "心理测试" 卡片
        View psychologyTestCard = view.findViewById(R.id.psy_test);
        // 生成伪无限循环的图片列表
        extendedImageResIds = new int[realImageResIds.length + 2];
        extendedImageResIds[0] = realImageResIds[realImageResIds.length - 1]; // 最后一张的副本放到第 0 张
        extendedImageResIds[extendedImageResIds.length - 1] = realImageResIds[0]; // 第一张的副本放到最后一张
        System.arraycopy(realImageResIds, 0, extendedImageResIds, 1, realImageResIds.length); // 复制真实图片

        // 初始化 ViewPager 和 指示点布局
        viewPager = view.findViewById(R.id.viewPager);
        dotsLayout = view.findViewById(R.id.dotsLayout);

        //初始化滑动设置适配器
        ImagePagerAdapter adapter = new ImagePagerAdapter(requireContext(), extendedImageResIds);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPage, false); // 设置当前显示为真实的第一张

        // ✅ 确保 `articleList` 不是 null
        articleList = new ArrayList<>();

        //初始化recyclerView适配器
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleAdapter = new HomeArticleAdapter(getContext(), articleList);
        recyclerView.setAdapter(articleAdapter);
        // 初始化指示点
        setupDots();
        // 监听 ViewPager 滑动事件，更新指示点状态 + 无限循环
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentPage = realImageResIds.length;
                    viewPager.setCurrentItem(currentPage, false); // 立刻跳到最后一张
                } else if (position == extendedImageResIds.length - 1) {
                    currentPage = 1;
                    viewPager.setCurrentItem(currentPage, false); // 立刻跳到第一张
                } else {
                    currentPage = position;
                }
                updateDots(currentPage - 1); // 更新指示点
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        // 启动自动轮播
        startAutoSlide();

        // 调用爬取方法
        WebScraperHelper.fetchArticles(5, this::updateRecyclerView);

        // ✅ "即时预约" 跳转到 `CounselorFragment`
        appointmentCard.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, new CounselorFragment());
            transaction.addToBackStack(null);
            transaction.commit();

            // 更新底部导航栏状态
            if (requireActivity() instanceof HomeActivity) {
                ((HomeActivity) requireActivity()).setSelectedNavigationItem(R.id.nav_consultation);
            }
        });
        // 添加心理测试卡片点击事件
        psychologyTestCard.findViewById(R.id.psy_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTestDialog();
            }
        });

        return view;
    }
    // 更新 RecyclerView
    private void updateRecyclerView(List<HomeArticle> newArticles) {
        articleList.clear();
        articleList.addAll(newArticles);
        articleAdapter.notifyDataSetChanged();
    }

    private void setupDots() {
        dots = new ImageView[realImageResIds.length]; // 只有真正的图片才有指示点
        for (int i = 0; i < realImageResIds.length; i++) {
            dots[i] = new ImageView(requireContext());
            dots[i].setImageResource(i == 0 ? R.drawable.dot_selected : R.drawable.dot_unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15); // 小圆点大小
            params.setMargins(5, 0, 5, 0); // 设置间距
            dots[i].setLayoutParams(params);
            dotsLayout.addView(dots[i]);
        }
    }

    private void updateDots(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageResource(i == position ? R.drawable.dot_selected : R.drawable.dot_unselected);
        }
    }

    private void startAutoSlide() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    if (currentPage >= extendedImageResIds.length - 1) {
                        currentPage = 1;
                        viewPager.setCurrentItem(currentPage, false);
                    } else {
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                    updateDots(currentPage - 1);
                });
            }
        }, 2000, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void sendData() {
        if (dataPasser != null) {
            dataPasser.onDataPass("Hello from HomeFragment");
        }
    }
    //心里测试弹窗
    private void showTestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("提示")
                .setMessage("是否进入心理测试？")
                .setPositiveButton("是", (dialog, which) -> {
                    Intent intent = new Intent(requireContext(), PsychologyTestActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("否", (dialog, which) -> {
                    Toast.makeText(requireContext(), "已取消测试", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
