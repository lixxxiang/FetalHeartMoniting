package upload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.infzm.slidingmenu.demo.R;

public class FileUploadActivity extends Activity implements OnClickListener {

	protected static final int SUCCESS = 2;
	protected static final int FAILD = 3;
	protected static int RESULT_LOAD_FILE = 1;
	private TextView cancel;
	private TextView upload;
	private EditText pathView;
	private Button buttonLoadImage;
	public static String picturePath;
	Button mBtnUpload;
	private View view;
	File file;
	String uploadUrl = "http://222.168.19.131:48547/UploadPhoto1/UploadServlet";
	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {

			case SUCCESS:
				view.setVisibility(View.INVISIBLE);
				picturePath = "";
				pathView.setText(picturePath);
				Toast.makeText(getApplicationContext(), "上传成功！",
						Toast.LENGTH_LONG).show();
				break;
			case FAILD:
				view.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
			return false;
		}

	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_upload);
		cancel = (TextView) findViewById(R.id.cancel);
		upload = (TextView) findViewById(R.id.upload);
		buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
		cancel.setOnClickListener(this);
		upload.setOnClickListener(this);
		buttonLoadImage.setOnClickListener(this);
		view = findViewById(R.id.show);
		pathView = (EditText) findViewById(R.id.file_path);
		pathView.setKeyListener(null);
		picturePath = "";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.buttonLoadPicture:
			Intent intent = new Intent(getApplicationContext(),FileSelectActivity.class);
			startActivityForResult(intent, RESULT_LOAD_FILE);
			break;
		case R.id.upload:
			uploadFile();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_LOAD_FILE
				&& data != null) {
			picturePath = data.getStringExtra("path");
			pathView.setText(picturePath);
		}
	}

	@Override
	protected void onDestroy() {
		view.setVisibility(View.INVISIBLE);
		super.onDestroy();
	}

	private void uploadFile() {
		view.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {

				Message msg = Message.obtain();
				Map<String, File> files = new HashMap<String, File>();
				String name = picturePath.substring(picturePath.lastIndexOf("/")+1,picturePath.length());
				file = new File(picturePath);
				files.put(name, file);
				DecimalFormat df = new DecimalFormat("0.00");
				double size = file.length() / 1024;
				String sizeafter = df.format(size / 1024);
				Log.e("UPLOADfile", sizeafter + "MB");
				Log.d("str--->>>", picturePath);
				try {
					String str=post(uploadUrl,new HashMap<String, String>(), files);
					System.out.println("str--->>>" + str);
					msg.what = SUCCESS;
				} catch (Exception e) {
					msg.what = FAILD;
				}
				mHandler.sendMessage(msg);

			}

			private String post(String uploadUrl,HashMap<String, String> hashMap, Map<String, File> files) throws IOException {
				// TODO Auto-generated method stub
				String BOUNDARY = java.util.UUID.randomUUID().toString();
				String PREFIX = "--", LINEND = "\r\n";
				String MULTIPART_FROM_DATA = "multipart/form-data";
				String CHARSET = "UTF-8";
				URL uri = new URL(uploadUrl);
				HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
				conn.setReadTimeout(5 * 1000);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("connection", "keep-alive");
				conn.setRequestProperty("Charsert", "UTF-8");
				conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA+ ";boundary=" + BOUNDARY);		
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, String> entry : hashMap.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET+ LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}
		
				DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
				outStream.write(sb.toString().getBytes());		
				if (files != null)
					for (Map.Entry<String, File> file : files.entrySet()) {
						StringBuilder sb1 = new StringBuilder();
						sb1.append(PREFIX);
						sb1.append(BOUNDARY);
						sb1.append(LINEND);
						sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""+ file.getKey() + "\"" + LINEND);
						sb1.append("Content-Type: application/octet-stream; charset="+ CHARSET + LINEND);
						sb1.append(LINEND);
						outStream.write(sb1.toString().getBytes());
		
						InputStream is = new FileInputStream(file.getValue());
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							outStream.write(buffer, 0, len);
						}
		
						is.close();
						outStream.write(LINEND.getBytes());
					}
		
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
				outStream.write(end_data);
				outStream.flush();
		
				int res = conn.getResponseCode();
				InputStream in = conn.getInputStream();
				if (res == 200) {
					int ch;
					StringBuilder sb2 = new StringBuilder();
					while ((ch = in.read()) != -1) {
						sb2.append((char) ch);
					}
				}
				outStream.close();
				conn.disconnect();
				return in.toString();
			}
		}.start();
	}
}

