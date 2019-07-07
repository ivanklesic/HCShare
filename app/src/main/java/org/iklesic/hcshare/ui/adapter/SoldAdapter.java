package org.iklesic.hcshare.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Shared;
import org.iklesic.hcshare.ui.listner.SoldClickListener;
import org.iklesic.hcshare.ui.viewholder.SoldViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SoldAdapter extends RecyclerView.Adapter<SoldViewHolder> {

    private List<Shared> shared = new ArrayList<>();
    private SoldClickListener listener;

    public SoldAdapter(SoldClickListener listener) {
        this.listener = listener;
    }

    public void setShared(List<Shared> shared) {
        this.shared.clear();
        this.shared.addAll(shared);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SoldViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SoldViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sold, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SoldViewHolder soldViewHolder, int i) {
        soldViewHolder.setItem(shared.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return shared.size();
    }
}
