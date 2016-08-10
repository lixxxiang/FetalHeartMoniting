package fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.infzm.slidingmenu.demo.R;

public class ChooseDocFragment extends Fragment {
	Spinner spinner;
	List<String> data_list;
	ArrayAdapter<String> arr_adapter;
	public static String DocName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_discuss, null);
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		data_list = new ArrayList<String>();
		data_list.add("ҽ��A");
		data_list.add("ҽ��B");
		data_list.add("ҽ��C");
		data_list.add("ҽ��D");

		// ������
		arr_adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_list);
		// ������ʽ
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ����������
		spinner.setAdapter(arr_adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				DocName = (String) spinner.getSelectedItem();
				Log.e("Doc", DocName);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void getString(Callback callback) {
		String msg = DocName;
		callback.getString(msg);
	}

	// �����ӿ�
	public interface Callback {
		public void getString(String msg);
	}

}
