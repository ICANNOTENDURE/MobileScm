package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class ScanForTransfer extends Activity {

	private TextView no;
	private Integer scanselectindex=0;
	private SimpleAdapter mAdapter;
	private EditText barcode;
	private TextView StkCatGrpDescView = null;
	private TextView StkCatGrpRowIdView = null;
	private ListView mylv;
	private Button btnquery;
	private Button btn_back;
	private Button saveItem;
	private ProgressDialog progressDialog = null;
	private ProgressDialog progressDialog2 = null;
	private String RetData = new String();
	private static final int handle_Init = 1;
	private static final int save_Initdetails = 2;
	private static final int query_Initdetails = 3;
	private static final int audit_Initdetails = 4;
	private static final int del_Initdetails = 5;
	private static ArrayList<HashMap<String, Object>> mylistItem = new ArrayList<HashMap<String, Object>>();

	private static HashMap<String, Integer> mymap = new HashMap<String, Integer>();

	private static String tempBarcodeValue;
	private boolean completeflag = false;
	private static TextView titleview = null;
	// ��������ID
	private static String initID = "";
	String transferNo = "";
	String initCatGrpID = "";
	String initCatGrpDesc = "";
	/* ����PDAɨ����� */
	Map<String, String> actionMap = new HashMap<String, String>();
	private DataReceiver dataReceiver = null;
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private String ACTION_CONTENT_NOTIFY_EMII = "com.ge.action.barscan";
	private String ACTION_CONTENT_NOTIFY_MOTO = "ACTION_CONTENT_NOTIFY_MOTO";
	private String ACTION_CONTENT_NOTIFY_HoneyWell = "com.android.server.scannerservice.broadcast"; // by

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == handle_Init) {
				try {
					if (RetData.indexOf("-10") != -1) {
						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(
								ScanForTransfer.this);
						build.setIcon(R.drawable.warning).setTitle("����")
								.setMessage("�����ӦҩƷ����ⵥ���鲻��Ӧ!")
								.setPositiveButton("ȷ��", null).show();
						queryTransferDetails(initID);
						return;

					}

					if (RetData.indexOf("error") != -1) {
						// �������б���ݣ���رնԻ���
						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(
								ScanForTransfer.this);
						build.setIcon(R.drawable.warning).setTitle("����")
								.setMessage("��ȡ��ݴ���,�����������ӣ�")
								.setPositiveButton("ȷ��", null).show();
						return;
					}
					JSONObject retString = new JSONObject(RetData);
					String total = retString.getString("total");
					if (total.compareTo("0") == 0) {
						// �������б���ݣ���رնԻ���

						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(
								ScanForTransfer.this);
						build.setIcon(R.drawable.add).setTitle("��ʾ")
								.setMessage("��ݿ��޶�Ӧ����,��˶Ժ����ԣ�")
								.setPositiveButton("ȷ��", null).show();
						// ��ѯ������ϸ
						queryTransferDetails(initID);
						return;
					}
					String ListData = "";
					int d = 1;
					char Delim = (char) d;
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					// ==============================================
					if (jsonArrayinfo.length() > 1) {
						// �������б���ݣ���رնԻ���
						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(
								ScanForTransfer.this);
						build.setIcon(R.drawable.add).setTitle("��ʾ")
								.setMessage("�����Ӧ��¼�����2��,���Ƚ��в��װ,Ȼ�����ԣ�")
								.setPositiveButton("ȷ��", null).show();
						return;
					}
					JSONObject itemsobj = jsonArrayinfo.getJSONObject(0);
					Intent intent2 = new Intent();
					// ��BundleЯ�����
					Bundle bundle2 = new Bundle();
					// ����name����Ϊtinyphp
					bundle2.putString("incidesc", itemsobj
							.getString("incidesc").toString().trim());
					bundle2.putString("batno", itemsobj.getString("batno")
							.toString().trim());
					bundle2.putString("expdate", itemsobj.getString("expdate")
							.toString().trim());
					bundle2.putString("packuom", itemsobj.getString("uomdesc")
							.toString().trim());
					bundle2.putString("initID", initID);
					bundle2.putString("initi", "");
					bundle2.putString("transferNo", transferNo);
					bundle2.putString("inclb", itemsobj.getString("inclb")
							.toString().trim());
					bundle2.putString("transqty", itemsobj.getString("trqty")
							.toString().trim());
					bundle2.putString("uom", itemsobj.getString("puomdr")
							.toString().trim());
					bundle2.putString("parbarcode",
							itemsobj.getString("barcode").toString().trim());
					bundle2.putString("stkcatgrpdesc",
							StkCatGrpDescView.getText().toString().trim());
					bundle2.putString("stkcatgrprowid",
							StkCatGrpRowIdView.getText().toString().trim());
					intent2.putExtras(bundle2);
					intent2.setClass(ScanForTransfer.this,
							ScanForUpdateQty.class);
					onDestroy();
					// �������б���ݣ���رնԻ���
					progressDialog.dismiss();
					startActivity(intent2);
					// ================================================
					/*
					 * ArrayList<HashMap<String, Object>> listItem = new
					 * ArrayList<HashMap<String, Object>>(); for (int i = 0; i <
					 * jsonArrayinfo.length(); i++) { JSONObject itemsobj =
					 * jsonArrayinfo.getJSONObject(i); if ((ListData == "") ||
					 * (ListData.equals(""))) { ListData = "^" +
					 * itemsobj.getString("inclb").toString() .trim() + "^1^" +
					 * itemsobj.getString("puomdr").toString() .trim() + "^^"; }
					 * else { ListData = ListData + Delim + "^" +
					 * itemsobj.getString("inclb").toString() .trim() + "^1^" +
					 * itemsobj.getString("puomdr").toString() .trim() + "^^"; }
					 * } // ��ϸid^���id^����^��λ^������ϸid^��ע����ϸid^���id^����^��λ^������ϸid^��ע
					 * // ���������ϸ progressDialog.dismiss();
					 * saveTransferDetails(ListData);
					 */
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (paramMessage.what == save_Initdetails) {
				if (RetData.equals("0")) {
					queryTransferDetails(initID);
				} else {

					// �Զ���Toast
					Toast toast = Toast.makeText(getApplicationContext(),
							"��ݱ���ʧ��!" + RetData, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					LinearLayout toastView = (LinearLayout) toast.getView();
					ImageView imageCodeProject = new ImageView(
							getApplicationContext());
					imageCodeProject.setImageResource(R.drawable.android_ok);
					toastView.addView(imageCodeProject, 0);
					toast.show();

				}
			}
			if (paramMessage.what == query_Initdetails) {
				try {
					if (RetData.indexOf("error") != -1) {
						// �������б���ݣ���رնԻ���
						AlertDialog.Builder build = new Builder(
								ScanForTransfer.this);
						build.setIcon(R.drawable.add).setTitle("��ʾ")
								.setMessage("��ȡ��ݴ���,�����������ӣ�")
								.setPositiveButton("ȷ��", null).show();
						return;
					}

					JSONObject retString = new JSONObject(RetData);
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo2 = new JSONArray(jsonarr1);
					mylistItem = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < jsonArrayinfo2.length(); i++) {
						JSONObject itemsobj = jsonArrayinfo2.getJSONObject(i);
						HashMap<String, Object> map = new HashMap();
						map.put("���", i + 1);
						map.put("���", itemsobj.getString("inciDesc"));
						map.put("����", itemsobj.getString("qty"));
						map.put("���", itemsobj.getString("batNo"));
						map.put("Ч��", itemsobj.getString("expDate"));
						map.put("��װ��λ", itemsobj.getString("uomDesc"));
						map.put("ID", itemsobj.getString("inci"));
						map.put("uom", itemsobj.getString("uom"));
						map.put("barcode", itemsobj.getString("barcode"));
						map.put("parbarcode", itemsobj.getString("parbarcode"));
						map.put("inclb", itemsobj.getString("inclb"));
						map.put("initi", itemsobj.getString("initi"));
						mylistItem.add(map);

					}
					mAdapter = new SimpleAdapter(
							ScanForTransfer.this, mylistItem,
							R.layout.scanfor_transferout_item, new String[] {
									"���", "���", "����", "���", "Ч��", "��װ��λ", "ID",
									"barcode", "parbarcode", "inclb", "initi",
									"uom" }, new int[] { R.id.xh,
									R.id.incidesc, R.id.transqty, R.id.batno,
									R.id.expdate, R.id.packuom, R.id.inci,
									R.id.barcode, R.id.parbarcode, R.id.inclb,
									R.id.initi, R.id.uom });
					mylv.setAdapter(mAdapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			// ��˳��ⵥ
			if (paramMessage.what == audit_Initdetails) {
				if (RetData.indexOf("error") != -1) {
					progressDialog2.dismiss();
					// �������б���ݣ���رնԻ���
					AlertDialog.Builder build = new Builder(
							ScanForTransfer.this);
					build.setIcon(R.drawable.add).setTitle("��ʾ")
							.setMessage("��ȡ��ݴ���,�����������ӣ�")
							.setPositiveButton("ȷ��", null).show();
					return;
				}

				if (RetData.equals("-100")) {
					progressDialog2.dismiss();
					commDialog("���ⵥ���Ǵ����״̬!" + RetData);
					return;
				} else if (RetData.equals("-101")) {
					progressDialog2.dismiss();
					commDialog("����:" + RetData);
					return;
				} else if (RetData.equals("-99")) {
					progressDialog2.dismiss();
					commDialog("����ʧ��!" + RetData);
					return;
				} else if (RetData.equals("-1")) {
					progressDialog2.dismiss();
					commDialog("���³��ⵥ״̬ʧ��!" + RetData);
				} else if (RetData.equals("-3")) {
					progressDialog2.dismiss();
					commDialog("������ʧ��!" + RetData);
					return;
				} else if (RetData.equals("-4")) {
					progressDialog2.dismiss();
					commDialog("����̨��ʧ��!" + RetData);
					return;
				} else if (RetData.equals("-5")) {
					progressDialog2.dismiss();
					commDialog("�����ӱ�״̬ʧ��!" + RetData);
					return;
				} else if (RetData.equals("-6")) {
					progressDialog2.dismiss();
					commDialog("����ռ������ʧ��!" + RetData);
					return;
				} else if (RetData.equals("-7")) {
					progressDialog2.dismiss();
					commDialog("�Զ����ճ��ⵥʧ�ܣ����ֶ�����!" + RetData);
					return;
				} else if (!RetData.equals("0")) {
					progressDialog2.dismiss();
					commDialog("���ʧ��!" + RetData);
					return;
				}
				if (RetData.equals("0")) {
					completeflag = true;
					// ����ɹ�����˰�ť���������Ϊ������
					progressDialog2.dismiss();
					//saveItem = (Button) findViewById(R.id.saveItem);
					//saveItem.setEnabled(false);
					//saveItem.setVisibility(View.INVISIBLE); // ��ť������

					// �Զ���Toast
					Toast toast = Toast.makeText(getApplicationContext(),
							"��˳ɹ�!" + RetData, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					LinearLayout toastView = (LinearLayout) toast.getView();
					ImageView imageCodeProject = new ImageView(
							getApplicationContext());
					imageCodeProject.setImageResource(R.drawable.accept_ok);
					toastView.addView(imageCodeProject, 0);
					toast.show();
				}
			}
			///ɾ����ϸ
			if (paramMessage.what == del_Initdetails) {
				progressDialog.dismiss();  //�������б���ݣ���رնԻ���
				// �Զ���Toast
				Toast tst = null;
				if (RetData.equals("0")) {
					mylistItem.remove(scanselectindex-1);//ѡ���е�λ��   
					mAdapter.notifyDataSetChanged();
					scanselectindex=0;
					tst = Toast.makeText(ScanForTransfer.this, "ɾ��ɹ���", Toast.LENGTH_SHORT);
				} else {
					tst = Toast.makeText(ScanForTransfer.this, "ɾ��ʧ�ܣ�", Toast.LENGTH_SHORT);
				}
				tst.show();
			}
		}
	};

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scanfor_transferout_list);
		super.onCreate(savedInstanceState);
		StkCatGrpDescView = (TextView) findViewById(R.id.StkCatGrpDesc);
		StkCatGrpRowIdView = (TextView) findViewById(R.id.StkCatGrpID);
		// ��ҳ��������
		Bundle bundle = this.getIntent().getExtras();
		// ����nameֵ
		transferNo = bundle.getString("transferNo"); // ����
		initID = bundle.getString("initID"); // ����ID
		initCatGrpID = bundle.getString("initCatGrpID"); // /����id
		initCatGrpDesc = bundle.getString("initCatGrpDesc");
		StkCatGrpDescView.setText(initCatGrpDesc);
		StkCatGrpRowIdView.setText(initCatGrpID);
		// ���յ���
		no = (TextView) findViewById(R.id.TranNo);
		no.setText(transferNo);

		// ��ȡlistview
		mylv = (ListView) findViewById(R.id.inciitmlist);
		// ������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainViewm = mLayoutInflater
				.inflate(R.layout.scanfor_transferout_header, null);
		// ��ͷ���ܵ��
		mainViewm.setOnClickListener(null);
		mylv.addHeaderView(mainViewm, null, false);
		// ���ӵ����¼���������ʱ����޸�����
		mylv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView,
					int arg2, long arg3) {

				if (completeflag) {
					AlertDialog.Builder build = new Builder(
							ScanForTransfer.this);
					build.setIcon(R.drawable.add).setTitle("��ʾ")
							.setMessage("��������ɣ�").setPositiveButton("ȷ��", null)
							.show();
					return;
				}
				HashMap<String, String> map = (HashMap<String, String>) mylv
						.getItemAtPosition(arg2);
				Intent intent2 = new Intent();
				// ��BundleЯ�����
				Bundle bundle2 = new Bundle();
				// ����name����Ϊtinyphp
				bundle2.putString("incidesc", map.get("���"));
				bundle2.putString("batno", map.get("���"));
				bundle2.putString("expdate", map.get("Ч��"));
				bundle2.putString("packuom", map.get("��װ��λ"));
				bundle2.putString("initi", map.get("initi"));
				bundle2.putString("inclb", map.get("inclb"));
				bundle2.putString("transqty", map.get("����"));
				bundle2.putString("transferNo", transferNo);
				bundle2.putString("initID", initID);
				bundle2.putString("uom", map.get("uom"));
				bundle2.putString("transferNo", transferNo);
				bundle2.putString("barcode", map.get("barcode"));
				bundle2.putString("parbarcode", map.get("parbarcode"));
				bundle2.putString("stkcatgrprowid", initCatGrpID);
				bundle2.putString("stkcatgrpdesc", initCatGrpDesc);

				intent2.putExtras(bundle2);
				intent2.setClass(ScanForTransfer.this, ScanForUpdateQty.class);
				startActivity(intent2);
			}
		});
		// ������ʱ��
		mylv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
            	scanselectindex=position;
            	registerForContextMenu(mylv);
				return false;
			}
		});

		btnquery = (Button) findViewById(R.id.btn_query);
		btnquery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				queryTransferDetails(initID);
			}
		});

		saveItem = (Button) findViewById(R.id.saveItem);
		saveItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (arg0.getId() == R.id.saveItem) {
					// ȷ�ϳ��ⵥ����˳��ⵥ
					AuditTransferDetails(initID);
				}
			}
		});
		// ҳ����ص�ʱ���ѯ���
		queryTransferDetails(initID);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("����");     
            menu.add(0, 0, 0, "�Ƴ�ѡ����"); 
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item)  
	{  
		if (scanselectindex<1)
		{
			return true;
		}
		delTransferDetails();
//		mylistItem.remove(scanselectindex-1);//ѡ���е�λ��   
//		mAdapter.notifyDataSetChanged();
//		scanselectindex=0;
        return true;  
    }
	
	/* ��ѯ����б� */
	private void QueryItemList(String barcode) {
		// ���״̬
		progressDialog = ProgressDialog.show(ScanForTransfer.this, "���Ե�...",
				"��ȡ�����...", true);
		String Param = "&LocId=" + LoginUser.UserLoc + "&BarCode=" + barcode
				+ "&IncCatGrp="
				+ StkCatGrpRowIdView.getText().toString().trim();
		ThreadHttpStr("web.DHCST.DHCSTSUPCHAUNPACK", "QueryIncBatPackList",
				Param, "Method", ScanForTransfer.this, 1);
	}

	// ���������ϸ
	private void saveTransferDetails(String dataString) {
		// ���״̬
		TextView barCodeView = null; //(TextView) findViewById(R.id.barcode);
		String barcode = barCodeView.getText().toString().toUpperCase();
		if (barcode.equals(tempBarcodeValue)) {
			// �Զ���Toast
			Toast toast = Toast.makeText(getApplicationContext(), "�����ظ�ɨ��!",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			LinearLayout toastView = (LinearLayout) toast.getView();
			ImageView imageCodeProject = new ImageView(getApplicationContext());
			imageCodeProject.setImageResource(R.drawable.android_ok);
			toastView.addView(imageCodeProject, 0);
			toast.show();
			// ֱ�Ӳ�ѯ,����
			queryTransferDetails(initID);
			return;
		}
		String Param = "&InitId=" + initID + "&ListData=" + dataString;

		ThreadHttpStr("web.DHCST.DHCINIsTrfItm", "Save", Param, "Method",
				ScanForTransfer.this, 2);
	}

	// ��ѯ���ⵥ��ϸ
	private void queryTransferDetails(String initID) {
		mylv.setAdapter(null);
		// ���״̬
		String Param = "&init=" + initID;
		ThreadHttpStr("web.DHCST.AndroidTransferOut", "jsQueryTransferOutDet", Param,
				"Method", ScanForTransfer.this, 3);
	}

	// ɾ����ⵥ��ϸ
	private void delTransferDetails() {
		// ���״̬
		progressDialog = ProgressDialog.show(ScanForTransfer.this, "���Ե�...",
				"ɾ�������...", true);
		// ���״̬
		String initi = mylistItem.get(scanselectindex-1).get("initi").toString();
		String Param = "&initi=" + initi;
		ThreadHttpStr("web.DHCST.AndroidTransferOut", "Delete", Param,
				"Method", ScanForTransfer.this, 5);
	}
	
	private void ThreadHttpStr(final String Cls, final String mth,
			final String Param, final String Typ, final Activity context,
			final int whatmsg) {

		Thread thread = new Thread() {
			public void run() {
				try {
					// this.sleep(100);
					try {
						RetData = HttpGetPostCls.LinkData(Cls, mth, Param, Typ,
								context);
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

	/**
	 * Activity�У�ָMainActivity�ࣩ��дonActivityResult����
	 * 
	 * requestCode �����룬������startActivityForResult()���ݹ�ȥ��ֵ resultCode
	 * ����룬��������ڱ�ʶ������������ĸ���Activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!data.equals(null)) {
			String trNo = data.getExtras().getString("transferNo").toString();
			String trInitID = data.getExtras().getString("initID").toString();
			String trStkCatGrpDesc = data.getExtras().getString("initCatGrpDesc").toString();
			String trStkCatGrpID = data.getExtras().getString("initCatGrpID").toString(); 
			switch (resultCode) {
			// ������
			case 0:
				StkCatGrpDescView.setText(trStkCatGrpDesc);
				StkCatGrpRowIdView.setText(trStkCatGrpID);
				initID = trInitID;
				// ���յ���
				no.setText(trNo);
				break;
			default:
				break;
			}			
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
			if (completeflag) {
				AlertDialog.Builder build = new Builder(ScanForTransfer.this);
				build.setIcon(R.drawable.add).setTitle("��ʾ")
						.setMessage("��������ɣ�").setPositiveButton("ȷ��", null)
						.show();
				return;
			}
			QueryItemList(content);
		}
	}

	public ScanForTransfer() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO,
				"com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_HoneyWell, "scannerdata");
	}

	// ���������ϸ,���
	private void AuditTransferDetails(String initID) {
		// ���״̬
		progressDialog2 = ProgressDialog.show(ScanForTransfer.this, "���Ե�...",
				"����Ŭ��������....", true);
		String Param = "&init=" + initID + "&comp=Y&status=11&type=6";
		ThreadHttpStr("web.DHCST.DHCINIsTrf", "SetCompleted", Param,
				"Method", ScanForTransfer.this, 4);
	}

	private void commDialog(String message) {
		AlertDialog.Builder build = new Builder(ScanForTransfer.this);
		build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage(message)
				.setPositiveButton("ȷ��", null).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(ScanForTransfer.this, IndexActivity.class);
			ScanForTransfer.this.finish();
			startActivity(intent);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
