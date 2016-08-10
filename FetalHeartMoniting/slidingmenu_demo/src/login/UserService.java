package login;


import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class UserService {
	private DatabaseHelper dbHelper;
	public UserService(Context context){
		dbHelper=new DatabaseHelper(context);
	}
	

	public boolean login(String username,String password){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where username=? and password=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{username,password});
		if(cursor.moveToFirst()==true){
			String string = cursor.getString(cursor.getColumnIndex("sex"));
			Log.e("ta", string);
			cursor.close();
			return true;
		}
		return false;
	}
	public boolean IsDoc(String name){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where username ="+   "\""   +name   +   "\"";
		Cursor cursor=sdb.rawQuery(sql, null);
		if(cursor.moveToFirst()==true){
			String string = cursor.getString(cursor.getColumnIndex("sex"));
			Log.e("d3", string);
			if (string.equals("Ò½Éú")) {
				return true;
			}
			cursor.close();
			return false;
		}
		return false;
	}

	public boolean register(User user) throws UnsupportedEncodingException{
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="insert into user(username,password,sex) values(?,?,?)";
		Object obj[]={user.getUsername(),user.getPassword(),user.getSex()};
		sdb.execSQL(sql, obj);	
		return true;
	}
}
