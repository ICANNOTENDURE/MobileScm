package com.dhcc.scm.ui;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhcc.scm.R;

public class ScanForUpdateQty extends Activity {
	// ����ID
	private static String initID = "";
	// ���?��
	private static String transferQty = "";
	// �ӱ�ID
	private static String initi = "";
	// ��λ(����)
	private static String uom = "";
	// ����
	private static String transferNo = "";
	private static String transferstkcatrowid = "";
	private static String transferstkcatdesc = "";

	private static final int save_Initdetails = 2;

	private String parbarcode = "";
	private String tmpqty;
	
	private EditText inciname;
	private EditText uomdesc;
	private EditText batno;
	private EditText expdate;
	private EditText qty;
	private EditText inclb;
	private EditText sfubarcode;
	private Button saveItm;

	/* ����PDAɨ����� */
	Map<String, String> actionMap = new HashMap<String, String>();
	private DataReceiver dataReceiver = null;
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private String ACTION_CONTENT_NOTIFY_EMII = "com.ge.action.barscan";
	private String ACTION_CONTENT_NOTIFY_MOTO = "ACTION_CONTENT_NOTIFY_MOTO";
	private String ACTION_CONTENT_NOTIFY_HoneyWell = "com.android.server.scannerservice.broadcast";
	
