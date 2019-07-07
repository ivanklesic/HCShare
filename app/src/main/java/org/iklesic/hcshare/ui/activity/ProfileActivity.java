package org.iklesic.hcshare.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.ui.fragment.BoughtFragment;
import org.iklesic.hcshare.ui.fragment.ChatsFragment;
import org.iklesic.hcshare.ui.fragment.MyItemsFragement;
import org.iklesic.hcshare.ui.fragment.SoldFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_fragment_holder)
    FrameLayout profileFragmentHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initMyItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_my_items: {
                initMyItems();
                break;
            }

            case R.id.menu_sold_items: {
                initSoldItems();
                break;
            }

            case R.id.menu_bought_items: {
                initBoughtItems();
                break;
            }

            case R.id.menu_messages: {
                initMessages();
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void initMyItems() {
        MyItemsFragement myItemsFragement = new MyItemsFragement();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_holder, myItemsFragement).commit();
    }

    private void initSoldItems() {
        SoldFragment soldFragment = new SoldFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_holder, soldFragment).commit();
    }

    private void initBoughtItems() {
        BoughtFragment boughtFragment = new BoughtFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_holder, boughtFragment).commit();
    }

    private void initMessages() {
        ChatsFragment chatsFragment = new ChatsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_holder, chatsFragment).commit();
    }
}
