package com.example.finalcut;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.finalcut.classes.Ticket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TicketSaver {
    private SharedPreferences preferences;
    private Gson gson;

    public TicketSaver(Context context,String s) {
        preferences = context.getSharedPreferences(s,Context.MODE_PRIVATE);
        gson = new Gson();

    }

    public void saveTickets(ArrayList<Ticket> tickets){
             SharedPreferences.Editor editor = preferences.edit();
             editor.putString("tickets",gson.toJson(tickets));
             editor.apply();
    }
    public ArrayList<Ticket> getTickets(){

      String str =  preferences.getString("tickets",null);
      Type ticketListType = new TypeToken<ArrayList<Ticket>>(){}.getType();
      ArrayList<Ticket> tickets = gson.fromJson(str, ticketListType);
      if (tickets!=null){
          return tickets;
      }
      return new ArrayList<>();
    }
    public void clearPreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
    }
}
