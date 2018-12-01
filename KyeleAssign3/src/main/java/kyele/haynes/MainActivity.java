package kyele.haynes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private DrawerLayout drawer;
	private FusedLocationProviderClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		requestPermission();

		Toolbar toolbar = findViewById(R.id.kyele_toolbar);
		setSupportActionBar(toolbar);

		drawer = findViewById(R.id.kyele_drawer_layout);
		NavigationView navigationView = findViewById(R.id.kyele_nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.openNavDrawer,R.string.closeNavDrawer);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		if(savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.kyele_fragment_container,
					new KyHome()).commit();
			navigationView.setCheckedItem(R.id.kyeleHome);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {


		switch(item.getItemId()){
			case R.id.kyele_sms:
				return true;
			case R.id.kyele_location:

				client = LocationServices.getFusedLocationProviderClient(this);

				if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					requestPermission();
				}
				client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						if(location!=null){
							//final Button b = (Button)findViewById(R.id.kyele_location);
							//Snackbar snackbar = Snackbar.make(b, "", Snackbar.LENGTH_LONG);
							//snackbar.show();
							Toast.makeText(MainActivity.this,location.toString(),Toast.LENGTH_SHORT);
						}
					}
				});
				return true;
			case R.id.kyele_help:
				String helpLink = getResources().getString(R.string.helpurl);
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(helpLink));
				startActivity(myIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		switch (menuItem.getItemId()){
			case R.id.kyeleHome:
				getSupportFragmentManager().beginTransaction().replace(R.id.kyele_fragment_container,
						new KyHome()).commit();
				break;
			case R.id.kyeleFirstname:
				getSupportFragmentManager().beginTransaction().replace(R.id.kyele_fragment_container,
						new KyeDown()).commit();
				break;
			case R.id.kyeleLastname:
				getSupportFragmentManager().beginTransaction().replace(R.id.kyele_fragment_container,
						new HaSrv()).commit();
				break;
			case R.id.kyeleSettings:
				getSupportFragmentManager().beginTransaction().replace(R.id.kyele_fragment_container,
						new HaySet()).commit();
				break;
		}
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onBackPressed(){
		if(drawer.isDrawerOpen(GravityCompat.START)){
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	public void requestPermission(){
		ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
	}

}
