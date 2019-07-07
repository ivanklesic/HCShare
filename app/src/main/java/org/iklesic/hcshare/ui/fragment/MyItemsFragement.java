package org.iklesic.hcshare.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.ShareItem;
import org.iklesic.hcshare.ui.activity.AddEditItemActivity;
import org.iklesic.hcshare.ui.adapter.MyItemsAdapter;
import org.iklesic.hcshare.ui.listner.MyItemsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyItemsFragement extends Fragment implements MyItemsListener {


    @BindView(R.id.my_items_list)
    RecyclerView myItemsList;
    @BindView(R.id.add_item)
    FloatingActionButton addItem;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private MyItemsAdapter myItemsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        myItemsAdapter = new MyItemsAdapter(this);
        myItemsList.setAdapter(myItemsAdapter);
        myItemsList.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseFirestore.collection("share_items").whereEqualTo("owner", firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e == null) {
                        List<ShareItem> myItems = new ArrayList<>();

                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot item : queryDocumentSnapshots
                            ) {
                                ShareItem shareItem = item.toObject(ShareItem.class);
                                myItems.add(shareItem);
                            }
                        }

                        myItemsAdapter.setItems(myItems);
                    }
                }
            });
        }

    }

    @OnClick(R.id.add_item)
    public void onAddClicked() {
        Intent intent = new Intent(getContext(), AddEditItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(ShareItem shareItem) {
        Intent intent = new Intent(getContext(), AddEditItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AddEditItemActivity.SHARE_ITEM_EXTRA, shareItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemLongClicked(ShareItem shareItem) {
        firebaseFirestore.collection("share_items").document(shareItem.getId()).delete();
    }
}
