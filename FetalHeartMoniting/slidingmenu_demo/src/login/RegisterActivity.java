package login;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.infzm.slidingmenu.demo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText editText1;
	EditText editText2;
	EditText editText3;
	RadioGroup radioGroup;
	RadioButton radioButton1;
	RadioButton radioButton2;
	Button button;
	private boolean usernameCursor = true;
	private boolean repasswordCursor = true;
	String character;
	@SuppressLint("HandlerLeak") private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (msg.what == 1) {//����1��˵�����յ���ע��ɹ�����Ϣ����ע��ɹ�ʱ���������᷵��1���ͻ���
				
					Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_SHORT)
					.show();
					Intent register_login = new Intent(RegisterActivity.this, LoginActivity.class);
					startActivity(register_login);
					finish();
				
				
			} else {
				if (editText1.getText().toString() == null) {
					Toast.makeText(RegisterActivity.this, "�û�������Ϊ��", Toast.LENGTH_SHORT)
					.show();
					editText1.requestFocus();
				} else{
					Toast.makeText(RegisterActivity.this, "�û����Ѵ��ڣ�������û���", Toast.LENGTH_SHORT)
					.show();
				}			
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		this.editText1 = (EditText) this.findViewById(R.id.usernameRegister);
		this.editText2 = (EditText) this.findViewById(R.id.passwordRegister);
		this.editText3 = (EditText) this.findViewById(R.id.passwordconfirm);
		this.radioGroup = (RadioGroup) this.findViewById(R.id.sexRegister);
		this.radioButton1 = (RadioButton) this.findViewById(R.id.nan);
		this.radioButton2 = (RadioButton) this.findViewById(R.id.woman);
		this.button = (Button) this.findViewById(R.id.Register);

		this.editText1.setOnFocusChangeListener(new CheckUsernameListener());
		this.editText3.setOnFocusChangeListener(new RePasswordListener());
		this.radioGroup.setOnCheckedChangeListener(new RadioGroupSex());
		this.button.setOnClickListener(new RegisterListener());

	}

	private class CheckUsernameListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			String myUsername = editText1.getText().toString();
			if (!usernameCursor) {
				if (isUsernameExisted(myUsername)) {
					Toast.makeText(RegisterActivity.this, "���û����Ѿ����ڣ�������û���",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		private boolean isUsernameExisted(String username) {
			boolean flag = false;
			return flag;
		}
	}

	private class RePasswordListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (repasswordCursor = !repasswordCursor) {
				if (!checkPassword(editText2.getText().toString(), editText3
						.getText().toString())) {
					editText3.setText("");
					// rePassword.requestFocus();
					Toast.makeText(RegisterActivity.this, "�������벻һ��������������",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	private boolean checkPassword(String psw1, String psw2) {
		if (psw1.equals(psw2))
			return true;
		else
			return false;
	}

	private class RadioGroupSex implements RadioGroup.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == radioButton1.getId()) {
				character = "doctor";
			} else {
				character = "pragnant";
			}
		}
	}

	private class RegisterListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (character == null) {
				String title = "��ʾ��";
				String message = "������Ϣ����������д������Ϣ�����������ṩ���õķ���";
				new AlertDialog.Builder(RegisterActivity.this).setTitle(title)
						.setMessage(message)
						.setPositiveButton("����ע��", new MakeSureListener())
						.setNegativeButton("�����޸�", null).show();
			} else if (checkPassword(editText2.getText().toString(), editText3
					.getText().toString())) {
				excuteRegister();
			} else {
				editText3.setText("");
				// rePassword.requestFocus();
				Toast.makeText(RegisterActivity.this, "�������벻һ��������������",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private class MakeSureListener implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (checkPassword(editText2.getText().toString(), editText3
					.getText().toString())) {
				excuteRegister();
			} else {
				editText3.setText("");
				// rePassword.requestFocus();
				Toast.makeText(RegisterActivity.this, "�������벻һ��������������",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void excuteRegister(){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				HttpClient client = new DefaultHttpClient();
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				NameValuePair pair = new BasicNameValuePair("index", "2");
				list.add(pair);
				NameValuePair pair1 = new BasicNameValuePair("username", editText1.getText().toString());
				NameValuePair pair2 = new BasicNameValuePair("password", editText2.getText().toString());
				NameValuePair pair3 = new BasicNameValuePair("sex", character);
				list.add(pair1);
				list.add(pair2);
				list.add(pair3);
				for (int i = 0; i < list.size(); i++) {
					Log.e("list", list.get(i).toString());
				}
				try {
					HttpEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
					Log.i("register----------->", "HttpPostǰ");
					HttpPost post = new HttpPost("http://222.168.19.131:48546/server/Servlet");
					Log.i("register----------->", "HttpPost��1");
					post.setEntity(entity);
					HttpResponse response = client.execute(post);
					Log.i("register----------->", "HttpPostǰ2");
					if (response.getStatusLine().getStatusCode() == 200) {
						InputStream in = response.getEntity().getContent();
						handler.sendEmptyMessage(in.read());//���������˷��ظ��ͻ��˵ı��ֱ�Ӵ����handler
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
		}.start();
	}
}
