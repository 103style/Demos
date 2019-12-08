package messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author https://github.com/103style
 * @date 2019/10/22 22:21
 */
public class MessengerService extends Service {

    public static final int MSG_FROM_CLIENT = 0x001;
    public static final int MSG_FROM_SERVICE = 0x002;
    private static final String TAG = "MessengerService";
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    Log.e(TAG, "get data form client, data =  " + msg.getData().get("msg"));
                    Messenger client = msg.replyTo;
                    Message replay = Message.obtain(null, MessengerService.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "Hi, Nice to meet you!");
                    replay.setData(bundle);
                    try {
                        client.send(replay);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
