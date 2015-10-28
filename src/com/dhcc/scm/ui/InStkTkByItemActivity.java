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
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.InStkTkBatDetAdapter;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class InStkTkByItemActivity extends Activity {
	private String inst;
	private ListView istMBatList;
	private TextView iskIncidesc;
	private TextView iskQty;
	private TextView iskUom;
	private TextView iskBatNo;
	private TextView iskExpDate;
	private TextView iskUomID;
	private TextView iskInclb;
	private Button btnClear=null;
	private Button btnSure=null;
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
		setContentView(R.layout.activity_instktkbyitem_main);
		
		iskIncidesc = (TextView) findViewById(R.id.instk_incidesc);
		iskBatNo = (TextView) findViewById(R.id.instk_batno);
		iskExpDate = (TextView) findViewById(R.id.instk_expdate);
		iskQty = (TextView) findViewById(R.id.instk_qty);
		iskUom = (TextView) findViewById(R.id.instk_uomdesc);
		iskUomID = (TextView) findViewById(R.id.instk_uomrowid);
		iskInclb = (TextView) findViewById(R.id.instk_inclb);
		
	    //��ȡIntent�е�Bundle����
        Bundle bundle = this.getIntent().getExtras();
        //��ȡBundle�е���ݣ�ע�����ͺ�key
        inst = bundle.getString("inst");
        
		//��ȡlistview
        istMBatList = (ListView) findViewById(R.id.instk_listview);
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.activity_instktkbyitem_mhearder, null);
		istMBatList.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		istMBatList.setAdapter(null);
		
		///���
		btnClear = (Button) findViewById(R.id.instk_clear);
		btnClear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	clearUIContent();
            }
        });
		
		///ȷ��
		btnSure = (Button) findViewById(R.id.instk_sure);
		btnSure.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	saveInStkTkDet();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	///��ս�������
	public void clearUIContent(){
		iskIncidesc.setText(null);
		iskQty.setText(null);
		iskUom.setText(null);
		iskBatNo.setText(null);
		iskExpDate.setText(null);
		iskUomID.setText(null);
		iskInclb.setText(null);
	}
	
	///�����̵���ϸ
	public void saveInStkTkDet(){
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(InStkTkByItemActivity.this, "���Ե�...", "������ϸ��...", true);
		String inclb = iskInclb.getText().toString();
		String CountQty = iskQty.getText().toString();
		String CountUom = iskUomID.getText().toString();
		String Param = "&ListData=" + inst+"^^"+LoginUser.UserDR+"^"+CountQty+"^"+CountUom+"^^^"+inclb;
		ThreadHttp("web.DHCST.AndroidInStkTk", "SaveInStkItmWd", Param, "Method", InStkTkByItemActivity.this, handle_thrid);
	}
	
	///����ת����ϸ�б�
	public void LoadDrgByBarCode(String barcode){
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(InStkTkByItemActivity.this, "���Ե�...", "����ҩƷ��ϸ...", true);
		String Param = "&LocId=" + LoginUser.UserLoc +"&BarCode=" + barcode+ "&IncCatGrp=";
		ThreadHttp("web.DHCST.DHCSTSUPCHAUNPACK", "QueryIncBatPackList", Param, "Method", InStkTkByItemActivity.this, handle_second);
	}
	
	///�������̵�������ϸ�б�
	public void LoadInStkTkBatDet(){
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		//progressDialog = ProgressDialog.show(InStkTkByItemActivity.this, "���Ե�...", "�����ϸ...", true);
		String Param = "&inst=" + inst +"&inclb=" + iskInclb.getText().toString();
		ThreadHttp("web.DHCST.AndroidInStkTk", "jsQueryInStkTkBatDet", Param, "Method", InStkTkByItemActivity.this, handle_Init);
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
					if (ListData.equals("")){
						 progressDialog.dismiss(); //�������б���ݣ���رնԻ���
						return; }
					JSONObject retString = new JSONObject(ListData);
					String total = retString.getString("results");
					if (total.equals("0")){
						 progressDialog.dismiss(); //�������б���ݣ���رնԻ���
						return; }
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					listItem = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < jsonArrayinfo.length(); i++) {
						JSONObject itemsobj = jsonArrayinfo.getJSONObject(i);
						HashMap<String, Object> map=new HashMap();
						map.put("Insti", itemsobj.getString("Insti"));
						map.put("Inclb", itemsobj.getString("Inclb"));
						map.put("BatNo", itemsobj.getString("BatNo"));
						map.put("ExpDate", itemsobj.getString("ExpDate"));
						map.put("FreUomDesc", itemsobj.getString("FreUomDesc"));
						map.put("FreQty", itemsobj.getString("FreQty"));
						map.put("CountQty", itemsobj.getString("CountQty"));
						listItem.add(map);
					}
				    
					InStkTkBatDetAdapter mAdapter = new InStkTkBatDetAdapter(InStkTkByItemActivity.this,listItem);
				    istMBatList.setAdapter(mAdapter);
			        progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (paramMessage.what == handle_second) {
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
					iskIncidesc.setText(itemsobj.getString("incidesc"));
					iskQty.setText(itemsobj.getString("qty"));
					iskUom.setText(itemsobj.getString("puomdesc"));
					iskBatNo.setText(itemsobj.getString("batno"));
					iskExpDate.setText(itemsobj.getString("expdate"));
					iskUomID.setText(itemsobj.getString("puomdr"));
					iskInclb.setText(itemsobj.getString("inclb"));
					LoadInStkTkBatDet();
			        //progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (paramMessage.what == handle_thrid) {
				try {
					if (!ListData.equals("0")){
						setTipErrorMessage("ȷ�ϳ��?");
					}else{
						LoadInStkTkBatDet();
						clearUIContent();
				        Toast tst = Toast.makeText(InStkTkByItemActivity.this, "����ɹ���", Toast.LENGTH_SHORT);
				        tst.show();
					}
			        progressDialog.dismiss(); //�������б���ݣ���رնԻ���
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	//���õ�λ���س��������ѶԻ���
	public void setTipErrorMessage(String ErrorMessage){
		progressDialog.dismiss();  //�������б���ݣ���رնԻ���
		AlertDialog.Builder build = new Builder(InStkTkByItemActivity.this);
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
			LoadDrgByBarCode(content);
		}

	}

	public InStkTkByItemActivity() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO,
				"com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_HoneyWell, "scannerdata");
	}

}
