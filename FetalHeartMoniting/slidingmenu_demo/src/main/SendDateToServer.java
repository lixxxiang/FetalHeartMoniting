package main;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

public class SendDateToServer {

	 private static String url="http://222.168.19.131:48547/ServerForGETMethod/ServletForGETMethod";
	    public static final int SEND_SUCCESS=0x123;
	    public static final int SEND_FAIL=0x124;
	    private Handler handler;
	    public SendDateToServer(Handler handler) {
	        // TODO Auto-generated constructor stub
	        this.handler=handler;
	    }   
	    /**
	     * ͨ��Get��ʽ���������������
	     * @param name �û���
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
	                        handler.sendEmptyMessage(SEND_SUCCESS);//֪ͨ���߳����ݷ��ͳɹ�
	                    }else {
	                        //�����ݷ��͸�������ʧ��
	                    }
	                } catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }               
	            }
	        }).start();
	    }
	    /**
	     * ����GET����
	     * @param map �������
	     * @param url ����·��
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
	            sb.deleteCharAt(sb.length()-1);//ɾ���ַ������ һ���ַ���&��
	        }
	        HttpURLConnection conn=(HttpURLConnection) new URL(sb.toString()).openConnection();
	        conn.setConnectTimeout(5000);
	        conn.setRequestMethod("GET");//��������ʽΪGET
	        if (conn.getResponseCode()==200) {
	            return true;
	        }
	        return false;
	    }
		public void SendDataToServer(String string) {
			// TODO Auto-generated method stub
			 // TODO Auto-generated method stub
	        final Map<String,String>map=new HashMap<String,String>();
	        map.put("info", string);
	        new Thread(new Runnable() {         
	            @Override
	            public void run() {
	                // TODO Auto-generated method stub
	                try {
	                     if (sendGetRequest(map,url,"utf-8")) {
	                        handler.sendEmptyMessage(SEND_SUCCESS);//֪ͨ���߳����ݷ��ͳɹ�
	                    }else {
	                        //�����ݷ��͸�������ʧ��
	                    }
	                } catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }               
	            }
	        }).start();
		}
	 
	}
