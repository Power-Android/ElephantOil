package com.xxjy.jyyh.dialog;

import android.animation.Animator;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NumberUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.databinding.DialogOilAmountLayoutBinding;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilDiscountEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayOrderParams;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/22/21 1:25 PM
 * @project ElephantOil
 * @description:
 */
public class OilAmountDialog extends BottomSheetDialog {
    private Context mContext;
    private BaseActivity mActivity;
    private OilEntity.StationsBean mStationsBean;
    private List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBeans;
    private int oilNoPosition, gunNoPosition;
    private BottomSheetBehavior mBehavior;
    private DialogOilAmountLayoutBinding mBinding;
    private List<OilDefaultPriceEntity.DefaultAmountBean> mAmountList = new ArrayList<>();
    private List<OilDiscountEntity> mDiscountList = new ArrayList<>();
    private MultiplePriceBean mMultiplePriceBean;
    private OilAmountAdapter mOilAmountAdapter;
    private OilDiscountAdapter mDiscountAdapter;
    private List<CouponBean> platformCoupons = new ArrayList<>();
    private List<CouponBean> businessCoupons = new ArrayList<>();
    private CouponBean mPlatCouponBean, mBusinessCouponBean;
    private String platId = "", businessAmount = "";//平台优惠券id， 商机优惠金额 每次刷新价格是要清空
    private OilHotDialog mOilHotDialog;

    public OilAmountDialog(Context context, BaseActivity activity, OilEntity.StationsBean stationsBean,
                           List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBeans,
                           int oilNoPosition, int gunNoPosition) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.mActivity = activity;
        this.mStationsBean = stationsBean;
        this.oilPriceListBeans = oilPriceListBeans;
        this.oilNoPosition = oilNoPosition;
        this.gunNoPosition = gunNoPosition;

