package org.iklesic.hcshare.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.ShareItem;
import org.iklesic.hcshare.ui.listner.MyItemsListener;
import org.iklesic.hcshare.ui.viewholder.MyItemsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MyItemsAdapter extends RecyclerView.Adapter<MyItemsViewHolder> {

    private List<ShareItem> myItems = new ArrayList<>();
    private MyItemsListener listener;

    public MyItemsAdapter(MyItemsListener listener){
        this.listener = listener;
    }

    public void setItems(List<ShareItem> myItems){
        this.myItems.clear();
        this.myItems.addAll(myItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemsViewHolder myItemsViewHolder, int i) {
        myItemsViewHolder.setItem(myItems.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }
}
