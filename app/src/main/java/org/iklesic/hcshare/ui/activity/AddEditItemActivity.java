package org.iklesic.hcshare.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Category;
import org.iklesic.hcshare.model.ShareItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditItemActivity extends AppCompatActivity {

    public static final String SHARE_ITEM_EXTRA = "share_item_extra";

    private static final int CAMERA_CODE = 100;
    private static final int GALLERY_CODE = 200;

    @BindView(R.id.add_item_title)
    EditText addItemTitle;
    @BindView(R.id.add_item_description)
    EditText addItemDescription;
    @BindView(R.id.add_item_category)
    Spinner addItemCategory;
    @BindView(R.id.add_item_image)
    ImageView addItemImage;
    @BindView(R.id.add_image_camera)
    ImageView addImageCamera;
    @BindView(R.id.add_image_gallery)
    ImageView addImageGallery;
    @BindView(R.id.add_item_add)
    RelativeLayout addItemAdd;

    private String photo = null;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private StorageReference reference;
    private List<Category> categoryList = new  ArrayList<>();
    private LocationRequest locationRequest = new LocationRequest();
    private FusedLocationProviderClient locationProviderClient;
    private LocationCallback locationCallback;
    private Task task;
    private Location userLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);

        getGeolocation();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("images");

        List<String> categories = new ArrayList<>();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        addItemCategory.setAdapter(spinnerAdapter);

        database.collection("categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e == null){
                    if (queryDocumentSnapshots != null) {
                        categoryList.clear();
                        for (QueryDocumentSnapshot item : queryDocumentSnapshots
                        ) {
                            Category category = item.toObject(Category.class);
                            categoryList.add(category);
                        }

                        categories.clear();
                        for (Category category: categoryList
                             ) {
                            categories.add(category.getTitle());
                        }

                        spinnerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(SHARE_ITEM_EXTRA) != null) {
            ShareItem shareItem = (ShareItem) getIntent().getExtras().getSerializable(SHARE_ITEM_EXTRA);

            if (shareItem != null) {
                addItemTitle.setText(shareItem.getTitle());
                addItemDescription.setText(shareItem.getDescription());

                Glide.with(addItemImage).load(shareItem.getImage()).into(addItemImage);
            }
        }

    }

    @OnClick(R.id.add_image_camera)
    public void onCameraClicked() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String timestamp = new SimpleDateFormat("yyyMMDD_HHmmss").format(new Date());
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photo = null;
        try {
            photo = File.createTempFile("SHARE_" + timestamp + "_", ".jpg", storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photo != null) {
            this.photo = photo.getAbsolutePath();

            Uri photoUri = FileProvider.getUriForFile(this, "org.iklesic.hcshare.fileprovider", photo);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            startActivityForResult(intent, CAMERA_CODE);
        }
    }

    @OnClick(R.id.add_image_gallery)
    public void onGalleryClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @OnClick(R.id.add_item_add)
    public void onAddItemClicked() {
        String id = UUID.randomUUID().toString();
        String title = addItemTitle.getText().toString();
        String description = addItemDescription.getText().toString();
        final String image = UUID.randomUUID().toString();
        String category = addItemCategory.getSelectedItem().toString();

        for (Category cat: categoryList
             ) {
            if (cat.getTitle().equals(category)){
                category = cat.getId();
                break;
            }
        }

        if (title.length() != 0 && description.length() != 0 && photo != null && photo.length() != 0 && auth.getCurrentUser() != null) {

            Bitmap bitmap = ((BitmapDrawable) addItemImage.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = reference.child(image).putBytes(data);
            String finalCategory = category;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.add_item_failed_text), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.child(image).getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.add_item_failed_text), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            ShareItem shareItem;

                            if (userLocation != null) {
                                shareItem = new ShareItem(id, title, finalCategory, description, uri.toString(), 0f, auth.getCurrentUser().getUid(), userLocation.getLatitude(), userLocation.getLongitude());
                            }else {
                                shareItem = new ShareItem(id, title, finalCategory, description, uri.toString(), 0f, auth.getCurrentUser().getUid(), 0, 0);

                            }

                            database.collection("share_items").document(id).set(shareItem).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.add_item_failed_text), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                        }
                    });
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.add_item_data_missing), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                photo = data.getData().toString();

                addItemImage.setImageURI(Uri.parse(photo));
            }
        }

        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            addItemImage.setImageURI(Uri.parse(photo));
        }
    }

    private void getGeolocation(){
        locationRequest.setInterval(60*1000);
        locationRequest.setMaxWaitTime(2*60*1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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

                        locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult != null){
                                    for (Location loc:locationResult.getLocations()
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

        }else {
            String[] permissions = new String[1];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && permissions.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getGeolocation();
        }

    }
}
