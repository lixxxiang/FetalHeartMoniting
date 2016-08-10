package dbtest;

import java.io.File;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MysqlJdbc {
	
	static String path = "C://test2/";
	static String string="";
	static Connection connect=null; 
	Statement stmt=null;
	
	public static String getLevel(int level)
	  {

	    StringBuilder sb=new StringBuilder();
	    return sb.toString();
	  }
	  public static void getAllFiles(File dir,int level)
	  {
	    level++;
	    File[] files=dir.listFiles();
	    for(int i=0;i<files.length;i++)
	    {
	      if(files[i].isDirectory())
	      {
	        getAllFiles(files[i],level);
	      }
	      else {
	        String path=getLevel(level)+files[i];
	        System.out.print(path+"   ");
	        String path2=path.replace("\\", "\\\\");
	        String array[]=path.split("\\\\");
	        String filename=array[4];
	        System.out.println(array[4]);
	        String array2[]=array[4].split("-");
	        String username=array2[0];
	        System.out.println("username:  "+username);
	        String array3[]=array2[2].split("\\.");
	        String docname=array3[0];
	        System.out.println(docname);
	         try {
	             Class.forName("com.mysql.jdbc.Driver"); 
	             connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?characterEncoding=utf8","root","root");     
	             Statement stmt = connect.createStatement();
	             String sqlstring=
	            		 "INSERT INTO fileindex(NAME,AUDIO_FILE,LOCATION,DOCNAME) SELECT '"+username+"','"+filename+"','"+path2+"','"+docname+"' FROM DUAL WHERE '"+filename+"' NOT IN (SELECT AUDIO_FILE from fileindex)";
	             stmt.execute(sqlstring);           
	             ResultSet rs = stmt.executeQuery("select * from fileindex");
	             while (rs.next()) {
	             }
	           }
	           catch (Exception e) {
	             e.printStackTrace();
	           }finally {
				if (connect!=null) {
					try {
						connect.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	      }	
	    }
	   
	  }
	  

	  public static void main(String args[]) {
		  TimerTask task=new TimerTask() {		
			  @Override
			  public void run() {
				  File file=new File("C://test3/");
				  getAllFiles(file, 0);
				  System.out.println();
			  }
		  };	
		  Calendar calendar = Calendar.getInstance();
		  Date date = calendar.getTime();
		  Timer timer = new Timer();
		  int period = 2 * 1000;
		  timer.schedule(task, date, period);
	  }
}
