package org.iklesic.hcshare.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Shared;
import org.iklesic.hcshare.ui.adapter.BoughtAdapter;
import org.iklesic.hcshare.ui.adapter.SoldAdapter;
import org.iklesic.hcshare.ui.listner.SoldClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SoldFragment extends Fragment implements SoldClickListener {

    @BindView(R.id.sold_list)
    RecyclerView soldList;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private SoldAdapter soldAdapter;
    private String user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_sold_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        soldAdapter = new SoldAdapter(this);
        soldList.setLayoutManager(new LinearLayoutManager(getContext()));
        soldList.setAdapter(soldAdapter);

        if (firebaseAuth.getCurrentUser() != null) {

            user = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("shared_items").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e == null) {
                        if (queryDocumentSnapshots != null) {

                            List<Shared> sharedList = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Shared shared = documentSnapshot.toObject(Shared.class);

                                if (shared.getSellerId().equals(user)) {
                                    sharedList.add(shared);
                                }

                            }

                            soldAdapter.setShared(sharedList);

                        }
                    }
                }
            });

        }

    }


    @Override
    public void onSoldClicked(Shared shared) {
        firebaseFirestore.collection("shared_items").document(shared.getId()).set(shared).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), getString(R.string.rating_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
