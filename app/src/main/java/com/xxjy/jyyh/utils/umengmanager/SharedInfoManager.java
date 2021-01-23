package com.xxjy.jyyh.utils.umengmanager;

import androidx.annotation.Nullable;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.entity.SharedInfoBean;
import com.xxjy.jyyh.dialog.SharedBoardDialog;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2018/8/9.
 * <p>
 * 分享内容的管理器
 */
public class SharedInfoManager {

    /**
     * 分享信息
     *
     * @param activity    dialog依赖activity
     * @param imgUrl      分享图片url
     * @param shareLink   分享的链接
     * @param title       分享的标题
     * @param description 分享的描述
     * @param listener    分享结果的监听
     */
    public static void shareInfo(final BaseActivity activity, final String imgUrl, final String shareLink,
                                 final String title, final String description,
                                 @Nullable final OnSharedResultListener listener) {
//        if (!AppContext.isIsCanShared()) {
//            MessageBuilderDialog dialog = new MessageBuilderDialog.Builder(activity)
//                    .setMessageText("移动端暂不支持分享，请您前往【微信】-关注【1号卡官微】公众号后进行分享，给您带来的不便敬请谅解")
//                    .build();
//            dialog.show();
//            return;
//        }
        SharedBoardDialog dialog = new SharedBoardDialog(activity);
        dialog.setOnShareCheckListener(new SharedBoardDialog.OnShareCheckListener() {
            @Override
            public void onShareChecked(SHARE_MEDIA checkType) {
                sharedInfos(activity, checkType, imgUrl, shareLink, title, description, listener);
            }
        });
        dialog.show();
    }

    private static void sharedInfos(BaseActivity activity, SHARE_MEDIA checkType, String imgUrl,
                                    String shareLink, String title, String description,
                                    final OnSharedResultListener listener) {
        UMImage image = new UMImage(activity, imgUrl);//网络图片
        UMWeb web = new UMWeb(shareLink);
        web.setTitle(title);//标题
        web.setDescription(description);
        web.setThumb(image);
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorVisibility(false);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        new ShareAction(activity).withMedia(web)
                .setPlatform(checkType)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        if (listener != null && listener instanceof OnSharedAllResultListener) {
                            OnSharedAllResultListener resultListener = (OnSharedAllResultListener) listener;
                            resultListener.onStart(platform);
                        }
                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        if (listener != null) {
                            listener.onSharedSuccess(platform);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, final Throwable t) {
                        if (listener != null) {
                            listener.onSharedError(platform, t);
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        if (listener != null && listener instanceof OnSharedAllResultListener) {
                            OnSharedAllResultListener resultListener = (OnSharedAllResultListener) listener;
                            resultListener.onCancel(platform);
                        }
                    }
                }).share();
    }

    /**
     * 根据分享bean快速开启一个分享
     *
     * @param cla
     * @param activity
     */
    public static void startQuickSharedInfo(BaseActivity activity, SharedInfoBean cla) {
        SharedInfoManager.shareInfo(activity, cla.getIcon(), cla.getLink(), cla.getTitle(),
                cla.getDesc(), SharedInfoManager.getDefaultListener(activity));
    }

    /**
     * 获取一个默认的监听,会通过baseactivity来进行分享结果的toast显示;
     *
     * @param activity
     * @return
     */
    public static OnSharedResultListener getDefaultListener(final BaseActivity activity) {
        return new OnSharedResultListener() {
            @Override
            public void onSharedSuccess(SHARE_MEDIA platform) {
                activity.showToastSuccess("分享成功");
            }

            @Override
            public void onSharedError(SHARE_MEDIA platform, Throwable t) {
                activity.showToastWarning("分享失败: " + t.getMessage());
            }
        };
    }

    public interface OnSharedResultListener {
        void onSharedSuccess(SHARE_MEDIA platform);

        void onSharedError(SHARE_MEDIA platform, final Throwable t);
    }

    public interface OnSharedAllResultListener extends OnSharedResultListener {
        void onStart(SHARE_MEDIA platform);

        void onCancel(SHARE_MEDIA platform);
    }
}
