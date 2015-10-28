package com.dhcc.scm.ui;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.http.thread.HttpGetPostCls;

public class mobilecom {

	public String RetData = "";
	public String Code = "";
	public static String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	public static String ACTION_CONTENT_NOTIFY_EMII = "com.ge.action.barscan";
	public static String ACTION_CONTENT_NOTIFY_MOTO = "ACTION_CONTENT_NOTIFY_MOTO";
	public static String ACTION_CONTENT_NOTIFY_SCAN = "com.android.server.scannerservice.broadcast";
	public static Map<String, String> actionMap = new HashMap<String, String>();
	public Map<String, String> RetDataPag = null;

	public mobilecom() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO, "com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_SCAN, "scannerdata");
	}

	public void searchcontrol(ViewGroup vv, Map OBJBTEMP) {
		for (int i = 0; i < vv.getChildCount(); i++) {

			if (vv.getChildAt(i) instanceof TextView) {
				TextView btn = (TextView) vv.getChildAt(i);
				String title = btn.getText().toString();
				if (title.indexOf("Title") != -1) {
					if (OBJBTEMP.get(title) == null) {
						if (title.equals("Title0")) {
							continue;
						} else {

							btn.setText("");
							btn.setLayoutParams(getlayparam(Integer.valueOf(2), btn.getLayoutParams().height));

							continue;
						}
					}
					String[] itm = (String[]) OBJBTEMP.get(title);
					btn.setText(itm[0]);
					// LayoutParams lp=(LayoutParams)btn.getLayoutParams();

					// btn.setWidth(Integer.valueOf(itm[1]));
					btn.setLayoutParams(getlayparam(Integer.valueOf(itm[1]), btn.getLayoutParams().height));
					// getScreenWidth() - DimensionUtility.dip2px(this, 20))
					// btn.setWidth((getScreenWidth() -
					// DimensionUtility.dip2px(this, 20))/3);
				}
				// btn.setWidth(180);
				// btn.setHeight(120);
				// String tag=(String)btn.getTag();
			}
			if (vv.getChildAt(i) instanceof LinearLayout) {
				searchcontrol((ViewGroup) vv.getChildAt(i), OBJBTEMP);
			}
			if (vv.getChildAt(i) instanceof RelativeLayout) {
				searchcontrol((ViewGroup) vv.getChildAt(i), OBJBTEMP);
			}
			if (vv.getChildAt(i) instanceof AbsoluteLayout) {
				searchcontrol((ViewGroup) vv.getChildAt(i), OBJBTEMP);
			}
		}

	}

	private LinearLayout.LayoutParams getlayparam(int w, int h) {
		LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(w, h);
		return lp0;

	}

	public static String ChangeDate(String itmdate) {

		String[] itm = itmdate.split("\\/");
		if (itm.length < 2)
			return itmdate;
		return itm[2] + "-" + itm[1] + "-" + itm[0];
	}

	public static String GetDate(String typ) {
		int mHour;

		int mMinute;

		int mYear;

		int mMonth;

		int mDay;
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		// ��ȡ��ǰ���
		mMonth = c.get(Calendar.MONTH) + 1;// ��ȡ��ǰ�·�
		mDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ��ǰ�·ݵ����ں���
		mHour = c.get(Calendar.HOUR_OF_DAY);// ��ȡ��ǰ��Сʱ��
		mMinute = c.get(Calendar.MINUTE);// ��ȡ��ǰ�ķ�����
		String Currdate = mDay + "/" + mMonth + "/" + mYear;
		String CurrTime = mHour + ":" + mMinute;
		if (typ.equals("d"))
			return Currdate;
		if (typ.equals("t"))
			return CurrTime;
		return Currdate + "," + CurrTime;

	}

	public static String GetDiffDate(String typ, int days) {
		int mHour;

		int mMinute;

		int mYear;

		int mMonth;

		int mDay;

		GregorianCalendar c = new GregorianCalendar();
		c.add(GregorianCalendar.DATE, days);

		// final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		// ��ȡ��ǰ���
		mMonth = c.get(Calendar.MONTH) + 1;// ��ȡ��ǰ�·�
		mDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ��ǰ�·ݵ����ں���
		mHour = c.get(Calendar.HOUR_OF_DAY);// ��ȡ��ǰ��Сʱ��
		mMinute = c.get(Calendar.MINUTE);// ��ȡ��ǰ�ķ�����
		String Currdate = mDay + "/" + mMonth + "/" + mYear;
		String CurrTime = mHour + ":" + mMinute;
		if (typ.equals("d"))
			return Currdate;
		if (typ.equals("t"))
			return CurrTime;
		return Currdate + "," + CurrTime;

	}

	public void ThreadHttp(final String Cls, final String mth, final String Param, final String Typ, final Context context, final int whatmsg, final Handler handler) {
		if (mobilecom.isConnect(context) == false) {
			Toast.makeText(context, "�����·��", 1000).show();
			return;
		}

		Thread thread = new Thread() {

			public void run() {
				try {
					this.sleep(10);
					RetData = "";
					RetData = HttpGetPostCls.LinkData(Cls, mth, Param, Typ, context);
					Message msg = new Message();
					msg.what = whatmsg;
					handler.sendMessage(msg);
				} catch (Exception e) {

				}

			}
		};
		thread.start();

	}

	public void ThreadHttp(final String Cls, final String mth, final String Param, final String Typ, final Context context, final int whatmsg, final Handler handler, final String Code) {
		if (mobilecom.isConnect(context) == false) {
			Toast.makeText(context, "�����·��", 1000).show();
			return;
		}

		Thread thread = new Thread() {

			public void run() {
				try {
					// this.sleep(10);
					RetData = "";
					RetData = HttpGetPostCls.LinkData(Cls, mth, Param, Typ, context);
					if (!Code.equals("")) {

						RetDataPag.put(Code, RetData);

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

	public static boolean isConnect(Context context) {
		// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ��?
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// ��ȡ�������ӹ���Ķ���
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// �жϵ�ǰ�����Ƿ��Ѿ�����
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("error", e.toString());
		}
		return false;
	}

}
