package org.iklesic.hcshare.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.ShareItem;
import org.iklesic.hcshare.ui.listner.ShareClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_share_image)
    ImageView itemShareImage;
    @BindView(R.id.item_share_title)
    TextView itemShareTitle;
    @BindView(R.id.item_share_arrow)
    ImageView itemShareArrow;
    @BindView(R.id.item_share)
    ConstraintLayout itemShare;

    private ShareItem shareItem;
    private ShareClickListener listener;

    public ShareViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(ShareItem shareItem, ShareClickListener listener){
        this.shareItem = shareItem;
        this.listener = listener;

        Glide.with(itemShareImage).load(shareItem.getImage()).into(itemShareImage);
        itemShareTitle.setText(shareItem.getTitle());
    }

    @OnClick(R.id.item_share)
    public void onItemClicked(){
        listener.onShareItemClicked(shareItem);
    }


}
