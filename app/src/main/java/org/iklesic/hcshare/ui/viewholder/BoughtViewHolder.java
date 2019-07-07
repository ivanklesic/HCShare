package org.iklesic.hcshare.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Shared;
import org.iklesic.hcshare.ui.listner.BoughtClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoughtViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.item_bought_image)
    ImageView itemBoughtImage;
    @BindView(R.id.item_bought_title)
    TextView itemBoughtTitle;
    @BindView(R.id.item_bought_one)
    ImageView itemBoughtOne;
    @BindView(R.id.item_bought_two)
    ImageView itemBoughtTwo;
    @BindView(R.id.item_bought_three)
    ImageView itemBoughtThree;
    @BindView(R.id.item_bought_four)
    ImageView itemBoughtFour;
    @BindView(R.id.item_bought_five)
    ImageView itemBoughtFive;
    @BindView(R.id.grade_holder)
    RelativeLayout gradeHolder;
    @BindView(R.id.item_bought)
    ConstraintLayout itemBought;

    public BoughtViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    private Shared shared;
    private BoughtClickListener listener;

    public void setItem(Shared shared, BoughtClickListener listener) {
        this.shared = shared;
        this.listener = listener;

        Glide.with(itemBoughtImage).load(shared.getItemImage()).into(itemBoughtImage);

        itemBoughtTitle.setText(shared.getItemTitle());

        if (shared.isSold()) {
            gradeHolder.setVisibility(View.VISIBLE);
        } else {
            gradeHolder.setVisibility(View.GONE);
        }

        float rating = shared.getRating();

        if (rating <= 1) {
            itemBoughtOne.setImageResource(R.drawable.ic_star_full);
        } else if (rating <= 2) {
            itemBoughtOne.setImageResource(R.drawable.ic_star_full);
            itemBoughtTwo.setImageResource(R.drawable.ic_star_full);
        } else if (rating <= 3) {
            itemBoughtOne.setImageResource(R.drawable.ic_star_full);
            itemBoughtTwo.setImageResource(R.drawable.ic_star_full);
            itemBoughtThree.setImageResource(R.drawable.ic_star_full);
        } else if (rating <= 4) {
            itemBoughtOne.setImageResource(R.drawable.ic_star_full);
            itemBoughtTwo.setImageResource(R.drawable.ic_star_full);
            itemBoughtThree.setImageResource(R.drawable.ic_star_full);
            itemBoughtFour.setImageResource(R.drawable.ic_star_full);
        } else if (rating <= 5) {
            itemBoughtOne.setImageResource(R.drawable.ic_star_full);
            itemBoughtTwo.setImageResource(R.drawable.ic_star_full);
            itemBoughtThree.setImageResource(R.drawable.ic_star_full);
            itemBoughtFour.setImageResource(R.drawable.ic_star_full);
            itemBoughtFive.setImageResource(R.drawable.ic_star_full);
        }
    }

    @OnClick(R.id.item_bought_one)
    public void onOneClicked(){
        shared.setRating(1f);
        listener.onBoughtRatingClicked(shared);
    }

    @OnClick(R.id.item_bought_two)
    public void onTwoClicked(){
        shared.setRating(2f);
        listener.onBoughtRatingClicked(shared);
    }

    @OnClick(R.id.item_bought_three)
    public void onThreeClicked(){
        shared.setRating(3f);
        listener.onBoughtRatingClicked(shared);
    }

    @OnClick(R.id.item_bought_four)
    public void onFourClicked(){
        shared.setRating(4f);
        listener.onBoughtRatingClicked(shared);
    }

    @OnClick(R.id.item_bought_five)
    public void onFiveClicked(){
        shared.setRating(5f);
        listener.onBoughtRatingClicked(shared);
    }

}
