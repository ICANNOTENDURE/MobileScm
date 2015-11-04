/**
 * 
 */
package com.dhcc.scm.ui;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.annotation.FindView;
import com.dhcc.scm.ui.base.BaseActivity;

/**
 * @author huaxiaoying 2015/10/21 15:05
 */

public class InGdRecSearchActivity extends BaseActivity implements OnClickListener {

	@FindView(id = R.id.ingdrecSearch_startDate_btn, click = true)
	private Button startDateBtn;

	@FindView(id = R.id.ingdrecSearch_startdate_txt)
	private TextView startdateTxt;

	@FindView(id = R.id.ingdrecSearch_endDate_btn, click = true)
	private Button endDateBtn;

	@FindView(id = R.id.ingdrecSearch_enddate_txt)
	private TextView enddateTxt;

	@FindView(id = R.id.ingdrecsearch_back_btn, click = true)
	private ImageView imgBack = null;

	@FindView(id = R.id.ingdrecSearch_endDate_btn, click = true)
	private Button ingdrecSearch_select = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ingdrec_search);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initView() {

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ingdrecSearch_startDate_btn:
			showDate(arg0);
			break;
		case R.id.ingdrecSearch_endDate_btn:
			showendDate(arg0);
			break;
		case R.id.ingdrecsearch_back_btn:
			finish();
			break;
		case R.id.ingdrecSearch_select_btn:
			Intent nIntent = new Intent(InGdRecSearchActivity.this, InGdRecSearchResultActivity.class);
			startActivity(nIntent);
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: showDate
	 * @Description: TODO(单击日期弹出日期控件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author huaxiaoying
	 * @date 2015年10月21日 下午2:45:46
	 */
	public void showDate(View v) {
		Calendar calendar = Calendar.getInstance();
		new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				startdateTxt = (TextView) findViewById(R.id.ingdrecSearch_startdate_txt);
				startdateTxt.setText(arg1 + "-" + (arg2 + 1) + "-" + arg3);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
		;
	}

	public void showendDate(View v) {
		Calendar calendar = Calendar.getInstance();
		new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				enddateTxt = (TextView) findViewById(R.id.ingdrecSearch_enddate_txt);
				enddateTxt.setText(arg1 + "-" + (arg2 + 1) + "-" + arg3);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
		;
	}

}