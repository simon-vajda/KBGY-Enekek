package hu.pimpi.enekek.lyrics;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.pimpi.enekek.R;

public class LyricsAdapter extends RecyclerView.Adapter<LyricsAdapter.ViewHolder> {

    private Context context;
    private List<Verse> list;

    public LyricsAdapter(Context context, List<Verse> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        Verse.Type type = list.get(listPosition).getType();
        String headerText;

        TypedValue typedValue = new TypedValue();
        int color;

        if(type == Verse.Type.BRIDGE) {
            headerText = context.getString(R.string.bridge);
            context.getTheme().resolveAttribute(R.attr.colorBridge, typedValue, true);
            color = typedValue.data;
        }
        else if(type == Verse.Type.CHORUS) {
            headerText = context.getString(R.string.chorus);
            context.getTheme().resolveAttribute(R.attr.colorChorus, typedValue, true);
            color = typedValue.data;
        }
        else {
            headerText = context.getString(R.string.verse);
            context.getTheme().resolveAttribute(R.attr.colorVerse, typedValue, true);
            color = typedValue.data;
        }

        holder.header.setBackgroundColor(color);
        holder.header.setText(headerText);
        holder.text.setText(list.get(listPosition).getText());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView header;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.lyrics_header);
            text = itemView.findViewById(R.id.lyrics_text);
        }
    }
}