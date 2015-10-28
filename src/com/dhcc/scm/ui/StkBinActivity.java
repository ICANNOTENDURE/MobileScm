package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class StkBinActivity extends Activity {
	private TextView stkbinIncidesc;
	private TextView stkbinQty;
	private TextView stkbinUom;
	private TextView stkbinBatNo;
	private TextView stkbinExpDate;
	private TextView stkbinUomID;
	private TextView stkbinInclb;
	private EditText stkbinPLabel;
	private EditText stkbinLLabel;
	private TextView stkbindesc;
	private Button btnClear=null;
	private Button btnSure=null;
	private TextView stkbincode;
    //����soundpool
	private SoundPool soundPool;
    SoundPool mSoundPool = null;
    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    
    private String ListData = new String();
    private static final int handle_Init = 1;
    private static final int handle_second = 2;
    private static final int handle_thrid = 3;
    private ProgressDialog progressDialog = null;
    private ArrayList<HashMap<String,Object>> listItem;
    /* ����PDAɨ����� ,ÿ�����涼Ҫô???*/
	Map<String, String> actionMap = new HashMap<String, String>();
	private DataReceiver dataReceiver = null;
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private String ACTION_CONTENT_NOTIFY_EMII = "com.ge.action.barscan";
	private String ACTION_CONTENT_NOTIFY_MOTO = "ACTION_CONTENT_NOTIFY_MOTO";
	private String ACTION_CONTENT_NOTIFY_HoneyWell = "com.android.server.scannerservice.broadcast";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_stkbin_main);
		/// ��ȡ����Ԫ��
		stkbinIncidesc = (TextView) findViewById(R.id.stkbin_incidesc);
		stkbinBatNo = (TextView) findViewById(R.id.stkbin_batno);
		stkbinExpDate = (TextView) findViewById(R.id.stkbin_expdate);
		stkbinQty = (TextView) findViewById(R.id.stkbin_qty);
		stkbinUom = (TextView) findViewById(R.id.stkbin_uomdesc);
		stkbinUomID = (TextView) findViewById(R.id.stkbin_uomrowid);
		stkbinInclb = (TextView) findViewById(R.id.stkbin_inclb);
		stkbinPLabel = (EditText) findViewById(R.id.stkbin_packlabel);
		stkbinLLabel = (EditText) findViewById(R.id.stkbin_linklabel);
		stkbindesc = (TextView) findViewById(R.id.stkbin_stkbindesc);
		stkbincode = (TextView) findViewById(R.id.stkbin_stkbincode);
		stkbinPLabel.setFocusable(true);
		stkbinPLabel.requestFocus();
		stkbinLLabel.clearFocus();
		stkbinPLabel.setInputType(InputType.TYPE_NULL);  //���������
		stkbinLLabel.setInputType(InputType.TYPE_NULL);
		///���
		btnClear = (Button) findViewById(R.id.stkbin_clear);
		btnClear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	clearUIContent();
            }
        });
		
		///ȷ��
		btnSure = (Button) findViewById(R.id.stkbin_sure);
		btnSure.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	LinkInclbStkBin();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	///����ת����ϸ�б�
	public void LoadDrgByBarCode(String barcode){
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(StkBinActivity.this, "���Ե�...", "����ҩƷ��ϸ...", true);
		String Param = "&LocId=" + LoginUser.UserLoc +"&BarCode=" + barcode+ "&IncCatGrp=";
		ThreadHttp("web.DHCST.DHCSTSUPCHAUNPACK", "QueryIncBatPackList", Param, "Method", StkBinActivity.this, handle_Init);
	}
		
	private void ThreadHttp(final String Cls, final String mth, final String Param, final String Typ, final Activity context, final int whatmsg) {
	
		Thread thread = new Thread() {
			public void run() {
				try {
					//this.sleep(100);
					try {
						ListData = HttpGetPostCls.LinkData(Cls, mth, Param, Typ, context);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = new Message();
					msg.what = whatmsg;
					handler.sendMessage(msg);
				} catch (Exception e) {
				}
			}
		};
		thread.start();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == handle_Init) {
				try {
					JSONObject retString = new JSONObject(ListData);
					String total = retString.getString("total");
					if (!total.equals("1")){
						setTipErrorMessage("ȡҩƷ��Ϣ����,��˶ԣ�");
						return;
					}
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					JSONObject itemsobj = jsonArrayinfo.getJSONObject(0);
					stkbinIncidesc.setText(itemsobj.getString("incidesc"));
					stkbinQty.setText(itemsobj.getString("qty"));
					stkbinUom.setText(itemsobj.getString("puomdesc"));
					stkbinBatNo.setText(itemsobj.getString("batno"));
					stkbinExpDate.setText(itemsobj.getString("expdate"));
					stkbinUomID.setText(itemsobj.getString("puomdr"));
					stkbinInclb.setText(itemsobj.getString("inclb"));
					stkbinPLabel.setText(itemsobj.getString("barcode"));
					stkbindesc.setText(itemsobj.getString("stkbindesc"));
					stkbincode.setText(itemsobj.getString("stkbincode"));
					
					///���ý���
					stkbinLLabel.setFocusable(true);
					stkbinLLabel.requestFocus();
					stkbinPLabel.clearFocus();
					
			        progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (paramMessage.what == handle_second) {
				progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				//JSONObject retString = new JSONObject(ListData);
				if (ListData.indexOf("-2") != -1){
			        Toast tst = Toast.makeText(StkBinActivity.this, ListData, Toast.LENGTH_SHORT);
			        tst.show();
				}else if (!ListData.contains("0")){
					setTipErrorMessage(ListData);
				}else{
					//clearUIContent();
			        Toast tst = Toast.makeText(StkBinActivity.this, "�����ɹ���", Toast.LENGTH_SHORT);
			        tst.show();
				}
			}
		}
	};
	
	//���õ�λ���س��������ѶԻ���
	public void setTipErrorMessage(String ErrorMessage){
		progressDialog.dismiss();  //�������б���ݣ���رնԻ���
		AlertDialog.Builder build = new Builder(StkBinActivity.this);
		build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage(ErrorMessage)
		.setPositiveButton("ȷ��", null).show();
	}
	
	///��ս�������
	public void clearUIContent(){
		stkbinIncidesc.setText(null);
		stkbinQty.setText(null);
		stkbinUom.setText(null);
		stkbinBatNo.setText(null);
		stkbinExpDate.setText(null);
		stkbinUomID.setText(null);
		stkbinInclb.setText(null);
		stkbinPLabel.setText(null);
		stkbinLLabel.setText(null);
		stkbindesc.setText(null);
		stkbincode.setText(null);
		///���ý���
		stkbinPLabel.setFocusable(true);
		stkbinPLabel.requestFocus();
		stkbinLLabel.clearFocus();
	}
	
	///��������
	public void LinkInclbStkBin(){
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(StkBinActivity.this, "���Ե�...", "����������...", true);
		String Param = "&ListDetail=" + stkbinInclb.getText().toString() + "^" + stkbinLLabel.getText().toString() ;
		ThreadHttp("web.DHCST.INCStkBin", "LinkInclbStkBin", Param, "Method", StkBinActivity.this, handle_second);
	}
	
	///���ҩƷ�Ƿ�ͻ��ܹ���
	public void CheckInclbAndStkBinIfLink(){
		String stkbinLabel = stkbinLLabel.getText().toString();
		String stkbinLcode = stkbincode.getText().toString();
		if((stkbinLcode.equals(""))){
			//��������
		    mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 3);
		    soundMap.put(1 , mSoundPool.load(this, R.raw.enough , 1));
			return;
		}
		if (!stkbinLabel.equals(stkbinLcode)){
			setTipErrorMessage("ҩƷ�ͻ�λ��ƥ�䣡");
		}
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

	
	protected void onResume() {  //onCreate֮���Լ�ÿ�κ������������д�������淵�ص�ʱ�򣬶������onResume()
    	registerReceiver();
    	//initialComponent();
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
			
			if (stkbinPLabel.hasFocus()){
				LoadDrgByBarCode(content);
			}
			if (stkbinLLabel.hasFocus()){
				stkbinLLabel.setText(content);
				CheckInclbAndStkBinIfLink();
			}
		}

	}

	public StkBinActivity() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO,
				"com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_HoneyWell, "scannerdata");
	}

}
