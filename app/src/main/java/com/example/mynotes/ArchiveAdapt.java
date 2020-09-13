package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static com.example.mynotes.ArchiveActivity.ADate;
import static com.example.mynotes.ArchiveActivity.ATitle;
import static com.example.mynotes.ArchiveActivity.Anotes;
import static com.example.mynotes.MainActivity.ArchiveDate;
import static com.example.mynotes.MainActivity.ArchiveNote;
import static com.example.mynotes.MainActivity.ArchiveTitle;
import static com.example.mynotes.MainActivity.date;
import static com.example.mynotes.MainActivity.notes;

public class ArchiveAdapt extends RecyclerView.Adapter<ArchiveAdapt.viewHolder>  {
    public ArrayList<String> Notes;
    Context context;
    int i=1;

    ArrayList<String>Date;
    public ArrayList<String>Title;

    static int Index;
    SharedPreferences sharedPreferences;
    ArrayList<String>STitle;
    ArrayList<String>SNotes;
    ArrayList<String>SDates;




    public ArchiveAdapt(ArrayList<String> notes, Context context,ArrayList<String> dates,ArrayList<String>title) {
        Notes = notes;
        this.context = context;
        Date=dates;
        Title=title;


    }



    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_adapter_22, parent, false);
        Log.i("reached","customAdapter and title is "+Title.get(0));
        Log.i("reached",Notes.size()+","+Date.size()+"," +Title.size());
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        Log.i("reached","the holder popsition is"+position);

        holder.notesText.setText(Notes.get(position));
        holder.dateText.setText(Date.get(position));
        if(Title.get(position)==""){
            holder.titleText.setText("Title here...");
        }else {
            holder.titleText.setText(Title.get(position));
        }
        holder.expandableLayout.setVisibility(View.GONE);
        final ViewGroup.LayoutParams params=holder.titleText.getLayoutParams();
        holder.titleText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        holder.titleText.setLayoutParams(params);
        holder.titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==1){
                    holder.expandableLayout.setVisibility(View.VISIBLE);
                    holder.titleText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));

                    i=2;
                }
                else{
                    holder.expandableLayout.setVisibility(View.GONE);
                    holder.titleText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                    holder.titleText.setLayoutParams(params);
                    i=1;
                }
            }
        });





        holder.expandableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableLayout.setVisibility(View.VISIBLE);
                holder.titleText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                Index=holder.getAdapterPosition();


                Random random =new Random();
                int color=Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256));
                Intent intent = new Intent(context, ArchiveNotesActivity.class);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
                String newDate;
                newDate=sdf.format(new Date());
                ADate.remove(holder.getAdapterPosition());
                ADate.add(holder.getAdapterPosition(),newDate);
                notifyDataSetChanged();
                sharedPreferences=context.getSharedPreferences("com.example.mynotes", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("Adate",ObjectSerializer.serialize(ADate)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("notesIndex",Index);
                System.out.println(Index);
                System.out.println(ADate);
                System.out.println(Anotes);
                System.out.println(ATitle);

                context.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return Date.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notesText;
        TextView dateText;
        TextView text;
        TextView titleText;
        ConstraintLayout expandableLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            notesText = itemView.findViewById(R.id.notesText);
            dateText=itemView.findViewById(R.id.dateText);
            text=itemView.findViewById(R.id.text);
            titleText=itemView.findViewById(R.id.titleText);
            expandableLayout=itemView.findViewById(R.id.expandableLayout);


        }


        @Override
        public void onClick(View v) {
        }
    }


}