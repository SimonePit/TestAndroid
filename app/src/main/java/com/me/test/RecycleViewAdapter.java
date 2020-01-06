package com.me.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<MainActivity.Exercise> dataList;
    private ArrayList<MainActivity.Exercise> dataListFiltered;
    private final OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(MainActivity.Exercise item);
    }

    public RecycleViewAdapter(ArrayList<MainActivity.Exercise> data, OnItemClickListener listener){
        this.dataList = data;
        this.dataListFiltered = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycler_view_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered =dataList;
                } else {
                    ArrayList <MainActivity.Exercise> filteredList = new ArrayList<>();
                    for (MainActivity.Exercise row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<MainActivity.Exercise>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity.Exercise ex = dataListFiltered.get(position);
        holder.bind(dataListFiltered.get(position), listener);
        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(ex.getName());
        Picasso.get().load(ex.getPictureUrl()).into(holder.imageview);
        holder.subTitleTextView.setText(ex.getRepsVerbose());
    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView nameTextView;
        TextView subTitleTextView;
        ImageView imageview;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.title);
            subTitleTextView = (TextView)itemView.findViewById(R.id.subTitle);
            imageview = (ImageView) itemView.findViewById(R.id.imageView);
        }
        void bind(final MainActivity.Exercise item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}
