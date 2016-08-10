package upload;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infzm.slidingmenu.demo.R;



public class FileSelectActivity extends ListActivity {
	private static final String root = new String(Environment
			.getExternalStorageDirectory().getPath() + File.separator);
	private TextView tv;
	private File[] files;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fileupload);
		tv = (TextView) findViewById(R.id.currPath);
		getFiles(root);
	}

	public void getFiles(String path) {
		String path2=path+"i5suoi/";
		Log.e("a", path2);
		tv.setText(path2);
		
		File f = new File(path2);

		File[] tem = f.listFiles();

		if (!path.equals(root)) {
			files = new File[tem.length + 1];
			System.arraycopy(tem, 0, files, 1, tem.length);
			files[0] = f.getParentFile();
		} else {
			files = tem;
		}
		sortFilesByDirectory(files);

		setListAdapter(new Adapter(this, files, files.length == tem.length));
	}


	private void sortFilesByDirectory(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.length()).compareTo(f2.length());
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File f = files[position];
		if (!f.canRead()) {
			Toast.makeText(this, "文件不可读", Toast.LENGTH_SHORT).show();
			return;
		}
		if (f.isFile()) {
			String path = f.getAbsolutePath();
			Intent intent = new Intent();
			intent.putExtra("path", path);
			setResult(FileUploadActivity.RESULT_LOAD_FILE, intent);
			finish();
		} else {
			getFiles(f.getAbsolutePath());
		}
	}

	class Adapter extends BaseAdapter {
		private File[] files;
		private boolean istop;
		private Context context;

		public Adapter(Context context, File[] files, boolean istop) {
			this.context = context;
			this.files = files;
			this.istop = istop;
		}

		@Override
		public int getCount() {
			return files.length;
		}

		@Override
		public Object getItem(int position) {
			return files[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(context, R.layout.item_fileupload,
						null);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.adapter_icon);
				holder.tv = (TextView) convertView
						.findViewById(R.id.adapter_txt);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			setconvertViewRow(position, holder);
			return convertView;
		}

		private void setconvertViewRow(int position, Holder holder) {
			File f = files[position];
			holder.tv.setText(f.getName());
			if (!istop && position == 0) {

				holder.iv.setImageResource(R.drawable.back_up);
			} else if (f.isFile()) {

				holder.iv.setImageResource(R.drawable.file);
			} else {

				holder.iv.setImageResource(R.drawable.dir);
			}
		}

		class Holder {
			private ImageView iv;
			private TextView tv;
		}
	}
}
