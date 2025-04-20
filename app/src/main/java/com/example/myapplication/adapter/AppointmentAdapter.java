package com.example.myapplication.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dao.AppointmentDAO;
import com.example.myapplication.dao.FeedbackDAO;
import com.example.myapplication.model.Appointment;
import com.example.myapplication.model.AppointmentDisplay;
import com.example.myapplication.model.Feedback;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private final Context context;
    private final List<AppointmentDisplay> appointmentList;
    private final String listType;

    private final AppointmentAction cancelAction;
    private final AppointmentAction completeAction;
    private final AppointmentAction editAction;
    private String TAG = "AppointmentAdapter";

    public interface AppointmentAction {
        void execute(AppointmentDisplay appointment);
    }

    public AppointmentAdapter(Context context, List<AppointmentDisplay> list, String listType,
                              AppointmentAction cancelAction,
                              AppointmentAction completeAction,
                              AppointmentAction editAction) {
        this.context = context;
        this.appointmentList = list;
        this.listType = listType;
        this.cancelAction = cancelAction;
        this.completeAction = completeAction;
        this.editAction = editAction;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCounselorName, tvTime, tvStatus, tvText;
        ImageView tvIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCounselorName = itemView.findViewById(R.id.tv_counselor_name);
            tvTime = itemView.findViewById(R.id.tv_appointment_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvIcon = itemView.findViewById(R.id.tv_feedback_icon);
            tvText = itemView.findViewById(R.id.tv_feedback_text);
        }
    }

    @NonNull
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.ViewHolder holder, int position) {
        AppointmentDisplay appointment = appointmentList.get(position);
        holder.tvCounselorName.setText("咨询师: " + appointment.getCounselorName());
        holder.tvTime.setText("时间: " + appointment.getFormattedDateTime());
        holder.tvStatus.setText("状态: " + appointment.getStatus());

        switch (appointment.getStatus().toLowerCase()) {
            case "待办":
                holder.tvStatus.setTextColor(Color.BLUE);
                break;
            case "取消":
                holder.tvStatus.setTextColor(Color.RED);
                break;
            case "完成":
                holder.tvStatus.setTextColor(Color.GREEN);
                break;
            default:
                holder.tvStatus.setTextColor(Color.DKGRAY);
        }

        if ("待办".equals(listType)) {
            holder.itemView.setOnLongClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, v, Gravity.END);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.appointment_item_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menu_cancel) {
                        if (cancelAction != null) cancelAction.execute(appointment);
                        Toast.makeText(context, "预约已取消", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (item.getItemId() == R.id.menu_complete) {
                        if (completeAction != null) completeAction.execute(appointment);
                        Toast.makeText(context, "预约已完成", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (item.getItemId() == R.id.menu_edit) {
                        // 修改时间
                        if (editAction != null) editAction.execute(appointment);
                        Toast.makeText(context, "修改已完成", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });
                popup.show();
                return true;
            });
        }
        //添加反馈的反馈的监听事件
        if ("完成".equals(listType)) {
            //获取appointments里面的feedback_id，如果是-1，就是未反馈
            AppointmentDAO appointmentDAO = new AppointmentDAO(context.getApplicationContext());
            Pair<Integer, Integer> ids = appointmentDAO.getUserAndCounselorIdByAppointmentId(appointment.getAppointmentId());
            int feed_id = appointmentDAO.getFeedbackIdByAppointmentId(appointment.getAppointmentId());
            holder.tvIcon.setVisibility(View.VISIBLE);
            holder.tvText.setVisibility(View.VISIBLE);
            if (feed_id == -1) {
                holder.tvIcon.setImageResource(R.drawable.ic_feedback_pending); // 未反馈图标
                holder.tvText.setText("未反馈");
                holder.tvText.setTextColor(Color.GRAY);
                if (ids != null) {
                    int userId = ids.first;
                    int counselorId = ids.second;
                    Log.d(TAG, "fid、uid、cid分别为1: " + feed_id + "、" + userId + "、" + counselorId);
                    holder.itemView.setOnLongClickListener(v -> {
                        //监听事件，弄一个弹窗出来去实现反馈功能
                        showFeedbackDialog(context, userId, counselorId, appointment.getAppointmentId());
                        return true;
                    });
                }
            } else {
                holder.tvIcon.setImageResource(R.drawable.ic_feedback_done); // 已反馈图标
                holder.tvText.setText("已反馈");
                holder.tvText.setTextColor(Color.parseColor("#4CAF50")); // 绿色
                holder.itemView.setOnLongClickListener(v -> {
                    // 不允许弹窗
                    Toast.makeText(context, "该预约已反馈，不能再次提交", Toast.LENGTH_SHORT).show();
                    return true;
                });
            }


        }
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public void removeItem(AppointmentDisplay appointment) {
        appointmentList.remove(appointment);
        notifyDataSetChanged();
    }

    public void addItem(AppointmentDisplay appointment) {
        appointmentList.add(0, appointment);
        notifyDataSetChanged();
    }

    private void showFeedbackDialog(Context context, int userId, int counselorId, int appointmentId) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow)));

        EditText etComment = dialogView.findViewById(R.id.etComment);

        new AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("提交", (dialog, which) -> {
                    int rating = (int) ratingBar.getRating();
                    String comment = etComment.getText().toString();

                    // 创建 Feedback 对象并插入数据库
                    Feedback feedback = new Feedback();
                    feedback.setUserId(userId);
                    feedback.setCounselorId(counselorId);
                    feedback.setRating(rating);
                    feedback.setComment(comment);
                    FeedbackDAO feedbackDAO = new FeedbackDAO(context.getApplicationContext());
                    long feedbackId = feedbackDAO.insertFeedback(feedback);

                    if (feedbackId != -1) {
                        // 更新 appointments 表的 feedback_id 字段
                        AppointmentDAO appointmentDAO = new AppointmentDAO(context.getApplicationContext());
                        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                        appointment.setFeedbackId((int) feedbackId);
                        appointmentDAO.updateAppointment(appointment);
                        Toast.makeText(context, "反馈提交成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("取消", null)
                .show();
    }

}
