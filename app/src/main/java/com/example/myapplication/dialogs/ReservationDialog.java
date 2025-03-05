package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.DateAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationDialog extends Dialog {

    private RecyclerView dateRecyclerView;
    private Button btnMorning, btnAfternoon, btnEvening, btnConfirmReservation;

    public ReservationDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reservation);

        // 设置弹窗宽高
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 让边角透明
        }

        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        btnMorning = findViewById(R.id.btnMorning);
        btnAfternoon = findViewById(R.id.btnAfternoon);
        btnEvening = findViewById(R.id.btnEvening);
        btnConfirmReservation = findViewById(R.id.btnConfirmReservation);

        // 设置日期列表（未来7天）
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            dates.add(calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 设置RecyclerView
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        DateAdapter adapter = new DateAdapter(getContext(), dates, selectedDate -> {
            Toast.makeText(getContext(), "选择日期: " + selectedDate, Toast.LENGTH_SHORT).show();
        });
        dateRecyclerView.setAdapter(adapter);
    }

}
