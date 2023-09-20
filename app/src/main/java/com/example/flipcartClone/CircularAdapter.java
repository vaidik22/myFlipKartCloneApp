package com.example.flipcartClone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.ViewHolder> {
    Context context;
    ArrayList<CircularModel> list;
    private OnItemClickListener mListener;

    public CircularAdapter(Context context, ArrayList<CircularModel> list) {
        this.context = context;
        this.list = list;
    }

    public CircularAdapter(Context context, ArrayList<CircularModel> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CircularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_circular, null);

        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.img_icon2.setImageResource(list.get(position).getIcon());
        holder.tv_title2.setText(list.get(position).getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position); // Call the click listener method
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_icon2;
        TextView tv_title2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon2 = itemView.findViewById(R.id.img_icon2);
            tv_title2 = itemView.findViewById(R.id.tv_title2);
        }
    }
}
