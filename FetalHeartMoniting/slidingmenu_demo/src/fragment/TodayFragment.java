
package fragment;

import java.io.File;
import record.MusicEnergy;
import record.RecordActivity;
import record.RecordAdapter;
import record.U;
import record.WavFileNameFilter;
import com.infzm.slidingmenu.demo.R;
import download.DownloadActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
@SuppressLint("InflateParams") @SuppressWarnings("unused")
public class TodayFragment extends Fragment {
	
	private ListView listView;
	private TextView empty,record,download;
	private RecordAdapter mAdapter;
	MusicEnergy energy;
	private LayoutInflater inflater = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab01, null);
		record =(TextView) view.findViewById(R.id.record);
		download=(TextView) view.findViewById(R.id.download);
		empty = (TextView) view.findViewById(R.id.empty);
		listView = (ListView) view.findViewById(R.id.listview);
		energy = (MusicEnergy) view.findViewById(R.id.music_energy);
		listView.setEmptyView(empty);
		record.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChooseDocFragment chooseDocFragment = new ChooseDocFragment();
				String docname=ChooseDocFragment.DocName;
//				Log.e("docname", docname);
				if (docname==null) {
					Toast.makeText(getActivity(), "请先选择医生姓名", Toast.LENGTH_LONG).show();
				}else{
					Log.e("docname", docname);
				Intent intent=new Intent(getActivity(),RecordActivity.class);
			    startActivity(intent);
				}
			}
		});
		
		download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(getActivity(),DownloadActivity.class);
				startActivity(intent2);
			}
		});
		
		U.createDirectory();
		return view;
		
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateData();
		energy.start();
	}

	private void updateData() {
		File[] files = null;
		
		if(U.sdCardExists()) {
			File file = new File(U.DATA_DIRECTORY); 
			WavFileNameFilter filenameFilter = new WavFileNameFilter(".wav");
			files = file.listFiles(filenameFilter);
		}
		
		mAdapter = new RecordAdapter(getActivity(), files);
		listView.setAdapter(mAdapter);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	
	
	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		energy.onClose();
		super.onPause();
	}

//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		System.exit(0);
//	}
}
