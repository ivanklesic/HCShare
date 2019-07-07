package org.iklesic.hcshare.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Shared;
import org.iklesic.hcshare.ui.listner.BoughtClickListener;
import org.iklesic.hcshare.ui.viewholder.BoughtViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BoughtAdapter extends RecyclerView.Adapter<BoughtViewHolder> {

    private List<Shared> shared = new ArrayList<>();
    private BoughtClickListener listener;

    public BoughtAdapter(BoughtClickListener listener) {
        this.listener = listener;
    }

    public void setShared(List<Shared> shared) {
        this.shared.clear();
        this.shared.addAll(shared);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BoughtViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BoughtViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bought, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BoughtViewHolder boughtViewholder, int i) {
        boughtViewholder.setItem(shared.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return shared.size();
    }
}