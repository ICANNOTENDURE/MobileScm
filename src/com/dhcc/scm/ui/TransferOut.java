package com.dhcc.scm.ui;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpConfig;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;
import com.dhcc.scm.utils.CommonTools;
import com.dhcc.scm.utils.Loger;

public class TransferOut extends BaseActivity implements OnClickListener {

	@FindView(id = R.id.fromLoc, click = true)
	EditText fromLocTxt; // 供给科室
	@FindView(id = R.id.toLoc, click = true)
	EditText toLocTxt; // 请求科室
	@FindView(id = R.id.createUser)
	EditText createUserTxt; // 建单人
	@FindView(id = R.id.createDate)
	EditText createDateTxt; // 简单日期
	@FindView(id = R.id.remark)
	EditText remarkTxt; // 备注
	@FindView(id = R.id.transferNo)
	EditText trnasfNoTxt; // 单号
	@FindView(id = R.id.stktypedesc, click = true)
	EditText stkTypeTxt; // 类組
	@FindView(id = R.id.saveDetails, click = true)
	Button saveDetailBtn; // 保存明细按钮
	@FindView(id = R.id.saveMaster, click = true)
	Button saveMasterBtn; // 扫码出库
	@FindView(id = R.id.btn_search, click = true)
	ImageView searchBtn; //
	@FindView(id = R.id.seekloc_btn, click = true)
	ImageView seekloc; // 查询科室
	@FindView(id = R.id.seekstk_btn, click = true)
	ImageView seekstk; // 查询类祖
	
	public String toLocID = "";
	public String fromLocID = "";
	public String StkCatGrpId = "";
	public String initID = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transferout);
		super.onCreate(savedInstanceState);
		initView();
	}

	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		stkTypeTxt.setInputType(InputType.TYPE_NULL);

		createDateTxt.setText(CommonTools.formatDate(new Date()));
		createDateTxt.setEnabled(false);
		fromLocTxt.setText(LoginUser.LocDesc);
		fromLocTxt.setEnabled(false);
		fromLocID = LoginUser.UserLoc;
		createUserTxt.setText(LoginUser.UserName);
		createUserTxt.setEnabled(false);
		trnasfNoTxt.setEnabled(false);
		saveDetailBtn.setEnabled(false);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.saveMaster:
			saveMaster(arg0);
			break;
		case R.id.saveDetails:
			saveDetails();
			break;
		case R.id.btn_search:
			Intent intent2 = new Intent();
			intent2.setClass(TransferOut.this, SearchTransferActivity.class);
			startActivity(intent2);
			break;
		case R.id.seekloc_btn:
			initToLoc();
			break;
		case R.id.fromloc:
			initFromLoc();
			break;
		case R.id.seekstk_btn:
			initStkGrp();
			break;
			
		}
	}

	private void saveDetails() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("transferNo", trnasfNoTxt.getText().toString());
		bundle.putString("initID", initID);
		bundle.putString("initCatGrpID", StkCatGrpId);
		bundle.putString("initCatGrpDesc", stkTypeTxt.getText().toString().trim());
		intent.putExtras(bundle);
		intent.setClass(TransferOut.this, ScanForTransfer.class);
		startActivity(intent);
	}

	public void initToLoc() {
		String locinputtext = "";
		locinputtext = toLocTxt.getText().toString().trim();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "to");
		bundle.putString("inputtext", locinputtext);
		intent.putExtras(bundle);
		intent.setClass(TransferOut.this, TransferOutLocListActivity.class);
		startActivityForResult(intent, 0);
	}

	public void initFromLoc() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "from");
		intent.putExtras(bundle);
        intent.setClass(TransferOut.this, TransferOutLocListActivity.class);
		startActivityForResult(intent, 1);
	}

	// ����
	@SuppressLint("CutPasteId")
	public void initStkGrp() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "from");
		intent.putExtras(bundle);
		intent.setClass(TransferOut.this, StkGrpCatListActivity.class);
		startActivityForResult(intent, 3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (!data.equals(null)) {
			String resultdesc = "";
			String resultrowid = "";
			switch (resultCode) {
			case 0:
				resultdesc = data.getExtras().getString("locdesc").toString();
				resultrowid = data.getExtras().getString("locid").toString();
				toLocTxt.setText(resultdesc);
				toLocID = resultrowid;
				break;
			case 1:
				resultdesc = data.getExtras().getString("locdesc").toString();
				resultrowid = data.getExtras().getString("locid").toString();
				fromLocTxt.setText(resultdesc);
				fromLocID = resultrowid;
				break;

			case 2:
				break;
			case 3:
				resultdesc = data.getExtras().getString("stkgrpdesc").toString();
				resultrowid = data.getExtras().getString("stkgrprowid").toString();
				stkTypeTxt.setText(resultdesc);
				StkCatGrpId = resultrowid;
				break;
			default:
				break;
			}
		}
	}

	public void saveMaster(View arg0) {

		String fromLocValue = fromLocTxt.getText().toString().trim();
		if ((fromLocValue.equals("")) || (fromLocID.equals(""))) {
			ViewInject.toast("供给科室不能为空");
			return;
		}

		String toLocValue = toLocTxt.getText().toString().trim();
		if ((toLocValue.equals("") || toLocID.equals(""))) {
			ViewInject.toast("请求科室不能为空");
			return;
		}
		if ((StkCatGrpId.equals("") || StkCatGrpId.equals(""))) {
			ViewInject.toast("类组不能为空");
			return;
		}
		String remarkValue = remarkTxt.getText().toString().trim();
		if (remarkValue.equals("")) {
			remarkValue = "PDA";
		}
		String MainInfo = fromLocID + "^" + toLocID + "^" + "" + "^" + "" + "^" + "N" + "^" + "10" + "^" + LoginUser.UserDR + "^" + StkCatGrpId + "^" + "G" + "^" + remarkValue;
		Loger.debug(MainInfo);
		HttpConfig config = new HttpConfig();
		config.cacheTime = 0;
		Http http = new Http(config);
		HttpParams params = new HttpParams();
		params.put("InitId", initID);
		params.put("MainInfo", MainInfo);
		params.put("className", "web.DHCST.AndroidTransferOut");
		params.put("methodName", "Save");
		params.put("type", "Method");
		http.post(getIpByType(), params, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				ViewInject.toast("网络不好" + strMsg);
			}

			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				if (t != null) {
					String str = new String(t);
					Loger.debug("登陆网络请求：" + new String(t));
					try {
						String[] strs = str.split(",");
						String no = strs[0];
						initID = strs[1];
						trnasfNoTxt.setText(no);
						saveDetailBtn.setEnabled(true);
						AlertDialog.Builder buildsucc = new Builder(TransferOut.this);
						buildsucc.setIcon(R.drawable.success).setTitle("提示").setMessage("保存成功!").setPositiveButton("确认", null).show();
					} catch (Exception e) {
						ViewInject.toast(e.getMessage());
						e.printStackTrace();
					}

				}
			}

			;
		});
	}

}
