package com.example.myapplication.home;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AppointmentAdapter;
import com.example.myapplication.dao.AppointmentDAO;
import com.example.myapplication.dialogs.EditReservationDialog;
import com.example.myapplication.model.AppointmentDisplay;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    private RecyclerView rvPending, rvCanceled, rvCompleted;
    private AppointmentAdapter pendingAdapter, canceledAdapter, completedAdapter;
    private AppointmentDAO appointmentDAO;
    private List<AppointmentDisplay> pendingList = new ArrayList<>();
    private List<AppointmentDisplay> canceledList = new ArrayList<>();
    private List<AppointmentDisplay> completedList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        refreshAppointments();  // 每次界面回到前台时刷新数据
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        rvPending = findViewById(R.id.rv_pending_appointments);
        rvCanceled = findViewById(R.id.rv_canceled_appointments);
        rvCompleted = findViewById(R.id.rv_completed_appointments);

        rvPending.setLayoutManager(new LinearLayoutManager(this));
        rvCanceled.setLayoutManager(new LinearLayoutManager(this));
        rvCompleted.setLayoutManager(new LinearLayoutManager(this));

        appointmentDAO = new AppointmentDAO(this);

        User currentUser = UserManager.getInstance().getUser();
        if (currentUser != null) {
            int userId = currentUser.getUserId();

            pendingList = appointmentDAO.getAppointmentsWithCounselorNameByUser(userId, "待办");
            canceledList = appointmentDAO.getAppointmentsWithCounselorNameByUser(userId, "取消");
            completedList = appointmentDAO.getAppointmentsWithCounselorNameByUser(userId, "完成");

            pendingAdapter = new AppointmentAdapter(this, pendingList, "待办", appointment -> {
                // 更新数据库状态为取消
                appointmentDAO.updateAppointmentStatus(appointment.getAppointmentId(), "取消");
                appointment.setStatus("取消");
                pendingAdapter.removeItem(appointment);
                canceledAdapter.addItem(appointment);
            }, appointment -> {
                // 更新数据库状态为完成
                appointmentDAO.updateAppointmentStatus(appointment.getAppointmentId(), "完成");
                appointment.setStatus("完成");
                pendingAdapter.removeItem(appointment);
                completedAdapter.addItem(appointment);
            }, appointment -> {
                // 修改功能：弹出修改弹窗
                // 传递 appointmentId 和当前的预约时间
                EditReservationDialog editDialog = new EditReservationDialog(AppointmentActivity.this,
                        appointment.getAppointmentId());
                editDialog.setOnAppointmentUpdatedListener(() -> {
                    // 修改完成后刷新数据
                    refreshAppointments();
                });
                editDialog.show();
            });

            canceledAdapter = new AppointmentAdapter(this, canceledList, "取消", null, null, null);
            completedAdapter = new AppointmentAdapter(this, completedList, "完成", null, null, null);

            rvPending.setAdapter(pendingAdapter);
            rvCanceled.setAdapter(canceledAdapter);
            rvCompleted.setAdapter(completedAdapter);
        } else {
            Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // TODO: 2025-04-09 刷新修改预约事件后的列表 
    // 刷新待办、已取消和已完成列表
    public void refreshAppointments() {
        User currentUser = UserManager.getInstance().getUser();
        if (currentUser != null) {
            int userId = currentUser.getUserId();

            pendingList.clear();
            pendingList.addAll(appointmentDAO.getAppointmentsWithCounselorNameByUser(userId, "待办"));
            pendingAdapter.notifyDataSetChanged();

            canceledList.addAll(appointmentDAO.getAppointmentsWithCounselorNameByUser(userId, "取消"));
            canceledAdapter.notifyDataSetChanged();

            completedList.addAll(appointmentDAO.getAppointmentsWithCounselorNameByUser(userId, "完成"));
            completedAdapter.notifyDataSetChanged();

        }
    }
}
