package com.example.finalcut.tests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.finalcut.R;
import com.example.finalcut.classes.Ticket;
import com.example.finalcut.recycleview.MyAdapter;
import com.example.finalcut.interfaces.RecycleViewInterface;

import java.util.ArrayList;

public class BookedTicketList extends AppCompatActivity implements RecycleViewInterface {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private ArrayList<Ticket> recycleList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_ticket_list);
        recyclerView = findViewById(R.id.bookedRecycler);
        myAdapter =new MyAdapter(getApplicationContext(), recycleList, BookedTicketList.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);


        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onRemove(int position) {

    }
}