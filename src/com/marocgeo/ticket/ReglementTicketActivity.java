package com.marocgeo.ticket;

import java.security.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.google.gson.Gson;
import com.marocgeo.als.ConnexionActivity;
import com.marocgeo.als.DeviceListActivity;
import com.marocgeo.als.OfflineActivity;
import com.marocgeo.als.PayementActivity;
import com.marocgeo.als.R;
import com.marocgeo.als.TicketOfflineActivity;
import com.marocgeo.als.R.id;
import com.marocgeo.als.R.layout;
import com.marocgeo.als.R.menu;
import com.marocgeo.als.models.Client;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.MyTicketPayement;
import com.marocgeo.als.models.MyTicketWitouhtProduct;
import com.marocgeo.als.models.Payement;
import com.marocgeo.als.models.Reglement;
import com.marocgeo.als.utils.MyTicket;
import com.marocgeo.offline.Offlineimpl;
import com.marocgeo.offline.ioffline;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ReglementTicketActivity extends Activity {

	/** Called when the activity is first created. */
	public static BluetoothAdapter myBluetoothAdapter;
	public String SelectedBDAddress;
	
	private MyTicketPayement myti;
	private int refreg;

	private ioffline myoffline;
	
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

	private static final String tab00c0 = "AAAAAAACEEEEIIII" +
			"DNOOOOO\u00d7\u00d8UUUUYI\u00df" +
			"aaaaaaaceeeeiiii" +
			"\u00f0nooooo\u00f7\u00f8uuuuy\u00fey" +
			"AaAaAaCcCcCcCcDd" +
			"DdEeEeEeEeEeGgGg" +
			"GgGgHhHhIiIiIiIi" +
			"IiJjJjKkkLlLlLlL" +
			"lLlNnNnNnnNnOoOo" +
			"OoOoRrRrRrSsSsSs" +
			"SsTtTtTtUuUuUuUu" +
			"UuUuWwYyYZzZzZzF";

	/************************************
	 * SERIALZABLE DATA
	 */
	private Compte compte;
	//private FileData objet;
	private MyTicket ticket;
	private MyTicketWitouhtProduct offlineticket;
	private Client clt;
	private Payement mypay;
	private Reglement myreg;
	private Button autre,quitter;
	private List<Reglement> lsreg;
	
	private int okey = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reglement_ticket);
		setTitle(R.string.bluetooth_unconnected);

		try {
			Bundle objetbunble  = this.getIntent().getExtras();

		
			
			myoffline = new Offlineimpl(getApplicationContext());

			if (objetbunble != null) {
				compte =  (Compte) getIntent().getSerializableExtra("user");
				refreg = (Integer)getIntent().getSerializableExtra("refreg");
				
				myti =  myoffline.checkMyReglementicket(refreg);
				
				
				Log.e("myti",myti.toString());
				
				offlineticket = myti.getOfflineticket();
				mypay = myti.getMypay();
				myreg = myti.getMyreg();
				clt = myti.getClt();
				
				lsreg = myti.getLsreg();
				
				ticket = myti.getTicket();
			}
			
			
			
			SelectedBDAddress = "";
			if((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter())==null)
			{
				Toast.makeText(this,getResources().getString(R.string.none_paired), Toast.LENGTH_LONG).show();
			}
			if(!myBluetoothAdapter.isEnabled())
			{
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);    
				startActivityForResult(enableBtIntent, 2);
			}

			
			
			autre = (Button) findViewById(R.id.rtgeocom);
			autre.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					SelectedBDAddress = "";
					if((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter())==null)
					{
						Toast.makeText(ReglementTicketActivity.this,getResources().getString(R.string.none_paired), Toast.LENGTH_LONG).show();
					}else{
						if(!"".equals(myBluetoothAdapter.getAddress()) && myBluetoothAdapter.getAddress() != null){
							if(okey == 1)
							{
								Intent serverIntent = new Intent(ReglementTicketActivity.this, DeviceListActivity.class);
								startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

							}else{
								createTicket(ticket);
							}
						}
					}
					if(!myBluetoothAdapter.isEnabled())
					{
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);    
						startActivityForResult(enableBtIntent, 2);
					}
					
					
					createTicket(ticket);
				}
			});

			quitter = (Button) findViewById(R.id.rtbtn_quit);
			quitter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(ReglementTicketActivity.this, ConnexionActivity.class);
					intent.putExtra("user", compte);
					startActivity(intent);
					ReglementTicketActivity.this.finish();
				}
			});
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		// Launch the DeviceListActivity to see devices and do scan
		Intent serverIntent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		//�˳�ǰ�ر�����
		BluetoothPrintDriver.close();
		super.onDestroy();
	}


	public void InitUIControl(){

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
		Log.e("req code ",requestCode+"");
		if(data != null){
			switch (requestCode) {
			case REQUEST_CONNECT_DEVICE_SECURE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) 
				{
					// ������һ���豸֮ǰ�ȹر��������������豸֮���л�ʱ�����
					BluetoothPrintDriver.close();  
					// ��ȡ�豸 MAC address
					SelectedBDAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					// ��������
					if(!BluetoothPrintDriver.OpenPrinter(SelectedBDAddress)) 	
					{
						BluetoothPrintDriver.close();
						setTitle(R.string.bluetooth_connect_fail);
						//	            		mTitle.setText("����ʧ��");

						Intent serverIntent = new Intent(ReglementTicketActivity.this, DeviceListActivity.class);
						startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

						okey = 1;
						return;
					}
					else
					{
						//	            		mTitle.setText(SelectedBDAddress);
						String bluetoothConnectSucess = getResources().getString(R.string.bluetooth_connect_sucess);
						this.setTitle(bluetoothConnectSucess+SelectedBDAddress);

						createTicket(ticket);
					}
				}
				break;
			case REQUEST_CONNECT_DEVICE_INSECURE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {

					createTicket(ticket);
				}
				break;
			}
		}else{
			Log.e("data nulled","walo walo");
			onStart();
		}
	
	}
	
	private void createTicket2(MyTicket ticket) {
		DecimalFormat df = new DecimalFormat("0.00");
		double ht = (mypay.getTotal()*100)/120D;
		double ttc = mypay.getTotal();
		double regle = myreg.getAmount();
		double cpt =  mypay.getTotal() - (mypay.getAmount()+myreg.getAmount());
		
		Log.e("MyTicket",ticket.toString()+ "ht# "+ht+" ttc# "+ttc+" regl#  "+regle+" cpt# "+cpt+" tva# "+df.format(mypay.getTotal() - ht));
	}

	/************************************ CREATION TICKET *********************************************/
	@SuppressLint("NewApi")
	private void createTicket(MyTicket ticket) {
		if(!BluetoothPrintDriver.IsNoConnection()){
			BluetoothPrintDriver.Begin();
			
			Gson gson = new Gson();

			/******************* ENVOYER TICKET ************************************/
			/******************** LOGO NAME SOCIETE ******************************/
			BluetoothPrintDriver.AddAlignMode((byte)1);//����
			BluetoothPrintDriver.SetLineSpace((byte)50);	
			BluetoothPrintDriver.SetZoom((byte)0x11);//���ߣ�����		
			BluetoothPrintDriver.ImportData(ticket.getNameSte().toUpperCase());
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			/************************ADDRESS SOCIETE********************************/
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddBold((byte)0x01);//����

			BluetoothPrintDriver.ImportData(removeDiacritic(ticket.getAddresse())); 
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/*
			String nm = null;
			try {
				nm = new String(ticket.getAddresse().getBytes("ISO-8859-1"), Charset.forName("ISO-8859-1"));
				BluetoothPrintDriver.ImportData(nm);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] byteImage = EncodingUtils.getBytes(ticket.getAddresse(), "UTF-8");
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.ImportData(byteImage, byteImage.length);
			 */

			/************************VENDEUR********************************/
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddBold((byte)0x01);//����


			BluetoothPrintDriver.ImportData(removeDiacritic(compte.getLogin())); 
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/************************ Ligne pointier ****************************/
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/************************ INFOS SUR TICKET ***************************/
			BluetoothPrintDriver.SetCharacterFont((byte) 0x031);
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			BluetoothPrintDriver.AddBold((byte)0);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0);//����
			BluetoothPrintDriver.ImportData(getResources().getString(R.string.facture_num)+" : "+ticket.getNumFacture());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			BluetoothPrintDriver.ImportData(getResources().getString(R.string.client)+" "+removeDiacritic(ticket.getClient()).toUpperCase());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			BluetoothPrintDriver.ImportData(getResources().getString(R.string.le)+ticket.getDateHeur());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			BluetoothPrintDriver.SetCharacterFont((byte) 0x00);
			/************************ Ligne pointier ****************************/
			BluetoothPrintDriver.AddBold((byte)0x01);//����
			BluetoothPrintDriver.AddAlignMode((byte) 1);//����
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			DecimalFormat df = new DecimalFormat("0.00");
			/************************* LES tables Headers ***********************
			String tmpString2 = this.getResources().getString(R.string.print_table_content2);
			BluetoothPrintDriver.AddBold((byte)0x01);//����
			BluetoothPrintDriver.SetZoom((byte)0x01);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x01);
			//BluetoothPrintDriver.ImportData(new byte[]{0x1d,0x21,0x01}, 3);	//���ñ���
			BluetoothPrintDriver.ImportData(String.format("  "+tmpString2+" "),true);
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

		

			/************************** LEs tautaux *******************************************/
			/*
			 * 	TOTAL H.T 		2090.00 DH
			TOTAL TTC		2508.00 DH
			D�j� r�gl�		2508.00 DH
			Reste � payer	0.00 	DH
			Infos TVA (20%)	418.00 	DH	
			 */

			/*
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			 */
			
			
			/*------------------------ HT---------------------------*/

			String data = getResources().getString(R.string.total_ht);
			double ht = (mypay.getTotal()*100)/120D;

			int espace = 32 - (df.format(ht).length() + data.length() + 3);

			for (int j = 0; j < espace; j++) {
				data = data + " ";
			}

			data = data + df.format(ht) + " DH";

			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x00);
			BluetoothPrintDriver.AddAlignMode((byte) 0);//����
			BluetoothPrintDriver.ImportData(data,true);
			/*------------------------ TTC --------------------------*/
			String data2 = getResources().getString(R.string.total_ttc);
			double ttc = mypay.getTotal();

			int espace2 = 32 - (df.format(ttc).length() + data2.length() + 3);

			for (int j = 0; j < espace2; j++) {
				data2 = data2 + " ";
			}
			data2 = data2 + df.format(ttc) + " DH";
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x01);
			BluetoothPrintDriver.AddAlignMode((byte) 0);//����
			BluetoothPrintDriver.ImportData(data2,true);

			/*------------------------ LIGNE --------------------------*/

			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x00);
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();


			/************************** Les Infos ********************************************/
			/*
			 * 	
				D�j� r�gl�		2508.00 DH
				Reste � payer	0.00 	DH
				Infos TVA (20%)	418.00 	DH	
			 */
			BluetoothPrintDriver.SetCharacterFont((byte) 0x00);

			/*------------------------ Deja Regle---------------------------*/
			String data3 = getResources().getString(R.string.regle);
			double regle = myreg.getAmount();
			int espace3 = 33 - (df.format(regle).length() + data3.length()  + 3);

			for (int j = 0; j < espace3; j++) {
				data3 = data3 + " ";
			}
			data3 = data3 + df.format(regle) + "DH";

			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x00);
			BluetoothPrintDriver.AddAlignMode((byte) 0);//����
			BluetoothPrintDriver.ImportData(data3,true);
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/*------------------------ Rest a Paye --------------------------*/

			String data4 = getResources().getString(R.string.rested);
			double cpt =  Double.parseDouble(myreg.getFk_facture());
			int espace4 = 33 - ((df.format(cpt)).length() + data4.length() + 3);

			for (int j = 0; j < espace4; j++) {
				data4 = data4 + " ";
			}
			data4 = data4 + df.format(cpt) + "DH";
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x00);
			BluetoothPrintDriver.AddAlignMode((byte) 0);//����
			BluetoothPrintDriver.ImportData(data4,true);
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			/*------------------------ INFO TVA --------------------------*/

			String data5 = "TVA (20%)";
			int espace5 = 33 - (df.format(mypay.getTotal() - ht).length() + 9 + 3);

			for (int j = 0; j < espace5; j++) {
				data5 = data5 + " ";
			}

			data5 = data5 + df.format(mypay.getTotal() - ht) + "DH";
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x00);
			BluetoothPrintDriver.AddAlignMode((byte) 0);//����
			BluetoothPrintDriver.ImportData(data5,true);
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/*------------------------ LIGNE --------------------------*/
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x00);
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			

			/************************ BAR CODE **************************************************/
			BluetoothPrintDriver.SetCharacterFont((byte) 0x00);
			String print1DBarcodeStr = "";
			String tmp = "";
			if (ticket.getNumFacture().substring(2).length() > 7) {
				int p = ticket.getNumFacture().indexOf("-");
				tmp = ticket.getNumFacture().substring(p);
				if(tmp.length() >  6 && tmp.length() == 11){
					print1DBarcodeStr = "-"+tmp.substring(6);
					tmp = ticket.getNumFacture().substring(p+1);
				}else{
					tmp = ticket.getNumFacture().substring(p+1);
					print1DBarcodeStr = "-"+tmp;
				}
			}else{
				print1DBarcodeStr = ticket.getNumFacture().substring(2);
				tmp = print1DBarcodeStr;
			}
			//String print1DBarcodeStr = ticket.getNumFacture().substring(2);
			//int len = print1DBarcodeStr.length();

			//    	for(int i=0; i<len; i++){
			//    		if(print1DBarcodeStr.charAt(i)<'0' || print1DBarcodeStr.charAt(i)>'9'){
			//    			//Utils.ShowMessage(PrinterOptionActivity.this, "�����ַ�ֻ����0��9λ֮�������!");
			//    			String tmpString = PrinterOptionActivity.this.getResources().getString(R.string.barcode_input_hint);
			//    			Utils.ShowMessage(PrinterOptionActivity.this, tmpString);
			//        		return;
			//        	}
			//    	}
			//BluetoothPrintDriver.AddCodePrint(BluetoothPrintDriver.Code128_B, print1DBarcodeStr);
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.Code128_B(tmp);
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();


			String val= " ";
			for (int i = 0; i < print1DBarcodeStr.length(); i++) {
				if(i < print1DBarcodeStr.length() -1 )
					val = val + print1DBarcodeStr.charAt(i) + "    ";
				else  val = val + print1DBarcodeStr.charAt(i);
			}

			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.ImportData(val);
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/*------------------------ LIGNE --------------------------*/
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddInverse((byte)0x00);
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/************************ FOOTER *******************************/
			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.SetCharacterFont((byte) 0x031);
			BluetoothPrintDriver.ImportData(removeDiacritic(ticket.getDescription()));
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.ImportData("Tel : "+ticket.getTel());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.ImportData("Fax : "+ticket.getFax());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.ImportData("I.F:"+ticket.getIF()+"  Patente:"+ticket.getPatente());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.ImportData(removeDiacritic(ticket.getMsg()));
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.AddAlignMode((byte) 0x01);//����
			BluetoothPrintDriver.ImportData(ticket.getSiteWeb());
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/************************ Ligne pointier pour La Fin ****************************/
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			BluetoothPrintDriver.AddAlignMode((byte) 1);//����
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();


			for (int i = 0; i < 23; i++) {
				BluetoothPrintDriver.LF();
				BluetoothPrintDriver.excute();
				BluetoothPrintDriver.ClearData();
			}

			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			/*
			String powered = "JobService By GEO.COM";
			BluetoothPrintDriver.AddAlignMode((byte)1);//����
			BluetoothPrintDriver.SetLineSpace((byte)20);	
			BluetoothPrintDriver.SetZoom((byte)0x11);//���ߣ�����	
			//BluetoothPrintDriver.ImportData(new byte[]{0x1d,0x21,0x01}, 3);	//���ñ���
			BluetoothPrintDriver.ImportData(powered,true);
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();

			String web = "www.marocgeo.ma";
			BluetoothPrintDriver.AddAlignMode((byte)1);//����
			BluetoothPrintDriver.SetLineSpace((byte)20);	
			BluetoothPrintDriver.SetZoom((byte)0x11);//���ߣ�����	
			//BluetoothPrintDriver.ImportData(new byte[]{0x1d,0x21,0x01}, 3);	//���ñ���
			BluetoothPrintDriver.ImportData(web,true);
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			 */
			/*
			BluetoothPrintDriver.AddBold((byte)0x00);//����
			BluetoothPrintDriver.SetZoom((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver.AddAlignMode((byte) 1);//����
			BluetoothPrintDriver.ImportData(ticket.getLine());
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.LF();
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
			*/
		}else{
			onStart();
		}
		
		
		
		
	}


	public String main(String data) {
		int n = data.length()/16;
		int nr =data.length() - (n*16);
		List<String> tb = new ArrayList<String>();

		int k = 0;
		int m = 16;
		//System.out.println("lenght "+data.length()+" et div "+n);
		for (int i = 0; i < n; i++) {
			tb.add(data.substring(k, m));
			//System.out.println(data.substring(k, m-1));
			k = m;
			m = m+16;
			//System.out.println("on i "+i+ " k :"+k+" & m :"+m);
		}
		if(nr != 0){
			tb.add(data.substring(k, data.length()-1));
		}
		int s=0;

		String mt = "";
		for(String st:tb){
			//System.out.println("voila res "+st);
			int kl = st.length();
			int dif = 32 - kl;
			String sb = "";
			for (int i = 0; i < dif; i++) {
				sb = sb + " ";
			}
			st = st + sb;
			System.out.println(st);
			mt = mt + st;
		}
		return mt;
	}

	private String stringtoascci(String mString) {
		StringBuilder result = new StringBuilder();
		for (byte c : mString.getBytes())
			result.append(String.valueOf(c));
		String test = result.toString();
		return test;
	}

	public static String removeDiacritic(String source) {
		char[] vysl = new char[source.length()];
		char one;
		for (int i = 0; i < source.length(); i++) {
			one = source.charAt(i);
			if (one >= '\u00c0' && one <= '\u017f') {
				one = tab00c0.charAt((int) one - '\u00c0');
			}
			vysl[i] = one;
		}
		return new String(vysl);
	}
	
	public double offlineregl(){
		double res =0;
		if(this.lsreg.size() > 0){
			for (int i = 0; i < lsreg.size(); i++) {
				res += lsreg.get(i).getAmount();
			}
		}
		Log.e("ttamont",res+"");
		return res;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.e("data keyup","is in");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent4 = new Intent(ReglementTicketActivity.this, OfflineActivity.class);
			intent4.putExtra("user", compte);
			startActivity(intent4);
			return true;
		}
		return false;
	}
}
