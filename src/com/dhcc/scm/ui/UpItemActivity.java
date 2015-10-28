package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;

public class UpItemActivity extends Activity {
	  private Spinner spinner;
	  private List<String> data_list;
	  private ArrayAdapter<String> arr_adapter;
	  
	  private String RetData = new String();
	  private static final int handle_Init = 1;
	  private ProgressDialog progressDialog = null;
	  
	  private String barcode="";
	  private String inci="";
	  private String inclb="";
	  
	  private int upretsult = 0;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_unpack);
		
	    //��ȡIntent�е�Bundle����
        Bundle bundle = this.getIntent().getExtras();  
        //��ȡBundle�е���ݣ�ע�����ͺ�key
        String incidesc = bundle.getString("incidesc");
        barcode = bundle.getString("barcode");
        inci = bundle.getString("inci");
        inclb = bundle.getString("inclb");
		//�������
        TextView viewdesc = (TextView)findViewById(R.id.gooddesc);
        viewdesc.setText(incidesc);
        
		spinner = (Spinner) findViewById(R.id.spinner2);
		
		Button btn_unpack = (Button) findViewById(R.id.btn_unpack);
		btn_unpack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveUnPack();
			}

		});
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                //�����ʹ��Intent����
                Intent intent = new Intent();
                //�ѷ�����ݴ���Intent
                intent.putExtra("upretsult", upretsult);
                //���÷������
                UpItemActivity.this.setResult(RESULT_OK, intent);
				UpItemActivity.this.finish();
			}

		});
		//��ʼ����λ�����ؼ�,ҳ�����ʱֱ�ӵ���
		progressDialog = ProgressDialog.show(UpItemActivity.this, "���Ե�...", "��ȡ���λ�б�...", true);
		String Param = "&inci=" + inci;
		ThreadHttp("web.DHCST.DHCSTSUPCHAUNPACK", "getUomList", Param, "Method", UpItemActivity.this, handle_Init);
	  
	}
	
	//���װ
	private void saveUnPack(){
		String retMessage = null;
		int retFlag = 0;
		String unPackQty = null ;
		
		TextView view = (TextView)findViewById(R.id.qty);
		unPackQty = view.getText().toString();
		if (unPackQty.equals("")||unPackQty.equals(null))
		{
			AlertDialog.Builder build = new Builder(UpItemActivity.this);
	    	build.setIcon(R.drawable.warning).setTitle("��ʾ").setMessage("������������!")
	    	.setPositiveButton("ȷ��", null).show();
	    	return;
		}
		String spnnerVal = spinner.getSelectedItem().toString();
		//���������
		progressDialog = ProgressDialog.show(UpItemActivity.this, "���Ե�...", "���������...", true);
		String Param = "&locdr=" + LoginUser.UserLoc+"&inclb=" +inclb+"&uom=" +spnnerVal+"&qty=" +unPackQty+"&parbarcode=" +barcode;
		try {
			//String saveUnPackStr = HttpGetPostCls.LinkData("web.DHCST.DHCSTSUPCHAUNPACK", "getUomList", Param, "Method", UpItemActivity.this);
			ThreadHttp("web.DHCST.DHCSTSUPCHAUNPACK", "UnPack", Param, "Method", UpItemActivity.this, 2);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chai_bao, menu);
		return true;
	}

	
	Handler handler = new Handler(){
		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == handle_Init) {
					if(RetData.indexOf("error")!=-1){
				        //�������б���ݣ���رնԻ���
				        progressDialog.dismiss();
						setTipErrorMessage("0");
						return;
					}
				//ArrayList<HashMap<String, Object>>listItem = new ArrayList<HashMap<String,Object>>();
					String[] ret=RetData.split("\\^");
					data_list = new ArrayList<String>();
					for (int i=0;i<ret.length;i++){
						
						data_list.add(ret[i]);
					}
			        //������
			        arr_adapter= new ArrayAdapter<String>(UpItemActivity.this,R.layout.upspinner, data_list);
			        //������ʽ
			        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        //����������
			        spinner.setAdapter(arr_adapter);
			        //�������б���ݣ���رնԻ���
			        progressDialog.dismiss();
			}
			if (paramMessage.what == 2) {
				if(RetData.indexOf("error")!=-1){
					setTipErrorMessage("1");
				}else if(RetData.compareTo("-1")==0){
					setTipErrorMessage("2");
				}else if(RetData.compareTo("-2")==0){
					setTipErrorMessage("3");
				}else{
					upretsult = 1;
			        Toast tst = Toast.makeText(UpItemActivity.this, "����ɹ���", Toast.LENGTH_SHORT);
			        tst.show();
	                //�����ʹ��Intent����,����ɹ����˳�����
	                Intent intent = new Intent();
	                //�ѷ�����ݴ���Intent
	                intent.putExtra("upretsult", upretsult);
	                //���÷������
	                UpItemActivity.this.setResult(RESULT_OK, intent);
					UpItemActivity.this.finish();
				}
		        //�������б���ݣ���رնԻ���
		        progressDialog.dismiss();
		 }
		}
	};
	
	//���õ�λ���س��������ѶԻ���
	public void setTipErrorMessage(String errFlag){
		String ErrorMessage = null ;
		if (errFlag.compareTo("0")==0){
			ErrorMessage = "ȡ���λ����,�������磡";
		}else if(errFlag.compareTo("2")==0){
			ErrorMessage = "�����ظ����";
		}else if(errFlag.compareTo("3")==0){
			ErrorMessage = "������λ���?";
		}else{
			ErrorMessage = "���װ���?";
		}
		AlertDialog.Builder build = new Builder(UpItemActivity.this);
    	build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage(ErrorMessage)
    	.setPositiveButton("ȷ��", null).show();
	}
	//�������back��
	@Override
	 public boolean onKeyDown(int keyCode,KeyEvent event){
	    if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            Intent intent = new Intent();
            //�ѷ�����ݴ���Intent
            intent.putExtra("upretsult", upretsult);
            //���÷������
            UpItemActivity.this.setResult(RESULT_OK, intent);
			UpItemActivity.this.finish();
			//return false; 
	    }
	    return false;
	 }
}
