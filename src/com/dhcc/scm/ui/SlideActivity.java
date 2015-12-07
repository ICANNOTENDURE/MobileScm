package com.dhcc.scm.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class SlideActivity extends RelativeLayout {
	private static String TAG = "SlideMenuLayout";

	private Context mContext;
	private Scroller mScroller; // Android �ṩ�Ļ���������
	private int mTouchSlop = 0; // �ڱ��ж�Ϊ����֮ǰ�û���ָ�����ƶ������ֵ
	private VelocityTracker mVelocityTracker; // ���ڼ�����ָ�������ٶ�
	public static final int SNAP_VELOCITY = 200; // ������ʾ��������಼��ʱ����ָ������Ҫ�ﵽ���ٶȣ�ÿ��200�����ص�
	private int mMaxScrollX = 0; // ���������룬����menu�Ŀ��

	public void setMaxScrollX(int maxScrollX) {
		this.mMaxScrollX = maxScrollX;
	}

	private float mDownX; // һ�ΰ���̧��Ķ����У�����ʱ��X��꣬���ں�̧��ʱ��X�Ƚϣ��ж��ƶ����롣����mTouchSlop���ж�Ϊԭ�ص��
	private float mLastX; // ��¼��������е�X���

	private boolean isMenuOpen = false; // �˵������Ƿ񱻴򿪣�ֻ����ȫ�򿪲�Ϊtrue

	public boolean isMenuOpen() {
		return isMenuOpen;
	}

	private View mContent;
	


	public SlideActivity(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		Log.v(TAG, "init start");
		mScroller = new Scroller(mContext);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			mContent = getChildAt(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		int curScrollX = getScrollX();
		// ��鴥�����Ƿ��ڻ�������(����content)�У�������򷵻�false������View��������¼�
		if (mContent != null) {
			Rect rect = new Rect();
			mContent.getHitRect(rect);
			if (!rect.contains((int) event.getX() + curScrollX,
					(int) event.getY())) {
				return false;
			}
		}

		float x = event.getX(); // ȡ�ñ���event��X���
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = x;
			mLastX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastX - x);
			if ((curScrollX + deltaX) < -mMaxScrollX) {
				deltaX = -mMaxScrollX - curScrollX;
			}
			if ((curScrollX + deltaX) > 0) {
				deltaX = -curScrollX;
			}

			if (deltaX != 0) {
				scrollBy(deltaX, 0);
			}
			mLastX = x;
			break;
		case MotionEvent.ACTION_UP:
			int velocityX = getScrollVelocity();
			int offsetX = (int) (x - mDownX);

			// ���������ƶ������Ѿ��ﵽ���ж�Ϊ��������ͱ�׼
			// ���������������ж�Ϊ����������Ϊ�ǵ�һ�ĵ������ر�menu
			if (Math.abs(offsetX) >= mTouchSlop) {
				
				// ����������ָ�ƶ��ٶȴ�꣬������Զ�������
				// �����������ٶȲ���꣬����Ȼ��Ҫ�жϵ�ǰSlideLayout��λ��
				// ����Ѿ�����һ�룬������Զ����ʣ�µĻ��������û�г���һ�룬���򻬶�
				if (Math.abs(velocityX) >= SNAP_VELOCITY) {
					if (velocityX > 0) {
						openMenu();
					} else if (velocityX < 0) {
						closeMenu();
					}
				} else {
					if (curScrollX >= -mMaxScrollX / 2) {
						closeMenu();
					} else {
						openMenu();
					}
				}
			} else {
				closeMenu();
			}

			recycleVelocityTracker();
			break;
		}
		return true;
	}

	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	// ��ȡ��ָ��View�ϵĻ����ٶ�,��ÿ�����ƶ��˶�������ֵΪ��λ
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		return (int) mVelocityTracker.getXVelocity();
	}

	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	// ��Menu����
	public void openMenu() {
		int curScrollX = getScrollX();
		scrollToDestination(-mMaxScrollX - curScrollX);
		isMenuOpen = true;
	}

	// �ر�Menu����
	public void closeMenu() {
		int curScrollX = getScrollX();
		scrollToDestination(-curScrollX);
		isMenuOpen = false;
	}

	private void scrollToDestination(int x) {
		if (x == 0)
			return;

		mScroller.startScroll(getScrollX(), 0, x, 0, Math.abs(x));
		invalidate();
	}
}