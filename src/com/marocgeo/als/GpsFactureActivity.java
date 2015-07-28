package com.marocgeo.als;


import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.GpsTracker;
import com.marocgeo.als.utils.ServiceDao;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class GpsFactureActivity extends Activity {

	private GpsTracker gps;
	private ServiceDao daoGps;
	private Compte compte;
	
	public GpsFactureActivity() {
		 daoGps = new ServiceDao();
		 compte = new Compte();
		 gps = new GpsTracker();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_facture);

		
	}

}
