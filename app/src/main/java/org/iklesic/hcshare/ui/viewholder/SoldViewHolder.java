package org.iklesic.hcshare.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Shared;
import org.iklesic.hcshare.ui.listner.SoldClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoldViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_sold_image)
    ImageView itemSoldImage;
    @BindView(R.id.item_sold_image_title)
    TextView itemSoldImageTitle;
    @BindView(R.id.item_sold_is_sold)
    CheckBox itemSoldIsSold;
    @BindView(R.id.item_sold)
    ConstraintLayout itemSold;

    public SoldViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    private Shared shared;
    private SoldClickListener listener;

    public void setItem(Shared shared, SoldClickListener listener){
        this.shared = shared;
        this.listener = listener;

        Glide.with(itemSoldImage).load(shared.getItemImage()).into(itemSoldImage);

        itemSoldImageTitle.setText(shared.getItemTitle());

        itemSoldIsSold.setChecked(shared.isSold());
    }

    @OnClick(R.id.item_sold_is_sold)
    public void onIsSoldClicked(){
        shared.setSold(true);
        itemSoldIsSold.setChecked(true);
        listener.onSoldClicked(shared);
    }
}
