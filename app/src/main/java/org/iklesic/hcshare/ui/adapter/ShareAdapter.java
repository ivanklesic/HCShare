package org.iklesic.hcshare.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.ShareItem;
import org.iklesic.hcshare.ui.listner.ShareClickListener;
import org.iklesic.hcshare.ui.viewholder.ShareViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareViewHolder> {

    private List<ShareItem> shareItemList = new ArrayList<>();
    private ShareClickListener listener;

    public ShareAdapter(ShareClickListener listener) {
        this.listener = listener;
    }

    public void setShareItemList(List<ShareItem> shareItemList) {
        this.shareItemList.clear();
        this.shareItemList.addAll(shareItemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ShareViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_share, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder shareViewHolder, int i) {
        shareViewHolder.setItem(shareItemList.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return shareItemList.size();
    }
}
