package ug.karuhanga.journally.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ug.karuhanga.journally.R;
import ug.karuhanga.journally.models.Entry;

public class Adapter extends RecyclerView.Adapter<Adapter.EntryViewHolder> {

    private List<Entry> entries;
    private OnEntryClickListener mListener;

    public Adapter(List<Entry> entries, OnEntryClickListener mListener) {
        this.entries = entries;
        this.mListener = mListener;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_view, parent, false));
    }

    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.update(getItem(position));
        holder.setViewClickListener(this.mListener, getItem(position));
    }

    private Entry getItem(int position) {
        return this.entries.get(position);
    }

    @Override
    public int getItemCount() {
        return this.entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {
        View entryView;
        TextView title;

        public EntryViewHolder(View itemView) {
            super(itemView);
            entryView = itemView;
            title = itemView.findViewById(R.id.text_view_entry_title);
        }

        public void update(Entry entry) {
            this.title.setText(entry.getTitle());
        }

        public void setViewClickListener(final OnEntryClickListener listener, final Entry entry) {
            this.entryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onEntryClicked(entry.getPk());
                }
            });
        }
    }
}
