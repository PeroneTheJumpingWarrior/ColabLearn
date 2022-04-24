package com.example.qmul_itmbrevision;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewAdapter>{

    // onCreateViewHolder, onBindViewHolder, getItemCount methods had to be overridden in order to use the ScoreAdapter Class

    List<ScoreData> list;  //A List variable is declared with type (ScoreData) to extract & store the user data from the List
    Context context;
    int i = 1;     //This is being made for Rank (See onBindViewHolder() method below)

    public ScoreAdapter(List<ScoreData> list, Context context){

        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ScoreViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.score_list_item, parent, false);  //For ScoreAdapter we have set its
        //layout to score_list_item
        return new ScoreViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewAdapter holder, int position) {

        ScoreData currentItem = list.get(position);

        holder.name.setText(currentItem.getName());  //As name is String no problem in setting the text of holder as value of "name"


        holder.score.setText(String.valueOf(currentItem.getScore()));  //As getScore(0 returns an integer thus we have to do String.valueOf here

        holder.rank.setText(String.valueOf(i));

        i++;  //i++ indicates that the rank increases for user as RecyclerView goes down

        //You Could have added Glide here but it is used for setting up the Image for a User

    }

    @Override
    public int getItemCount() {

        return list.size();  //Item Count in the RecyclerView equals the Size of the List (which is the number of users registered in the program)
    }

    public class ScoreViewAdapter extends RecyclerView.ViewHolder{

        TextView name;
        TextView score;
        TextView rank;

        public ScoreViewAdapter(@NonNull View itemView){

            super(itemView);

            name = itemView.findViewById(R.id.score_user_name);
            score = itemView.findViewById(R.id.score_user_result);
            rank = itemView.findViewById(R.id.score_user_rank);

            //If we want to set a value in each of these variables then in the onBindViewHolder() method we need to get the current item
        }
    }
}
