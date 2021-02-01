package com.xxjy.jyyh.jscalljava;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2018/5/13.
 * 添加供给 js 的调用方法接口 ,为了方便统计,在这里抽取 方法
 * <p>
 * 已经实现的方法有
 * 1, {@link #getAppInfo()}                 获取app信息
 * 2, {@link #toBenXiangHome()}              跳转到主界面
 * 3, {@link #startShare(String)}           调起分享
 * 4, {@link #showSharedIcon(String)}       显示分享图标并传递分享内容
 * 5, {@link #showHelpIcon(String)}         显示客服图标并传递手机号
 * 6, {@link #toRechargeOneCard()}          跳转app内部充值1号卡界面
 * 7, {@link #dialPhone(String)}            跳转拨打电话界面
 * 8, {@link #toLogin()}                    跳转登录界面
 * 9, {@link #toWechatPay(String)}          跳转微信支付界面
 * 9, {@link #toAppletPay(String)}          跳转微信小程序支付界面
 * 10, {@link #toAccountDetails(String checkPosition)}   账户明细
 * 11, {@link #nativeActivity(String)}      跳转app内部本地界面
 * 12, {@link #toAliPay(String)}            调起支付宝支付
 */
public interface JsOperationMethods {

    /**
     * 获取app 的信息
     */
    String getAppInfo();


    /**
     * 调起分享
     *
     * @param shardInfo 分享的json信息
     */
    void startShare(String shardInfo);

    /**
     * 展示分享图标并传入分享内容
     *
     * @param shardInfo
     */
    void showSharedIcon(String shardInfo);

    /**
     * 隐藏分享图标并传入分享内容
     */
    void hideSharedIcon();

    /**
     * 显示客服图标并传递手机号
     *
     * @param phoneNumber
     */
    void showHelpIcon(String phoneNumber);

    /**
     * 跳转跳转app内部充值1号卡
     */
    void toRechargeOneCard();

    /**
     * 调起拨打手机界面
     *
     * @param phoneNumber
     */
    void dialPhone(String phoneNumber);

    /**
     * 跳转登录界面
     */
    void toLogin();

    /**
     * 打开微信支付处理
     *
     * @param url 后台返回的js文本
     */
    void toWechatPay(String url);

    /**
     * 打开微信小程序支付处理
     *
     * @param params 小程序的参数
     */
    void toAppletPay(String params);

    /**
     * 账户明细
     *
     * @param checkPosition 默认选中的item
     */
    void toAccountDetails(String checkPosition);

    /**
     * 跳转本地的界面
     *
     * @param intentInfo
     */
    void nativeActivity(String intentInfo);

    /**
     * 调起支付宝支付
     *
     * @param intentInfo
     */
    void toAliPay(String intentInfo);

    /**
     * 将toolbar状态栏的背景修改成bgcolor
     *
     * @param bgColor
     */
    void changeToolBarState(String bgColor);

    /**
     * 改变toolbar状态栏至默认状态
     */
    void changeToolBarDefault();

    /**
     * 跳转小程序购买权益卡
     */
    void goWeChatBuyEquityCard(String parameter);

    /**
     * 保存图片
     * @param data
     */
    void saveImage(String data);

    /**
     * 是否显示搜索
     * @param isShow
     */
    void showSearch(boolean isShow);

    /**
     * 标题栏显示
     * @param isShow
     */
    void showToolbar(boolean isShow);

    /**
     * 业绩中心
     * @param isShow
     */
    void showPerformanceCenterView(boolean isShow);

    /**
     * 返回
     */
    void goBack();

    /**
     * 获取状态栏
     * @return
     */
    int getStatusBarHeight();

    /**
     * 是否显示沉浸式头部
     * @param isShow
     */
    void showImmersiveToolBar(boolean isShow);

    /**
     * 提现明细
     * @param isShow
     */
    void showWithdrawalDetailsView(boolean isShow);

    /**
     * 修改状态栏颜色
     * @param isDefault
     * @param bgColor
     */
    void changeToolBarState(boolean isDefault, String bgColor);

    /**
     * 跳转到微信
     */
    void goWeChat();

    /**
     * 关闭页面
     */
    void finishActivity();
    /**
     * 分享图片到微信
     */
    void shareImageToWeChat(String data);

    /**
     * 加油省钱页
     */
    void toRefuellingPage();

    /**
     * 首页
     */
    void toHomePage();

}
