package org.iklesic.hcshare.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.ShareItem;
import org.iklesic.hcshare.model.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemDetailsActivity extends AppCompatActivity {

    public static final String ITEM_DETAILS_EXTRA = "item_details_extra";

    @BindView(R.id.item_details_title)
    TextView itemDetailsTitle;
    @BindView(R.id.item_details_one)
    ImageView itemDetailsOne;
    @BindView(R.id.item_details_two)
    ImageView itemDetailsTwo;
    @BindView(R.id.item_details_three)
    ImageView itemDetailsThree;
    @BindView(R.id.item_details_four)
    ImageView itemDetailsFour;
    @BindView(R.id.item_details_five)
    ImageView itemDetailsFive;
    @BindView(R.id.item_details_image)
    ImageView itemDetailsImage;
    @BindView(R.id.item_details_chat)
    Button itemDetailsChat;
    @BindView(R.id.item_details_description)
    TextView itemDetailsDescription;
    @BindView(R.id.item_details_buy)
    Button itemDetailsBuy;

    private ShareItem shareItem;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(ITEM_DETAILS_EXTRA) != null) {
            shareItem = (ShareItem) getIntent().getExtras().getSerializable(ITEM_DETAILS_EXTRA);

            if (shareItem != null) {

                itemDetailsTitle.setText(shareItem.getTitle());
                itemDetailsDescription.setText(shareItem.getDescription());
                Glide.with(itemDetailsImage).load(shareItem.getImage()).into(itemDetailsImage);

                float rating = shareItem.getRating();

                firebaseFirestore.collection("shared_items").whereEqualTo("itemId", shareItem.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e == null){
                            if (queryDocumentSnapshots != null){

                                List<Shared> sharedList = new ArrayList<>();

                                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){

                                    Shared shared = doc.toObject(Shared.class);

                                    sharedList.add(shared);

                                }

                                float sum = 0;

                                for (Shared s :
                                        sharedList) {
                                    sum += s.getRating();
                                }

                                float rating = sum/sharedList.size();

                                if (rating <= 1) {
                                    itemDetailsOne.setImageResource(R.drawable.ic_star_full);
                                } else if (rating <= 2) {
                                    itemDetailsOne.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsTwo.setImageResource(R.drawable.ic_star_full);
                                } else if (rating <= 3) {
                                    itemDetailsOne.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsTwo.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsThree.setImageResource(R.drawable.ic_star_full);
                                } else if (rating <= 4) {
                                    itemDetailsOne.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsTwo.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsThree.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsFour.setImageResource(R.drawable.ic_star_full);
                                } else if (rating <= 5) {
                                    itemDetailsOne.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsTwo.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsThree.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsFour.setImageResource(R.drawable.ic_star_full);
                                    itemDetailsFive.setImageResource(R.drawable.ic_star_full);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @OnClick(R.id.item_details_chat)
    public void onChatClicked() {
        Intent intent = new Intent(this, MessagesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MessagesActivity.RECEIVER_ID_EXTRA, shareItem.getOwner());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.item_details_buy)
    public void onBuyClciked() {
        if (firebaseAuth.getCurrentUser() != null){

            String id= UUID.randomUUID().toString();
            String user =firebaseAuth.getCurrentUser().getUid();

            Shared shared = new Shared(id, shareItem.getOwner(), user, shareItem.getId(), shareItem.getImage(), shareItem.getTitle(),false, Timestamp.now());

            firebaseFirestore.collection("shared_items").document(id).set(shared).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.buy_failed), Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            Toast.makeText(this, getString(R.string.buy_failed), Toast.LENGTH_SHORT).show();
        }
    }
}
