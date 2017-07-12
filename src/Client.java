import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by dongz on 2017/7/12.
 */
public class Client {

    public static void main(String[] args) {
        Client client=new Client();
        client.onRun();
    }

    private void onRun(){
        Socket socket=null;
        BufferedReader br=null;
        PrintWriter pw=null;
        try {
            socket=new Socket("127.0.0.1",6666);
            pw= new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
                String userInput=userInputBR.readLine();
                pw.println(userInput);
                pw.flush();
                if(userInput.equals("done")) System.exit(0);
                String s=br.readLine();
                System.out.println("服务器返回了："+s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                br.close();
                pw.close();
                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