	mobilecom com = new mobilecom();

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == save_Initdetails) {
				if (com.RetData.equals("0")) {
					Intent intent = new Intent();
					Bundle bundle2 = new Bundle();
					// ����name����Ϊtinyphp
					bundle2.putString("transferNo", transferNo);
					bundle2.putString("initID", initID);
					bundle2.putString("initCatGrpDesc", transferstkcatdesc);
					bundle2.putString("initCatGrpID", transferstkcatrowid);
					
					intent.putExtras(bundle2);
					// ��BundleЯ�����
					intent.setClass(ScanForUpdateQty.this,
							ScanForTransfer.class);
					ScanForUpdateQty.this.finish();
					startActivity(intent);
				}else if(com.RetData.equals("-2")){
					AlertDialog.Builder build = new Builder(
							ScanForUpdateQty.this);
					build.setIcon(R.drawable.add).setTitle("��ʾ")
							.setMessage("�����ѳ���,�����ٴ�ɨ�룡")
							.setPositiveButton("ȷ��", null).show();
					return;
				}else if(com.RetData.equals("-3")){
					AlertDialog.Builder build = new Builder(
							ScanForUpdateQty.this);
					build.setIcon(R.drawable.add).setTitle("��ʾ")
							.setMessage("����ת�Ƶ���ϸ������˶���ݺ����ԣ�")
							.setPositiveButton("ȷ��", null).show();
					return;
				}
			}
		}
	};

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.transferout_updateqty);
		// ��ҳ��������
		Bundle bundle = this.getIntent().getExtras();
		// ����nameֵ
		inciname = (EditText) findViewById(R.id.upd_inciName);
		inciname.setText(bundle.getString("incidesc"));

		uomdesc = (EditText) findViewById(R.id.upd_uomDesc);
		uomdesc.setText(bundle.getString("packuom"));

		uom = bundle.getString("uom");

		batno = (EditText) findViewById(R.id.upd_batNo);
		batno.setText(bundle.getString("batno"));

		expdate = (EditText) findViewById(R.id.upd_expdate);
		expdate.setText(bundle.getString("expdate"));

		qty = (EditText) findViewById(R.id.upd_transqty);
		tmpqty = bundle.getString("transqty");
		qty.setText(bundle.getString("transqty"));
		qty.setFocusable(true);

		inclb = (EditText) findViewById(R.id.upd_inclb);
		inclb.setText(bundle.getString("inclb"));
		
		sfubarcode = (EditText) findViewById(R.id.upd_barcode);
		sfubarcode.setText(bundle.getString("barcode"));
		parbarcode=bundle.getString("parbarcode");
		
		initi = bundle.getString("initi");
		transferQty = bundle.getString("transferNo");
		initID = bundle.getString("initID");
		transferNo = bundle.getString("transferNo");
		transferstkcatrowid= bundle.getString("stkcatgrprowid");
		transferstkcatdesc= bundle.getString("stkcatgrpdesc");
		// ���水ťע���¼�
		saveItm = (Button) findViewById(R.id.upd_saveItem);
		saveItm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				saveItem();
			}
		});
		
		//���ý���
		sfubarcode=(EditText) findViewById(R.id.upd_barcode);
		sfubarcode.setFocusable(true);
		sfubarcode.requestFocus();
		
		//���������
		sfubarcode.setInputType(InputType.TYPE_NULL);
	}

	// �޸�����
	public void saveItem() {
		qty = (EditText) findViewById(R.id.upd_transqty);
		String newqty = qty.getText().toString().toString();
		String barcodestr = sfubarcode.getText().toString();
		if (newqty.equals("") || newqty.equals("0")) {
			Toast.makeText(ScanForUpdateQty.this, "����������!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if ((!newqty.equals(tmpqty)) & barcodestr.equals("")) {
			Toast.makeText(ScanForUpdateQty.this, "�����ı�,����¼���������!", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		inclb = (EditText) findViewById(R.id.upd_inclb);
		String inclbStr = inclb.getText().toString();
		
		String ListData = initi + "^" + inclbStr + "^" + newqty + "^" + uom
				+ "^^";

		String Param = "&InitId=" + initID + "&ListData=" + ListData+ "&barcode=" + barcodestr+ "&parbarcode=" + parbarcode;

		//com.ThreadHttp("web.DHCST.DHCINIsTrfItm", "Save", Param, "Method",ScanForUpdateQty.this, save_Initdetails, handler);
		com.ThreadHttp("web.DHCST.DHCSTSUPCHAUNPACK", "saveTransferOut", Param, "Method",ScanForUpdateQty.this, save_Initdetails, handler);
	}

	//�������back��
	@Override
	 public boolean onKeyDown(int keyCode,KeyEvent event){
	    if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			Intent intent = new Intent();
			Bundle bundle2 = new Bundle();
			bundle2.putString("transferNo", transferNo);
			bundle2.putString("initID", initID);
			bundle2.putString("initCatGrpDesc", transferstkcatdesc);
			bundle2.putString("initCatGrpID", transferstkcatrowid);
			intent.putExtras(bundle2);
			// ��BundleЯ�����
			intent.setClass(ScanForUpdateQty.this,
					ScanForTransfer.class);
			ScanForUpdateQty.this.finish();
			startActivity(intent);
	    }
	    return false;
	 }
	
	/* ��������PDAɨ�빦�� */
	private void registerReceiver() {
		if (dataReceiver != null)
			return;
		dataReceiver = new DataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		intentFilter.addAction(ACTION_CONTENT_NOTIFY_EMII);
		intentFilter.addAction(ACTION_CONTENT_NOTIFY_MOTO);
		intentFilter.addAction(ACTION_CONTENT_NOTIFY_HoneyWell);

		registerReceiver(dataReceiver, intentFilter);
	}

	private void unregisterReceiver() {
		if (dataReceiver != null) {
			unregisterReceiver(dataReceiver);
		}
	}

	protected void onResume() { // onCreate֮���Լ�ÿ�κ������������д�������淵�ص�ʱ�򣬶������onResume()
		registerReceiver();
		// initialComponent();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}

	private class DataReceiver extends BroadcastReceiver {
		String content = "", result = "";

		@Override
		public void onReceive(Context context, Intent intent) {
			String paraName = "";
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY);
			} else if (intent.getAction().equals(ACTION_CONTENT_NOTIFY_EMII)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY_EMII);
			} else if (intent.getAction().equals(ACTION_CONTENT_NOTIFY_MOTO)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY_MOTO);
			} else if (intent.getAction().equals(
					ACTION_CONTENT_NOTIFY_HoneyWell)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY_HoneyWell);
			} else {
				return;
			}

			Bundle bundle = new Bundle();
			bundle = intent.getExtras();
			content = bundle.getString(paraName);
			if(content.equals(parbarcode)){
				AlertDialog.Builder build = new Builder(
						ScanForUpdateQty.this);
				build.setIcon(R.drawable.warning).setTitle("����")
						.setMessage("ɨ�������ȷ�Ϻ����ԣ�")
						.setPositiveButton("ȷ��", null).show();
				return;
			}
			sfubarcode.setText(content);
		}
	}

	public ScanForUpdateQty() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO,
				"com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_HoneyWell, "scannerdata");
	}
}
