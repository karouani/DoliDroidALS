package com.marocgeo.als;


import com.marocgeo.als.models.Client;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.Prospection;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class PersonnePhysiqueActivity extends Activity {
	
	private Prospection client;
	
	public PersonnePhysiqueActivity() {
		// TODO Auto-generated constructor stub
		client = new Prospection();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personne_physique);
		
		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			client = (Prospection) getIntent().getSerializableExtra("client");
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personne_physique, menu);
		return true;
	}


}
