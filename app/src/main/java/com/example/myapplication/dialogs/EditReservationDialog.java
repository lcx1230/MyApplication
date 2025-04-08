package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DateAdapter;
import com.example.myapplication.dao.AppointmentDAO;
import com.example.myapplication.home.AppointmentActivity;
import com.example.myapplication.model.Appointment;
import com.example.myapplication.model.AppointmentDisplay;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditReservationDialog extends Dialog {
    private Context context;
    private RecyclerView dateRecyclerView;
    private GridLayout timeGridLayout;
    private Button btnConfirmEdit;
    private List<Button> timeButtons = new ArrayList<>();
    private String selectedDate = null;
    private String selectedTime = null;

    private int appointmentId;  // 预约ID

    public EditReservationDialog(@NonNull Context context, int appointmentId) {
        super(context);
        this.appointmentId = appointmentId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_reservation);

        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        timeGridLayout = findViewById(R.id.timeGridLayout);
        btnConfirmEdit = findViewById(R.id.btnConfirmReservation);

        // 设置日期列表（未来7天）
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            dates.add((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 设置日期选择
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        DateAdapter adapter = new DateAdapter(getContext(), dates, selected -> {
            selectedDate = selected;
        });
        dateRecyclerView.setAdapter(adapter);

        // 设置时间段按钮
        setupTimeSlots();

        // 确认修改按钮点击事件
        btnConfirmEdit.setOnClickListener(v -> {
            if (selectedDate != null && selectedTime != null) {
                // 创建 JSON 对象存储修改后的时间段信息
                JSONObject jsonTime = new JSONObject();
                try {
                    jsonTime.put("date", selectedDate);
                    jsonTime.put("time", selectedTime);

                    AppointmentDAO appointmentDAO = new AppointmentDAO(getContext());
                    boolean isUpdated = appointmentDAO.updateAppointmentTime(appointmentId, jsonTime.toString());

                    if (isUpdated) {
                        Toast.makeText(getContext(), "预约修改成功", Toast.LENGTH_LONG).show();
                        // 刷新主界面的数据
                        if (context instanceof AppointmentActivity) {
                            ((AppointmentActivity) context).refreshAppointments();
                        }
                        dismiss();  // 关闭弹窗
                    } else {
                        Toast.makeText(getContext(), "修改失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "数据处理异常", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "请选择日期和时间", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 设置时间段按钮
    private void setupTimeSlots() {
        String[] timeSlots = {"9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
                "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00"};

        for (String timeSlot : timeSlots) {
            Button button = new Button(getContext());
            button.setText(timeSlot);
            button.setAllCaps(false);
            button.setBackgroundResource(R.drawable.selector_button);
            button.setTextColor(Color.BLACK);
            button.setPadding(8, 8, 8, 8);

            // 设置 GridLayout 样式
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(16, 8, 16, 8);
            button.setLayoutParams(params);

            // 选中逻辑
            button.setOnClickListener(v -> {
                for (Button btn : timeButtons) {
                    btn.setSelected(false);
                }
                button.setSelected(true);
                selectedTime = button.getText().toString(); // 更新选中的时间
            });

            timeButtons.add(button);
            timeGridLayout.addView(button);
        }
    }
}
