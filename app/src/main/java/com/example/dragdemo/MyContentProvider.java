package com.example.dragdemo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by 邢燕 on 2019/7/30 20:36.
 * Email:lucky_xyic@sina.cn
 * ToDo:
 */
public class MyContentProvider extends ContentProvider {

    private DBHelper mDBHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private static final int User_Code = 1;
    private static final int Job_Code = 2;

    //设置 contentProvider 的唯一表示
    private static final String AUTHORITY = "com.example.dragdemo";
    private static UriMatcher mUriMatcher;

    //通过 UriMatcher 往 contentProvider 中注册所需的 URI
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "user", User_Code);
        mUriMatcher.addURI(AUTHORITY, "job", Job_Code);
    }

    @Override
    public boolean onCreate() {//运行在ContentProvider进程中的主线程，不能做耗时操作
        //初始化 DBHelper 获取 数据库对象 SQLiteDatabase
        mDBHelper = new DBHelper(getContext());
        mSQLiteDatabase = mDBHelper.getWritableDatabase();

        //初始化数据库中表的数据
        mSQLiteDatabase.execSQL("delete from user");
        mSQLiteDatabase.execSQL("insert into user values(1,'xing yan');");
        mSQLiteDatabase.execSQL("insert into user values(2,'cao tian jin');");
        mSQLiteDatabase.execSQL("delete from job");
        mSQLiteDatabase.execSQL("insert into job values(1,'ji shu 1');");
        mSQLiteDatabase.execSQL("insert into job values(2,'ji shu 2');");

        return true;
    }

    /**
     * 增、删、改、查 运行在ContentProvider进程的Binder线程池中（不是主线程）
     * 所以 存在多线程并发访问，需要实现线程同步。
     * 若ContentProvider的数据存储方式是使用SQLite & 一个，则不需要自己实现同步，因为SQLite内部实现好了线程同步；
     * 若是多个SQLite则需要，因为SQL对象之间无法进行线程同步；
     * 若ContentProvider的数据存储方式是内存，则也需要自己实现线程同步.
     */

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        return mSQLiteDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (mUriMatcher.match(uri)) {
            case User_Code:
                tableName = DBHelper.USER_TABLE_NAME;
                break;
            case Job_Code:
                tableName = DBHelper.JOB_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }

    // 得到数据类型，即返回当前 Url 所代表数据的MIME类型
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);

        mSQLiteDatabase.insert(tableName, null, values);

        //===当该URI的ContentProvider数据发生变化时，通知外界（即访问该ContentProvider数据的访问者）
        ContentResolver mContentResolver = getContext().getContentResolver();
        mContentResolver.notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
