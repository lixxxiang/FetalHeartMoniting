package login;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import main.MainActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.infzm.slidingmenu.demo.R;

public class LoginActivity extends Activity {
	EditText username;
	EditText password;
	Button login;
	TextView register;
	public static String name;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		findViews();

	}

	private void findViews() {
		username = (EditText) findViewById(R.id.username_edit);
		password = (EditText) findViewById(R.id.password_edit);
		login = (Button) findViewById(R.id.signin_button);
		register = (TextView) findViewById(R.id.register_link);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				HttpClient client = new DefaultHttpClient();
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				NameValuePair pair = new BasicNameValuePair("index", "0");
				list.add(pair);
				NameValuePair pair1 = new BasicNameValuePair("username",
						username.getText().toString());
				NameValuePair pair2 = new BasicNameValuePair("password",
						password.getText().toString());
				name = username.getText().toString();
				Log.e("username", username.getText().toString());
				Log.e("password", password.getText().toString());
				list.add(pair1);
				list.add(pair2);
				for (int j = 0; j < list.size(); j++) {
					Log.e("list", list.get(j).toString());
				}

				try {
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							list, "UTF-8");
					HttpPost post = new HttpPost(
							"http://222.168.19.131:48546/server/Servlet");
					post.setEntity(entity);
					HttpResponse response = client.execute(post);
					Log.e("code", "" + response.getStatusLine().getStatusCode());// 404
					if (response.getStatusLine().getStatusCode() == 200) {
						InputStream in = response.getEntity().getContent();
						int msg = in.read();
						Log.e("in.read", "" + msg);
						if (msg == 1) {
							Intent login_main = new Intent(LoginActivity.this,
									MainActivity.class);
							Log.i("login_main----------------->", "success");
							startActivity(login_main);
							finish();
						} else {
							Toast.makeText(LoginActivity.this, "µÇÂ½Ê§°Ü",
									Toast.LENGTH_SHORT).show();
						}
						in.close();
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

}