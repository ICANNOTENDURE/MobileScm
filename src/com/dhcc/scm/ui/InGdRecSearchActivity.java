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
import com.dhcc.scm.ui.base.BaseActivity;

/**
 * @author huaxiaoying
 *  2015/10/21 15:05
 */

public class InGdRecSearchActivity extends BaseActivity implements OnClickListener {
    private Button startDateBtn;
	private TextView startdateTxt;
	private Button endDateBtn;
	private TextView enddateTxt;

	private ImageView imgBack = null;// 回退按钮
	private Button ingdrecSearch_select = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ingdrec_search);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		startDateBtn = (Button) findViewById(R.id.ingdrecSearch_startDate_btn);
		startdateTxt = (TextView) findViewById(R.id.ingdrecSearch_startdate_txt);
		endDateBtn = (Button) findViewById(R.id.ingdrecSearch_endDate_btn);
		enddateTxt = (TextView) findViewById(R.id.ingdrecSearch_enddate_txt);

		imgBack = (ImageView) findViewById(R.id.ingdrecsearch_back_btn);
		imgBack.setOnClickListener(this);
		ingdrecSearch_select = (Button) findViewById(R.id.ingdrecSearch_select_btn);
		ingdrecSearch_select.setOnClickListener(this);

	}

	@Override
	protected void initView() {
		startDateBtn.setOnClickListener(this);
		startdateTxt = (TextView) findViewById(R.id.ingdrecSearch_startdate_txt);
		endDateBtn.setOnClickListener(this);
		enddateTxt = (TextView) findViewById(R.id.ingdrecSearch_enddate_txt);
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