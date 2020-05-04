package gstores.merchandiser_beta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;

import static gstores.merchandiser_beta.components.AppLiterals.RequestCodes.ADD_SALES;
import static gstores.merchandiser_beta.components.AppLiterals.RequestCodes.CUSTOMER_REQUEST;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
, SelloutFragment.OnListFragmentInteractionListener
    ,TargetFragment.OnFragmentInteractionListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.bAddSales);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSales();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AppCompatTextView delivery_profileNameView;
        delivery_profileNameView = navigationView.getHeaderView(0).findViewById(R.id.delivery_profileNameView);
        AppCompatTextView delivery_profileDescriptionView;
        delivery_profileDescriptionView = navigationView.getHeaderView(0).findViewById(R.id.delivery_profileDescriptionView);
        delivery_profileNameView.setText(Util.getToken(getApplicationContext()).full_name);
        delivery_profileDescriptionView.setText(Util.getToken(getApplicationContext()).user_name);

        loadSellOutFragment();
    }

    private void addSales() {
        Intent intent = new Intent(getApplicationContext(),AddSalesActivity.class);
        startActivityForResult(intent,ADD_SALES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        if (requestCode == ADD_SALES) { loadSellOutFragment();  }
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logoff) {
            Util.Logoff(getApplicationContext());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String title = getString(R.string.app_name);

        if (id == R.id.sell_out) {
            title = getString( R.string.sale_out);
            loadSellOutFragment();
        }

        /*if (id == R.id.profile) {
            title = getString( R.string.profile);
            loadProfileAction();
        }*/

        if (id == R.id.target) {
            title = getString( R.string.target);
            loadTargetFragment();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadProfileAction() {
        Intent intent = new Intent(getApplicationContext(),UserProfileListActivity.class);
        startActivity(intent);
    }

    private void loadTargetFragment() {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home,
                        TargetFragment.newInstance("","")).commit();
    }

    private void loadSellOutFragment() {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home,
                        SelloutFragment.newInstance(1)).commit();
    }

    @Override
    public void onListFragmentInteraction(DeliveryHeader item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
