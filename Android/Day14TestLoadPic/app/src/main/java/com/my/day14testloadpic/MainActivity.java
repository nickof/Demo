package com.my.day14testloadpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1001;
    private String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
    private AlertDialog dialog;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

            // 检查该权限是否已经获取
            int getsave = ContextCompat.checkSelfPermission(this, permissions[0]);
            int getcamer = ContextCompat.checkSelfPermission(this, permissions[1]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝

        for (String permission:
                permissions){
            if ( ContextCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 321);
                break;
            }
        }

//            if (getsave != PackageManager.PERMISSION_GRANTED) {
//                // 如果没有授予该权限，就去提示用户请求
//                System.out.println("permisson need");
//                ActivityCompat.requestPermissions(this, permissions, 321);
//            } else if (getcamer != PackageManager.PERMISSION_GRANTED) {
//                // 开始提交请求权限
//                ActivityCompat.requestPermissions(this, permissions, 321);
//            }
    }


    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("onRequestPermissionsResult");
        System.out.println("requestCode="+requestCode  );

        if (requestCode == 321) {
            System.out.println("321"  );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean getsave = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!getsave) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                       showDialogTipUserGoToAppSettting("存储权限", "存储权限不可用");
                    }
                } else if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    boolean getcammer = shouldShowRequestPermissionRationale(permissions[1]);
                    if (!getcammer) {
                       showDialogTipUserGoToAppSettting("相机权限", "相机权限不可用");
                    }
                }
            }
        }



    }


    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting(String title, String message) {

        dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 123);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int getsave = ContextCompat.checkSelfPermission(this, permissions[0]);
                int getcammer = ContextCompat.checkSelfPermission(this, permissions[1]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (getsave != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting("存储权限", "存储权限不可用");
                } else if (getcammer != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting("相机权限", "相机权限不可用");
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }
    }


    public void  load_pic(View v){

        imageView = (ImageView)findViewById(R.id.iv_pic);
        EditText edit_path =(EditText)findViewById(R.id.edit_path);
        String file=edit_path.getText().toString();

        System.out.println(file);
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(file);
        } catch (Error e) {
            System.out.println("错误"+e.getMessage () );
            e.printStackTrace();
        }

        imageView.setImageBitmap(bitmap);

    }


}