        mBinding = DialogOilAmountLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_amount_layout, null));
        init();
        initData();
        initListener();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void init() {
        getWindow().getAttributes().windowAnimations =
                R.style.bottom_sheet_dialog;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        //快捷价格列表
        mBinding.amountRecyclerView.setLayoutManager(
                new GridLayoutManager(mContext, 3));
        mOilAmountAdapter = new OilAmountAdapter(R.layout.adapter_oil_amount, mAmountList);
        mBinding.amountRecyclerView.setAdapter(mOilAmountAdapter);
        mOilAmountAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<OilDefaultPriceEntity.DefaultAmountBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            mBinding.amountEt.setText(NumberUtils.format(
                    Float.parseFloat(data.get(position).getAmount()), 0));
            mOilAmountAdapter.notifyDataSetChanged();
            //刷新价格互斥
            refreshData();
        });

        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add(new OilDiscountEntity(0, "请选择加油金额","请选择加油金额",
                    "请选择加油金额", 0, false));
        }
        mBinding.discountRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext));
        mDiscountAdapter = new OilDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);

        mBinding.cancelIv.setOnClickListener(view -> dismiss());
        mBinding.backIv.setOnClickListener(view -> dismiss());
        mDiscountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (platformCoupons.size() > 0 && position == 1 ||
                    businessCoupons.size() > 0 && position == 2 &&
                            mOnItemClickedListener != null) {
                UiUtils.canClickViewStateDelayed(view, 1000);
                mOnItemClickedListener.onOilDiscountClick(adapter, view, position,
                        mBinding.amountEt.getText().toString(),
                        String.valueOf(oilPriceListBeans
                                .get(oilNoPosition).getOilNo()));
            }
        });

        mDiscountAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.item_switch_tv://是否使用余额
                    List<OilDiscountEntity> data = adapter.getData();
                    if (data.get(position).isUseBill()) {
                        data.get(position).setUseBill(false);
                    } else {
                        if (data.get(position).getBalance() > 0) {
                            data.get(position).setUseBill(true);
                        }
                    }
                    //刷新互斥价格
                    getMultiplePrice(platId, businessAmount);
                    break;
            }
        });

        getDefaultPrice();//获取快捷价格
        getPlatformCoupon();//平台优惠券
        getBusinessCoupon();//商家优惠券
        getBalance();//余额
        //再次进来时需要刷新数据
        getMultiplePrice(platId, businessAmount);
    }

    private void initListener() {
        mBinding.amountEt.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    //刷新快捷价格的选中状态
                    List<OilDefaultPriceEntity.DefaultAmountBean> data = mOilAmountAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (NumberUtils.format(Float.parseFloat(s.toString()), 0)
                                .equals(NumberUtils.format(Float.parseFloat(data.get(i).getAmount()), 0))) {
                            data.get(i).setSelected(true);
                        } else {
                            data.get(i).setSelected(false);
                        }
                    }
                    mOilAmountAdapter.notifyDataSetChanged();
                }
            }
        });

        KeyboardUtils.registerSoftInputChangedListener(mActivity, height -> {
            if (height == 0) {
                //刷新价格互斥
                refreshData();
            }
        });

        mBinding.cancelIv.setOnClickListener(view ->{
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.closeAll();
            }
        } );
        mBinding.backIv.setOnClickListener(view -> dismiss());
        mBinding.createOrderTv.setOnClickListener(view -> {
            createOrder(view);
        });
        mBinding.hotIv.setOnClickListener(view -> {
            if (mOilHotDialog == null){
                mOilHotDialog = new OilHotDialog(mContext, mActivity);
                mOilHotDialog.show(view);
            }else {
                mOilHotDialog.show(view);
            }

        });
    }

    private void createOrder(View view) {
        mBinding.loadingView.setVisibility(View.VISIBLE);
        RxHttp.postForm(ApiService.CREATE_ORDER)
                .add("amount", mBinding.amountEt.getText().toString())
                .add("payAmount", mMultiplePriceBean.getDuePrice())
                .add("usedBalance", mMultiplePriceBean.getBalancePrice())
                .add("gasId", mStationsBean.getGasId())
                .add("gunNo", String.valueOf(oilPriceListBeans.get(oilNoPosition).
                        getGunNos().get(gunNoPosition).getGunNo()))
                .add("oilNo", String.valueOf(oilPriceListBeans.get(oilNoPosition).getOilNo()))
                .add("oilName", oilPriceListBeans
                        .get(oilNoPosition).getOilName())
                .add("gasName", mStationsBean.getGasName())
                .add("priceGun", oilPriceListBeans.get(oilNoPosition).getPriceGun())
                .add("priceUnit", oilPriceListBeans.get(oilNoPosition).getPriceYfq())
                .add("oilType", oilPriceListBeans.get(oilNoPosition).getOilType() + "")
                .add("phone", SPUtils.getInstance().getString(SPConstants.MOBILE))
                .add("xxCouponId", platId)
                .add("czbCouponId", mBusinessCouponBean == null ? "" : mBusinessCouponBean.getId())
                .add("czbCouponAmount", businessAmount)
                .asResponse(String.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(orderId -> {
                            if (mOnItemClickedListener != null) {
                                UiUtils.canClickViewStateDelayed(view, 1000);
                                mOnItemClickedListener.onCreateOrder(view, orderId, mMultiplePriceBean.getDuePrice());
                            }
                        },
                        onError -> {
                            MyToast.showError(mContext, onError.getMessage());
                            mBinding.loadingView.setVisibility(View.GONE);
                        },
                        () -> mBinding.loadingView.setVisibility(View.GONE));
    }

    private void getPlatformCoupon() {
        //0真正可用 1已用 2过期  3时间未到 4 金额未达到
        if (TextUtils.isEmpty(mBinding.amountEt.getText())) return;
        RxHttp.postForm(ApiService.PLATFORM_COUPON)
                .add("canUse", "1")
                .add("rangeType", "2")
                .add("amount", mBinding.amountEt.getText())
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponseList(CouponBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(couponBeans -> {
                    if (couponBeans != null && couponBeans.size() > 0) {
                        platformCoupons = couponBeans;
                        mDiscountAdapter.getData().get(1).setPlatformDesc("请选择优惠券");
                    } else {
                        mDiscountAdapter.getData().get(1).setPlatformDesc("暂无可用优惠券");
                    }
                    mDiscountAdapter.notifyDataSetChanged();
                });
    }

    private void getBusinessCoupon() {
        if (TextUtils.isEmpty(mBinding.amountEt.getText())) return;
        RxHttp.postForm(ApiService.BUSINESS_COUPON)
                .add("canUse", "1")
                .add("amount", mBinding.amountEt.getText())
                .add(Constants.OIL_NUMBER_ID, oilNoPosition)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponseList(CouponBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(couponBeans -> {
                    if (couponBeans != null && couponBeans.size() > 0) {
                        businessCoupons = couponBeans;
                        mDiscountAdapter.getData().get(2).setBusinessDesc("请选择优惠券");
                    } else {
                        mDiscountAdapter.getData().get(2).setBusinessDesc("暂无可用优惠券");
                    }
                    mDiscountAdapter.notifyDataSetChanged();
                });
    }

    private void getDefaultPrice() {
        RxHttp.postForm(ApiService.OIL_PRICE_DEFAULT)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .add(Constants.OIL_NUMBER_ID, String.valueOf(oilPriceListBeans
                        .get(oilNoPosition).getOilNo()))
                .asResponse(OilDefaultPriceEntity.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(priceEntity -> {
                    List<OilDefaultPriceEntity.DefaultAmountBean> defaultAmount = priceEntity.getDefaultAmount();
                    mOilAmountAdapter.setNewData(defaultAmount);
                });
    }

    private void getBalance() {
        RxHttp.postForm(ApiService.QUERY_BALANCE)
                .asResponse(Float.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(balance -> {
                    if (balance > 0) {
                        mDiscountAdapter.getData().get(3).setBalance(balance);
                        mDiscountAdapter.getData().get(3).setUseBill(true);
                    } else {
                        mDiscountAdapter.getData().get(3).setBalance(0);
                        mDiscountAdapter.getData().get(3).setUseBill(false);
                    }
                    mDiscountAdapter.notifyDataSetChanged();
                });
    }

    /**
     * 获取互斥价格
     * 说明：商家优惠券和直降金额互斥
     */
    private void getMultiplePrice(String platId, String businessAmount) {
        mBinding.loadingView.setVisibility(View.VISIBLE);
        RxHttp.postForm(ApiService.OIL_MULTIPLE_PRICE)
                .add("amount", mBinding.amountEt.getText())
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .add(Constants.OIL_NUMBER_ID, String.valueOf(
                        oilPriceListBeans.get(oilNoPosition).getOilNo()))
                .add("canUseBill", mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0")
                .add("czbCouponAmount", TextUtils.isEmpty(businessAmount) ? "0" : businessAmount)
                .add("couponId", platId)
                .asResponse(MultiplePriceBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(multiplePriceBean -> {
                            this.mMultiplePriceBean = multiplePriceBean;
                            //直降金额
                            if (Float.parseFloat(multiplePriceBean.getDepreciateAmount()) > 0){
                                mDiscountAdapter.getData().get(0).setFallAmount(
                                        Float.parseFloat(multiplePriceBean.getDepreciateAmount()));
                            }else {
                                mDiscountAdapter.getData().get(0).setFallAmount(
                                        Float.parseFloat(multiplePriceBean.getDepreciateAmount()));
                                mDiscountAdapter.getData().get(0).setFallDesc("暂无优惠");
                            }
                            //升数
                            mBinding.literTv.setText(String.format("约%sL",
                                    multiplePriceBean.getLiter()));
                            //实付金额
                            mBinding.currentPriceTv.setText(String.format("实付：¥%s",
                                    multiplePriceBean.getDuePrice()));
                            //优惠金额
                            mBinding.discountPriceTv.setText(String.format("优惠合计：¥%s",
                                    multiplePriceBean.getSumDiscountPrice()));
                            //抵扣余额
                            mDiscountAdapter.getData().get(3).setBalanceDiscount(
                                    Float.parseFloat(multiplePriceBean.getBalancePrice()));

                            mDiscountAdapter.notifyDataSetChanged();

                            mBinding.createOrderTv.setEnabled(
                                    Float.parseFloat(multiplePriceBean.getDuePrice()) > 0 ||
                                    Float.parseFloat(multiplePriceBean.getSumDiscountPrice()) > 0);

                        }, throwable -> mBinding.loadingView.setVisibility(View.GONE),
                        () -> mBinding.loadingView.setVisibility(View.GONE));
    }

    public void setCouponInfo(CouponBean couponBean, boolean isPlat) {
        if (couponBean != null) {
            if (isPlat) {
                mPlatCouponBean = couponBean;
                platId = couponBean.getId();
                mDiscountList.get(1).setPlatformDesc("-¥" + couponBean.getAmountReduce());
            } else {
                mBusinessCouponBean = couponBean;
                businessAmount = couponBean.getAmountReduce();
                mDiscountList.get(2).setBusinessDesc("-¥" + couponBean.getAmountReduce());
            }
        }else {
            if (isPlat){
                platId = "";
                mDiscountList.get(1).setPlatformDesc("请选择优惠券");
            }else {
                businessAmount = "";
                mBusinessCouponBean = null;
                mDiscountList.get(2).setBusinessDesc("请选择优惠券");
            }
        }
        mDiscountAdapter.notifyDataSetChanged();
        getMultiplePrice(platId, businessAmount);
    }

    private void refreshData(){
        //每次价格改变时，要清空这俩
        platId = "";
        businessAmount = "";
        mBusinessCouponBean = null;

        getPlatformCoupon();
        getBusinessCoupon();
        //刷新价格互斥
        getMultiplePrice(platId, businessAmount);
    }

    public interface OnItemClickedListener {

        void onOilDiscountClick(BaseQuickAdapter adapter, View view, int position,
                                String amount, String oilNo);

        void onCreateOrder(View view, String orderId, String payAmount);

        void closeAll();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
