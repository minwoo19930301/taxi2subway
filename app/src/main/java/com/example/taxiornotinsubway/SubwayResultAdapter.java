package com.example.taxiornotinsubway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubwayResultAdapter extends RecyclerView.Adapter<SubwayResultAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> subwayList;

    public SubwayResultAdapter(Context context, ArrayList<String> subwayList) {
        this.context = context;
        this.subwayList = subwayList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView start;
        public TextView end;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            start = itemView.findViewById(R.id.start_result);
            end = itemView.findViewById(R.id.end_result);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.result_subway, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        if (position == getItemCount()-1) {
            return;
        }
        viewHolder.start.setText(subwayList.get(position));
        viewHolder.end.setText(subwayList.get(position + 1));

    }

    public int getItemCount() {
        return subwayList.size();
    }
}