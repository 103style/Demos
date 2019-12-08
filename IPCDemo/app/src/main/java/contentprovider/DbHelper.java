package contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author https://github.com/103style
 * @date 2019/11/6 21:44
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "todo";
    private static final String DB_NAME = "todo.db";
    private static final int VERSION = 1;

    private String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(_id INTEGER PRIMARY KEY, title TEXT, priority INT)";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
