package contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lxk.ipcdemo.R;

/**
 * @author https://github.com/103style
 * @date 2019/12/8 18:02
 */
public class ProviderActivity extends AppCompatActivity {

    private static final String TAG = ProviderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Uri uri = Uri.parse(TestProvider.TODO_URI);
        ContentValues values = new ContentValues();
//        values.put("_id", 3);
        values.put("title", "read Android艺术开发探索");
        values.put("priority", 5);
        getContentResolver().insert(uri, values);

        query(uri);
        getContentResolver().delete(uri, "priority=10", null);

        query(uri);
    }


    private void query(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "title", "priority"}, null, null, null);
        if (cursor == null) {
            return;
        }
        while (cursor.moveToNext()) {
            Todo todo = new Todo();
            todo._id = cursor.getInt(0);
            todo.title = cursor.getString(1);
            todo.priority = cursor.getInt(2);
            Log.e(TAG, "query todo: " + todo.toString());
        }
        cursor.close();
    }
}
