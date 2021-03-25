package com.example.runningtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLogAdapter extends RecyclerView.Adapter<ExerciseLogAdapter.ExerciseLogViewHolder> {
    private List<ExerciseLog> data;
    private Context context;
    private LayoutInflater layoutInflater;

    //Constructor for the adapter that creates data, context and layout inflater
    ExerciseLogAdapter(Context context){
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Called creation of viewholder, links to xml view file and returns the viewholder
    @NonNull
    @Override
    public ExerciseLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.log_layout_view,parent,false);
        return new ExerciseLogViewHolder(itemView);
    }

    //Used to bind the data
    @Override
    public void onBindViewHolder(@NonNull ExerciseLogViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    //Returns the number of elements in the data set
    @Override
    public int getItemCount() {
        return data.size();
    }

    //Sets the data to that returned by the dao
    public void setData(List<ExerciseLog> newData) {
        if(data!=null){
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }else{
            data = newData;
        }
    }

    //Fills all the different views with their respective information
    class ExerciseLogViewHolder extends RecyclerView.ViewHolder{
        TextView idView;
        TextView dateView;
        TextView distanceView;
        TextView timeView;
        TextView speedView;
        TextView topSpeedView;


        public ExerciseLogViewHolder(@NonNull View itemView) {

            super(itemView);
            idView = itemView.findViewById(R.id.idView);
            dateView = itemView.findViewById(R.id.dateView);
            distanceView = itemView.findViewById(R.id.distanceView);
            timeView = itemView.findViewById(R.id.timeView);
            speedView = itemView.findViewById(R.id.speedView);
            topSpeedView = itemView.findViewById(R.id.topSpeedView);
        }

        void bind(final ExerciseLog exerciseLog){
            if(exerciseLog != null){

                //Setting all text views to values from the database
                idView.setText(""+exerciseLog.id);
                dateView.setText(exerciseLog.getDate());
                timeView.setText(+exerciseLog.getTime()+" s");
                distanceView.setText(+exerciseLog.getDistance()+" m");
                speedView.setText("Avg. Speed "+ exerciseLog.getSpeed()+" km/h");
                topSpeedView.setText("Top Speed "+ exerciseLog.getTopSpeed()+" km/h");

            }
        }
    }
}
