package hu.pimpi.enekek.songs;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.pimpi.enekek.R;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

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
        SongItem item = list.get(listPosition);

        holder.title.setText(Html.fromHtml(item.getTitle()));
        if(item.getLyrics() != null && !item.getLyrics().isEmpty()) {
            holder.lyrics.setText(Html.fromHtml(item.getLyrics()));
            holder.lyrics.setVisibility(View.VISIBLE);
        } else {
            holder.lyrics.setVisibility(View.GONE);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView lyrics;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            title = itemView.findViewById(R.id.item_song_title);
            lyrics = itemView.findViewById(R.id.item_song_lyrics);
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

    public ArrayList<SongItem> getList() {
        return list;
    }

    public void showResults(List<SongItem> results) {
        list.clear();
        list.addAll(results);
        notifyDataSetChanged();
    }

    public void resetList() {
        showResults(fullList);
    }
}