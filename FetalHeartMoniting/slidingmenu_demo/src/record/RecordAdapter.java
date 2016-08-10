package record;

import java.io.File;

import upload.FileUploadActivity;


import main.MainActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infzm.slidingmenu.demo.R;



public class RecordAdapter extends BaseAdapter {
	
	private Context mContext;
	private File[] mData;
	private LayoutInflater mInflater;
	private MediaPlayer mPlayer;
	
	
	public RecordAdapter(Context context, File[] data) {
		super();
		mContext = context;
		mData = data;
		mPlayer  =   new MediaPlayer();
		mInflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		return mData.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mData[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.list_item, null);
		TextView fileName = (TextView) convertView.findViewById(R.id.fileName);
		TextView createtime = (TextView) convertView.findViewById(R.id.createtime);
		
		final File file = mData[position];
		fileName.setText(file.getName());
		createtime.setText(U.millis2CalendarString(file.lastModified(), "yyyy年MM月dd日 HH:mm:ss"));
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				play(file.getAbsolutePath());
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mActivity)
				.setMessage("请选择操作")
				.setNegativeButton("上传音频", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(MainActivity.mActivity,FileUploadActivity.class);
						MainActivity.mActivity.startActivity(intent);
					}
				})
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				builder.show();
				
				return false;
			}
		});
		
		return convertView;
	}
	
	private void play(String path) {
        try {
        	mPlayer.reset();
			mPlayer.setDataSource(path);
			mPlayer.prepare();
	        mPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}




