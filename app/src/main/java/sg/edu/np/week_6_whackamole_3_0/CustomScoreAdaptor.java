package sg.edu.np.week_6_whackamole_3_0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    private Context currentContext;
    private UserData userdata;
    private ArrayList<Integer> level_list;
    private ArrayList<Integer> score_list;

    public CustomScoreAdaptor(UserData userdata, Context c){
        this.userdata = userdata;
        this.level_list = userdata.getLevels();
        this.score_list = userdata.getScores();
        this.currentContext = c;
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select,parent,false);
        return new CustomScoreViewHolder(item);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){

        holder.level.setText("Level " + level_list.get(position).toString());
        holder.score.setText("Highest Score: " + score_list.get(position).toString());

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + userdata.getMyUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(currentContext,Main4Activity.class);
                i.putExtra("level",level_list.get(position));
                i.putExtra("currentUser",userdata.getMyUserName());
                currentContext.startActivity(i);
            }
        });

    }

    public int getItemCount(){
        return score_list.size();
    }
}