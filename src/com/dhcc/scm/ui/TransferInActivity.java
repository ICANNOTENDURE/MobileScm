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
import com.dhcc.scm.adapter.TransferInMainAdapter;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;
import com.dhcc.scm.ui.annotation.FindView;

public class TransferInActivity extends Activity {
	
	private EditText trInStartdDate;
	private EditText trInEndDate;
	private EditText trInFromLoc;
	private String trInFromLocID = "";
	private EditText trInToLoc;
	private String trInToLocID;
	private ImageView trInSeekloc;
	private ListView trInMainList;
	private Calendar c = null;
	private ImageView trinsearch; // ���Ұ�ť
	private ProgressDialog progressDialog = null;
	private static final int handle_Init = 1;
	private String ListData = new String();
	private ArrayList<HashMap<String,Object>> listItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.transfer_in_main);
		//����
		trInStartdDate=(EditText)findViewById(R.id.trinstartdate);
		trInEndDate=(EditText)findViewById(R.id.trinenddate);
		//��ѯ����
		trInFromLoc=(EditText)findViewById(R.id.trinfromloc);
		trInSeekloc=(ImageView)findViewById(R.id.btn_trinseekloc);
		
		trinsearch = (ImageView)findViewById(R.id.btn_trinsearch);
		// ����������¼�
		trInSeekloc.setOnClickListener(new View.OnClickListener() {
			@Override
	        public void onClick(View v) {
				initInTrFromLoc();
	        }
		});
		
		//��ȡlistview
		trInMainList = (ListView) findViewById(R.id.trin_mainlistview);
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.activity_tranferin_header, null);
		trInMainList.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		trInMainList.setAdapter(null);
		trInMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView textViewTrInit = null;
				if (convertView != null) {
					textViewTrInit = (TextView) convertView.findViewById(R.id.trInit);
				}
				Intent intent = new Intent(TransferInActivity.this,TransferInDetActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("trInit", textViewTrInit.getText().toString());
				intent.putExtras(bundle);

				startActivity(intent);
			}});
		
		// �����¼�
		trInStartdDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(1);
            	}
        });
		trInEndDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(2);
            }
        });
		trInStartdDate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
	            	showDialog(1);
		        }
		    }
		});
		trInEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
		            showDialog(2); 
		        }
		    }
		});
		
		//��ѯ�¼�
		trinsearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	QueryTransferIn();
            }
        });
		
		//���������
		trInStartdDate.setInputType(InputType.TYPE_NULL);
		trInEndDate.setInputType(InputType.TYPE_NULL);
		
		deFaultDate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		trInStartdDate = (EditText) findViewById(R.id.trinstartdate);
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
		trInStartdDate.setText(st.format(new Date()).toString());
		trInEndDate = (EditText) findViewById(R.id.trinenddate);
		SimpleDateFormat et = new SimpleDateFormat("yyyy-MM-dd");
		trInEndDate.setText(et.format(new Date()).toString());
		// Ĭ�ϵ�¼����
		trInToLoc = (EditText) findViewById(R.id.trintoloc);
		trInToLoc.setText(LoginUser.LocDesc);
		trInToLoc.setEnabled(false);
		trInToLocID = LoginUser.UserLoc;
	}
	
	public void initInTrFromLoc() {
		String locinputtext="";
		locinputtext=trInFromLoc.getText().toString().trim();
		Intent intent = new Intent();
		// ��BundleЯ�����
		Bundle bundle = new Bundle();
		// ����name����Ϊtinyphp
		bundle.putString("flag", "to");
		bundle.putString("inputtext", locinputtext);
		intent.putExtras(bundle);
		intent.setClass(TransferInActivity.this, TransferOutLocList.class);
		startActivityForResult(intent, 0);
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
                	if (id==1)
                	{
                		trInStartdDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                	}
                	else
                	{
                		trInEndDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                		
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
			case 0:
				// �������ֵ������ã�������>=0
				trInFromLoc.setText(result);
				trInFromLocID = locid; // ����ID
				break;
			default:
				break;
			}
		}
	}
	
	private void QueryTransferIn () {
		trInMainList.setAdapter(null);
		String startDate = trInStartdDate.getText().toString();
		String endDate = trInEndDate.getText().toString();

		//���״̬
		progressDialog = ProgressDialog.show(TransferInActivity.this, "���Ե�...", "��ȡ�����...", true);
		String Param ="&startDate="+startDate+"&endDate="+endDate+"&fromLocID="+trInFromLocID+"&toLocID="+trInToLocID;
		ThreadHttp("web.DHCST.AndroidTransferIn", "jsQueryTransferIn", Param, "Method", TransferInActivity.this, handle_Init);
		
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
						AlertDialog.Builder build = new Builder(TransferInActivity.this);
						build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage("����ת�Ƶ���ϸ������˶���ݺ����ԣ�").setPositiveButton("ȷ��", null).show();
						return;
					}
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					if (jsonArrayinfo.length()==0){
						progressDialog.dismiss();  //�������б���ݣ���رնԻ���
						Toast tst = Toast.makeText(TransferInActivity.this, "û��Ҫ���յ���ݣ�", Toast.LENGTH_SHORT);
				        tst.show();
						return;
					}
					listItem = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < jsonArrayinfo.length(); i++) {
						JSONObject itemsobj = jsonArrayinfo.getJSONObject(i);
						HashMap<String, Object> map=new HashMap();
						map.put("trInit", itemsobj.getString("Init"));
						map.put("trInNo", itemsobj.getString("TrNo"));
						map.put("fromLocDesc", itemsobj.getString("LocDesc"));
						map.put("trInUser", itemsobj.getString("User"));
						listItem.add(map);
					}
				    
				    TransferInMainAdapter mAdapter = new TransferInMainAdapter(TransferInActivity.this,listItem);
				    trInMainList.setAdapter(mAdapter);
			        progressDialog.dismiss();  //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
}
