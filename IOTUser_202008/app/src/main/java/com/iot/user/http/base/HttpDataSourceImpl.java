package com.iot.user.http.base;

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

//import io.reactivex.rxjava3.core.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;

public class HttpDataSourceImpl  implements HttpDataSource {
    private IotHttpService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;
    private volatile static HttpDataSourceImpl payINSTANCE = null;

    public static HttpDataSourceImpl getInstance(IotHttpService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static HttpDataSourceImpl getPayInstance(IotHttpService apiService) {
        if (payINSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (payINSTANCE == null) {
                    payINSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return payINSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
        payINSTANCE = null;
    }

    private HttpDataSourceImpl(IotHttpService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<BaseResponse> login(LoginRequest loginJsonRequest){
        return apiService.login(loginJsonRequest);
    }

    @Override
    public Observable<BaseResponse> uInfo(UnitFamilyListRequest commonJsonRequst) {
        return apiService.uInfo(commonJsonRequst);
    }

    @Override
    public Observable<BaseResponse> logout(UnitFamilyListRequest logoutJsonRequest) {
        return apiService.logout(logoutJsonRequest);
    }

    @Override
    public Observable<BaseResponse> changePassword(ChangePasswordsonRequest changePasswordsonRequest) {
        return apiService.changePassword(changePasswordsonRequest);
    }

    @Override
    public Observable<BaseResponse> edituInfo(EditUserInfoJsonRequest editUserInfoJsonRequest) {
        return apiService.edituInfo(editUserInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> regist(RegistJsonRequest registJsonRequest) {
        return apiService.regist(registJsonRequest);
    }

    @Override
    public Observable<BaseResponse> forgetPwd(ResetPwdJsonRequest resetPwdJsonRequest) {
        return apiService.forgetPwd(resetPwdJsonRequest);
    }

    @Override
    public Observable<BaseResponse> signInCode(LoginSignCodeRequest signCodeRequest) {
        return apiService.signInCode(signCodeRequest);
    }

    @Override
    public Observable<BaseResponse> vCodeSend(VcodeSendJsonRequest vcodeSendJsonRequest) {
        return apiService.vCodeSend(vcodeSendJsonRequest);
    }

    @Override
    public Observable<BaseResponse> vCodeCheck(VcodeCheckJsonRequest vcodeCheckJsonRequest) {
        return apiService.vCodeCheck(vcodeCheckJsonRequest);
    }

    @Override
    public Observable<BaseResponse> app_update(CheckUpdateJsonRequst checkUpdateJsonRequst) {
        return apiService.app_update(checkUpdateJsonRequst);
    }

    @Override
    public Observable<BaseResponse> postUnitLoginWithCode(VcodeCheckJsonRequest checkJsonRequest) {
        return apiService.postUnitLoginWithCode(checkJsonRequest);
    }

    @Override
    public Observable<BaseResponse> pluginPush(PluginPushJsonRequest pluginPushJsonRequest) {
        return apiService.pluginPush(pluginPushJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitFamilyNewCount(UnitFamilyListRequest familyListRequest) {
        return apiService.postUnitFamilyNewCount(familyListRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitFamilyList(UnitFamilyListRequest unitFamilyListRequest) {
        return apiService.postUnitFamilyList(unitFamilyListRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitAlertDevList(UnitDevShareRequest shareRequest) {
        return apiService.postUnitAlertDevList(shareRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitFamilyDevList(UnitHomeFamilyDevRequest unitHomeFamilyDevRequest) {
        return apiService.postUnitFamilyDevList(unitHomeFamilyDevRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitRoomDevList(UnitRoomDevRequest roomDevRequest) {
        return apiService.postUnitRoomDevList(roomDevRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevList(UnitDevListRequest devListRequest) {
        return apiService.postUnitDevList(devListRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevInfo(UnitDevDetailRequest unitDevDetailRequest) {
        return apiService.postUnitDevInfo(unitDevDetailRequest);
    }

    @Override
    public Observable<BaseResponse> postFamilyDevStatusNum(UnitHomeDevStatusRequest devStatusRequest) {
        return apiService.postFamilyDevStatusNum(devStatusRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitFamilyRoomList(UnitFamilyRoomRequest roomRequest) {
        return apiService.postUnitFamilyRoomList(roomRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitMessageHistorylist(UnitMessageHistoryRequest messageHistoryRequest) {
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("uId",messageHistoryRequest.getuId());
        hashMap.put("token",messageHistoryRequest.getToken());
        hashMap.put("pageNum",String.valueOf(messageHistoryRequest.getPageNum()));
        hashMap.put("pageSize",String.valueOf(messageHistoryRequest.getPageSize()));
        return apiService.postUnitMessageHistorylist(hashMap);
    }

    @Override
    public Observable<BaseResponse> postUnitMessageHistoryDetail(DevHistoryJsonRequst messageHistoryRequest) {
        Map<String,String> hashMap=new HashMap<>();
        DevHistoryJsonRequst.Body body=messageHistoryRequest.getBody();
        hashMap.put("uId",body.getuId());
        hashMap.put("token",body.getToken());
        hashMap.put("recordId",body.getPackRecodId());
        return apiService.postUnitMessageHistoryDetail(hashMap);
    }

    @Override
    public Observable<BaseResponse> postUnitMessageFamilyShareList(UnitMessageFamilyRequest messageFamilyRequest) {
        return apiService.postUnitMessageFamilyShareList(messageFamilyRequest);
    }

    @Override
    public Observable<BaseResponse> postAgreeUnitMessageFamilyShare(UnitMessageFamilyAgreeRequest familyAgreeRequest) {
        return apiService.postAgreeUnitMessageFamilyShare(familyAgreeRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitMessageDevShareList(UnitMessageFamilyRequest messageFamilyRequest) {
        return apiService.postUnitMessageDevShareList(messageFamilyRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDeleteFamilyMessage(UnitMsgDeleteFamilyRequest familyRequest) {
        return apiService.postUnitDeleteFamilyMessage(familyRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDeleteDevMessage(UnitMsgDeleteDevRequest devRequest) {
        return apiService.postUnitDeleteDevMessage(devRequest);
    }

    @Override
    public Observable<BaseResponse> queryNoReadNotice(UnitNotificationListRequest historyRequest) {
        return apiService.queryNoReadNotice(historyRequest);
    }

    @Override
    public Observable<BaseResponse> queryReadNotice(UnitNotificationListRequest historyRequest) {
        return apiService.queryReadNotice(historyRequest);
    }

    @Override
    public Observable<BaseResponse> queryAllNotice(UnitNotificationListRequest historyRequest) {
        return apiService.queryAllNotice(historyRequest);
    }

    @Override
    public Observable<BaseResponse> setReadNotice(SetNoticeReadJsonRequst setNoticeReadJsonRequst) {
        return apiService.setReadNotice(setNoticeReadJsonRequst);
    }

    @Override
    public Observable<BaseResponse> postUnitMineUserInfo(UnitMineRequest unitMineRequest) {
        return apiService.postUnitMineUserInfo(unitMineRequest);
    }

    @Override
    public Observable<BaseResponse> getVersion() {
        return apiService.getVersion();
    }

    @Override
    public Observable<Response<BaseResponse>> getEnterpriseInfo() {
        return apiService.getEnterpriseInfo();
    }

    @Override
    public Observable<BaseResponse> devRepair(RepairJsonRequest repairJsonRequest) {
        return apiService.devRepair(repairJsonRequest);
    }

    @Override
    public Observable<BaseResponse> modifyMark(ModifyMarkJsonRequest modifyMarkJsonRequest) {
        return apiService.modifyMark(modifyMarkJsonRequest);
    }

    @Override
    public Observable<BaseResponse> feedback(FeedbackJsonRequest feedbackJsonRequest) {
        return apiService.feedback(feedbackJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevCloseVoice(UnitDevCloseVoiceRequest closeVoiceRequest) {
        return apiService.postUnitDevCloseVoice(closeVoiceRequest);
    }

    @Override
    public Observable<BaseResponse> modifyOnlineDevDoStat(ModifyOnlineDevJsonRequest modifyOnlineDevJsonRequest) {
        return apiService.modifyOnlineDevDoStat(modifyOnlineDevJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevPermission(CommonDevLowJsonRequest devLowJsonRequest) {
        return apiService.postUnitDevPermission(devLowJsonRequest);
    }

    @Override
    public Observable<BaseResponse> IsDevReg_V902(CommonDevLowJsonRequest commonDevJsonRequest) {
        return apiService.IsDevReg_V902(commonDevJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postAddAndBindUnitDev(UnitDevAddBindRequest devAddBindRequest) {
        return apiService.postAddAndBindUnitDev(devAddBindRequest);
    }

    @Override
    public Observable<BaseResponse> postUpdateAndBindUnitDev(UnitDevUpdateBindRequest updateBindRequest) {
        return apiService.postUpdateAndBindUnitDev(updateBindRequest);
    }

    @Override
    public Observable<BaseResponse> getDevBindInfo(CommonDevLowJsonRequest devLowJsonRequest) {
        return apiService.getDevBindInfo(devLowJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevBinder(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return apiService.postUnitDevBinder(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevUnAttention(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return apiService.postUnitDevUnAttention(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevUnBinder(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return apiService.postUnitDevUnBinder(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevEdit(UnitDevEditRequest devEditRequest) {
        return apiService.postUnitDevEdit(devEditRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevRemovePlace(UnitDevRemovePlaceRequest removePlaceRequest) {
        return apiService.postUnitDevRemovePlace(removePlaceRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitCloseClicket(CommonDevLowJsonRequest devLowJsonRequest) {
        return apiService.postUnitCloseClicket(devLowJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevHAddr(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return apiService.postUnitDevHAddr(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevNodeAddr(UnitDevDetailNodeRequest devDetailNodeRequest) {
        return apiService.postUnitDevNodeAddr(devDetailNodeRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitNodeOpenCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return apiService.postUnitNodeOpenCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitNodeCloseCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return apiService.postUnitNodeCloseCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitHArrOpenCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return apiService.postUnitHArrOpenCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitHArrCloseCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return apiService.postUnitHArrCloseCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitNodeProductCloseCode(UnitDevNodeOpenRequest nodeOpenRequest) {
        return apiService.postUnitNodeProductCloseCode(nodeOpenRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevChart(UnitDevDetailChartRequest chartRequest) {
        return apiService.postUnitDevChart(chartRequest);
    }

    @Override
    public Observable<BaseResponse> postDevGasAlarm(UnitDevDetailRequest singleDevInfoJsonRequest) {
        return apiService.postDevGasAlarm(singleDevInfoJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postDevChartAlarmGasvalue(CommonDevLowJsonRequest devRequest) {
        return apiService.postDevChartAlarmGasvalue(devRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevMemberList(UnitDevDetailRequest devUperJsonRequest) {
        return apiService.postUnitDevMemberList(devUperJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitRemoveMemberList(UnitMemberRemoveRequest memberRemoveRequest) {
        return apiService.postUnitRemoveMemberList(memberRemoveRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitAddMemberList(UnitMemberAddRequest memberAddRequest) {
        return apiService.postUnitAddMemberList(memberAddRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitMemberInfo(UnitMemberInfoRequest memberInfoRequest) {
        return apiService.postUnitMemberInfo(memberInfoRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitFamilyShare(UnitFamilyShareRequest familyShareRequest) {
        return apiService.postUnitFamilyShare(familyShareRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitDevShareList(UnitDevShareRequest devShareRequest) {
        return apiService.postUnitDevShareList(devShareRequest);
    }

    @Override
    public Observable<GaoDeGetRegionAreaJsonResp> queryRegionArea(String keywords) {
        return apiService.queryRegionArea(keywords);
    }

    @Override
    public Observable<BaseResponse> postUnitDeleteRoom(UnitRoomDeleteRequest roomDeleteRequest) {
        return apiService.postUnitDeleteRoom(roomDeleteRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitPublicRoomList(UnitFamilyListRequest roomAddRequest) {
        return apiService.postUnitPublicRoomList(roomAddRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitCheckRoom(UnitCheckRoomNameRequest checkRoomNameRequest) {
        return apiService.postUnitCheckRoom(checkRoomNameRequest);
    }

    @Override
    public Observable<BaseResponse> postUnitCreateRoom(UnitCheckRoomNameRequest checkRoomNameRequest) {
        return apiService.postUnitCreateRoom(checkRoomNameRequest);
    }

    @Override
    public Observable<BaseResponse> postShoppingDeviceList(ShoppingDeviceListJsonRequest shoppingDeviceListJsonRequest) {
        return apiService.postShoppingDeviceList(shoppingDeviceListJsonRequest);
    }

    @Override
    public Observable<BaseResponse> postOrderSwitch(ShoppingOrderSwitchRequest commonJsonRequst) {
        return apiService.postOrderSwitch(commonJsonRequst);
    }

    @Override
    public Observable<BaseResponse<ShoppingOrderDiscountListModel>> postOrderDiscountList(ShoppingOrderDiscountRequest shoppingOrderDiscountRequest) {
        return apiService.postOrderDiscountList(shoppingOrderDiscountRequest);
    }

    @Override
    public Observable<BaseListResponse<List<OrderInfo>>> postOrderlist(OrderInfoJsonRequst orderInfoJsonRequst) {
        return apiService.postOrderlist(orderInfoJsonRequst);
    }

    @Override
    public Observable<BaseResponse<ShoppingDeviceSubmitInfo>> postOrderAccountDetail(ShoppingDeviceSubmitRequest shoppingDeviceSubmitRequest) {
        return apiService.postOrderAccountDetail(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<BaseResponse> postCreateOrder(ShoppingDeviceSubmitRequest shoppingDeviceSubmitRequest) {
        return apiService.postCreateOrder(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<AliPayJsonResp> postApiPay(ShoppingOrderApiPayRequest shoppingDeviceSubmitRequest) {
        return apiService.postApiPay(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<PayJsonResp> postWeixinPay(ShoppingOrderApiPayRequest shoppingDeviceSubmitRequest) {
        return apiService.postWeixinPay(shoppingDeviceSubmitRequest);
    }

    @Override
    public Observable<BaseResponse<ShoppingOrderListResp>> postNewOrderList(ShoppingOrderListRequest shoppingOrderListRequest) {
        return apiService.postNewOrderList(shoppingOrderListRequest);
    }

    @Override
    public Observable<BaseResponse<ShoppingOrderDetailResp>> postNewOrderDetail(ShoppingOrderDetailRequest shoppingOrderDetailRequest) {
        return apiService.postNewOrderDetail(shoppingOrderDetailRequest);
    }

    @Override
    public Observable<BaseResponse> postCancelOrderWithTradeNo(ShoppingOrderDetailRequest shoppingOrderDetailRequest) {
        return apiService.postCancelOrderWithTradeNo(shoppingOrderDetailRequest);
    }

}
