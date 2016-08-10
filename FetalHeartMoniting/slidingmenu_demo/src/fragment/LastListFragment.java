package fragment;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.MainActivity;


import com.infzm.slidingmenu.demo.R;

import date.NumericWheelAdapter;
import date.OnWheelScrollListener;
import date.WheelView;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

@SuppressLint("InflateParams") public class LastListFragment extends Fragment {
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	
	PopupWindow menuWindow;
	TextView t1,t2,t3;
	Button tv_date,tv_date2;
public static String string3;
	@SuppressWarnings("static-access")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater=(LayoutInflater) MainActivity.mActivity.getSystemService(MainActivity.mActivity.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.frag_lastlist, null);
		
		tv_date=(Button) view.findViewById(R.id.tv_date);
		tv_date2=(Button) view.findViewById(R.id.button1);
		Button button=(Button) view.findViewById(R.id.button2);
		t1=(TextView) view.findViewById(R.id.textView2);
		t2=(TextView) view.findViewById(R.id.textView4);
		t3=(TextView) view.findViewById(R.id.textView5);
        
		tv_date2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopwindow(getDataPick2());
				
			}
		});
		
		tv_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPopwindow(getDataPick1());
				
			}
		});
		
		button.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("SimpleDateFormat") @Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str1=""+t1.getText();
				String str2=""+t2.getText();
				int days=0;
				 try {

			            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			            Date date1 = sdf.parse(str1);
			            Date date2 = sdf.parse(str2);
			            Calendar can1 = Calendar.getInstance();
			            can1.setTime(date1);
			            Calendar can2 = Calendar.getInstance();
			            can2.setTime(date2);
			            int year1 = can1.get(Calendar.YEAR);
			            int year2 = can2.get(Calendar.YEAR);
			            
			            Calendar can = null;
			            if(can1.before(can2)){
			                days -= can1.get(Calendar.DAY_OF_YEAR);
			                days += can2.get(Calendar.DAY_OF_YEAR);
			                can = can1;
			            }else{
			                days -= can2.get(Calendar.DAY_OF_YEAR);
			                days += can1.get(Calendar.DAY_OF_YEAR);
			                can = can2;
			            }
			            for (int i = 0; i < Math.abs(year2-year1); i++) {

			                days += can.getActualMaximum(Calendar.DAY_OF_YEAR);

			                can.add(Calendar.YEAR, 1);
			            }
			           
			            t3.setText(""+days/7+"周零"+days%7+"天");
			           
			        } catch (ParseException e) {
			            e.printStackTrace();
			        }
			}
		});		
	

		return view;
	}
	
	
	

	@SuppressWarnings("deprecation")
	private void showPopwindow(View view) {
		menuWindow = new PopupWindow(view,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow=null;
			}
		});
	}
	
	
	@SuppressLint("InflateParams") private View getDataPick1() {
		
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curDate = c.get(Calendar.DATE);
		final View view = inflater.inflate(R.layout.datapick, null);
		
		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(2014, curYear+10));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);
		
		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
		
		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear,curMonth);
		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - 2014);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		
		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String string = (year.getCurrentItem()+2014) + "-"+ (month.getCurrentItem()+1)+"-"+(day.getCurrentItem()+1);
				t1.setText(string);
				Log.e("a", ""+t1.getText());
				menuWindow.dismiss();
				
			}

			
		});
		
		
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 2014;
			int n_month = month.getCurrentItem() + 1;
			initDay(n_year,n_month);
		}
	};

	@SuppressLint("InflateParams") private View getDataPick2() {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curDate = c.get(Calendar.DATE);
		final View view = inflater.inflate(R.layout.datapick, null);
		
		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(2014, curYear+10));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener2);
		
		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener2);
		
		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear,curMonth);
		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - 2014);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		
		Button bt = (Button) view.findViewById(R.id.set);

		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String string2 = (year.getCurrentItem()+2014) + "-"+ (month.getCurrentItem()+1)+"-"+(day.getCurrentItem()+1);
				t2.setText(string2);
				menuWindow.dismiss();
			}

			
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}

	OnWheelScrollListener scrollListener2 = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 2014;
			int n_month = month.getCurrentItem() + 1;
			initDay(n_year,n_month);
		}
	};

	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}

}


