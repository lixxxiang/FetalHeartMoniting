package fileupload;

//file:DownloadServer.java   
import java.net.*;  
import java.io.*;  
class ServerOneDownload extends Thread {  
    private Socket socket = null;  
    private String downloadRoot = null;  
    private static final int Buffer = 8 * 1024;  
    public ServerOneDownload(Socket socket, String downloadRoot) {  
        super();  
        this.socket = socket;  
        this.downloadRoot = downloadRoot;  
        start();  
    }  
    // 检查文件是否真实存在，核对下载密码，若文件不存在或密码错误，则返回-1，否则返回文件长度  
    // 此处只要密码不为空就认为是正确的  
    private long getFileLength(String fileName, String password) {  
        // 若文件名或密码为null，则返回-1  
        if ((fileName == null) || (password == null))  
            return -1;  
        // 若文件名或密码长度为0，则返回-1  
        if ((fileName.length() == 0) || (password.length() == 0))  
            return -1;  
        String filePath = downloadRoot + fileName; // 生成完整文件路径  
        System.out.println("DownloadServer getFileLength----->" + filePath);  
        File file = new File(filePath);  
        // 若文件不存在，则返回-1  
        if (!file.exists())  
            return -1;  
        return file.length(); // 返回文件长度  
    }  
    // 用指定输出流发送指定文件  
    private void sendFile(DataOutputStream out, String fileName)  
            throws Exception {  
        String filePath = downloadRoot + fileName; // 生成完整文件路径  
        // 创建与该文件关联的文件输入流  
        FileInputStream in = new FileInputStream(filePath);  
        System.out.println("DownloadServer sendFile----->" + filePath);  
        byte[] buf = new byte[Buffer];  
        int len;  
        // 反复读取该文件中的内容，直到读到的长度为-1  
        while ((len = in.read(buf)) >= 0) {  
            out.write(buf, 0, len); // 将读到的数据，按读到的长度写入输出流  
            out.flush();  
        }  
        out.close();  
        in.close();  
    }  
    // 提供下载服务  
    public void download() throws IOException {  
        System.out.println("启动下载... ");  
        System.out.println("DownloadServer currentThread--->"  
                + currentThread().getName());  
        System.out.println("DownloadServer currentThread--->"  
                + currentThread().getId());  
        // 获取socket的输入流并包装成BufferedReader  
        BufferedReader in = new BufferedReader(new InputStreamReader(  
                socket.getInputStream()));  
        // 获取socket的输出流并包装成DataOutputStream  
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
        try {  
            String parameterString = in.readLine(); // 接收下载请求参数  
            // 下载请求参数字符串为自定义的格式，由下载文件相对于下载根目录的路径和  
            // 下载密码组成，两者间以字符 "@ "分隔，此处按 "@ "分割下载请求参数字符串  
            String[] parameter = parameterString.split("@ ");  
            String fileName = parameter[0]; // 获取相对文件路径  
            String password = parameter[1]; // 获取下载密码  
            // 打印请求下载相关信息  
            System.out.print(socket.getInetAddress().getHostAddress()  
                    + "提出下载请求， ");  
            System.out.println("请求下载文件: " + fileName);  
            // 检查文件是否真实存在，核对下载密码，获取文件长度  
            long len = getFileLength(fileName, password);  
            System.out.println("download fileName----->" + fileName);  
            System.out.println("download password----->" + password);  
            out.writeLong(len); // 向客户端返回文件大小  
            out.flush();  
            // 若获取的文件长度大于等于0,则允许下载，否则拒绝下载  
            if (len >= 0) {  
                System.out.println("允许下载 ");  
                System.out.println("正在下载文件 " + fileName + "... ");  
                sendFile(out, fileName); // 向客户端发送文件  
                System.out.println(fileName +": "+"下载完毕 ");  
            } else {  
                System.out.println("下载文件不存在或密码错误，拒绝下载！ ");  
            }  
        } catch (Exception e) {  
            System.out.println(e.toString());  
        } finally {  
            socket.close(); // 关闭socket  
        }  
    }  
    @Override  
    public void run() {  
        try {  
            download();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        // TODO Auto-generated method stub  
        super.run();  
    }  
}  
public class DownloadServer {  
    private final static int port = 65525;  
    private static String root = "/Users/lxiang/Desktop/abc/ "; // 下载根目录  
    public static void main(String[] args) throws IOException {  
        String temp = null;  
        // 监听端口  
        try {  
            // 包装标准输入为BufferedReader  
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(  
                    System.in));  
            while (true) {  
                System.out.print("请输入下载服务器的下载根目录： ");  
                root = systemIn.readLine().trim(); // 从标准输入读取下载根目录  
                File file = new File(root);  
                // 若该目录确实存在且为目录，则跳出循环  
                if ((file.exists()) && (file.isDirectory())) {  
                    temp = root.substring(root.length() - 1, root.length());  
                    if (!temp.equals("//"))  
                        root += "//";  
                }  
                break;  
            }  
        } catch (Exception e) {  
            System.out.println(e.toString());  
        }  
        ServerSocket serverSocket = new ServerSocket(port);  
        System.out.println("Server start...");  
        try {  
            while (true) {  
                Socket socket = serverSocket.accept();  
                new ServerOneDownload(socket, root);  
            }  
        } finally {  
            serverSocket.close();  
        }  
    }  
}  