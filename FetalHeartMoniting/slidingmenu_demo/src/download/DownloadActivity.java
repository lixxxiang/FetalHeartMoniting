package download;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import login.LoginActivity;
import login.UserService;
import com.infzm.slidingmenu.demo.R;
import fragment.ChooseDocFragment;
import fragment.ChooseDocFragment.Callback;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class DownloadActivity extends Activity {
	String buffer = "";
	TextView txt1;
	Button send;
	String geted1;
	 ListView listView;
	 String username=LoginActivity.name;
	 String docname;
	  private String downloadFile = null;    
	  private static String serverIP = "222.168.19.131";
	  private Socket socket=null;
	  public static DownloadActivity mainActivity;
	  private FragmentManager manager;
	  Handler handler=new Handler(){
	        @SuppressLint("HandlerLeak") 
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case SendDateToServer.SEND_SUCCESS:
	                Toast.makeText(DownloadActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
	                Log.e("DOcname", docname);
	                break;
	            case SendDateToServer.SEND_FAIL:
	                Toast.makeText(DownloadActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
	                break;
	 
	            default:
	                break;
	            }
	        };      
	    };
    @SuppressLint({ "NewApi", "CommitTransaction" }) 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        if (android.os.Build.VERSION.SDK_INT > 9) {
    	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	    StrictMode.setThreadPolicy(policy);
        }
        mainActivity=this;
       
        
        Button button = (Button) findViewById(R.id.button1);
        txt1=(TextView) findViewById(R.id.textView1);
        listView=(ListView) findViewById(R.id.listView1);
        manager = getFragmentManager();
        manager.beginTransaction();
        final ChooseDocFragment fragment=new ChooseDocFragment();
        
        
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	fragment.getString(new Callback() {

		@Override
		public void getString(String msg) {
			// TODO Auto-generated method stub
			docname=msg;
			Log.e("msg",docname);
		}
		
	});
			
				Log.e("Code", getEncoding(username)+"   "+getEncoding(docname));
				Log.e("DOcname", username+docname);
				new SendDateToServer(handler).SendDataToServer(username,docname);
				new MyThread(geted1).start();
			}
		});
    }
    class MyThread extends Thread {
		public String txt1;
		public MyThread(String str) {
			txt1 = str;
		}

		@Override
		public void run() {
			Message msg = new Message();
			msg.what = 0x11;
			Bundle bundle = new Bundle();
			bundle.clear();
			try {
				socket = new Socket();
				socket.connect(new InetSocketAddress(serverIP, 48548), 5000);
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream() ,"GB2312"));
				String line = null;
				buffer = "";
				while ((line = bff.readLine()) != null) {
					buffer = line + buffer;
				}
				String string = buffer.toString();
				bundle.putString("msg", string);			   
				msg.setData(bundle);
				myHandler.sendMessage(msg);
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				msg.setData(bundle);
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    @SuppressLint("HandlerLeak") 
    public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				String finalString=bundle.getString("msg");
				Log.e("finalstring", finalString);
				final String[] finalsplit = finalString.split("   ");//获得到字符串数组
				for (int i = 0; i < finalsplit.length; i++) {
					String string[]=finalsplit[i].split("-");//获得用户名
					String filename=string[0];
					Log.e("finalsplit","截取的："+filename+"用户名："+username);
					
				}
				for (int j = 0; j < finalsplit.length; j++) {
					Log.e("finalsplit为", finalsplit[j]);
				}
				
				for (int j = 0; j < finalsplit.length; j++) {
					Log.e("length", ""+finalsplit.length);
			        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DownloadActivity.this, R.layout.array_item, finalsplit); 
			        listView.setAdapter(adapter1);
			        downloadFile= finalsplit[j];
			        
			        listView.setOnItemClickListener(new OnItemClickListener() {

						@SuppressLint("ShowToast") 
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							Log.e("d2", finalsplit[arg2]);
							downloadFile=finalsplit[arg2];
							AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this)
							.setMessage("请选择操作")
							.setNegativeButton("下载音频", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
									UserService uService=new UserService(DownloadActivity.this);
						            Log.e("d3",""+uService.IsDoc(username));
						            try{  
						                  socket = new Socket(serverIP, 48549);  						                    
						              }catch(IOException e){                      
						              }  
						            if(uService.IsDoc(username)){
						            	
						            	Thread downFileThread = new Thread(new DownFileThread(socket, downloadFile));  
							               downFileThread.start(); 
							               Toast toast=Toast.makeText(DownloadActivity.this, "download success!",Toast.LENGTH_LONG);
							                toast.show();
							                
						            }else{
						            	 
							              Log.e("d2", downloadFile);
							              String string[]=downloadFile.split("-");//获得用户名
											String filename=string[0];
							              if (!filename.equals(username)) {
											Toast toast=Toast.makeText(DownloadActivity.this, "只能下载登陆名开头的用户文件", Toast.LENGTH_LONG);
											toast.show();
							            	  Log.e("d2", "cannot download");
										}else{
							               Thread downFileThread = new Thread(new DownFileThread(socket, downloadFile));  
							               downFileThread.start(); 
							               Toast toast=Toast.makeText(DownloadActivity.this, "download success!",Toast.LENGTH_LONG);
							                toast.show();
										}
						            }
									
								}
							})
							.setPositiveButton("取消", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									
								}
							});
							builder.show();
						}
					});
				}
			}
		}



	};
	
	public static String getEncoding(String str) {      
	       String encode = "GB2312";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s = encode;      
	              return s;      
	           }      
	       } catch (Exception exception) {      
	       }      
	       encode = "ISO-8859-1";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s1 = encode;      
	              return s1;      
	           }      
	       } catch (Exception exception1) {      
	       }      
	       encode = "UTF-8";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s2 = encode;      
	              return s2;      
	           }      
	       } catch (Exception exception2) {      
	       }      
	       encode = "GBK";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s3 = encode;      
	              return s3;      
	           }      
	       } catch (Exception exception3) {      
	       }      
	      return "";      
	   }      
}



