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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.TransferInItmAdapter;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class TransferInDetActivity extends Activity {
	private EditText trInEndDate;
	private ListView trInItmList;
	private String trInit;
	private String trIniti=null;
	private TextView trInIncidesc;
	private TextView trInQty;
	private TextView trInUom;
	private TextView trInBatNo;
	private TextView trInExpDate;
	private Button btnUnRecive=null;
	private Button btnRecive=null;
	private Button btnSure=null;
    private String ListData = new String();
    private static final int handle_Init = 1;
    private static final int handle_second = 2;
    private static final int handle_thrid = 3;
    private ProgressDialog progressDialog = null;
    private ArrayList<HashMap<String,Object>> listItem;
    
	/* ����PDAɨ����� */
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
		setContentView(R.layout.transfer_in_detail);
		
		trInIncidesc = (TextView) findViewById(R.id.trin_incidesc);
		trInQty = (TextView) findViewById(R.id.trin_qty);
		trInUom = (TextView) findViewById(R.id.trin_uom);
		trInBatNo = (TextView) findViewById(R.id.trin_batno);
		trInExpDate = (TextView) findViewById(R.id.trin_expdate);
		
	    //��ȡIntent�е�Bundle����
        Bundle bundle = this.getIntent().getExtras();
        //��ȡBundle�е���ݣ�ע�����ͺ�key
        trInit = bundle.getString("trInit");

		//��ȡlistview
		trInItmList = (ListView) findViewById(R.id.trin_itemlistview);
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.activity_tranferin_detheader, null);
		trInItmList.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		trInItmList.setAdapter(null);
		///δ����
		btnUnRecive = (Button) findViewById(R.id.btn_unreciverr);
		btnUnRecive.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background_select));
		btnUnRecive.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	btnUnRecive.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background_select));
            	btnRecive.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background)); 
            	LoadTransferInDetList("21");
            }
        });
		///�ѽ���
		btnRecive = (Button) findViewById(R.id.btn_recive);
		btnRecive.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	btnRecive.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background_select));
            	btnUnRecive.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background)); 
            	LoadTransferInDetList("31");
            }
        });
		///ȷ�Ͻ���
		btnSure = (Button) findViewById(R.id.btn_sure);
		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    if(trIniti ==null){
					Toast tst = Toast.makeText(TransferInDetActivity.this, "ת���ӱ�IDΪ��,���ܽ��գ�", Toast.LENGTH_SHORT);
			        tst.show();
			        return;
				}
		        TransInAuditPart();
			}

		});
		
		///����ת����ϸ�б�
		LoadTransferInDetList("21");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	///����ת����ϸ�б�
	public void LoadTransferInDetList(String flag){
		trInItmList.setAdapter(null);
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(TransferInDetActivity.this, "���Ե�...", "����ת����ϸ�б�...", true);
		String Param = "&init=" + trInit+"&flag=" + flag;
		ThreadHttp("web.DHCST.AndroidTransferIn", "jsQueryTransferInDet", Param, "Method", TransferInDetActivity.this, handle_Init);
	}
	
	///����ת����ϸ�б�
	public void LoadTranferByBarCode(String barcode){
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(TransferInDetActivity.this, "���Ե�...", "����ҩƷ��ϸ...", true);
		String Param = "&Ininit=" + trInit+"&barcode=" + barcode;
		ThreadHttp("web.DHCST.AndroidTransferIn", "jsQueryTranferByBarCode", Param, "Method", TransferInDetActivity.this, handle_second);
	}
	
	///ɨ�����
	public void TransInAuditPart(){
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(TransferInDetActivity.this, "���Ե�...", "������ϸ...", true);
		String Param = "&initi=" + trIniti+"&UserId=" + LoginUser.UserDR;
		ThreadHttp("web.DHCST.AndroidTransferIn", "TransInAuditPart", Param, "Method", TransferInDetActivity.this, handle_thrid);
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
					String ErrCode = retString.getString("ErrCode");
					if (!ErrCode.equals("0")){
						setTipErrorMessage("��ѯת�Ƶ���ϸ���?");
						return;
					}
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					listItem = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < jsonArrayinfo.length(); i++) {
						JSONObject itemsobj = jsonArrayinfo.getJSONObject(i);
						HashMap<String, Object> map=new HashMap();
						map.put("Initi", itemsobj.getString("Initi"));
						map.put("TrInciDesc", itemsobj.getString("TrInciDesc"));
						map.put("TrQty", itemsobj.getString("TrQty"));
						map.put("TrUom", itemsobj.getString("TrUom"));
						map.put("TrReqQty", itemsobj.getString("TrReqQty"));
						listItem.add(map);
					}
				    
				    TransferInItmAdapter mAdapter = new TransferInItmAdapter(TransferInDetActivity.this,listItem);
				    trInItmList.setAdapter(mAdapter);
			        progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (paramMessage.what == handle_second) {
				try {
					JSONObject retString = new JSONObject(ListData);
					String ErrCode = retString.getString("ErrCode");
					if (ErrCode.equals("-1")){
						setTipErrorMessage("���벻����,��˶Ժ����ԣ�");
						return;
					}
					if (ErrCode.equals("-2")){
						setTipErrorMessage("ת�Ƶ������ڶ�Ӧ����,���ʵ�����ԣ�");
						return;
					}
					if (ErrCode.equals("-3")){
						setTipErrorMessage("�������ӦҩƷ�ѽ��գ�");
						return;
					}
					if (ErrCode.equals("-4")){
						setTipErrorMessage("�����Ӧ���ݻ�δ����ȷ��,���ʵ�����ԣ�");
						return;
					}
					if (ErrCode.equals("-5")){
						setTipErrorMessage("���Ϊ��,���ʵ�����ԣ�");
						return;
					}
					if (ErrCode.equals("-7")){
						setTipErrorMessage("��λΪ��,���ʵ�����ԣ�");
						return;
					}
					if (!ErrCode.equals("0")){
						setTipErrorMessage("ȡ��ϸ���?");
						return;
						}
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					JSONObject itemsobj = jsonArrayinfo.getJSONObject(0);
					trIniti=itemsobj.getString("trIniti");
					trInIncidesc.setText(itemsobj.getString("trInciDesc"));
					trInQty.setText(itemsobj.getString("trQty"));
					trInUom.setText(itemsobj.getString("trUom"));
					trInBatNo.setText(itemsobj.getString("trBatNo"));
					trInExpDate.setText(itemsobj.getString("trExpDate"));
			        progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (paramMessage.what == handle_thrid) {
				try {
					JSONObject retString = new JSONObject(ListData);
					String ErrCode = retString.getString("ErrCode");
					if (!ErrCode.equals("0")){
						setTipErrorMessage("���ճ��?");
					}else{
						trIniti=null;
						trInIncidesc.setText(null);
						trInQty.setText(null);
						trInUom.setText(null);
						trInBatNo.setText(null);
						trInExpDate.setText(null);
				        Toast tst = Toast.makeText(TransferInDetActivity.this, "����ɹ���", Toast.LENGTH_SHORT);
				        tst.show();
					}
			        progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	//���õ�λ���س��������ѶԻ���
	public void setTipErrorMessage(String ErrorMessage){
		progressDialog.dismiss();  //�������б���ݣ���رնԻ���
		AlertDialog.Builder build = new Builder(TransferInDetActivity.this);
    	build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage(ErrorMessage)
    	.setPositiveButton("ȷ��", null).show();
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
			LoadTranferByBarCode(content);
		}
	}

	public TransferInDetActivity() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO,
				"com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_HoneyWell, "scannerdata");
	}
}
