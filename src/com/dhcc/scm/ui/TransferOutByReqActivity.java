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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;
public class TransferOutByReqActivity extends Activity {
	private EditText startdate=null;
	private EditText enddate=null;
	private EditText fromloc=null; // ������
	private EditText toloc=null; // ������
	public String tolocid = "";
	public String fromlocid = "";
    private Calendar c = null;
    private ImageView search; // ���Ұ�ť
    private ImageView seekloc;
    private String RetData = new String();
    private static final int handle_Init = 1;
    private ProgressDialog progressDialog = null;
    private ListView reqitmlist;
    private View currUpView;
    private SimpleAdapter mAdapter;  
    private ArrayList<HashMap<String, Object>> listItem;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transferout_byreq);
		//��ȡlistview
		reqitmlist = (ListView) findViewById(R.id.reqitmlist);  //��xml��Ӧlist
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.transreq_title, null);  //����
		reqitmlist.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		reqitmlist.setAdapter(null);
		startdate=(EditText)findViewById(R.id.startdate);
		enddate=(EditText)findViewById(R.id.enddate);
		fromloc=(EditText)findViewById(R.id.fromloc);
		toloc=(EditText)findViewById(R.id.toloc);
		search=(ImageView)findViewById(R.id.btn_search);
		seekloc=(ImageView)findViewById(R.id.seekloc_btn);
		// �����¼�
		startdate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(1);
            	}
        });
		enddate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	hideIM(v);
            	showDialog(2);
            }
        });
		startdate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
	            	showDialog(1);
		        }
		    }
		});
		enddate.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            hideIM(v);
		            showDialog(2); 
		        }
		    }
		});

		// ����������¼�
		seekloc.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
				initToLoc();
            }
		});
		//��ѯ�¼�
		search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	QueryReqInfo();
            }
        });
		//�����ϸ������ϸ����
		reqitmlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					break;
				default:
				    String reqno =null;
				    String reqlocdesc=null;
				    String prolocdesc=null;
				    currUpView = convertView;
				    if (convertView != null) {
				 	   TextView reqnoview = (TextView) convertView.findViewById(R.id.reqNo);
				 	   TextView reqlocdescview = (TextView) convertView.findViewById(R.id.toLocDesc);
			 		   reqno = reqnoview.getText().toString();
			 		   reqlocdesc= reqlocdescview.getText().toString();
			 		   prolocdesc=LoginUser.LocDesc;
		       		}
					Intent intent=new Intent(TransferOutByReqActivity.this,TransferOutByReqDetailActivity.class);
					Bundle bundle = new Bundle();  
					bundle.putString("reqno", reqno);  
					bundle.putString("reqlocdesc", reqlocdesc); 
					bundle.putString("prolocdesc", prolocdesc); 
					intent.putExtras(bundle);  
					startActivity(intent); 
				}
			}
        });
		startdate.setInputType(InputType.TYPE_NULL);  //���������
		enddate.setInputType(InputType.TYPE_NULL);
		deFaultDate(); // Ĭ������
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
                		startdate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                	}
                	else
                	{
                		enddate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                		
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
	public void initToLoc() {
		String locinputtext="";
		locinputtext=toloc.getText().toString().trim();
		Intent intent = new Intent();
		// ��BundleЯ�����
		Bundle bundle = new Bundle();
		// ����name����Ϊtinyphp
		bundle.putString("flag", "to");
		bundle.putString("inputtext", locinputtext);
		intent.putExtras(bundle);
		intent.setClass(TransferOutByReqActivity.this, TransferOutLocList.class);
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
		startdate = (EditText) findViewById(R.id.startdate);
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
		startdate.setText(st.format(new Date()).toString());
		enddate = (EditText) findViewById(R.id.enddate);
		SimpleDateFormat et = new SimpleDateFormat("yyyy-MM-dd");
		enddate.setText(et.format(new Date()).toString());
		// Ĭ�ϵ�¼����
		fromloc = (EditText) findViewById(R.id.fromloc);
		fromloc.setText(LoginUser.LocDesc);
		fromloc.setEnabled(false);
		fromlocid = LoginUser.UserLoc;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (!data.equals(null)) {
			String result = data.getExtras().getString("locdesc").toString();// �õ���Activity
			String locid = data.getExtras().getString("locid").toString();// �õ���Activity
			switch (resultCode) {
			// ������
			case 0:
				// �������ֵ������ã�������>=0
				toloc = (EditText) findViewById(R.id.toloc);
				toloc.setText(result);
				tolocid = locid; // ����ID
				break;
			// ������
			case 1:
				// �������ֵ������ã�������>=0
				fromloc = (EditText) findViewById(R.id.fromLoc);
				fromloc.setText(result);
				fromlocid = locid;
				break;

			// ���ؼ�������
			case 2:
				break;
				// ���ؼ�������
			case 3:
				break;
			//��ѯ���ⵥ����
			case 4:
				break;
			default:
				break;
			}
		}
	}
private void QueryReqInfo () {
	reqitmlist.setAdapter(null);
	String sd = startdate.getText().toString();
	String ed=enddate.getText().toString();
	String proloc=fromlocid;  //fromloc.getText().toString();  //��ôȡid
	String reqloc=tolocid; //toloc.getText().toString();
	//���״̬
	progressDialog = ProgressDialog.show(TransferOutByReqActivity.this, "���Ե�...", "��ȡ�����...", true);
	String TmpParam=sd+"^"+ed+"^"+proloc+"^"+reqloc+"^"+0+"^"+0;
	String Param ="&Start="+0+"&Limit="+999+"&Sort="+""+"&Dir="+""+""+"&sPar="+TmpParam;
	ThreadHttp("web.DHCST.DHCSTPDATRANSBYREQ", "jsINReqForTransfer", Param, "Method", TransferOutByReqActivity.this, handle_Init);
	
}
private void ThreadHttp(final String Cls, final String mth, final String Param, final String Typ, final Activity context, final int whatmsg) {

	Thread thread = new Thread() {
		public void run() {
			try {
				//this.sleep(100);
				try {
					RetData = HttpGetPostCls.LinkData(Cls, mth, Param, Typ, context);
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
				if(RetData.indexOf("error")!=-1){
			        //�������б���ݣ���رնԻ���
			        progressDialog.dismiss();
					AlertDialog.Builder build = new Builder(TransferOutByReqActivity.this);
			    	build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage("��ȡ��ݴ���,�����������ӣ�")
			    	.setPositiveButton("ȷ��", null).show();
					return;
				}
				JSONObject retString = new JSONObject(RetData);
				String total = retString.getString("results");
				/*if(total.compareTo("0")==0){
			        //�������б���ݣ���رնԻ���
			        progressDialog.dismiss();
					AlertDialog.Builder build = new Builder(TransferOutByReqActivity.this);
			    	build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage("��ݿ��޶�Ӧ����,��˶Ժ����ԣ�")
			    	.setPositiveButton("ȷ��",null).show();
					return;
				}*/
				String jsonarr1 = retString.getString("rows");
				JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
				listItem = new ArrayList<HashMap<String,Object>>();
				for (int i = 0; i < jsonArrayinfo.length(); i++) {
					JSONObject itemsobj = jsonArrayinfo.getJSONObject(i);
					HashMap<String, Object> map=new HashMap();
					map.put("����", itemsobj.getString("reqNo"));
					map.put("������", itemsobj.getString("toLocDesc"));
					map.put("����", itemsobj.getString("StkTypeDesc"));
					map.put("������", itemsobj.getString("userName"));
					listItem.add(map);
				}
			    mAdapter=new SimpleAdapter(TransferOutByReqActivity.this, listItem, R.layout.transreq_itmlist,new String[]{"����","������","����","������"}, new int[]{R.id.reqNo,R.id.toLocDesc,R.id.StkTypeDesc,R.id.userName});
		        reqitmlist.setAdapter(mAdapter);
		        progressDialog.dismiss();  //�������б���ݣ���رնԻ���
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
};
}

