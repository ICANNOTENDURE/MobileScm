package com.dhcc.scm.ui;

import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.ui.annotation.FindView;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.utils.CommonTools;

public class TransferOut extends BaseActivity implements OnClickListener {

	@FindView(id = R.id.fromLoc, click = true)
	private EditText fromLocTxt; // 供给科室
	
	@FindView(id = R.id.toLoc, click = true)
	private EditText toLocTxt; // 请求科室
	
	@FindView(id = R.id.createUser)
	private EditText createUserTxt; // 建单人
	
	@FindView(id = R.id.createDate)
	private EditText createDateTxt; // 简单日期
	
	@FindView(id = R.id.remark)
	private EditText remarkTxt; // 备注
	
	@FindView(id = R.id.transferNo)
	private EditText trnasfNoTxt; // 单号
	
	@FindView(id = R.id.stktypedesc, click = true)
	private EditText stkTypeTxt; // 类祖
	
	@FindView(id = R.id.saveDetails, click = true)
	private Button saveDetailBtn; // 保存明细按钮
	
	@FindView(id = R.id.saveMaster, click = true)
	private Button saveMasterBtn; // 扫码出库
	
	@FindView(id = R.id.btn_search, click = true)
	private ImageView searchBtn; //
	
	@FindView(id = R.id.seekloc_btn, click = true)
	private ImageView seekloc; // 查询科室
	public String toLocID = "";
	public String fromLocID = "";
	public String StkCatGrpId = "";
	public String initID = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transferout);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
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
			intent2.setClass(TransferOut.this, SearchTransfer.class);
			startActivity(intent2);
			break;
		case R.id.seekloc_btn:

			initToLoc();
			break;
		case R.id.fromloc:
			initFromLoc();
			break;
		case R.id.stktypedesc:
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

	mobilecom com = new mobilecom();

	Handler handler = new Handler() {
		public void handleMessage(Message paramMessage) {

			if (com.RetData.toLowerCase(Locale.ENGLISH).indexOf("error") != -1) {
				CommonTools.showShortToast(TransferOut.this, "系统错误!");
				return;
			}
			if (paramMessage.what == 1) {
				if (!com.RetData.equals("")) {
					String str = com.RetData.toString().trim();
					String[] strs = str.split(",");
					String no = strs[0];
					initID = strs[1];
					trnasfNoTxt.setText(no);
					saveDetailBtn.setEnabled(true);
					AlertDialog.Builder buildsucc = new Builder(TransferOut.this);
					buildsucc.setIcon(R.drawable.success).setTitle("提示").setMessage("保存成功!").setPositiveButton("确认", null).show();

				}
			}
		}
	};

	public void initToLoc() {
		String locinputtext = "";
		locinputtext = toLocTxt.getText().toString().trim();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "to");
		bundle.putString("inputtext", locinputtext);
		intent.putExtras(bundle);
		intent.setClass(TransferOut.this, TransferOutLocList.class);
		startActivityForResult(intent, 0);
	}

	public void initFromLoc() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "from");
		intent.putExtras(bundle);

		intent.setClass(TransferOut.this, TransferOutLocList.class);
		startActivityForResult(intent, 1);
	}

	// ����
	@SuppressLint("CutPasteId")
	public void initStkGrp() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "from");
		intent.putExtras(bundle);
		intent.setClass(TransferOut.this, StkGrpCatList.class);
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
			CommonTools.showShortToast(TransferOut.this, "供给科室不能为空");
			return;
		}

		String toLocValue = toLocTxt.getText().toString().trim();
		if ((toLocValue.equals("") || toLocID.equals(""))) {
			CommonTools.showShortToast(TransferOut.this, "请求科室不能为空");
			return;
		}
		if ((StkCatGrpId.equals("") || StkCatGrpId.equals(""))) {
			CommonTools.showShortToast(TransferOut.this, "类组不能为空");
			return;
		}
		String remarkValue = remarkTxt.getText().toString().trim();
		if (remarkValue.equals("")) {
			remarkValue = "PDA[JD]";
		}
		String MainInfo = fromLocID + "^" + toLocID + "^" + "" + "^" + "" + "^" + "N" + "^" + "10" + "^" + LoginUser.UserDR + "^" + StkCatGrpId + "^" + "G" + "^" + remarkValue;
		String Params = "&InitId=" + initID + "&MainInfo=" + MainInfo;
		com.ThreadHttp("web.DHCST.AndroidTransferOut", "Save", Params, "Method", TransferOut.this, 1, handler);
	}

}
