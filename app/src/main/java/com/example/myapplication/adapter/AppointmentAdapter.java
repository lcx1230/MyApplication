package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.AppointmentDisplay;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private final Context context;
    private final List<AppointmentDisplay> appointmentList;
    private final String listType;

    private final AppointmentAction cancelAction;
    private final AppointmentAction completeAction;
    private final AppointmentAction editAction;

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
        TextView tvCounselorName, tvTime, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCounselorName = itemView.findViewById(R.id.tv_counselor_name);
            tvTime = itemView.findViewById(R.id.tv_appointment_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
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
                        // 目前没有实现修改功能，保留菜单项
                        if (editAction != null) editAction.execute(appointment);
                        Toast.makeText(context, "修改功能暂未实现", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });
                popup.show();
                return true;
            });
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
}
