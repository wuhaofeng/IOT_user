package com.iot.user.http.base;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.iot.user.http.model.BaseListResponse;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.request.alert.ModifyOnlineDevJsonRequest;
import com.iot.user.http.request.alert.UnitDevCloseVoiceRequest;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.http.request.dev.UnitDevAddBindRequest;
import com.iot.user.http.request.dev.UnitDevDetailChartRequest;
import com.iot.user.http.request.dev.UnitDevDetailNodeRequest;
import com.iot.user.http.request.dev.UnitDevEditRequest;
import com.iot.user.http.request.dev.UnitDevNodeOpenRequest;
import com.iot.user.http.request.dev.UnitDevRemovePlaceRequest;
import com.iot.user.http.request.dev.UnitDevUpdateBindRequest;
import com.iot.user.http.request.dev.member.UnitMemberAddRequest;
import com.iot.user.http.request.dev.member.UnitMemberRemoveRequest;
import com.iot.user.http.request.login.CheckUpdateJsonRequst;
import com.iot.user.http.request.login.LoginRequest;
import com.iot.user.http.request.login.LoginSignCodeRequest;
import com.iot.user.http.request.login.PluginPushJsonRequest;
import com.iot.user.http.request.login.ResetPwdJsonRequest;
import com.iot.user.http.request.login.UnitFamilyListRequest;
import com.iot.user.http.request.login.VcodeCheckJsonRequest;
import com.iot.user.http.request.login.VcodeSendJsonRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.http.request.main.UnitDevListRequest;
import com.iot.user.http.request.main.UnitDevShareRequest;
import com.iot.user.http.request.main.UnitFamilyRoomRequest;
import com.iot.user.http.request.main.UnitHomeDevStatusRequest;
import com.iot.user.http.request.main.UnitHomeFamilyDevRequest;
import com.iot.user.http.request.main.UnitRoomDevRequest;
import com.iot.user.http.request.message.DevHistoryJsonRequst;
import com.iot.user.http.request.message.UnitMessageFamilyAgreeRequest;
import com.iot.user.http.request.message.UnitMessageFamilyRequest;
import com.iot.user.http.request.message.UnitMessageHistoryRequest;
import com.iot.user.http.request.message.UnitMsgDeleteDevRequest;
import com.iot.user.http.request.message.UnitMsgDeleteFamilyRequest;
import com.iot.user.http.request.mine.ChangePasswordsonRequest;
import com.iot.user.http.request.mine.EditUserInfoJsonRequest;
import com.iot.user.http.request.mine.FeedbackJsonRequest;
import com.iot.user.http.request.mine.ModifyMarkJsonRequest;
import com.iot.user.http.request.mine.RepairJsonRequest;
import com.iot.user.http.request.mine.UnitMineRequest;
import com.iot.user.http.request.notification.SetNoticeReadJsonRequst;
import com.iot.user.http.request.notification.UnitNotificationListRequest;
import com.iot.user.http.request.register.RegistJsonRequest;
import com.iot.user.http.request.room.UnitCheckRoomNameRequest;
import com.iot.user.http.request.room.UnitRoomDeleteRequest;
import com.iot.user.http.request.share.UnitFamilyShareRequest;
import com.iot.user.http.request.share.UnitMemberInfoRequest;
import com.iot.user.http.request.shopping.OrderInfoJsonRequst;
import com.iot.user.http.request.shopping.ShoppingDeviceListJsonRequest;
import com.iot.user.http.request.shopping.ShoppingDeviceSubmitRequest;
import com.iot.user.http.request.shopping.ShoppingOrderApiPayRequest;
import com.iot.user.http.request.shopping.ShoppingOrderDetailRequest;
import com.iot.user.http.request.shopping.ShoppingOrderDiscountRequest;
import com.iot.user.http.request.shopping.ShoppingOrderListRequest;
import com.iot.user.http.request.shopping.ShoppingOrderSwitchRequest;
import com.iot.user.ui.adapter.shopping.AliPayJsonResp;
import com.iot.user.ui.adapter.shopping.PayJsonResp;
import com.iot.user.ui.model.location.GaoDeGetRegionAreaJsonResp;
import com.iot.user.ui.model.shopping.OrderInfo;
import com.iot.user.ui.model.shopping.ShoppingDeviceSubmitInfo;
import com.iot.user.ui.model.shopping.ShoppingOrderDetailResp;
import com.iot.user.ui.model.shopping.ShoppingOrderDiscountListModel;
import com.iot.user.ui.model.shopping.ShoppingOrderListResp;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public class RespositoryData implements HttpDataSource {
    private volatile static RespositoryData INSTANCE = null;
    private volatile static RespositoryData payINSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private RespositoryData(@NonNull HttpDataSource httpDataSource) {
        this.mHttpDataSource = httpDataSource;
    }

    public static RespositoryData getInstance(HttpDataSource httpDataSource) {
        if (INSTANCE == null) {
            synchronized (RespositoryData.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RespositoryData(httpDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public static RespositoryData getPayInstance(HttpDataSource httpDataSource) {
        if (payINSTANCE == null) {
            synchronized (RespositoryData.class) {
                if (payINSTANCE == null) {
                    payINSTANCE = new RespositoryData(httpDataSource);
                }
            }
        }
        return payINSTANCE;
    }


    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
        payINSTANCE = null;
    }

    @Override
    public Observable<BaseResponse> login(LoginRequest loginJsonRequest){
        return mHttpDataSource.login(loginJsonRequest);
    }
    @Override
    public Observable<BaseResponse> signInCode(LoginSignCodeRequest signCodeRequest) {
        return mHttpDataSource.signInCode(signCodeRequest);
    }
    @Override
    public Observable<BaseResponse> uInfo(UnitFamilyListRequest commonJsonRequst) {
        return mHttpDataSource.uInfo(commonJsonRequst);
    }

    @Override
    public Observable<BaseResponse> logout(UnitFamilyListRequest logoutJsonRequest) {
        return mHttpDataSource.logout(logoutJsonRequest);
    }
    @Override
    public Observable<BaseResponse> changePassword(ChangePasswordsonRequest changePasswordsonRequest) {
        return mHttpDataSource.changePassword(changePasswordsonRequest);
    }

    @Override
    public Observable<BaseResponse> edituInfo(EditUserInfoJsonRequest editUserInfoJsonRequest) {
        return mHttpDataSource.edituInfo(editUserInfoJsonRequest);
    }
    @Override
    public Observable<BaseResponse> regist(RegistJsonRequest registJsonRequest) {
        return mHttpDataSource.regist(registJsonRequest);
    }

    @Override
    public Observable<BaseResponse> forgetPwd(ResetPwdJsonRequest resetPwdJsonRequest) {
        return mHttpDataSource.forgetPwd(resetPwdJsonRequest);
    }

    @Override
    public Observable<BaseResponse> vCodeSend(VcodeSendJsonRequest vcodeSendJsonRequest) {
        return mHttpDataSource.vCodeSend(vcodeSendJsonRequest);
    }

    @Override
    public Observable<BaseResponse> vCodeCheck(VcodeCheckJsonRequest vcodeCheckJsonRequest) {
        return mHttpDataSource.vCodeCheck(vcodeCheckJsonRequest);
    }
    @Override
    public Observable<BaseResponse> app_update(CheckUpdateJsonRequst checkUpdateJsonRequst) {
        return mHttpDataSource.app_update(checkUpdateJsonRequst);
    }
    @Override
    public Observable<BaseResponse> postUnitLoginWithCode(VcodeCheckJsonRequest checkJsonRequest) {
        return mHttpDataSource.postUnitLoginWithCode(checkJsonRequest);
    }
    @Override
    public Observable<BaseResponse> pluginPush(PluginPushJsonRequest pluginPushJsonRequest) {
        return mHttpDataSource.pluginPush(pluginPushJsonRequest);
    }
    @Override
    public Observable<BaseResponse> postUnitFamilyNewCount(UnitFamilyListRequest familyListRequest) {
        return mHttpDataSource.postUnitFamilyNewCount(familyListRequest);
    }
    @Override
    public Observable<BaseResponse> postUnitFamilyList(UnitFamilyListRequest unitFamilyListRequest) {
        return mHttpDataSource.postUnitFamilyList(unitFamilyListRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitAlertDevList(UnitDevShareRequest shareRequest) {
        return mHttpDataSource.postUnitAlertDevList(shareRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitFamilyDevList(UnitHomeFamilyDevRequest unitHomeFamilyDevRequest) {
        return mHttpDataSource.postUnitFamilyDevList(unitHomeFamilyDevRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitRoomDevList(UnitRoomDevRequest roomDevRequest) {
        return mHttpDataSource.postUnitRoomDevList(roomDevRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevList(UnitDevListRequest devListRequest) {
        return mHttpDataSource.postUnitDevList(devListRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevInfo(UnitDevDetailRequest unitDevDetailRequest) {
        return mHttpDataSource.postUnitDevInfo(unitDevDetailRequest);
    }
    @Override
    public Observable<BaseResponse> postFamilyDevStatusNum(UnitHomeDevStatusRequest devStatusRequest) {
        return mHttpDataSource.postFamilyDevStatusNum(devStatusRequest);
    }
    @Override
    public Observable<BaseResponse> postUnitFamilyRoomList(UnitFamilyRoomRequest roomRequest) {
        return mHttpDataSource.postUnitFamilyRoomList(roomRequest);
    }
    @Override
    public Observable<BaseResponse> postUnitMessageHistorylist(UnitMessageHistoryRequest messageHistoryRequest) {
        return mHttpDataSource.postUnitMessageHistorylist(messageHistoryRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitMessageHistoryDetail(DevHistoryJsonRequst messageHistoryRequest) {
        return mHttpDataSource.postUnitMessageHistoryDetail(messageHistoryRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitMessageFamilyShareList(UnitMessageFamilyRequest messageFamilyRequest) {
        return mHttpDataSource.postUnitMessageFamilyShareList(messageFamilyRequest);
    }

    @Override
    public Observable<BaseResponse> postAgreeUnitMessageFamilyShare(UnitMessageFamilyAgreeRequest familyAgreeRequest) {
        return mHttpDataSource.postAgreeUnitMessageFamilyShare(familyAgreeRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitMessageDevShareList(UnitMessageFamilyRequest messageFamilyRequest) {
        return mHttpDataSource.postUnitMessageDevShareList(messageFamilyRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDeleteFamilyMessage(UnitMsgDeleteFamilyRequest familyRequest) {
        return mHttpDataSource.postUnitDeleteFamilyMessage(familyRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDeleteDevMessage(UnitMsgDeleteDevRequest devRequest) {
        return mHttpDataSource.postUnitDeleteDevMessage(devRequest);
    }
    @Override
    public Observable<BaseResponse> queryNoReadNotice(UnitNotificationListRequest historyRequest) {
        return mHttpDataSource.queryNoReadNotice(historyRequest);
    }

    @Override
    public Observable<BaseResponse> queryReadNotice(UnitNotificationListRequest historyRequest) {
        return mHttpDataSource.queryReadNotice(historyRequest);
    }

    @Override
    public Observable<BaseResponse> queryAllNotice(UnitNotificationListRequest historyRequest) {
        return mHttpDataSource.queryAllNotice(historyRequest);
    }

    @Override
    public Observable<BaseResponse> setReadNotice(SetNoticeReadJsonRequst setNoticeReadJsonRequst) {
        return mHttpDataSource.setReadNotice(setNoticeReadJsonRequst);
    }
    @Override
    public Observable<BaseResponse> postUnitMineUserInfo(UnitMineRequest unitMineRequest) {
        return mHttpDataSource.postUnitMineUserInfo(unitMineRequest);
    }
    @Override
    public Observable<BaseResponse> getVersion() {
        return mHttpDataSource.getVersion();
    }

    @Override
    public Observable<Response<BaseResponse>> getEnterpriseInfo() {
        return mHttpDataSource.getEnterpriseInfo();
    }

    @Override
    public Observable<BaseResponse> devRepair(RepairJsonRequest repairJsonRequest) {
        return mHttpDataSource.devRepair(repairJsonRequest);
    }

    @Override
    public Observable<BaseResponse> modifyMark(ModifyMarkJsonRequest modifyMarkJsonRequest) {
        return mHttpDataSource.modifyMark(modifyMarkJsonRequest);
    }

    @Override
    public Observable<BaseResponse> feedback(FeedbackJsonRequest feedbackJsonRequest) {
        return mHttpDataSource.feedback(feedbackJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevCloseVoice(UnitDevCloseVoiceRequest closeVoiceRequest) {
        return mHttpDataSource.postUnitDevCloseVoice(closeVoiceRequest);
    }

    @Override
    public Observable<BaseResponse> modifyOnlineDevDoStat(ModifyOnlineDevJsonRequest modifyOnlineDevJsonRequest) {
        return mHttpDataSource.modifyOnlineDevDoStat(modifyOnlineDevJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevPermission(CommonDevLowJsonRequest devLowJsonRequest) {
        return mHttpDataSource.postUnitDevPermission(devLowJsonRequest);
    }

    @Override
    public Observable<BaseResponse> IsDevReg_V902(CommonDevLowJsonRequest commonDevJsonRequest) {
        return mHttpDataSource.IsDevReg_V902(commonDevJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postAddAndBindUnitDev(UnitDevAddBindRequest devAddBindRequest) {
        return mHttpDataSource.postAddAndBindUnitDev(devAddBindRequest);
    }

    @Override
    public Observable<BaseResponse> postUpdateAndBindUnitDev(UnitDevUpdateBindRequest updateBindRequest) {
        return mHttpDataSource.postUpdateAndBindUnitDev(updateBindRequest);
    }

    @Override
    public Observable<BaseResponse> getDevBindInfo(CommonDevLowJsonRequest devLowJsonRequest) {
        return mHttpDataSource.getDevBindInfo(devLowJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevBinder(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return mHttpDataSource.postUnitDevBinder(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevUnAttention(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return mHttpDataSource.postUnitDevUnAttention(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevUnBinder(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return mHttpDataSource.postUnitDevUnBinder(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevEdit(UnitDevEditRequest devEditRequest) {
        return mHttpDataSource.postUnitDevEdit(devEditRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevRemovePlace(UnitDevRemovePlaceRequest removePlaceRequest) {
        return mHttpDataSource.postUnitDevRemovePlace(removePlaceRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitCloseClicket(CommonDevLowJsonRequest devLowJsonRequest) {
        return mHttpDataSource.postUnitCloseClicket(devLowJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevHAddr(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return mHttpDataSource.postUnitDevHAddr(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevNodeAddr(UnitDevDetailNodeRequest devDetailNodeRequest) {
        return mHttpDataSource.postUnitDevNodeAddr(devDetailNodeRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitNodeOpenCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return mHttpDataSource.postUnitNodeOpenCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitNodeCloseCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return mHttpDataSource.postUnitNodeCloseCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitHArrOpenCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return mHttpDataSource.postUnitHArrOpenCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitHArrCloseCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return mHttpDataSource.postUnitHArrCloseCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitNodeProductCloseCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return mHttpDataSource.postUnitNodeProductCloseCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevChart(UnitDevDetailChartRequest chartRequest) {
        return mHttpDataSource.postUnitDevChart(chartRequest);
    }

    @Override
    public Observable<BaseResponse> postDevGasAlarm(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return mHttpDataSource.postDevGasAlarm(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postDevChartAlarmGasvalue(CommonDevLowJsonRequest devRequest) {
        return mHttpDataSource.postDevChartAlarmGasvalue(devRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevMemberList(UnitDevDetailRequest devUperJsonRequest) {
        return mHttpDataSource.postUnitDevMemberList(devUperJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitRemoveMemberList(UnitMemberRemoveRequest memberRemoveRequest) {
        return mHttpDataSource.postUnitRemoveMemberList(memberRemoveRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitAddMemberList(UnitMemberAddRequest memberAddRequest) {
        return mHttpDataSource.postUnitAddMemberList(memberAddRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitMemberInfo(UnitMemberInfoRequest memberInfoRequest) {
        return mHttpDataSource.postUnitMemberInfo(memberInfoRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitFamilyShare(UnitFamilyShareRequest familyShareRequest) {
        return mHttpDataSource.postUnitFamilyShare(familyShareRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevShareList(UnitDevShareRequest devShareRequest) {
        return mHttpDataSource.postUnitDevShareList(devShareRequest);
    }

    @Override
    public Observable<GaoDeGetRegionAreaJsonResp> queryRegionArea(String keywords) {
        return mHttpDataSource.queryRegionArea(keywords);
    }

    @Override
    public Observable<BaseResponse> postUnitDeleteRoom(UnitRoomDeleteRequest roomDeleteRequest) {
        return mHttpDataSource.postUnitDeleteRoom(roomDeleteRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitPublicRoomList(UnitFamilyListRequest roomAddRequest) {
        return mHttpDataSource.postUnitPublicRoomList(roomAddRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitCheckRoom(UnitCheckRoomNameRequest checkRoomNameRequest) {
        return mHttpDataSource.postUnitCheckRoom(checkRoomNameRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitCreateRoom(UnitCheckRoomNameRequest checkRoomNameRequest) {
        return mHttpDataSource.postUnitCreateRoom(checkRoomNameRequest);
    }

    @Override
    public Observable<BaseResponse> postShoppingDeviceList(ShoppingDeviceListJsonRequest shoppingDeviceListJsonRequest) {
        return mHttpDataSource.postShoppingDeviceList(shoppingDeviceListJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postOrderSwitch(ShoppingOrderSwitchRequest commonJsonRequst) {
        return mHttpDataSource.postOrderSwitch(commonJsonRequst);
    }

    @Override
    public Observable<BaseResponse<ShoppingOrderDiscountListModel>> postOrderDiscountList(ShoppingOrderDiscountRequest shoppingOrderDiscountRequest) {
        return mHttpDataSource.postOrderDiscountList(shoppingOrderDiscountRequest);
    }

    @Override
    public Observable<BaseListResponse<List<OrderInfo>>> postOrderlist(OrderInfoJsonRequst orderInfoJsonRequst) {
        return mHttpDataSource.postOrderlist(orderInfoJsonRequst);
    }

    @Override
    public Observable<BaseResponse<ShoppingDeviceSubmitInfo>> postOrderAccountDetail(ShoppingDeviceSubmitRequest shoppingDeviceSubmitRequest) {
        return mHttpDataSource.postOrderAccountDetail(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<BaseResponse> postCreateOrder(ShoppingDeviceSubmitRequest shoppingDeviceSubmitRequest) {
        return mHttpDataSource.postCreateOrder(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<AliPayJsonResp> postApiPay(ShoppingOrderApiPayRequest shoppingDeviceSubmitRequest) {
        return mHttpDataSource.postApiPay(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<PayJsonResp> postWeixinPay(ShoppingOrderApiPayRequest shoppingDeviceSubmitRequest) {
        return mHttpDataSource.postWeixinPay(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<BaseResponse<ShoppingOrderListResp>> postNewOrderList(ShoppingOrderListRequest shoppingOrderListRequest) {
        return mHttpDataSource.postNewOrderList(shoppingOrderListRequest);
    }

    @Override
    public Observable<BaseResponse<ShoppingOrderDetailResp>> postNewOrderDetail(ShoppingOrderDetailRequest shoppingOrderDetailRequest) {
        return mHttpDataSource.postNewOrderDetail(shoppingOrderDetailRequest);
    }

    @Override
    public Observable<BaseResponse> postCancelOrderWithTradeNo(ShoppingOrderDetailRequest shoppingOrderDetailRequest) {
        return mHttpDataSource.postCancelOrderWithTradeNo(shoppingOrderDetailRequest);
    }
}
