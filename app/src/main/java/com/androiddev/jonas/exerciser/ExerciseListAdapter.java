package com.androiddev.jonas.exerciser;

import android.content.Context;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jonas on 14.07.16.
 */
public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>{

    private ArrayList<ExerciseListItem> listData;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public ExerciseListAdapter(ArrayList<ExerciseListItem> items, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listData = items;
    }

    @Override
    public ExerciseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.exercise_list_item, parent, false);
        return new ExerciseListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ExerciseListViewHolder holder, int position) {
        ExerciseListItem item = listData.get(position);
        // Set title on list item
        holder.title.setText(item.getTitle());
        // Set subtitle on list item
        holder.description.setText(item.getSubTitle());

        //holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    /** ViewHolder **/
    class ExerciseListViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private Button editButton;
        private View container; // The linearlayout
        private OnItemClickListener listener;

        public ExerciseListViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.ExerciseListItem_text);
            description = (TextView) itemView.findViewById(R.id.ExerciseListItem_subText);
            editButton = (Button) itemView.findViewById(R.id.ExerciseListItem_editButton);
            container = itemView.findViewById(R.id.ExerciseListItem_root);

            this.listener = l;

            // Add onClickListener to edit button
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExerciseListItem item = listData.get(getAdapterPosition());
                    if(listener != null) {
                        listener.onItemClick(item);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(ExerciseListItem item);
    }

    public void setListData(ArrayList<ExerciseListItem> items) {
        this.listData = items;
    }

}
