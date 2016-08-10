package com.jph.servlet;
 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

@WebServlet("/ServletForGETMethod")
public class ServletForGETMethod extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static String string="";
    public static String name;
    public static String info;
    public static String docname;
    public static String path="";

    public ServletForGETMethod() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub    
        name=request.getParameter("name");       
        docname=request.getParameter("docname");
        System.out.println(name+docname);
//        info=request.getParameter("info");
        info=name+docname;
        System.out.println(info);
        File txt2 = new File("C://info.txt");
		if (!txt2.exists()) {
			txt2.createNewFile();
		}
		byte bytes2[] = new byte[512];
		String txtinfo2 = info;
		bytes2 = txtinfo2.getBytes();
		FileOutputStream fos2 = new FileOutputStream(txt2);
		fos2.write(bytes2);
		fos2.close();
		path = "C://test/"+docname+"/"+name+"/";//找到用户名所在的文件夹
		System.out.println("path="+path);
		File txt = new File("C://temp.txt");
		if (!txt.exists()) {
			txt.createNewFile();
		}
		byte bytes[] = new byte[512];
		String txtinfo = path;
		bytes = txtinfo.getBytes();
		FileOutputStream fos = new FileOutputStream(txt);
		fos.write(bytes);
		fos.close();
	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    	String docme=request.getParameter("docname");
    	System.out.println(docme+"aaa");
    }
}