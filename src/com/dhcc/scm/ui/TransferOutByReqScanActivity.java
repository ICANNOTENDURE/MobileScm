package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class TransferOutByReqScanActivity extends Activity {
    String 	inreqitm;
    String 	incidesc;
    String  initrowid;
    Integer scanselectindex=0;
    Integer linkposition;
    private ProgressDialog progressDialog = null;
    private ListView scanitmlist;
    private View currUpView;
    private SimpleAdapter addListAdapter;  
    private ArrayList<HashMap<String, Object>> addlistItem;
    //����soundpool
	private SoundPool soundPool;
    SoundPool mSoundPool = null;
    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    //���
    private TextView reqitmview=null;
    private TextView transedqtyview=null;
    private TextView reqqtyview=null;
    private TextView incidescview=null;
    private TextView batnoview=null;
    private TextView expdateview=null;
    private TextView uomdescview=null;
    private TextView uomrowidview=null;
    private TextView prolocqtyview=null;
    private TextView inclbrowidview=null;
    private TextView initrowidview=null;
    private TextView incirowidview=null;
    private EditText inclblabeltext=null;
    private EditText linklabeltext=null;
    private EditText transqtytext=null;
    private Button addtotransbutton=null;
    private Button transauditbutton=null;
    private String RetData = new String();
    private static final int handle_inreqinci = 1;
    private static final int handle_inclbinfo = 2;
    private static final int handle_transout = 3;
    mobilecom com = new mobilecom();
    /* ����PDAɨ����� ,ÿ�����涼Ҫô???*/
	Map<String, String> actionMap = new HashMap<String, String>();
	private DataReceiver dataReceiver = null;
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private String ACTION_CONTENT_NOTIFY_EMII = "com.ge.action.barscan";
	private String ACTION_CONTENT_NOTIFY_MOTO = "ACTION_CONTENT_NOTIFY_MOTO";
	private String ACTION_CONTENT_NOTIFY_HoneyWell = "com.android.server.scannerservice.broadcast"; // by
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transferoutbyreq_scan);
		//��ȡlistview
		scanitmlist = (ListView) findViewById(R.id.scanitmlist);  //��xml��Ӧlist,���������
		//������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.transreq_scantitle, null);  //�󶨱���
		scanitmlist.addHeaderView(mainView);
		//��ʼʱ,Ĭ��������Ϊ��,������ʾ��ͷ
		scanitmlist.setAdapter(null);
		//��ȡ���
		reqitmview=(TextView) findViewById(R.id.inreqitm);
		incidescview=(TextView) findViewById(R.id.incidesc);
		reqqtyview=(TextView) findViewById(R.id.reqqty);
		transedqtyview=(TextView) findViewById(R.id.transedqty);
		batnoview=(TextView) findViewById(R.id.batno);
		expdateview=(TextView) findViewById(R.id.expdate);
		uomdescview=(TextView) findViewById(R.id.uomdesc);
		prolocqtyview=(TextView) findViewById(R.id.inclbqty);
		uomrowidview=(TextView) findViewById(R.id.uomrowid);
		inclbrowidview=(TextView) findViewById(R.id.inclbrowid);
		incirowidview=(TextView) findViewById(R.id.incirowid);
		initrowidview=(TextView) findViewById(R.id.initrowid);
		transqtytext=(EditText) findViewById(R.id.transqty);
		addtotransbutton=(Button)findViewById(R.id.addtotransbtn);	
		transauditbutton=(Button)findViewById(R.id.transauditbtn);	
		// ��ҳ��������
		Bundle bundle = this.getIntent().getExtras();
		// ����nameֵ
		inreqitm = bundle.getString("inreqitm"); // ����
		incidesc=bundle.getString("incidesc"); // ҩƷ���
		initrowid=bundle.getString("initrowid"); 
		linkposition=bundle.getInt("linkposition"); 
		reqitmview.setText(inreqitm);
		incidescview.setText(incidesc);
		initrowidview.setText(initrowid);
		// ���������ϸ��ȡ��ݼ�����Ϣ
		QueryReqDetail(inreqitm);
		//���ý���
		inclblabeltext=(EditText) findViewById(R.id.packlabel);
		linklabeltext=(EditText) findViewById(R.id.linklabel);
		inclblabeltext.setFocusable(true);
		inclblabeltext.requestFocus();
		linklabeltext.clearFocus();
		inclblabeltext.setInputType(InputType.TYPE_NULL);  //���������
		linklabeltext.setInputType(InputType.TYPE_NULL);
		//��̬����еĴ���
		addlistItem = new ArrayList<HashMap<String,Object>>();
	    addListAdapter=new SimpleAdapter(TransferOutByReqScanActivity.this, addlistItem, R.layout.transreq_scanlist,new String[]{"batno","expdate","transqty","uomdesc","inclblabel","linklabel","inclbrowid","uomrowid"}, new int[]{R.id.batno,R.id.expdate,R.id.transqty,R.id.uomdesc,R.id.packlabel,R.id.linklabel,R.id.inclbrowid,R.id.uomrowid});
	    scanitmlist.setAdapter(addListAdapter);
	    //button�����¼�
		addtotransbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	//�����б�
            	AddToTransList();
            }
        });
		transauditbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	TransOutAudit();
            	
            }
        });
		scanitmlist.setOnItemLongClickListener(new OnItemLongClickListener(){  
            @Override  
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,  
                    int position, long id) {  
            	scanselectindex=position;
            	registerForContextMenu(scanitmlist);
                return false;  
            }  
        });
		//��������
	    mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 3);
	    soundMap.put(1 , mSoundPool.load(this, R.raw.enough , 1));
	}
	private void QueryReqDetail(String inreqitmrowid) {
		// �������K
		progressDialog = ProgressDialog.show(TransferOutByReqScanActivity.this, "���Ե�...", "��ȡ�����...", true);
		String Param ="&inreqitmrowid="+inreqitmrowid;
		ThreadHttp("web.DHCST.DHCSTPDATRANSBYREQ", "GetInReqDetailInciInfo", Param, "Method", TransferOutByReqScanActivity.this, handle_inreqinci);
		
	}
	private void GetInclbInfoByPackLabel() {
		// ��������ȡ��������Ϣ���
		String inclbbarcode = inclblabeltext.getText().toString().toUpperCase();
		String transuom=uomrowidview.getText().toString();
		String inreqinci=incirowidview.getText().toString();
		progressDialog = ProgressDialog.show(TransferOutByReqScanActivity.this, "���Ե�...",
				"��ȡ�����...", true);
		String Param = "&locid=" + LoginUser.UserLoc+"&packlabel=" + inclbbarcode+"&transuom="+transuom+"&incirowid="+inreqinci;
		ThreadHttp("web.DHCST.DHCSTPDATRANSBYREQ", "GetInclbInfoByLabel", Param, "Method", TransferOutByReqScanActivity.this, handle_inclbinfo);
		
	}
	
	/// Descript:������������Ƿ���
	private void CheckReqQty(){
		String reqqty=reqqtyview.getText().toString();  //��������
		String transqty=transqtytext.getText().toString(); //ת������
		double transedqty=Double.parseDouble(transedqtyview.getText().toString()); //�Ѿ�ת������
		transedqty=transedqty+Double.parseDouble(transqty);
		if(Double.parseDouble(reqqty)>=transedqty){
			AddToTransList();  ///ɨ����ɺ�ֱ�Ӽ����б�
		}
	}
	
	private void AddToTransList() {
		// ��������ݽ���Ϣ���ܵ��б���ʾ
		//��֤�������ڴ��б��Ƿ��Ѵ���
		
		//��������
		HashMap<String, Object> addmap=new HashMap();	
		String batno=batnoview.getText().toString();
		String expdate=expdateview.getText().toString();
		String transqty=transqtytext.getText().toString();
		String uomdesc=uomdescview.getText().toString();
		String inclblabel=inclblabeltext.getText().toString().toUpperCase();
		String linklabel=linklabeltext.getText().toString().toUpperCase();
		String inclbrowid=inclbrowidview.getText().toString();
		String uomrowid=uomrowidview.getText().toString();
		String reqqty=reqqtyview.getText().toString();
		String inclbqty=prolocqtyview.getText().toString();
		/*if (inclbrowid.equals("")||inclbrowid.equals(null)||(inclbrowid.toString().trim()=="")){						
			AlertDialog.Builder build = new Builder(
					TransferOutByReqScanActivity.this);
			build.setIcon(R.drawable.warning).setTitle("����")
					.setMessage("����Ϣ�ɼ����б?��ɨ������ԣ�")
					.setPositiveButton("ȷ��", null).show();
			return;
		}
		*/
		if(CheckIfExitSpecLabel(inclblabel)){
	        Toast tst = Toast.makeText(TransferOutByReqScanActivity.this, "��Ӧ�����Ѵ���,���ʵ�����ԣ�", Toast.LENGTH_SHORT);
	        tst.show();
			return;
		}
		if(transqty.equals("")||transqty.equals(null)||(transqty.toString().trim()=="")){
	        Toast tst = Toast.makeText(TransferOutByReqScanActivity.this, "ת������������߼�,���ʵ�����ԣ�", Toast.LENGTH_SHORT);
	        tst.show();
	        return;
		}
		if(Double.parseDouble(inclbqty)<Double.parseDouble(transqty)){
	        Toast tst = Toast.makeText(TransferOutByReqScanActivity.this, "��ο�治��,���ʵ�����ԣ�", Toast.LENGTH_SHORT);
	        tst.show();
	        return;
		}

		addmap.put("batno", batno);
		addmap.put("expdate", expdate);
		addmap.put("transqty",transqty);
		addmap.put("uomdesc", uomdesc);
		addmap.put("inclblabel",inclblabel);
		addmap.put("linklabel", linklabel);
		addmap.put("inclbrowid",inclbrowid);
		addmap.put("uomrowid",uomrowid);
		addlistItem.add(addmap);
		addListAdapter.notifyDataSetChanged();
		//��������б��,������ת����,���ԭ�����
		double newtransedqty=0;
		double oldtransedqty=0;
		oldtransedqty=Double.parseDouble(transedqtyview.getText().toString());
		newtransedqty=oldtransedqty+Double.parseDouble(transqty);
		transedqtyview.setText(String.valueOf(newtransedqty));
		//�����ܹ��㹻����������ʾ
		if (newtransedqty>=Double.parseDouble(reqqty))
		{
			mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
		}
		batnoview.setText("");
		expdateview.setText("");
		transqtytext.setText("");
		inclblabeltext.setText("");
		linklabeltext.setText("");
		inclbrowidview.setText("");
	}
	
	private void TransOutAudit() {
		//�����б����,���г���
		String detailinfo="";
		String detail="";
		String initrowid="";
		String reqitmrowid="";
		String inclbrowid="";
		String transqty="";
		String uomrowid="";
		String packlabel="";
		String linklabel="";
		Map<String,Object> listmap = null;  
		reqitmrowid=reqitmview.getText().toString();
		int count = scanitmlist.getCount();
		for(int i = 1;i < count;i++){
			listmap = (Map<String, Object>) scanitmlist.getItemAtPosition(i); 
			inclbrowid=(String) listmap.get("inclbrowid");  
			transqty=(String) listmap.get("transqty");  
			uomrowid=(String) listmap.get("uomrowid");  
			packlabel=(String) listmap.get("inclblabel");  
			linklabel=(String) listmap.get("linklabel");  
			detail=reqitmrowid+"^"+inclbrowid+"^"+transqty+"^"+uomrowid+"^"+packlabel+"^"+linklabel;
			if (detailinfo=="")
			{
				detailinfo=detail;
			}
			else
			{
				detailinfo=detailinfo+"!!"+detail;
			}
		}
		if ((detailinfo=="")||(detailinfo==null))
		{
			AlertDialog.Builder build = new Builder(TransferOutByReqScanActivity.this);
			build.setIcon(R.drawable.warning).setTitle("����")
			.setMessage("�޿�����ݱ���!")
			.setPositiveButton("ȷ��", null).show();
			return;
		}
		initrowid=initrowidview.getText().toString();
		if ((initrowid=="")||(initrowid==null))
		{
			AlertDialog.Builder build = new Builder(TransferOutByReqScanActivity.this);
			build.setIcon(R.drawable.warning).setTitle("����")
			.setMessage("�޷���Ӧ���ⵥ!")
			.setPositiveButton("ȷ��", null).show();
			return;
		}
		String Param ="&init="+initrowid+"&userid="+LoginUser.UserDR+"&detailinfo="+detailinfo;  //����session
		ThreadHttp("web.DHCST.DHCSTPDATRANSBYREQ", "DoTransOutDetail", Param, "Method", TransferOutByReqScanActivity.this, handle_transout);
		
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
			if (paramMessage.what == handle_inreqinci) {
				//��ȡ����ֵ
					String str =RetData;
					String[] strs = str.split(",");
					String inreqinci = strs[0];
					String inreqreqqty = strs[1];
					String inrequom = strs[2];
					String inrequomdesc = strs[3];
					String inreqproqty = strs[4];
					String inreqtransedqty = strs[5];
					reqqtyview.setText(inreqreqqty);
					transedqtyview.setText(inreqtransedqty);
					uomdescview.setText(inrequomdesc);
					//prolocqtyview.setText(inreqproqty);
					uomrowidview.setText(inrequom);
					incirowidview.setText(inreqinci);
			        progressDialog.dismiss();
			}
			if (paramMessage.what == handle_inclbinfo) {
				//��ȡ����ֵ
					String str =RetData;
					if (str.equals("-1"))
					{
						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(
								TransferOutByReqScanActivity.this);
						build.setIcon(R.drawable.warning).setTitle("����")
								.setMessage("��ǰҩƷ���������ƥ����ݣ�")
								.setPositiveButton("ȷ��", null).show();
						return;
					}
					String[] strs = str.split(",");
					String firstflag=strs[0].toString().trim();
					if (firstflag.equals("-1"))
					{
						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(
								TransferOutByReqScanActivity.this);
						build.setIcon(R.drawable.warning).setTitle("����")
								.setMessage("Ч����ʾ��")
								.setPositiveButton("ȷ��", null).show();
						return;
					}
					String inclb=strs[1];
					String batno=strs[2];
					String expdate=strs[3];
					String packqty=strs[4];
					String inclbqty=strs[5];
					//====
					String reqqty=reqqtyview.getText().toString();
					if(Double.parseDouble(reqqty)<=Double.parseDouble(packqty)){
						packqty = reqqty;
					}
					//====
					batnoview.setText(batno);
					expdateview.setText(expdate);
					prolocqtyview.setText(inclbqty);
					transqtytext.setText(packqty);
					inclbrowidview.setText(inclb);
					//CheckReqQty();  ///ɨ����ɺ��жϵ�ǰ�����Ƿ������������
			        progressDialog.dismiss();
			}
			if (paramMessage.what == handle_transout) {
					//������ݺ��ȡ����ֵ
					String retstr =RetData.toString().trim();
					if (retstr.equals("0"))
					{
						//����ɹ���ж�ؽ���,������һ��
		                //�����ʹ��Intent����
		                Intent intent = new Intent();
		                //�ѷ�����ݴ���Intent
		                intent.putExtra("linkposition", linkposition);
		                //���÷������
		                TransferOutByReqScanActivity.this.setResult(RESULT_OK, intent);
		                TransferOutByReqScanActivity.this.finish();
					}
					else
					{
				        progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(TransferOutByReqScanActivity.this);
				    	build.setIcon(R.drawable.warning).setTitle("����")
				    	.setMessage(retstr)
				    	.setPositiveButton("ȷ��", null).show();	
					}
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
    public boolean onContextItemSelected(MenuItem item)  
	{  
		if (scanselectindex<1)
		{
			return true;
		}
		addlistItem.remove(scanselectindex-1);//ѡ���е�λ��   
		addListAdapter.notifyDataSetChanged();
		//dAdapter.invalidate();  
		scanselectindex=0;
        return true;  
    } 
	//�������back��
	@Override
	 public boolean onKeyDown(int keyCode,KeyEvent event){
	    if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            Intent intent = new Intent();
            //�ѷ�����ݴ���Intent
            intent.putExtra("linkposition", 0);
            //���÷������
            TransferOutByReqScanActivity.this.setResult(RESULT_OK, intent);
            TransferOutByReqScanActivity.this.finish();
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
			if (linklabeltext.hasFocus())
			{
				linklabeltext.setText("");
				linklabeltext.setText(content);
			}
			if (inclblabeltext.hasFocus())
			{
				//���ڽ���ʱ������
				inclblabeltext.setText("");
				inclblabeltext.setText(content);
				GetInclbInfoByPackLabel();
			}
		}

	}

	public TransferOutByReqScanActivity() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO,
				"com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_HoneyWell, "scannerdata");
	}
	/// �ж�ָ�������Ƿ��Ѿ�����
	private boolean CheckIfExitSpecLabel(String InLabel){
		boolean quitflag = false;
		Map<String,Object> TempScanItmList = null; 
		int count = scanitmlist.getCount();
		for(int i = 1;i < count;i++){
			TempScanItmList = (Map<String, Object>) scanitmlist.getItemAtPosition(i);  
			String packlabel=(String) TempScanItmList.get("inclblabel");
			if (packlabel.equals(InLabel)){
				quitflag = true;
				break;
			}
		}
		return quitflag;
	}
}
