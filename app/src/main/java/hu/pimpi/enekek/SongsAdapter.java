package hu.pimpi.enekek;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> implements Filterable {

    private ArrayList<SongItem> list;
    private ArrayList<SongItem> fullList;

    private OnItemClickListener onItemClickListener;

    public SongsAdapter(ArrayList<SongItem> list) {
        this.list = list;
        this.fullList = new ArrayList<>(list);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("onClick", "click");
            }
        });
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        holder.title.setText(list.get(listPosition).getTitle());
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence pattern) {
            List<SongItem> filteredList = new ArrayList<>();

            if(pattern == null || pattern.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                for(SongItem item : fullList) {
                    if(item.getTitle().toLowerCase().replaceAll("[^a-z0-9]", "")
                            .contains(pattern.toString().toLowerCase().replaceAll("[^a-z0-9]", "")))
                        filteredList.add(item);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            title = itemView.findViewById(R.id.item_song_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(onItemClickListener != null)
                onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public ArrayList<SongItem> getFullList() {
        return fullList;
    }

    public void setFullList(ArrayList<SongItem> fullList) {
        this.fullList = fullList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}