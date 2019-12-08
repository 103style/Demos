package socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.lxk.ipcdemo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author https://github.com/103style
 * @date 2019/11/7 23:49
 */
public class SocketTestActivity extends AppCompatActivity {

    private static final String TAG = "SocketTestActivity";
    private Socket mClientSocket;
    private PrintWriter mPrintWriter;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        editText = findViewById(R.id.input);

        startService(new Intent(this, TCPServerService.class));

        init();

        findViewById(R.id.send).setOnClickListener(v -> new Thread(() -> sendMsg()).start());
    }

    private void init() {
        new Thread(() -> {
            try {
                initSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void initSocket() throws IOException {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", TCPServerService.PORT);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())), true);
                Log.e(TAG, "connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                Log.e(TAG, "initSocket: connect fail, retry....");
            }
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (!SocketTestActivity.this.isFinishing()) {
            String msg = bufferedReader.readLine();
            if (TextUtils.isEmpty(msg)) {
                SystemClock.sleep(1000);
                continue;
            }
            Log.e(TAG, "receive msg from server = " + msg);
        }
        Log.e(TAG, "chat quit");
        mPrintWriter.close();
        bufferedReader.close();
    }

    private void sendMsg() {
        String temp = editText.getText().toString();
        if (TextUtils.isEmpty(temp) || mPrintWriter == null) {
            return;
        }
        mPrintWriter.println(temp);
        editText.setText("");
    }


    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mPrintWriter != null) {
            mPrintWriter.close();
        }
        super.onDestroy();
    }
}
