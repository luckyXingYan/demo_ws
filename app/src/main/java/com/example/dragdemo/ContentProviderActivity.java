package com.example.dragdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * contentProvider 获取手机通讯录
 * 首先配置读写权限  <READ_CONTACTS/> 和 <WRITE_CONTACTS/>
 * 获取this.getContentResolver() == ContentProvider类并不会直接与外部进程交互，而是通过 ContentResolver 类
 * ContentResolver统一管理不同 ContentProvider间的操作
 */
public class ContentProviderActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ContentProviderActivity";
    private Button btnVisitMailList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);
        initView();
    }

    private void initView() {
        btnVisitMailList = (Button) findViewById(R.id.btn_visit_mail_list);
        btnVisitMailList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_visit_mail_list://contentProvider 进程间通讯 访问手机通讯录
                //申请读通讯录权限
                requestPermission(this, 10000, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
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
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {

            StringBuilder stringBuilder = new StringBuilder();

            //获取联系人 id 和 name
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            stringBuilder.append("contacts_id = ").append(id).append("，contacts_name = ").append(name);
            //获取联系人 phone
            Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);
            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                stringBuilder.append("contacts_phone = ").append(phone);
            }
            //获取联系人 email
            Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                    null, null);
            while (emailCursor.moveToNext()) {
                String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                stringBuilder.append("contacts_email = ").append(email);
            }
            Log.e(TAG, stringBuilder.toString());
        }
    }

    @PermissionFail(requestCode = 10000)
    public void onRequestPermissionsFail() {
        Log.e(TAG, "onRequestPermissionsFail");
    }

}
