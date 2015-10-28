package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.TransferOutByReqDetailAdapter;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class TransferOutByReqDetailActivity extends Activity{
	String reqno="";
	String reqlocdesc="";
	String prolocdesc="";
	private TextView reqlocname=null;
	private TextView prolocname=null;
	private TextView transnotitle=null;
	private TextView transferrowid=null;
	private Button begintrans=null;
    private ListView reqitmdetaillist;
    private ProgressDialog progressDialog = null;
    private String RetData = new String();
    private static final int handle_Init = 1;
    private static final int saveTrans_ByReq = 2;
    private View currUpView;
    private SimpleAdapter dAdapter;  
    private Integer listselectindex=0;
    private ArrayList<HashMap<String, Object>> listDetailItem;
    private TransferOutByReqDetailAdapter mAdapter;
    mobilecom com = new mobilecom();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transferoutbyreq_detail);
		//��ȡlistview
		reqitmdetaillist = (ListView) findViewById(R.id.reqitmdetaillist);  //��xml��Ӧlist
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.transreq_detailtitle, null);  //����
		reqitmdetaillist.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		reqitmdetaillist.setAdapter(null);
		// ��ҳ��������
		Bundle bundle = this.getIntent().getExtras();
		// ����nameֵ
		reqno = bundle.getString("reqno"); // ����
		reqlocdesc = bundle.getString("reqlocdesc"); // ����ID
		prolocdesc = bundle.getString("prolocdesc");
		reqlocname=(TextView) findViewById(R.id.tolocdesc);
		prolocname=(TextView) findViewById(R.id.fromlocdesc);
		reqlocname.setText(reqlocdesc);
		prolocname.setText(prolocdesc);
		//ֱ�Ӽ������
		QueryReqDetailInfo();
		reqitmdetaillist.setOnItemLongClickListener(new OnItemLongClickListener(){  
            @Override  
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,  
                    int position, long id) {  
            	listselectindex=position;
            	registerForContextMenu(reqitmdetaillist);
                return false;  
            }  
        });
		//�����ϸ������ϸ����
		reqitmdetaillist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2,
					long arg3) {
				//���ؽ���ǰ����������
				String tmptransflag="";
				tmptransflag=transferrowid.getText().toString().trim();
				if (tmptransflag.equals("ROWID"))
				{
					AlertDialog.Builder build = new Builder(TransferOutByReqDetailActivity.this);
			    	build.setIcon(R.drawable.warning).setTitle("����").setMessage("���ȵ����ʼ���ⴴ�����ⵥ!")
			    	.setPositiveButton("ȷ��", null).show();
			    	return;
				}
				
				switch (arg2) {
				case 0:
					break;
				default:
					Intent intent=new Intent(TransferOutByReqDetailActivity.this,TransferOutByReqScanActivity.class);
					String 	inreqitm=""; //������ϸid
					String  incidesc="" ; //����
				    currUpView = convertView;
				    if (convertView != null) {  //��̨��ȡ���
				 	   TextView inreqitmview = (TextView) convertView.findViewById(R.id.rowid);	
				 	   TextView incidescview = (TextView) convertView.findViewById(R.id.desc);	
				 	   inreqitm = inreqitmview.getText().toString();
				 	   incidesc=incidescview.getText().toString();
		       		}
				    listselectindex=arg2;
					Bundle bundle = new Bundle();  
					bundle.putString("inreqitm", inreqitm); 
					bundle.putString("incidesc", incidesc); 
					bundle.putString("initrowid", tmptransflag); 
					bundle.putInt("linkposition", listselectindex); 
					intent.putExtras(bundle);  
					startActivityForResult(intent,0); //����ȹ�ȥ,��ͷ�ٸ�result
				}
			}
        });
		begintrans=(Button)findViewById(R.id.preoutbtn);
		// ��ʼ�����¼�
		begintrans.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
                new AlertDialog.Builder(TransferOutByReqDetailActivity.this).setTitle("��ʾ")
                .setMessage("�Ƿ�ȷ�ϴ������ⵥ?")
                .setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {//���ȷ����ť  
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�      
                        // TODO Auto-generated method stub  
                        CreateTranfer();             
                    }               
                }).setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {//��ӷ��ذ�ť  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {//��Ӧ�¼�  
                    }     
                }).show();
            }
		});
		transnotitle=(TextView)findViewById(R.id.transno);
		transferrowid=(TextView)findViewById(R.id.transrowid);
	}
	private void QueryReqDetailInfo () {
		reqitmdetaillist.setAdapter(null);
		//���״̬
		progressDialog = ProgressDialog.show(TransferOutByReqDetailActivity.this, "���Ե�...", "��ȡ�����...", true);
		String Param ="&reqno="+reqno;
		ThreadHttp("web.DHCST.DHCSTPDATRANSBYREQ", "jsINReqD", Param, "Method", TransferOutByReqDetailActivity.this, handle_Init);
		
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
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == handle_Init) {
				try {
					if(RetData.indexOf("error")!=-1){
				        //�������б���ݣ���رնԻ���
				        progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(TransferOutByReqDetailActivity.this);
				    	build.setIcon(R.drawable.warning).setTitle("����").setMessage("��ȡ��ݴ���,�����������ӣ�")
				    	.setPositiveButton("ȷ��", null).show();
						return;
					}
					JSONObject retString = new JSONObject(RetData);
					String total = retString.getString("results");
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					listDetailItem = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < jsonArrayinfo.length(); i++) {
						JSONObject itemsobj1 = jsonArrayinfo.getJSONObject(i);
						HashMap<String, Object> map=new HashMap();
						/*
						map.put("ҩƷ���", itemsobj1.getString("desc"));
						map.put("��������", itemsobj1.getString("qty"));
						map.put("��λ", itemsobj1.getString("uomDesc"));
						map.put("��ת����", itemsobj1.getString("transedqty"));
						map.put("��ǰ���", itemsobj1.getString("prvqty"));
						map.put("����", itemsobj1.getString("manf"));
						map.put("���", itemsobj1.getString("rp"));
						map.put("rowid", itemsobj1.getString("rowid"));
						map.put("inci", itemsobj1.getString("inci"));
						*/
						map.put("desc", itemsobj1.getString("desc"));
						map.put("qty", itemsobj1.getString("qty"));
						map.put("uomDesc", itemsobj1.getString("uomDesc"));
						map.put("transedqty", itemsobj1.getString("transedqty"));
						map.put("prvqty", itemsobj1.getString("prvqty"));
						map.put("manf", itemsobj1.getString("manf"));
						map.put("rp", itemsobj1.getString("rp"));
						map.put("rowid", itemsobj1.getString("rowid"));
						map.put("inci", itemsobj1.getString("inci"));
						listDetailItem.add(map);
					}
				    //dAdapter=new SimpleAdapter(TransferOutByReqDetailActivity.this, listDetailItem, R.layout.transreq_detaillist,new String[]{"ҩƷ���","��������","��λ","��ת����","��ǰ���","����","���","rowid","inci"}, new int[]{R.id.desc,R.id.qty,R.id.uomDesc,R.id.transedqty,R.id.prvqty,R.id.manf,R.id.rp,R.id.rowid,R.id.inci});
					mAdapter = new TransferOutByReqDetailAdapter(TransferOutByReqDetailActivity.this,listDetailItem);
				    reqitmdetaillist.setAdapter(mAdapter);
			        progressDialog.dismiss();  //�������б���ݣ���رնԻ���
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (paramMessage.what == saveTrans_ByReq) {
				//��ȡ����ֵ
					String str =RetData;
					String[] strs = str.split(",");
					String transno = strs[0];
					String transrowid = strs[1];
					if (RetData.indexOf("-")!=-1)
					{
				        progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(TransferOutByReqDetailActivity.this);
				    	build.setIcon(R.drawable.warning)
				    	.setTitle("����")
				    	.setMessage(transrowid)
				    	.setPositiveButton("ȷ��", null).show();
						return;
					}
					transnotitle.setText(transno);
					transferrowid.setText(transrowid);
			        progressDialog.dismiss();
					AlertDialog.Builder buildsucc = new Builder(TransferOutByReqDetailActivity.this);
			    	buildsucc.setIcon(R.drawable.success).setTitle("��ʾ").setMessage("�������ⵥ�ɹ�,׼������!")
			    	.setPositiveButton("ȷ��", null).show();
			}
		}
	};
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("����");     
            menu.add(0, 0, 0, "�Ƴ�ѡ����"); 
	}
	@Override
    public boolean onContextItemSelected(MenuItem item){
		if (listselectindex<1)  //��ֹ����ɾ���
		{
			return true;
		}
    	listDetailItem.remove(listselectindex-1);//ѡ���е�λ��   
		//dAdapter.notifyDataSetChanged();
		mAdapter.notifyDataSetChanged();
		//dAdapter.invalidate();  
		listselectindex=0;
        return true;  
    } 
	//������󵥺Ŵ������ⵥ
	private void CreateTranfer() {
		String tmptransflag="";
		tmptransflag=transferrowid.getText().toString().trim();
		if (!tmptransflag.equals("ROWID"))
		{
			AlertDialog.Builder build = new Builder(TransferOutByReqDetailActivity.this);
	    	build.setIcon(R.drawable.warning).setTitle("����").setMessage("�Ѵ������ⵥ!��ֱ�ӳ���!")
	    	.setPositiveButton("ȷ��", null).show();
		}
		else
		{
			String Param ="&reqno="+reqno+"&userid="+LoginUser.UserDR;  //����session
			ThreadHttp("web.DHCST.DHCSTPDATRANSBYREQ", "SaveTransferMaster", Param, "Method", TransferOutByReqDetailActivity.this, saveTrans_ByReq);
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
			listselectindex=data.getExtras().getInt("linkposition"); // �õ���Activity
			if (listselectindex<1)  //��ֹ����ɾ���
			{
				return;
			}
	    	listDetailItem.remove(listselectindex-1);//ѡ���е�λ��   
			//dAdapter.notifyDataSetChanged();
			mAdapter.notifyDataSetChanged();
			listselectindex=0;
			
		}
	}
}
