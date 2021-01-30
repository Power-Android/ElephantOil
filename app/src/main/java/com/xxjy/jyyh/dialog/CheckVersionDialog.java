package com.xxjy.jyyh.dialog;

import android.Manifest;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BaseDialog;
import com.xxjy.jyyh.entity.VersionEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.utils.toastlib.Toasty;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rxhttp.RxHttp;

public class CheckVersionDialog extends BaseDialog {
    private static final int FORCE_UPDATE_TRUE = 1;

    private BaseActivity activity;

    public VersionEntity version;
    private int forceUpdate;

    private String sdPath = "";
    private boolean isDownLoadSuccess = false;

    private TextView confirm;
    private TextView cancle;
    private ProgressBar lineProgress;
    @Override
    protected int getContentLayoutId() {
        return R.layout.dialog_check_version;
    }
    public CheckVersionDialog(BaseActivity context, VersionEntity version) {
        super(context, Gravity.CENTER, false, false);
        this.activity = context;
        this.forceUpdate = version.getForceUpdate();
        this.version = version;
        initView();
        initListener();
        initData();
    }

    private void initView() {
        confirm = findViewById(R.id.confirm);
        cancle = findViewById(R.id.cancle);
        lineProgress = findViewById(R.id.line_progress);
    }
    private void initData() {
        /* 设置弹出窗口特征 */
        // 设置视图     是否强制更新，0：否，1：是
        if (forceUpdate == FORCE_UPDATE_TRUE) {
            cancle.setVisibility(View.GONE);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        } else {
            cancle.setVisibility(View.VISIBLE);
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }
    }
    private void initListener() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDownLoadSuccess) {
                    install();
                } else {
                    confirm.setClickable(false);
                    confirm.setText("正在下载...");
                    downloadAPK();
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void downloadAPK() {
        if (TextUtils.isEmpty(version.getUrl())) {
            activity.showToastInfo("请去应用商店下载");
            confirm.setText("立即更新");
            confirm.setClickable(true);
            dismiss();
            return;
        }

        PermissionUtils.permission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        downApk();
                    }

                    @Override
                    public void onDenied() {
                        Toasty.error(activity, "无存储权限，无法更新").show();
                        dismiss();
                        if (forceUpdate == FORCE_UPDATE_TRUE) {
                            if (activity instanceof MainActivity) {
                                AppUtils.exitApp();
                            }
                        }

                    }
                })
                .request();


    }

    private void downApk() {

        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 获得存储卡的路径
//            sdPath = Environment.getExternalStorageDirectory() + File.separator + "runElephant";
            sdPath = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "LittleElephant";
            File file = new File(sdPath);
            // 判断文件目录是否存在
            if (!file.exists()) {
                file.mkdir();
            }
            File apkFile = new File(sdPath, "LittleElephant.apk");
            if (apkFile.exists()) {
                apkFile.delete();
            }
        }
        lineProgress.setVisibility(View.VISIBLE);
        String destPath = sdPath + "/LittleElephant.apk";
        RxHttp.get(version.getUrl())
                .asDownload(destPath, AndroidSchedulers.mainThread(), progress -> {
                    //下载进度回调,0-100，仅在进度有更新时才会回调，最多回调101次，最后一次回调文件存储路径
                    int currentProgress = progress.getProgress(); //当前进度 0-100
//                    long currentSize = progress.getCurrentSize(); //当前已下载的字节大小
//                    long totalSize = progress.getTotalSize();     //要下载的总字节大小
                    lineProgress.setProgress(currentProgress);
                }) //指定主线程回调
                .to(RxLife.to(activity)) //感知生命周期
                .subscribe(s -> {//s为String类型，这里为文件存储路径
                    //下载完成，处理相关逻辑
                    install();
                    isDownLoadSuccess = true;
                    confirm.setClickable(true);
                    confirm.setText("立即安装");
                }, throwable -> {
                    //下载失败，处理相关逻辑
                    confirm.setClickable(true);
                    confirm.setText("立即更新");
                    activity.showToastWarning("下载失败,请稍后重试");
                });

    }


    private void install() {
        File file = new File(sdPath, "LittleElephant.apk");
        AppUtils.installApp(file);
    }
}
