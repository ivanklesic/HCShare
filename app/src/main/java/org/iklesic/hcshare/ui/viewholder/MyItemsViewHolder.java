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
import org.iklesic.hcshare.ui.listner.MyItemsListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MyItemsViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.item_my_image)
    ImageView itemMyImage;
    @BindView(R.id.item_my_title)
    TextView itemMyTitle;
    @BindView(R.id.item_my_arrow)
    ImageView itemMyArrow;
    @BindView(R.id.my_item)
    ConstraintLayout myItem;

    private MyItemsListener listener;
    private ShareItem item;

    public MyItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(ShareItem shareItem, MyItemsListener listener) {
        item = shareItem;
        itemMyTitle.setText(shareItem.getTitle());
        Glide.with(itemMyImage).load(shareItem.getImage()).into(itemMyImage);
        this.listener = listener;
    }

    @OnClick(R.id.my_item)
    public void onClick(){
        listener.onItemClicked(item);
    }

    @OnLongClick(R.id.my_item)
    public boolean onLongClick(){
        listener.onItemLongClicked(item);
        return true;
    }
}
