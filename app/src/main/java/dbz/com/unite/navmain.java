package dbz.com.unite;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class navmain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    user_availability onlineservice;
    notify_userup onlineservice2;
    boolean isBound=false;

    public ViewPager viewPager;
    protected DrawerLayout drawer;
    public TabLayout tabLayout;
    public String[] pageTitle = {"Global", "Contacts", "Search","Your Groups","Search a Group","Create a group"};


    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connectionnot);
        NotificationManager nManager = ((NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancelAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent in=new Intent(this,user_availability.class);
        bindService(in,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            userDetails.username=savedInstanceState.getString("MyName1");
            userDetails.preferred_language=savedInstanceState.getString("preflang1");
            userDetails.of_name=savedInstanceState.getInt("ofName1");
        }
        setContentView(R.layout.activity_navmain);
       // Intent ni=new Intent(this,notify_user.class);

        Intent in=new Intent(this,user_availability.class);
        bindService(in,connection, Context.BIND_AUTO_CREATE);
        Intent in2=new Intent(this,notify_userup.class);
        bindService(in2,connectionnot, Context.BIND_AUTO_CREATE);
        Firebase.setAndroidContext(this);
       // startService(ni);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        viewPager.setOffscreenPageLimit(5);
        setSupportActionBar(toolbar);
        Firebase ref=new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/language");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             userDetails.preferred_language=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 6; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //change ViewPager page when tab selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        startService(new Intent(this,user_availability.class));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fr1) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.fr2) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.fr3) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.go) {
            Intent intent = new Intent(getBaseContext(), facetoface.class);
            startActivity(intent);
        }
        else if (id == R.id.lc) {
            Intent intent = new Intent(getBaseContext(), livechat.class);
            startActivity(intent);
        }
        else if (id == R.id.ygrp) {
          viewPager.setCurrentItem(3);
        }
        else if (id == R.id.sgrp) {
            viewPager.setCurrentItem(4);
        }
        else if (id == R.id.cgrp) {
            viewPager.setCurrentItem(5);
        }
        else if (id == R.id.close) {
            SharedPreferences preferencesuser = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencesuser.edit();
            editor.putString("username_refer","");
            editor.commit();
            SharedPreferences preferenceslog = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
            editor = preferenceslog.edit();
            editor.putInt("login_success",0);
            editor.commit();
            finish();
        }
        else if (id == R.id.signout) {
            SharedPreferences preferencesuser = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencesuser.edit();
            editor.putString("username_refer","");
            editor.commit();
            SharedPreferences preferenceslog = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
            editor = preferenceslog.edit();
            editor.putInt("login_success",0);
            editor.commit();
            Intent intent = new Intent(this, getstarted.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_refresh:finish();
                startActivity(getIntent());break;
            case R.id.action_settings:
                Intent in=new Intent(this,settings.class);
                startActivity(in);break;

        }
        return super.onOptionsItemSelected(item);
    }
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound=true;
            user_availability.LocalBinder binder=(user_availability.LocalBinder) service;
            onlineservice=binder.getService();
            onlineservice.onlineornot();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        isBound=false;
        }
    };
    ServiceConnection connectionnot=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            notify_userup.LocalBinder binder=(notify_userup.LocalBinder) service;
            onlineservice2=binder.getService();
            //onlineservice2.not();

                    }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("MyName1", userDetails.username);
        savedInstanceState.putString("preflang1", userDetails.preferred_language);
        savedInstanceState.putInt("ofName1", userDetails.of_name);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userDetails.username=savedInstanceState.getString("MyName1");
        userDetails.preferred_language=savedInstanceState.getString("preflang1");
        userDetails.of_name=savedInstanceState.getInt("ofName1");
    }
}

