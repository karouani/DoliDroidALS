package com.marocgeo.als;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.location.LocationListener;
import com.marocgeo.als.VendeurActivity.ServerSideTask;
import com.marocgeo.als.business.AuthentificationManager;
import com.marocgeo.als.business.DefaultAuthentificationManager;
import com.marocgeo.als.dao.ConnexionDao;
import com.marocgeo.als.dao.ConnexionDaoMysql;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ConfigGps;
import com.marocgeo.als.models.MyTicketWitouhtProduct;
import com.marocgeo.als.models.Produit;
import com.marocgeo.als.models.Reglement;
import com.marocgeo.als.models.Services;
import com.marocgeo.als.utils.CheckOutNet;
import com.marocgeo.als.utils.ConnexionManagerFactory;
import com.marocgeo.als.utils.ForcerActivationGps;
import com.marocgeo.als.utils.JSONParser;
import com.marocgeo.als.utils.MyLocationListener;
import com.marocgeo.als.utils.TinyDB;
import com.marocgeo.als.utils.UpdateApp;
import com.marocgeo.database.StockVirtual;
import com.marocgeo.offline.Offlineimpl;
import com.marocgeo.offline.ioffline;
import com.marocgeo.ticket.ReglementTicketActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class ConnexionActivity extends Activity implements OnClickListener {

	private ProgressDialog mProgressDialog;
	
	
	//private RadioButton btn1,btn2;
	
	//FOrcer Activation GPS
	private ForcerActivationGps forcer;
	
	private WakeLock wakelock;

	//Asynchrone avec connexion 
	private ProgressDialog dialog;
	private ProgressDialog dialog2;
	private ProgressDialog dialogupdate;

	private EditText login;
	private EditText password;
	private Button connexion;
	private Button test;

	private AuthentificationManager auth;
	
	private Offlineimpl myoffline;

	private String log;
	private String pass;
	private Compte compte;
	private ConfigGps conf;

	private Services service;
	
	private JSONParser json;
	private UpdateApp atualizaApp;
	private String ExistingVersionName;
	
	private String dest_file_path;
	private int downloadedSize = 0, totalsize;
	private String download_file_url;
	
	
	private String imei;

	private TinyDB db;
	@Override
	public void onBackPressed() {
		Log.e("inback","bak presed");
		Toast.makeText(this, getResources().getString(R.string.msg_retour), Toast.LENGTH_LONG).show();
		stopService(new Intent(this,ShowLocationActivity.class));
		this.finish();
		System.exit(0);
	}


	public ConnexionActivity() {
		auth = ConnexionManagerFactory.getCConnexionManager();
		compte  = new Compte();
		conf = new ConfigGps();
		service = new Services();
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion);
        db = new TinyDB(this);
        db.clear();
        myoffline = new Offlineimpl(getBaseContext());
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
        wakelock.acquire();
       
        
        
        if(!myoffline.checkFolderexsiste()){
        	showmessageOffline();
        	Log.e("here out ","check folder exsite");
        }
        
		try {
			forcer = new ForcerActivationGps(this);

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}



			//Getting the Object of TelephonyManager 
			TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			//Getting IMEI Number of Devide
			imei=tManager.getDeviceId();

			login = (EditText) findViewById(R.id.login);
			password = (EditText) findViewById(R.id.password);

			connexion = (Button) findViewById(R.id.connecter);
			connexion.setOnClickListener(this);

			Bundle objetbunble  = this.getIntent().getExtras();

			if (objetbunble != null) {
				compte = (Compte) getIntent().getSerializableExtra("user");
				if(compte != null){
					login.setText(compte.getLogin());
					password.setText(compte.getPassword());
				}
				
			}
			
			
			if(CheckOutNet.isNetworkConnected(getApplicationContext())){
				/*
				dialogupdate =  ProgressDialog.show(ConnexionActivity.this,getResources().getString(R.string.tecv39),
						getResources().getString(R.string.msg_wait), true);
				*/
				/*
				mProgressDialog = new ProgressDialog(ConnexionActivity.this);
				mProgressDialog.setMessage("Recherche d\'une mise à jour disponible");
				mProgressDialog.setIndeterminate(false);
				mProgressDialog.setMax(100);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				*/
				
				if(myoffline.checkForUpdate() == 1){
					new UpdateTask().execute();
				}
				
				//new UpdateTask().execute();
			}
			/*
			btn1 = (RadioButton)findViewById(R.id.radioButton1);
			btn1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					login.setText("thami");
					password.setText("123");
				}
			});
			btn2 = (RadioButton)findViewById(R.id.radioButton2);
			btn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					login.setText("mohamed");
					password.setText("mohamed");
				}
			});
			*/
			getGpsApplication();
			
			
			myoffline.TotalMemory();
			myoffline.FreeMemory();
			
			
			StockVirtual sv = new StockVirtual(ConnexionActivity.this);
			List<Produit> dv_pd = new ArrayList<>();
			dv_pd = sv.getAllProduits();
			for (int i = 0; i < dv_pd.size(); i++) {
				Log.e(dv_pd.get(i).getRef(),"type "+dv_pd.get(i).getDesig()+"# qte "+dv_pd.get(i).getQteDispo());
			}
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	private void createGpsDisabledAlert() {

		forcer.turnGPSOn();


		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder
		.setMessage(getResources().getString(R.string.msg_gps_desactive))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.btn_gps_activer),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				ConnexionActivity.this.showGpsOptions();
				forcer.turnGPSOn();
			}
		}
				);
		localBuilder.setNegativeButton(getResources().getString(R.string.btn_gps_deactiver),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				paramDialogInterface.cancel();
				ConnexionActivity.this.finish();
			}
		}
				);
		localBuilder.create().show();

	}

	private void erreurNetwork(){
		AlertDialog.Builder local = new AlertDialog.Builder(this);

		local
		.setTitle(getResources().getString(R.string.msg_network))
		.setMessage(getResources().getString(R.string.msg_network_alert))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				ConnexionActivity.this.finish();
			}
		}
				);
		local.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				return;
			}
		}
				);
		local.create().show();
	}

	private void compteDesactiver(){
		AlertDialog.Builder local = new AlertDialog.Builder(this);

		local
		.setTitle(getResources().getString(R.string.msg_device_unsupported))
		.setMessage(getResources().getString(R.string.msg_deviced))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						return;
					}
				}
						);
		local.setNegativeButton(getResources().getString(R.string.appel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:00212522400587"));
				startActivity(intent);
				return;
			}
		}
				);

		local.setNeutralButton("Email",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String[] TO = {"contact@geocom.ma"};
				String[] CC = {"contact@marocgeo.ma"};
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("text/plain");


				emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
				emailIntent.putExtra(Intent.EXTRA_CC, CC);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject_email));
				emailIntent.putExtra(Intent.EXTRA_TEXT, "");

				try {
					startActivity(Intent.createChooser(emailIntent, "Envoyer Email..."));
					finish();
					Log.i("Finished sending email...", "");
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ConnexionActivity.this, 
							getResources().getString(R.string.no_email), Toast.LENGTH_SHORT).show();
				}
			}
		});
		local.create().show();
	}

	private void showGpsOptions() {
		startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
	}

	public void getGpsApplication(){

		LocationManager mlocManager=null;
		android.location.LocationListener mlocListener;
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);

		if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createGpsDisabledAlert();
		}

	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.connecter){

			log = login.getText().toString();
			pass = password.getText().toString();

			//stopService(new Intent(ConnexionActivity.this,  ShowLocationActivity.class));

			if("".equals(log) || "".equals(pass)){

				Toast toast = Toast.makeText(ConnexionActivity.this, getResources().getString(R.string.msg_fields), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}else{
				if(CheckOutNet.isNetworkConnected(this)){
					getGpsApplication();
					
					
					//Log.e("Compte",compte.toString());
						//if(myoffline.checkAvailableofflinestorage() > 0 ){
							
							dialog = ProgressDialog.show(ConnexionActivity.this, getResources().getString(R.string.connexion),
									getResources().getString(R.string.msg_wait), true);
							new ConnexionTask().execute();
						
				}else{
					//erreurNetwork();
					dialog = ProgressDialog.show(ConnexionActivity.this, getResources().getString(R.string.connexion),
							getResources().getString(R.string.msg_wait), true);
					new OfflineTask().execute();
				}
			}

		}
	}

	//---you need this to prevent the webview from
	// launching another browser when a url
	// redirection occurs---

	private class Callback extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(
				WebView view, String url) {
			return(false);
		}
	}

	class ConnexionTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			
			try {
				
				Log.e("start load ","in load");
				compte = auth.login(log+"/karouaniYassine/"+imei, pass);
				conf = auth.getGpsConfig();
				
				/*
				myoffline.CleanProspection();
				myoffline.CleanInvoice();
				myoffline.CleanReglement();
				
				myoffline.CleanPayementTicket();
				myoffline.CleanBluetooth();
				*/
				
				Log.e("compte ",compte.toString());
				
				if(!myoffline.checkFolderexsiste()){
					showmessageOffline();
				}else{
					myoffline.shynchronizeCompte(compte);
					myoffline.shynchronizeSociete(auth.lodSociete(""));


					if("technicien".equals(compte.getProfile())){
						service = auth.getService(log, pass);
						
						List<Services> ls = new ArrayList<>();
						ls.add(service);
						myoffline.shynchronizeService(ls);
					}
				}
			
				

				
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("error in me ",e.getMessage()+" boca down");
			}
				
			
				
				
				
			
			return null;
		}

		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing()){
					dialog.dismiss();
					
					//Log.e("Compte connected",compte.toString());
					/*
					if(myoffline.checkAvailableofflinestorage() > 0){
					dialog2 = ProgressDialog.show(ConnexionActivity.this, "Synchronisation avec le serveur",
							getResources().getString(R.string.msg_wait_sys), true);
					new ServerSideTask().execute(); 
					}
					*/
					
					switch (compte.getActiver()) {
					case 1:
					case 2:
					case 0:
						Toast toast = Toast.makeText(ConnexionActivity.this,compte.getMessage()  +" << ", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						break;
					case 3:
						Intent intent = new Intent(ConnexionActivity.this, TrackingActivity.class);
						intent.putExtra("user", compte);
						startActivity(intent);
						break;
					case 8:
						compteDesactiver();
						break;
					case 4 :
					default: 
						String act = compte.getProfile();
						act  = act.toLowerCase();

						Log.i("Voila Profile",act);

						Intent intentService = new Intent(ConnexionActivity.this, ShowLocationActivity.class);
						intentService.putExtra("user", compte);
						startService(intentService);

						if("vendeur".equals(act)){
							Intent intent1 = new Intent(ConnexionActivity.this, VendeurActivity.class); //CatalogeActivity.class  //CmdViewActivity
							intent1.putExtra("user", compte);
							intent1.putExtra("cmd", "0");
							startActivity(intent1);
						}
						else if("technicien".equals(act)){
							//service = auth.getService(log, pass);
							Intent intent2 = new Intent(ConnexionActivity.this, TechnicienTabActivity.class);
							intent2.putExtra("user", compte);
							intent2.putExtra("service", service.getService());
							intent2.putExtra("nmbService", service.getNmb_cmp()+"");
							for (int i = 0; i < service.getLabels().size(); i++) {
								intent2.putExtra("labels"+i, service.getLabels().get(i).getLabel());
							}

							startActivity(intent2);
						}
						else if("commercial".equals(act)){
							Intent intent3 = new Intent(ConnexionActivity.this, CommercialActivity.class);
							intent3.putExtra("user", compte);
							startActivity(intent3);

						}
						else if("PRE-VENDEURS".equals(act) || "PRE-VENDEURS".toLowerCase().equals(act.toLowerCase())){
							Intent intent3 = new Intent(ConnexionActivity.this, CatalogeActivity.class);
							intent3.putExtra("user", compte);
							intent3.putExtra("cmd", "1");
							startActivity(intent3);
						}
						else if("livraison".equals(act)){
							Intent intent3 = new Intent(ConnexionActivity.this, CommercialActivity.class);
							intent3.putExtra("user", compte);
							startActivity(intent3);
						}
						else if("administrateurs".equals(act)){
							String url =  com.marocgeo.als.utils.URL.URL+"administration.php?username="+log+"&password="+pass;
							/*
							 * Intent i = new Intent(Intent.ACTION_VIEW);
							 * i.setData(Uri.parse(url));
							   startActivity(i);
							 */
							Intent intent4 = new Intent(ConnexionActivity.this, AdminActivity.class);
							intent4.putExtra("url", url);
							intent4.putExtra("user", compte);
							startActivity(intent4);
							//ConnexionActivity.this.finish();
						}else if("Administrateur magasinier".toLowerCase().equals(act)){
							Intent intent4 = new Intent(ConnexionActivity.this, TransfertstockActivity.class);
							intent4.putExtra("user", compte);
							startActivity(intent4);
							//ConnexionActivity.this.finish();
						}else{
							String text = String.format(
							    getResources().getString(R.string.msg_profile_error),
							    act);
							
							Toast toast1 = Toast.makeText(ConnexionActivity.this,text, Toast.LENGTH_SHORT);
							toast1.setGravity(Gravity.CENTER, 0, 0);
							toast1.show();
						}

						break;
					}

				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.fatal_error),
						Toast.LENGTH_LONG).show();
				Log.e("Error","");
			}
		}

	}
	
	class OfflineTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			try {
				Log.e("in offline cpt","star");
				
				if(!myoffline.checkFolderexsiste()){
					showmessageOffline();
				}else{
					compte = myoffline.LoadCompte(login.getText().toString(), password.getText().toString());
					conf = myoffline.getGpsTracker();
					
					
					if("technicien".equals(compte.getProfile())){
						service = myoffline.LoadServices("");
					}	
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("in offline",e.getMessage()  +" << ");
			}
				
			
				/*
				if("technicien".equals(compte.getProfile())){
					service = auth.getService(log, pass);
				}
				*/
			
			return null;
		}

		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing()){
					dialog.dismiss();
					
					//Log.e("Compte connected",compte.toString());
					if(compte != null){
						
						
						
						switch (compte.getActiver()) {
						case 1:
						case 2:
						case 0:
							Toast toast = Toast.makeText(ConnexionActivity.this,compte.getMessage()  +" << ", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							break;
						case 3:
							Intent intent = new Intent(ConnexionActivity.this, TrackingActivity.class);
							intent.putExtra("user", compte);
							startActivity(intent);
							break;
						case 8:
							compteDesactiver();
							break;
						case 4 :
						default: 
							String act = compte.getProfile();
							act  = act.toLowerCase();

						//	Log.e("Voila Profile",act);

							Intent intentService = new Intent(ConnexionActivity.this, ShowLocationActivity.class);
							intentService.putExtra("user", compte);
							startService(intentService);

							if("vendeur".equals(act)){
								Intent intent1 = new Intent(ConnexionActivity.this, VendeurActivity.class); //CatalogeActivity.class
								intent1.putExtra("user", compte);
								intent1.putExtra("cmd", "0");
								startActivity(intent1);
							}
							else if("technicien".equals(act)){
								//service = auth.getService(log, pass);
								Intent intent2 = new Intent(ConnexionActivity.this, TechnicienTabActivity.class);
								intent2.putExtra("user", compte);
								intent2.putExtra("service", service.getService());
								intent2.putExtra("nmbService", service.getNmb_cmp()+"");
								for (int i = 0; i < service.getLabels().size(); i++) {
									intent2.putExtra("labels"+i, service.getLabels().get(i).getLabel());
								}

								startActivity(intent2);
							}
							else if("commercial".equals(act)){
								Intent intent3 = new Intent(ConnexionActivity.this, CommercialActivity.class);
								intent3.putExtra("user", compte);
								startActivity(intent3);

							}
							else if("PRE-VENDEURS".equals(act) || "PRE-VENDEURS".toLowerCase().equals(act.toLowerCase())){
								Intent intent3 = new Intent(ConnexionActivity.this, CatalogeActivity.class);
								intent3.putExtra("user", compte);
								intent3.putExtra("cmd", "1");
								startActivity(intent3);
							}
							else if("livraison".equals(act)){
								Intent intent3 = new Intent(ConnexionActivity.this, CommercialActivity.class);
								intent3.putExtra("user", compte);
								startActivity(intent3);
							}
							else if("administrateurs".equals(act)){
								String url =  com.marocgeo.als.utils.URL.URL+"administration.php?username="+log+"&password="+pass;
								/*
								 * Intent i = new Intent(Intent.ACTION_VIEW);
								 * i.setData(Uri.parse(url));
								   startActivity(i);
								 */
								Intent intent4 = new Intent(ConnexionActivity.this, AdminActivity.class);
								intent4.putExtra("url", url);
								intent4.putExtra("user", compte);
								startActivity(intent4);
								//ConnexionActivity.this.finish();
							}else{
								String text = String.format(
								    getResources().getString(R.string.msg_profile_error),
								    act);
								
								Toast toast1 = Toast.makeText(ConnexionActivity.this,text, Toast.LENGTH_SHORT);
								toast1.setGravity(Gravity.CENTER, 0, 0);
								toast1.show();
							}

							break;
						}
					}else{
						
						//pas de fichier de configuration
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.fatal_error),
								Toast.LENGTH_LONG).show();
					}
					

				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.fatal_error),
						Toast.LENGTH_LONG).show();
				Log.e("Error","");
			}
		}

	}
	
	
	class ServerSideTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			try {
				
				
				myoffline = new Offlineimpl(getApplicationContext());
				
				if(!myoffline.checkFolderexsiste()){
					showmessageOffline();
				}else{
					compte = myoffline.LoadCompte(login.getText().toString(), password.getText().toString());
					
					//myoffline.synchronisationClientsEnligne(compte);
				//	myoffline.synchronisationReglementEnligne(null, compte);
					if(compte != null && CheckOutNet.isNetworkConnected(getApplicationContext()))myoffline.SendOutData(compte);
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erreu synchro",e.getMessage()  +" << ");
			}
			
			return null;
		}

		protected void onPostExecute(String sResponse) {
			try {
				if (dialog2.isShowing()){
					dialog2.dismiss();
				}
				
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.fatal_error),
						Toast.LENGTH_LONG).show();
				Log.e("Error","");
			}
		}

	}
	
	
	public void showmessageOffline(){
		try {
			 
	         LayoutInflater inflater = this.getLayoutInflater();
	         View dialogView = inflater.inflate(R.layout.msgstorage, null);
	         
	         AlertDialog.Builder dialog =  new AlertDialog.Builder(ConnexionActivity.this);
	         dialog.setView(dialogView);
 	 	     dialog.setTitle(R.string.caus1);
 	 	     dialog.setPositiveButton(R.string.caus8, new DialogInterface.OnClickListener() {
 	 	        public void onClick(DialogInterface dialog, int which) { 
 	 	        	 dialog.cancel();
 	 	        }
 	 	     });
 	 	     dialog.setCancelable(true);
 	 	     dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	void downloadAndOpenPDF(String lien,String name) {
		String apkurl = lien;
		String outputFileName = name;
		try {
			URL url = new URL(apkurl);
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			
			int fileLength = c.getContentLength();

			String PATH = Environment.getExternalStorageDirectory()
					+ "/apk/";
			File file = new File(PATH);
			file.mkdirs();
			File outputFile = new File(file, outputFileName);
			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = c.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			long total = 0;
			
			while ((len1 = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len1);
				total += len1;
				// Publish the progress
				//publishProgress((int) (total * 100 / fileLength));
			}
			fos.close();
			is.close();// .apk is download to sdcard in download file

			// install the .apk
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory()
					+ "/apk/"
					+ outputFileName)),
					"application/vnd.android.package-archive");
			ConnexionActivity.this.startActivity(intent);
		} catch (IOException e) {
			Toast.makeText(ConnexionActivity.this.getApplicationContext(), "Update error!",
					Toast.LENGTH_LONG).show();
		}
	}
	
