package ai.portfolio.dev.project.app.com.tictactoe.Activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ai.portfolio.dev.project.app.com.tictactoe.Custom.CustomFlipAnimation;
import ai.portfolio.dev.project.app.com.tictactoe.Fragments.TicTacToe_Fragment;
import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.OnFragmentInteractionListener;
import ai.portfolio.dev.project.app.com.tictactoe.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState != null){

        }else{
            final ConstraintLayout lay = (ConstraintLayout)this.findViewById(R.id.button_layout_constraint);
            Button game = (Button)this.findViewById(R.id.btn_ai);

            CustomFlipAnimation flip = new CustomFlipAnimation(game,game,1000);
            if (game.getVisibility() == View.GONE) {
                flip.reverse();
            }else{
                game.startAnimation(flip);
            }
            game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lay.setVisibility(View.GONE);
                    addFragment(R.id.main,
                            new TicTacToe_Fragment(),
                            TicTacToe_Fragment.FRAGMENT_TAG);
                }
            });

            Button online = (Button)this.findViewById(R.id.btn_multiplayer);
//            ObjectAnimator anim2 = (ObjectAnimator) AnimatorInflater.loadAnimator(this.getApplicationContext(), R.animator.flipping);
//            anim2.setTarget(online);
//            anim2.setDuration(3000);
//            anim2.start();
            online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this,"Feature not yet implemented :(",Toast.LENGTH_LONG).show();
                    //Display search for game
                }
            });

        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

     switch (id){
         case R.id.nav_logout:
             //do something
             break;
         case R.id.nav_settings:
             openSettingsActivity();
             break;

     }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSettingsActivity() {
        this.startActivity(new Intent(this,SettingsActivity.class));
    }

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
