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
import com.example.myapplication.model.Appointment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationDialog extends Dialog {

    private RecyclerView dateRecyclerView;
    private GridLayout timeGridLayout;
    private Button btnConfirmReservation;
    private List<Button> timeButtons = new ArrayList<>();
    private String selectedDate = null;

    private int userId;  // 用户ID
    private int counselorId; // 咨询师ID

    // 修改构造方法，添加 userId 和 counselorId
    public ReservationDialog(@NonNull Context context, int userId, int counselorId) {
        super(context);
        this.userId = userId;
        this.counselorId = counselorId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reservation);

        // 设置弹窗宽高
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        timeGridLayout = findViewById(R.id.timeGridLayout);
        btnConfirmReservation = findViewById(R.id.btnConfirmReservation);

        // 设置日期列表（未来7天）
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            dates.add((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // RecyclerView 绑定适配器
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        DateAdapter adapter = new DateAdapter(getContext(), dates, selected -> {
            selectedDate = selected;
        });
        dateRecyclerView.setAdapter(adapter);

        // 设置时间段按钮
        setupTimeSlots();

        // 确认预约按钮点击事件
        btnConfirmReservation.setOnClickListener(v -> {
            String selectedTime = getSelectedTime();
            if (selectedDate != null && selectedTime != null) {
                // 这里可以进行网络请求，将预约信息提交到服务器
                confirmReservation(userId, counselorId, selectedDate, selectedTime);
            } else {
                Toast.makeText(getContext(), "请选择日期和时间", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 生成时间段按钮
     */
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
            });

            timeButtons.add(button);
            timeGridLayout.addView(button);
        }
    }

    /**
     * 获取选中的时间
     */
    private String getSelectedTime() {
        for (Button button : timeButtons) {
            if (button.isSelected()) {
                return button.getText().toString();
            }
        }
        return null;
    }

    /**
     * 确认预约（此处可添加实际的预约提交逻辑）
     */
    private void confirmReservation(int userId, int counselorId, String date, String time) {
        try {
            // 创建 JSON 对象存储时间段信息
            JSONObject jsonTime = new JSONObject();
            jsonTime.put("date", date);
            jsonTime.put("time", time);

            // 创建预约对象
            Appointment appointment = new Appointment();
            appointment.setUserId(userId);
            appointment.setCounselorId(counselorId);
            appointment.setAppointmentTime(jsonTime.toString()); // 存入 JSON 格式的时间
            appointment.setStatus("待办"); // 预约状态（可自行修改）
            appointment.setFeedbackId(-1); // 初始反馈 ID，-1 表示暂无反馈

            // 调用 DAO 插入数据
            AppointmentDAO appointmentDAO = new AppointmentDAO(getContext());
            long result = appointmentDAO.insertAppointment(appointment);

            if (result != -1) {
                Toast.makeText(getContext(), "预约成功！", Toast.LENGTH_LONG).show();
                dismiss();
            } else {
                Toast.makeText(getContext(), "预约失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "数据处理异常", Toast.LENGTH_SHORT).show();
        }
    }
}