class UpdateTask extends AsyncTask<String, Integer, String> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			mProgressDialog = new ProgressDialog(ConnexionActivity.this);
			// Set your ProgressBar Title 
			mProgressDialog.setTitle(getResources().getString(R.string.cnxlab1));
			//mProgressDialog.setIcon(R.drawable.ic_launcher);
			// Set your ProgressBar Message
			mProgressDialog.setMessage(getResources().getString(R.string.cnxlab2));
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// Show ProgressBar
			mProgressDialog.setCancelable(false);
			//  mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... paramsw) {
			
			try {
								
				//Getting the Object of TelephonyManager 
				TelephonyManager tManager2 = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				//Getting IMEI Number of Devide
				imei=tManager2.getDeviceId();

				atualizaApp = new UpdateApp();
				atualizaApp.setContext(getApplicationContext());

				json = new JSONParser();

				try {
					ExistingVersionName = getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0 ).versionName;
				} catch (NameNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


				// TODO Auto-generated method stub
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("imei", imei));
				params.add(new BasicNameValuePair("date", new Date().toGMTString()));
				params.add(new BasicNameValuePair("type", CheckOutNet.type));
				params.add(new BasicNameValuePair("version", ExistingVersionName));

				String jsonString = json.makeHttpRequest(CheckOutNet.url_update, "POST", params);
				Log.e("Json",jsonString);

				try {
					JSONObject obj = new JSONObject(jsonString);
					String update = obj.getString("update");
					String version = obj.getString("version");

					//Log.e("Version",ExistingVersionName);
					if("yes".equals(update)){
						
					
						if(!ExistingVersionName.equals(version)){
							String apk = obj.getString("apk");

							if(!apk.contains("http://")) apk = "http://"+apk;

							download_file_url = apk;
							dest_file_path = obj.getString("name")+".apk";

							//downloadAndOpenPDF(download_file_url,dest_file_path);
							
							String apkurl = download_file_url;
							String outputFileName = dest_file_path;
							try {
								URL url = new URL(apkurl);
								HttpURLConnection c = (HttpURLConnection) url.openConnection();
								c.setRequestMethod("GET");
								c.setDoOutput(true);
								c.connect();
								
								int fileLength = c.getContentLength();

								String PATH = Environment.getExternalStorageDirectory()
										+ "/apk/";
								File file = new File(PATH);
								file.mkdirs();
								File outputFile = new File(file, outputFileName);
								FileOutputStream fos = new FileOutputStream(outputFile);

								InputStream is = c.getInputStream();

								byte[] buffer = new byte[1024];
								int len1 = 0;
								long total = 0;
								
								while ((len1 = is.read(buffer)) != -1) {
									fos.write(buffer, 0, len1);
									total += len1;
									// Publish the progress
									publishProgress((int) (total * 100 / fileLength));
								}
								fos.close();
								is.close();// .apk is download to sdcard in download file

								// install the .apk
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setDataAndType(Uri.fromFile(new File(Environment
										.getExternalStorageDirectory()
										+ "/apk/"
										+ outputFileName)),
										"application/vnd.android.package-archive");
								ConnexionActivity.this.startActivity(intent);
							} catch (IOException e) {
								Toast.makeText(ConnexionActivity.this.getApplicationContext(), "Update error!",
										Toast.LENGTH_LONG).show();
							}
							
						}else{
							// TODO Auto-generated method stub
							List<NameValuePair> params2 = new ArrayList<NameValuePair>();
							params2.add(new BasicNameValuePair("imei", imei));
							params2.add(new BasicNameValuePair("date", new Date().toGMTString()));
							params2.add(new BasicNameValuePair("type", CheckOutNet.type));
							params2.add(new BasicNameValuePair("version", ExistingVersionName));
							params2.add(new BasicNameValuePair("update", "yes"));

							json.makeHttpRequest(CheckOutNet.url_update, "POST", params2);
						}
					}else{
						// TODO Auto-generated method stub
						List<NameValuePair> params2 = new ArrayList<NameValuePair>();
						params2.add(new BasicNameValuePair("imei", imei));
						params2.add(new BasicNameValuePair("date", new Date().toGMTString()));
						params2.add(new BasicNameValuePair("type", CheckOutNet.type));
						params2.add(new BasicNameValuePair("version", ExistingVersionName));
						params2.add(new BasicNameValuePair("update", "yes"));

						json.makeHttpRequest(CheckOutNet.url_update, "POST", params2);
							if(mProgressDialog.isShowing()){
								mProgressDialog.dismiss();
							}
					
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erreu synchro",e.getMessage()  +" << ");
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				/*
				if(dialogupdate.isShowing()){
					dialogupdate.dismiss();
				}
				*/
				//dialogupdate.dismiss();
				mProgressDialog.dismiss();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.fatal_error),
						Toast.LENGTH_LONG).show();
				Log.e("Error","");
			}
		}
		
		
		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			mProgressDialog.setProgress(progress[0]);
		}

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.connexion, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/************************MENU Carte *****************************************/
		case R.id.updatefind:
			if(CheckOutNet.isNetworkConnected(getApplicationContext())){
				alertmaps2();
			}else{
				alertmaps();
			}
			break;

		}
		return true;

	}
	
	public void alertmaps(){
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(ConnexionActivity.this);
		alert.setTitle(getResources().getString(R.string.caus25));
		alert.setMessage(getResources().getString(R.string.caus18));
		alert.setNegativeButton("Bien compris", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				return;
			}
		});
		alert.setCancelable(false);
		alert.create().show();
	}
	
	public void alertmaps2(){
		
		String ExistingVersionName2="";
		try {
			ExistingVersionName2 = "\n [ + "+getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0 ).versionName+"]";
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AlertDialog.Builder alert = new AlertDialog.Builder(ConnexionActivity.this);
		alert.setTitle(getResources().getString(R.string.caus25)+ExistingVersionName2);
		alert.setMessage(getResources().getString(R.string.caus24));
		alert.setNegativeButton("Lancer la recherche", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new UpdateTask().execute();
				return;
			}
		});
		alert.setCancelable(true);
		alert.create().show();
	}
}


