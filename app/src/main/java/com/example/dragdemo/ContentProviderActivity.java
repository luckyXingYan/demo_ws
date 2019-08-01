package com.example.dragdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * contentProvider 内容提供者，四大组件之一， 底层原理是 android 中的 binder 机制
 */
public class ContentProviderActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ContentProviderActivity";
    private Button btnVisitMailList;
    private Button btnCpDb;

    /**
     * ContentObserver 内容观察者
     * 观察 Uri引起 ContentProvider 中的数据变化(增、删 & 改) & 通知外界（即访问该数据访问者）
     * 可触发 ContentObserver 的 onChange 方法，在 onChange 方法中可做出针对数据库变化的反应，比如更新UI等。
     * & 通过ContentObserver可通知外界
     * 注册 ContentObserver --> 通知外界 notifyChange --> 反注册 ContentObserver
     */
    private ContentObserver contentObserver = new ContentObserver(new Handler()) {

        @Override
        public boolean deliverSelfNotifications() {
            Log.e(TAG, "deliverSelfNotifications");
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.e(TAG, "onChange：selfChange = " + selfChange);
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.e(TAG, "onChange：selfChange = " + selfChange + "，uri = " + uri);
            super.onChange(selfChange, uri);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        btnVisitMailList = (Button) findViewById(R.id.btn_visit_mail_list);
        btnCpDb = (Button) findViewById(R.id.btn_cp_db);
        btnVisitMailList.setOnClickListener(this);
        btnCpDb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_visit_mail_list://contentProvider 进程间通讯 访问手机通讯录
                /**
                 * 不需要再 manifest.xml 中注册 provider
                 *
                 *  contentProvider 获取手机通讯录
                 * 首先配置读写权限  <READ_CONTACTS/> 和 <WRITE_CONTACTS/>
                 * 获取this.getContentResolver() == ContentProvider类并不会直接与外部进程交互，而是通过 ContentResolver 类
                 * ContentResolver统一管理不同 ContentProvider间的操作
                 */
                //申请读通讯录权限
                requestPermission(this, 10000, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
                break;
            case R.id.btn_cp_db://进程内通信
                /**
                 * 需要 manifest.xml 中配置 <provider/> 不然 Cursor 会空指针
                 * 1、创建数据库 集成 SQLiteOpenHelper
                 * 2、自定义 contentProvider
                 * 3、manifest 中注册 contentProvider
                 * 4、进程内访问 ContentProvider中的数据
                 */
                //user 表
                Uri uri = Uri.parse("content://com.example.dragdemo/user");
                ContentResolver mContentResolver = getContentResolver();

                //===注册内容观察者ContentObserver
                mContentResolver.registerContentObserver(uri, true, contentObserver);

                //插入
                ContentValues mContentValues = new ContentValues();
                mContentValues.put("_id", "3");
                mContentValues.put("name", "he ye");
                mContentResolver.insert(uri, mContentValues);
                //查询
                Cursor mCursor = mContentResolver.query(uri, new String[]{"_id", "name"}, null, null, null);
                while (mCursor.moveToNext()) {
                    Log.e(TAG, "query user:" + mCursor.getInt(0) + " " + mCursor.getString(1) + "\n");
                }
                mCursor.close();
                //===解除观察者
                mContentResolver.unregisterContentObserver(contentObserver);

                //job 表
                Uri uriJob = Uri.parse("content://com.example.dragdemo/job");
                ContentResolver mContentResolverJob = getContentResolver();
                //插入
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "3");
                contentValues.put("job", "ji shu 3");
                mContentResolverJob.insert(uriJob, contentValues);
                //查询
                Cursor mCursorJob = mContentResolverJob.query(uriJob, new String[]{"_id", "job"}, null, null, null);
                while (mCursorJob.moveToNext()) {
                    Log.e(TAG, "query job:" + mCursorJob.getInt(0) + " " + mCursorJob.getString(1) + "\n");
                }
                mCursorJob.close();

                break;
            default:
                break;
        }
    }

    public void requestPermission(Activity context, int requestCode, String... permissions) {
        PermissionGen.with(context)
                .addRequestCode(requestCode)
                .permissions(permissions)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 10000)
    public void onRequestPermissionsSucess() {
        Log.e(TAG, "onRequestPermissionsSucess");
        ContentResolver contentResolver = getContentResolver();

        //获取联系人 id 和 name === //content://com.android.contacts/contacts 操作的数据是联系人信息Uri
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        Log.e(TAG, cursor.moveToNext() + "");
        while (cursor.moveToNext()) {
            StringBuilder stringBuilder = new StringBuilder();

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            stringBuilder.append("contacts_id = ").append(id).append("，contacts_name = ").append(name);

            //获取联系人 phone === //content://com.android.contacts/data/phones 联系人电话Uri
            Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                stringBuilder.append(" ，contacts_phone = ").append(phone);
            }
            phoneCursor.close();//关闭游标

            //获取联系人 email === //content://com.android.contacts/data/emails 联系人Email Uri
            Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);

            while (emailCursor.moveToNext()) {
                String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                stringBuilder.append("，contacts_email = ").append(email);
            }
            emailCursor.close();//关闭游标
            Log.e(TAG, stringBuilder.toString());
        }
        cursor.close();//关闭游标
    }

    @PermissionFail(requestCode = 10000)
    public void onRequestPermissionsFail() {
        Log.e(TAG, "onRequestPermissionsFail");
    }
}
