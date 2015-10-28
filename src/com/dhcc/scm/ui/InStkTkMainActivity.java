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
import android.content.DialogInterface;
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
import com.dhcc.scm.adapter.InStkTkMainAdapter;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class InStkTkMainActivity extends Activity {
	private EditText istStartDate;
	private EditText istEndDate;
	private ListView istMainList;
	private EditText istLoc;
	private EditText istWay;
	private String istLocID = "";
	private Calendar c = null;
	private ImageView istsearch; // ���Ұ�ť
	private ProgressDialog progressDialog = null;
	private static final int handle_Init = 1;
	private String ListData = new String();
	private ArrayList<HashMap<String,Object>> listItem;
	private int selectedWayIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_instktk_main);
		
		istLoc = (EditText) findViewById(R.id.ist_locdesc);
		istWay=(EditText)findViewById(R.id.ist_way);
		
		//����
		istStartDate=(EditText)findViewById(R.id.ist_startdate);
		istEndDate=(EditText)findViewById(R.id.ist_enddate);
		//���������
		istStartDate.setInputType(InputType.TYPE_NULL);
		istEndDate.setInputType(InputType.TYPE_NULL);
		istWay.setInputType(InputType.TYPE_NULL);
		
		//��ȡlistview
		istMainList = (ListView) findViewById(R.id.ist_mainlistview);
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.activity_instktk_mheader, null);
		istMainList.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		istMainList.setAdapter(null);
		istMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView textViewInst = null;
				if (convertView != null) {
					textViewInst = (TextView) convertView.findViewById(R.id.ist_rowid);
				}
				Intent intent = new Intent(InStkTkMainActivity.this,InStkTkByItemActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("inst", textViewInst.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}}
		);
		
		istsearch = (ImageView)findViewById(R.id.btn_search);
		// ����������¼�
		istsearch.setOnClickListener(new View.OnClickListener() {
			@Override
	        public void onClick(View v) {
				QueryInStkTk();
	        }
		});
		// �����¼�
		istStartDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(1);
            	}
        });
		istEndDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(2);
            }
        });
		istStartDate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
	            	showDialog(1);
		        }
		    }
		});
		istEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
		            showDialog(2); 
		        }
		    }
		});
		
		// �̵㷽ʽ
		istWay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
				final String[] wayStr = {"��ҩƷ","����λ"};
				//boolean[] bool = {true,false};
				AlertDialog.Builder dailog = new Builder(InStkTkMainActivity.this);
				dailog.setTitle("�̵㷽ʽ").setItems(wayStr, null)
				.setPositiveButton("ȷ��", new  DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						istWay.setText(wayStr[selectedWayIndex]);
						
					}})
				.setSingleChoiceItems(wayStr, 0, new DialogInterface.OnClickListener() {
					 
				     @Override
				     public void onClick(DialogInterface dialog, int which) {
				      selectedWayIndex = which;
				     }
				    }).show();
            }
        });
		
		///����Ĭ������
		deFaultDate();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
                		istStartDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                	}
                	else
                	{
                		istEndDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                		
                	}
                	}
            }, 
            c.get(Calendar.YEAR), // �������
            c.get(Calendar.MONTH), // �����·�
            c.get(Calendar.DAY_OF_MONTH) // ��������
        );
        return dialog;
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
		SimpleDateFormat sysdate = new SimpleDateFormat("yyyy-MM-dd");
		istStartDate.setText(sysdate.format(new Date()).toString());
		istEndDate.setText(sysdate.format(new Date()).toString());
		// Ĭ�ϵ�¼����
		
		istLoc.setText(LoginUser.LocDesc);
		istLoc.setEnabled(false);
		istLocID = LoginUser.UserLoc;
	}
	
	private void QueryInStkTk () {
		istMainList.setAdapter(null);
		String startDate = istStartDate.getText().toString();
		String endDate = istEndDate.getText().toString();

		//���״̬
		progressDialog = ProgressDialog.show(InStkTkMainActivity.this, "���Ե�...", "��ȡ�����...", true);
		String Param ="&startDate="+startDate+"&endDate="+endDate+"&LocID="+istLocID;
		ThreadHttp("web.DHCST.AndroidInStkTk", "jsQueryInStkTk", Param, "Method", InStkTkMainActivity.this, handle_Init);
		
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
						setTipErrorMessage("�����̵㵥�ݳ��?");
						return;
					}
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					if (jsonArrayinfo.length()==0){
						progressDialog.dismiss();  //�������б���ݣ���رնԻ���
						Toast tst = Toast.makeText(InStkTkMainActivity.this, "û��Ҫ�̵�ĵ��ݣ�", Toast.LENGTH_SHORT);
				        tst.show();
						return;
					}
					listItem = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < jsonArrayinfo.length(); i++) {
						JSONObject itemsobj = jsonArrayinfo.getJSONObject(i);
						HashMap<String, Object> map=new HashMap();
						map.put("istId", itemsobj.getString("Inst"));
						map.put("istNo", itemsobj.getString("InstNo"));
						map.put("istTime", itemsobj.getString("InstDate"));
						map.put("istUser", itemsobj.getString("User"));
						listItem.add(map);
					}
				    
					InStkTkMainAdapter mAdapter = new InStkTkMainAdapter(InStkTkMainActivity.this,listItem);
				    istMainList.setAdapter(mAdapter);
			        progressDialog.dismiss();  //�������б���ݣ���رնԻ���
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
		AlertDialog.Builder build = new Builder(InStkTkMainActivity.this);
    	build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage(ErrorMessage)
    	.setPositiveButton("ȷ��", null).show();
	}
	
}
