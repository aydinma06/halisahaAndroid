package halisahaandroid.mammy.halisahaandroid.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import halisahaandroid.mammy.halisahaandroid.fragment.AppointmentFragment;
import halisahaandroid.mammy.halisahaandroid.fragment.ChooseFieldFragment;
import halisahaandroid.mammy.halisahaandroid.fragment.EditUserInfosFragment;
import halisahaandroid.mammy.halisahaandroid.R;

public class MainMenuActivity extends AppCompatActivity  {

    SNavigationDrawer sNavigationDrawer;
    int color1=0;
    Class fragmentClass;
    public static Fragment fragment;
    int myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }

        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItemClass> menuItems = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        myID = preferences.getInt("userID", 0);
        if(myID==0)
        {
        menuItems.add(new MenuItemClass("Halı Saha Sorgulama",R.drawable.news_bg));
        //menuItems.add(new MenuItemClass("Kullanıcı Bilgileri",R.drawable.feed_bg));
        //menuItems.add(new MenuItemClass("Randevu Bilgileri",R.drawable.message_bg));
        menuItems.add(new MenuItemClass("Giriş Yap",R.drawable.music_bg));
        }

        else
        {
            menuItems.add(new MenuItemClass("Halı Saha Sorgulama",R.drawable.news_bg));
            menuItems.add(new MenuItemClass("Kullanıcı Bilgileri",R.drawable.feed_bg));
            menuItems.add(new MenuItemClass("Randevu Bilgileri",R.drawable.message_bg));
            menuItems.add(new MenuItemClass("Çıkış Yap",R.drawable.music_bg));
        }
        sNavigationDrawer.setMenuItemList(menuItems);
        fragmentClass =  ChooseFieldFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }


        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position "+position);

                switch (position){
                    case 0:{
                        color1 = R.color.red;
                        fragmentClass = ChooseFieldFragment.class;
                        break;
                    }
                    case 1:{
                        if(myID != 0){
                        color1 = R.color.orange;
                        fragmentClass = EditUserInfosFragment.class;}
                        else {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivityForResult(intent, 0);
                            overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
                            //finish();
                            break;
                        }
                        break;
                    }
                    case 2:{
                        color1 = R.color.green;
                        fragmentClass = AppointmentFragment.class;
                        //Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        //startActivityForResult(intent, 0);
                        //overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
                        //finish();
                        break;
                    }
                    case 3:{
                        color1 = R.color.blue;
                        if(myID==0){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(intent, 0);
                        overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
                        finish();
                        break;
                        }
                        else
                            {
                                myID=0;
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("userID", 0);
                                editor.putString("userEmail","");
                                editor.putString("userFirstName","");
                                editor.putString("userLastName","");
                                editor.putString("userPhoneNum","");
                                editor.putString("userBDay","");
                                editor.putString("useBDay","");
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                                startActivityForResult(intent, 0);
                                overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
                                finish();
                            }
                    }

                }
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening(){

                    }

                    @Override
                    public void onDrawerClosing(){
                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State "+newState);
                    }
                });
            }
        });


    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_Dark_Dialog));
        dialogBuilder.setTitle(R.string.warning_message);
        dialogBuilder.setMessage(R.string.logout_message);

        dialogBuilder.setPositiveButton("Evet",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                System.exit(0);
            }
        });

        dialogBuilder.setNegativeButton("Hayır",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();
    }

}