class DownFileThread extends Thread {  
    private Socket socket;  
    private String downloadFile;  
    private final static int Buffer = 8 * 1024; 
    
    
    public DownFileThread(Socket socket, String downloadFile) {  
        super();  
        this.socket = socket;  
        this.downloadFile = downloadFile;  
    }  
    public Socket getSocket() {  
        return socket;  
    }  
    public void setSocket(Socket socket) {  
        this.socket = socket;  
    }  
    public String getDownloadFile() {  
        return downloadFile;  
    }  
    public void setDownloadFile(String downloadFile) {  
        this.downloadFile = downloadFile;  
    }  

    private long request(String fileName, String password) throws IOException {  
  
        DataInputStream in = new DataInputStream(socket.getInputStream());    
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));  
        String requestString = fileName + "@ " + password;  
        out.println(requestString);   
        out.flush();  
        return in.readLong();   
    }  
    private void receiveFile(String localFile) throws Exception {  
         
        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());  
        FileOutputStream out = new FileOutputStream(localFile);  
        byte[] buf = new byte[Buffer];  
        int len;  
        while ((len = in.read(buf)) >= 0) {  
            out.write(buf, 0, len);  
            out.flush();  
        }  
        out.close();  
        in.close();  
    }  

    public void download(String downloadFile) throws Exception {  
        try {  
            String password = "password";    
            String localpath = "/storage/emulated/0/";  
            String localFile = localpath + downloadFile;  
            long fileLength = request(downloadFile, password);  
  
            if (fileLength >= 0) {  
                receiveFile(localFile);  
                System.out.println("file:"+downloadFile+"had save to"+localFile); 
                Toast toast=Toast.makeText(DownloadActivity.mainActivity, "download success!",Toast.LENGTH_LONG);
                toast.show();
            } else {  
                System.out.println("download " + downloadFile + " error! ");  
            }  
        } catch (IOException e) {  
            System.out.println(e.toString());  
        } finally {  
            socket.close(); 
        }  
    }  
    @Override  
    public void run() {  
        System.out.println("DownFileThread currentThread--->"  + DownFileThread.currentThread().getId());  
        // TODO Auto-generated method stub  
        try {  
            download(downloadFile);  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        super.run();  
    }  
    
}  

 class SendDateToServer {
    private static String url="http://222.168.19.131:48547/ServerForGETMethod/ServletForGETMethod";
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;
    private Handler handler;
    public SendDateToServer(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler=handler;
    }   
    /**
     * 通过Get方式向服务器发送数据
     * @param name 用户名
     */
    public void SendDataToServer(String name,String docname) {
        // TODO Auto-generated method stub
        final Map<String,String>map=new HashMap<String,String>();
        map.put("name", name);
        map.put("docname", docname);
        new Thread(new Runnable() {         
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                     if (sendGetRequest(map,url,"utf-8")) {
                        handler.sendEmptyMessage(SEND_SUCCESS);//通知主线程数据发送成功
                    }else {
                        //将数据发送给服务器失败
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }               
            }
        }).start();
    }
    /**
     * 发送GET请求
     * @param map 请求参数
     * @param url 请求路径
     * @return
     * @throws Exception 
     */
    private  boolean sendGetRequest(Map<String,String> param, String url,String encoding) throws Exception {
        // TODO Auto-generated method stub
        //http://localhost:8080/ServerForGETMethod/ServletForGETMethod?name=aa&pwd=124
        StringBuffer sb=new StringBuffer(url);
        if (!url.equals("")&!param.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String,String>entry:param.entrySet()) {
                sb.append(entry.getKey()+"=");              
                    sb.append(URLEncoder.encode(entry.getValue(), encoding));               
                sb.append("&");
            }
            sb.deleteCharAt(sb.length()-1);//删除字符串最后 一个字符“&”
        }
        HttpURLConnection conn=(HttpURLConnection) new URL(sb.toString()).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");//设置请求方式为GET
        if (conn.getResponseCode()==200) {
            return true;
        }
        return false;
    }
 
}
