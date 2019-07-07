package org.iklesic.hcshare.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Category;
import org.iklesic.hcshare.model.ShareItem;
import org.iklesic.hcshare.ui.adapter.ShareAdapter;
import org.iklesic.hcshare.ui.listner.ShareClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends AppCompatActivity implements ShareClickListener {

    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.search_background)
    RelativeLayout searchBackground;
    @BindView(R.id.my_items)
    Button myItems;
    @BindView(R.id.share_list)
    RecyclerView shareList;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.share_category)
    Spinner shareCategory;
    @BindView(R.id.share_category_card)
    CardView shareCategoryCard;
    @BindView(R.id.distance)
    EditText distance;
    @BindView(R.id.search_distance_background)
    ConstraintLayout searchDistanceBackground;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ShareAdapter shareAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private Category currentCategory = new Category("All", "All");
    private List<Category> categoryList = new ArrayList<>();
    private List<String> categoryTitleList = new ArrayList<>();
    private String searchTerm = "";
    private LocationRequest locationRequest = new LocationRequest();
    private FusedLocationProviderClient locationProviderClient;
    private LocationCallback locationCallback;
    private Task task;
    private List<ShareItem> shareItems = new ArrayList<>();
    private Location userLocation;
    float userDistance = 50000000;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        getGeolocation();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        shareAdapter = new ShareAdapter(this);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryTitleList);
        shareCategory.setAdapter(arrayAdapter);

        shareList.setLayoutManager(new LinearLayoutManager(this));
        shareList.setAdapter(shareAdapter);

        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
        }

        firebaseFirestore.collection("categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {

                    categoryList.clear();

                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot document :
                                queryDocumentSnapshots) {
                            Category category = document.toObject(Category.class);
                            categoryList.add(category);
                        }

                        categoryTitleList.clear();

                        for (Category c : categoryList) {
                            categoryTitleList.add(c.getTitle());
                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        firebaseFirestore.collection("share_items").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {

                    if (queryDocumentSnapshots != null) {
                        shareItems.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots
                        ) {
                            ShareItem shareItem = document.toObject(ShareItem.class);
                            shareItems.add(shareItem);
                        }
                        getItems();
                    }
                }
            }
        });
    }


    private void getItems() {
        List<ShareItem> items = new ArrayList<>();
        float itemDistance = 0;

        for (ShareItem shareItem : shareItems
        ) {

            if (userLocation != null) {
                Location locationUser = new Location("user");
                Location locationSeller = new Location("Seller");

                locationUser.setLatitude(userLocation.getLatitude());
                locationUser.setLongitude(userLocation.getLongitude());

                locationSeller.setLatitude(shareItem.getLat());
                locationSeller.setLongitude(shareItem.getLng());

                itemDistance = locationUser.distanceTo(locationSeller) / 1000;
            }


            if (currentCategory.getId().equals("All")) {
                if (searchTerm.length() == 0) {
                    if (!shareItem.getOwner().equals(userId) && itemDistance < userDistance) {
                        items.add(shareItem);
                    }
                } else {
                    if (!shareItem.getOwner().equals(userId) && shareItem.getTitle().contains(searchTerm) && itemDistance < userDistance) {
                        items.add(shareItem);
                    }
                }
            } else {
                if (searchTerm.length() == 0) {
                    if (!shareItem.getOwner().equals(userId) && shareItem.getCategory().equals(currentCategory.getId()) && itemDistance < userDistance) {
                        items.add(shareItem);
                    }
                } else {
                    if (!shareItem.getOwner().equals(userId) && shareItem.getCategory().equals(currentCategory.getId()) && shareItem.getTitle().contains(searchTerm) && itemDistance < userDistance) {
                        items.add(shareItem);
                    }
                }
            }
        }

        shareAdapter.setShareItemList(items);
    }

    @OnClick(R.id.my_items)
    public void onMyItemsClicked() {

    }

    @OnClick(R.id.search_button)
    public void onSearchClicked() {

        searchTerm = search.getText().toString();
        if (distance.getText().toString().length() != 0) {
            userDistance = Float.parseFloat(distance.getText().toString());
        }

        String selCat = (String) shareCategory.getSelectedItem();

        for (Category c :
                categoryList) {
            if (selCat.equals(c.getTitle())) {
                currentCategory = c;
                break;
            }
        }

        getItems();
    }

    @Override
    public void onShareItemClicked(ShareItem shareItem) {

    }

    private void getGeolocation() {
        locationRequest.setInterval(60 * 1000);
        locationRequest.setMaxWaitTime(2 * 60 * 1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            task = LocationServices.getSettingsClient(this).checkLocationSettings(new LocationSettingsRequest.Builder().build());
            task.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    userLocation = location;
                                }
                            }
                        });

                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult != null) {
                                    for (Location loc : locationResult.getLocations()
                                    ) {
                                        userLocation = loc;
                                    }
                                }
                            }
                        };

                        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }
            });

        } else {
            String[] permissions = new String[1];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && permissions.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getGeolocation();
        }

    }
}
