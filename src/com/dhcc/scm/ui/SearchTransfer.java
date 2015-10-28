package com.dhcc.scm.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.SearchTransferAdapter;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class SearchTransfer extends Activity {
	private EditText trOutStartdDate;
	private EditText trOutEndDate;
	private EditText trOutfromloc;
	private String trOutfromlocID;
	private EditText trOutToLoc;
	private String trOutToLocID = "";
	private ListView trOutMainList;
	private Calendar c = null;
	private ImageView trOutSeekloc;
	private ImageView trOutsearch; // ���Ұ�ť
	private ProgressDialog progressDialog = null;
	private static final int handle_Init = 1;
	private String ListData = new String();
	private ArrayList<HashMap<String,Object>> listItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_transferout_list);
		
		//����
		trOutStartdDate=(EditText)findViewById(R.id.troutstartdate);
		trOutEndDate=(EditText)findViewById(R.id.troutenddate);
		
		//��ѯ����
		trOutToLoc=(EditText)findViewById(R.id.trouttoloc);
		trOutfromloc=(EditText)findViewById(R.id.troutfromloc);
		
		//��ȡlistview
		trOutMainList = (ListView) findViewById(R.id.trout_mainlistview);
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.search_transferout_header, null);
		trOutMainList.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		trOutMainList.setAdapter(null);
		// ���ӵ����¼���������ʱ����޸�����
		trOutMainList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView,
					int arg2, long arg3) {
				//��ת��ɨ�����
				Intent intent=new Intent();
				// ��BundleЯ�����
				Bundle bundle = new Bundle();
				// ����name����Ϊtinyphp

				TextView trNo=(TextView)convertView.findViewById(R.id.s_transferno);
				TextView trInti=(TextView)convertView.findViewById(R.id.s_initID);
				TextView trStkCatGrpId=(TextView)convertView.findViewById(R.id.s_catgrpid);
				TextView trCatGrpDesc=(TextView)convertView.findViewById(R.id.s_catgrpdesc);
				bundle.putString("transferNo",trNo.getText().toString());
				bundle.putString("initID",trInti.getText().toString());
				bundle.putString("initCatGrpID",trStkCatGrpId.getText().toString());
				bundle.putString("initCatGrpDesc",trCatGrpDesc.getText().toString());
				intent.putExtras(bundle);
				intent.setClass(SearchTransfer.this, ScanForTransfer.class);
				startActivity(intent);
			}
		});
		// �����¼�
		trOutStartdDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(1);
            	}
        });
		trOutEndDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(2);
            }
        });
		trOutStartdDate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
	            	showDialog(1);
		        }
		    }
		});
		trOutEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
		            showDialog(2);
		        }
		    }
		});
		
		//���������
		trOutStartdDate.setInputType(InputType.TYPE_NULL);
		trOutEndDate.setInputType(InputType.TYPE_NULL);
		
		trOutSeekloc=(ImageView)findViewById(R.id.btn_troutseekloc);
		// ����������¼�
		trOutSeekloc.setOnClickListener(new View.OnClickListener() {
			@Override
	        public void onClick(View v) {
				initInTrFromLoc();
	        }
		});
		trOutsearch = (ImageView)findViewById(R.id.btn_troutsearch);
		//��ѯ�¼�
		trOutsearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	QueryTransferOut();
            }
        });
		
		deFaultDate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/// �����������
	public void initInTrFromLoc() {
		String locinputtext="";
		locinputtext=trOutToLoc.getText().toString().trim();
		Intent intent = new Intent();
		// ��BundleЯ�����
		Bundle bundle = new Bundle();
		// ����name����Ϊtinyphp
		bundle.putString("flag", "from");
		bundle.putString("inputtext", locinputtext);
		intent.putExtras(bundle);
		intent.setClass(SearchTransfer.this, TransferOutLocList.class);
		startActivityForResult(intent, 0);
	}
	
	/**
	 * ����Ĭ������
	 * 
	 * @Title: deFaultDate
	 * @Description: TODO(������һ�仰�����������������)
	 * @param �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	@SuppressLint("SimpleDateFormat")
	public void deFaultDate() {
		// Ĭ������
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
		trOutStartdDate.setText(st.format(new Date()).toString());
		SimpleDateFormat et = new SimpleDateFormat("yyyy-MM-dd");
		trOutEndDate.setText(et.format(new Date()).toString());
		// Ĭ�ϵ�¼����
		trOutfromloc.setText(LoginUser.LocDesc);
		trOutfromloc.setEnabled(false);
		trOutfromlocID = LoginUser.UserLoc;
	}
	
    /**
     * �������ڼ�ʱ��ѡ��Ի���
     */
    @Override
    protected Dialog onCreateDialog(final int id) {
    	
        Dialog dialog = null;
        c = Calendar.getInstance();
        dialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                	if (id==1){
                		trOutStartdDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                	}
                	else{
                		trOutEndDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                		
                	}
                }
            }, 
            c.get(Calendar.YEAR), // �������
            c.get(Calendar.MONTH), // �����·�
            c.get(Calendar.DAY_OF_MONTH) // ��������
        );
        return dialog;
    }
    
    private void hideIM(View edt){
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = edt.getWindowToken();
             
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        }
        catch (Exception e) {
             
        }
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
			String result = data.getExtras().getString("locdesc").toString();// �õ���Activity
			String locid = data.getExtras().getString("locid").toString();// �õ���Activity
			switch (resultCode) {
			// ������
			case 1:
				// �������ֵ������ã�������>=0
				trOutToLoc.setText(result);
				trOutToLocID = locid; // ����ID
				break;
			default:
				break;
			}
		}
	}
	
	private void QueryTransferOut () {
		trOutMainList.setAdapter(null);
		String startDate = trOutStartdDate.getText().toString();
		String endDate = trOutEndDate.getText().toString();

		//���״̬
		progressDialog = ProgressDialog.show(SearchTransfer.this, "���Ե�...", "��ȡ�����...", true);
		String Param ="&startDate="+startDate+"&endDate="+endDate+"&fromLocID="+trOutfromlocID+"&toLocID="+trOutToLocID;
		ThreadHttp("web.DHCST.AndroidTransferOut", "jsQueryTransferOut", Param, "Method", SearchTransfer.this, handle_Init);	
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
						progressDialog.dismiss();  //�������б���ݣ���رնԻ���
						AlertDialog.Builder build = new Builder(SearchTransfer.this);
						build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage("��ѯ���ⵥ�ݳ��?").setPositiveButton("ȷ��", null).show();
						return;
					}
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					if (jsonArrayinfo.length()==0){
						progressDialog.dismiss();  //�������б���ݣ���رնԻ���
						Toast tst = Toast.makeText(SearchTransfer.this, "δ�鵽�κγ��ⵥ�ݣ�", Toast.LENGTH_SHORT);
				        tst.show();
						return;
					}
					listItem = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < jsonArrayinfo.length(); i++) {
						JSONObject itemsobj = jsonArrayinfo.getJSONObject(i);
						HashMap<String, Object> map=new HashMap();
						map.put("trInit", itemsobj.getString("Init"));
						map.put("trDate", itemsobj.getString("TrDate"));
						map.put("trInNo", itemsobj.getString("TrNo"));
						map.put("toLocDesc", itemsobj.getString("LocDesc"));
						map.put("trUser", itemsobj.getString("User"));
						map.put("StkGrpID", itemsobj.getString("StkGrpID"));
						map.put("StkGrpDesc", itemsobj.getString("StkGrpDesc"));
						listItem.add(map);
					}
				    
					SearchTransferAdapter mAdapter = new SearchTransferAdapter(SearchTransfer.this,listItem);
				    trOutMainList.setAdapter(mAdapter);
			        progressDialog.dismiss();  //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
}
