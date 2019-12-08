package contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author https://github.com/103style
 * @date 2019/11/6 21:11
 */
public class TestProvider extends ContentProvider {
    public static final String AUTH = "com.lxk.ipcdemo.provider";
    public static final String TODO_URI = "content://" + AUTH + "/todo";
    public static final int TODO_CODE = 1;

    private static final String TAG = TestProvider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTH, "todo", TODO_CODE);
    }

    private SQLiteDatabase sqLiteDatabase;
    private Context mContext;

    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate, thread = " + Thread.currentThread().getName());
        mContext = getContext();
        DbHelper dbHelper = new DbHelper(mContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        initData();
        return false;
    }

    private void initData() {
        sqLiteDatabase.execSQL("insert into todo values(1,'buy computer',10);");
//        sqLiteDatabase.execSQL("insert into todo values(2,'install androidstudio',5);");
    }

    public String getTableName(Uri uri) {
        if (URI_MATCHER.match(uri) == TODO_CODE) {
            return DbHelper.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("illegal uri = " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e(TAG, "getType");
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG, "query, thread = " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        return sqLiteDatabase.query(tableName, projection, selection, selectionArgs,
                null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e(TAG, "insert");
        String tableName = getTableName(uri);
        sqLiteDatabase.insert(tableName, null, values);
        notify(uri);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "delete");
        String tableName = getTableName(uri);
        int count = sqLiteDatabase.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            notify(uri);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "update");
        String tableName = getTableName(uri);
        int row = sqLiteDatabase.update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            notify(uri);
        }
        return row;
    }

    private void notify(Uri uri) {
        mContext.getContentResolver().notifyChange(uri, null);
    }
}
