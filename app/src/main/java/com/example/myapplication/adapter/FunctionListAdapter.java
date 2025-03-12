package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Function;
import java.util.List;


public class FunctionListAdapter extends RecyclerView.Adapter<FunctionListAdapter.FunctionViewHolder> {

    private List<Function> functionList;

    public FunctionListAdapter(List<Function> functionList) {
        this.functionList = functionList;
    }

    @NonNull
    @Override
    public FunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new FunctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionViewHolder holder, int position) {
        Function function = functionList.get(position);
        holder.icon.setImageResource(function.getIcon());
        holder.functionName.setText(function.getName());
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }

    static class FunctionViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView functionName;

        public FunctionViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_icon);
            functionName = itemView.findViewById(R.id.tv_function_name);
        }
    }
}