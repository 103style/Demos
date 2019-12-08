package socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author https://github.com/103style
 * @date 2019/11/7 23:38
 */
public class TCPServerService extends Service {

    private static final String TAG = "TCPServerService";
    public static int PORT = 10024;
    private boolean destroyed = false;

    @Override
    public void onCreate() {
        new Thread(new TCPServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        destroyed = true;
        super.onDestroy();
    }

    private void responseClient(Socket client) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        pw.println("welcome to chat room!");
        while (!destroyed) {
            String s = br.readLine();
            if (TextUtils.isEmpty(s)) {
                SystemClock.sleep(1000);
                continue;
            }
            Log.e(TAG, "client send msg = " + s);
            if (s.contains("吗?")) {
                s = s.replace("吗?", "!");
            } else {
                char end = s.charAt(s.length() - 1);
                if (end < '0' || (end > '9' && end < 'A')) {
                    s = s.substring(0, s.length() - 1) + "!";
                } else {
                    s += "!";
                }
            }

            pw.println(s);
        }
        pw.close();
        br.close();
        client.close();

    }

    private class TCPServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            while (serverSocket == null) {
                try {
                    serverSocket = new ServerSocket(PORT);
                } catch (IOException e) {
                    Log.e(TAG, "establish tcp server failed, port =" + PORT);
                    PORT++;
                    SystemClock.sleep(1000);
                    Log.e(TAG, "retry...");
                }
            }
            while (!destroyed) {
                try {
                    Socket client = serverSocket.accept();
                    Log.e(TAG, "erverSocket accepted");

                    new Thread(() -> {
                        try {
                            responseClient(client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    Log.e(TAG, "serverSocket.accept error");
                    SystemClock.sleep(1000);
                }
            }
        }
    }
}
