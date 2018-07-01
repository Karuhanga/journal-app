package ug.karuhanga.journally.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;
import ug.karuhanga.journally.R;
import ug.karuhanga.journally.models.Database;
import ug.karuhanga.journally.models.Entry;

public class Adapter extends RecyclerView.Adapter<Adapter.EntryViewHolder> implements RVHAdapter {

    private List<Entry> entries;
    private EntryInteractionListener mListener;
    private int SWIPE_RIGHT = 32;
    private int SWIPE_LEFT = 16;
    private List<Entry> data;

    public Adapter(List<Entry> entries, EntryInteractionListener mListener) {
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

    @Override
    public void onItemDismiss(int position, int direction) {
        if (direction == SWIPE_RIGHT) {
            mListener.areYouSure(getItem(position), position);
        } else {
            refreshView(position);
//            mListener.share(getItem(position));
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        // TODO
        return false;
    }

    public void completeDelete(Entry entry, int position) {
        this.entries.remove(position);
        Database.getDb(mListener.requestContext()).getEntryDao().deleteEntry(entry);
        mListener.mNotifyItemRemoved(position);
    }

    public void refreshView(int position) {
        notifyItemChanged(position);
    }

    public void setData(List<Entry> data) {
        this.data = data;
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder {
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

        public void setViewClickListener(final EntryInteractionListener listener, final Entry entry) {
            this.entryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onEntryClicked(entry.getPk());
                }
            });
        }

        @Override
        public void onItemClear() {
            //  TODO
        }

        @Override
        public void onItemSelected(int actionstate) {
            // TODO
        }
    }
}
