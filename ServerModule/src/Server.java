import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dongz on 2017/7/12.
 */
public class Server {

    public static void main(String[] args) {
        System.out.println("Server startup.");
        //一定要在此声明serverSocket为null，因为需要在finally中关闭这些流。
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6666);//服务器端核心代码1
            while (true) {
                Socket socket = serverSocket.accept();//服务器端核心代码2
                new Thread(new mySocket(socket)).start();//服务器端核心代码3，新建一个线程处理当前访问者。
                                                            //更好的方法是建立socket列表，用列表中的socket处理访问者socket，用列表能控制最大连接数。
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception e) {
            }
        }
    }

}

//真正连接客户端socket的服务器socket实例，以新线程的形式从主线程脱离出来
class mySocket implements Runnable{
    private Socket socket;

    public mySocket(Socket socket) {
        this.socket=socket;
    }

    //每次收到客户端来的信息后，向客户端返回字符串。当收到"done"字符串时，停止程序。
    public void run(){
        //一定要在此声明buffereader和printwriter为null，因为需要在finally中关闭这些流。
        BufferedReader br=null;
        PrintWriter pw=null;
        try {
            //socket连接过程中，服务器向流写入写出数据需要以下两个对象
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                //readLine()是阻塞函数，当没有数据可读时就一直等待，并不返回null。因此在此循环中循环不会无限运行，而是等待readLine()收到数据才执行一次循环
                String receiveText=br.readLine();
                //最好用println向流中写入数据，println自带换行符，另一端readLine()时找不到换行符就会一直阻塞
                //如果用write()写入数据，需要在结尾增加'\n'或"\r\n"
                //pw.write("roger that\n");
                if(receiveText.equals("done")) System.exit(0);
                pw.println("server roger :"+receiveText);//将服务器返回的数据写入流中
                pw.flush();
            }
        } catch (Exception e) {
            System.out.println("server onRun problem.");
            e.printStackTrace();
        }
        finally{
            try {
                //一定要关闭流，否则会出现connection reset的情况。
                br.close();
                pw.close();
                socket.close();
            }catch(Exception e){}
        }
    }
}
