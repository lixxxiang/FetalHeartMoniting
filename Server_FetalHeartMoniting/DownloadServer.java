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
    // ����ļ��Ƿ���ʵ���ڣ��˶��������룬���ļ������ڻ���������򷵻�-1�����򷵻��ļ�����  
    // �˴�ֻҪ���벻Ϊ�վ���Ϊ����ȷ��  
    private long getFileLength(String fileName, String password) {  
        // ���ļ���������Ϊnull���򷵻�-1  
        if ((fileName == null) || (password == null))  
            return -1;  
        // ���ļ��������볤��Ϊ0���򷵻�-1  
        if ((fileName.length() == 0) || (password.length() == 0))  
            return -1;  
        String filePath = downloadRoot + fileName; // ���������ļ�·��  
        System.out.println("DownloadServer getFileLength----->" + filePath);  
        File file = new File(filePath);  
        // ���ļ������ڣ��򷵻�-1  
        if (!file.exists())  
            return -1;  
        return file.length(); // �����ļ�����  
    }  
    // ��ָ�����������ָ���ļ�  
    private void sendFile(DataOutputStream out, String fileName)  
            throws Exception {  
        String filePath = downloadRoot + fileName; // ���������ļ�·��  
        // ��������ļ��������ļ�������  
        FileInputStream in = new FileInputStream(filePath);  
        System.out.println("DownloadServer sendFile----->" + filePath);  
        byte[] buf = new byte[Buffer];  
        int len;  
        // ������ȡ���ļ��е����ݣ�ֱ�������ĳ���Ϊ-1  
        while ((len = in.read(buf)) >= 0) {  
            out.write(buf, 0, len); // �����������ݣ��������ĳ���д�������  
            out.flush();  
        }  
        out.close();  
        in.close();  
    }  
    // �ṩ���ط���  
    public void download() throws IOException {  
        System.out.println("��������... ");  
        System.out.println("DownloadServer currentThread--->"  
                + currentThread().getName());  
        System.out.println("DownloadServer currentThread--->"  
                + currentThread().getId());  
        // ��ȡsocket������������װ��BufferedReader  
        BufferedReader in = new BufferedReader(new InputStreamReader(  
                socket.getInputStream()));  
        // ��ȡsocket�����������װ��DataOutputStream  
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
        try {  
            String parameterString = in.readLine(); // ���������������  
            // ������������ַ���Ϊ�Զ���ĸ�ʽ���������ļ���������ظ�Ŀ¼��·����  
            // ����������ɣ����߼����ַ� "@ "�ָ����˴��� "@ "�ָ�������������ַ���  
            String[] parameter = parameterString.split("@ ");  
            String fileName = parameter[0]; // ��ȡ����ļ�·��  
            String password = parameter[1]; // ��ȡ��������  
            // ��ӡ�������������Ϣ  
            System.out.print(socket.getInetAddress().getHostAddress()  
                    + "����������� ");  
            System.out.println("���������ļ�: " + fileName);  
            // ����ļ��Ƿ���ʵ���ڣ��˶��������룬��ȡ�ļ�����  
            long len = getFileLength(fileName, password);  
            System.out.println("download fileName----->" + fileName);  
            System.out.println("download password----->" + password);  
            out.writeLong(len); // ��ͻ��˷����ļ���С  
            out.flush();  
            // ����ȡ���ļ����ȴ��ڵ���0,���������أ�����ܾ�����  
            if (len >= 0) {  
                System.out.println("�������� ");  
                System.out.println("���������ļ� " + fileName + "... ");  
                sendFile(out, fileName); // ��ͻ��˷����ļ�  
                System.out.println(fileName +": "+"������� ");  
            } else {  
                System.out.println("�����ļ������ڻ�������󣬾ܾ����أ� ");  
            }  
        } catch (Exception e) {  
            System.out.println(e.toString());  
        } finally {  
            socket.close(); // �ر�socket  
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
    private static String root = "/Users/lxiang/Desktop/abc/ "; // ���ظ�Ŀ¼  
    public static void main(String[] args) throws IOException {  
        String temp = null;  
        // �����˿�  
        try {  
            // ��װ��׼����ΪBufferedReader  
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(  
                    System.in));  
            while (true) {  
                System.out.print("���������ط����������ظ�Ŀ¼�� ");  
                root = systemIn.readLine().trim(); // �ӱ�׼�����ȡ���ظ�Ŀ¼  
                File file = new File(root);  
                // ����Ŀ¼ȷʵ������ΪĿ¼��������ѭ��  
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