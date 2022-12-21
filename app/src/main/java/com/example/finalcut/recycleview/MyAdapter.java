package com.example.finalcut.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalcut.R;
import com.example.finalcut.classes.Ticket;
import com.example.finalcut.interfaces.RecycleViewInterface;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> {
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    ArrayList<Ticket> list;

    public MyAdapter(Context context, ArrayList<Ticket> list, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.list = list;
        this.recycleViewInterface = recycleViewInterface;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,list.get(viewType).getServiceName() , Toast.LENGTH_SHORT).show();
            }
        });
        return new viewHolder(v,recycleViewInterface);

    }
    public void setNewList(ArrayList<Ticket> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
      Ticket ticket = list.get(position);
      holder.serviceName.setText(ticket.getServiceName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;

        public viewHolder(@NonNull View itemView,RecycleViewInterface recycleViewInterface) {
            super(itemView);
        serviceName = itemView.findViewById(R.id.serviceName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recycleViewInterface!=null){
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        recycleViewInterface.onItemClick(position);
                    }
                }
            }
        });
        }


    }
}
