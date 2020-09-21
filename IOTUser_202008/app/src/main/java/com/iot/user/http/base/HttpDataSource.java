package com.iot.user.http.base;

import com.iot.user.constant.Constants;
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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpDataSource {
    /**用户登录，第一层直接返回数据给biz处理数据，存数据库等操作**/
    Observable<BaseResponse> signInCode(@Body LoginSignCodeRequest signCodeRequest);
    /**密码登录**/
    Observable<BaseResponse> login(@Body LoginRequest loginJsonRequest);
    /**获取用户信息***/
    Observable<BaseResponse> uInfo(@Body UnitFamilyListRequest commonJsonRequst);
    /**退出登录***/
    Observable<BaseResponse> logout(@Body UnitFamilyListRequest logoutJsonRequest);
    /**修改密码***/
    Observable<BaseResponse> changePassword(@Body ChangePasswordsonRequest changePasswordsonRequest);
    /**修改昵称***/
    Observable<BaseResponse> edituInfo(@Body EditUserInfoJsonRequest editUserInfoJsonRequest);

    /***注册**/
    Observable<BaseResponse> regist(@Body RegistJsonRequest registJsonRequest);
    /***忘记密码**/
    Observable<BaseResponse> forgetPwd(@Body ResetPwdJsonRequest resetPwdJsonRequest);
    /**
     * 发送短信验证码
     * @param vcodeSendJsonRequest
     * @return
     */
    Observable<BaseResponse> vCodeSend(@Body VcodeSendJsonRequest vcodeSendJsonRequest);
    /**
     * 验证短信验证码
     * @param vcodeCheckJsonRequest
     * @return
     */
    Observable<BaseResponse> vCodeCheck(@Body VcodeCheckJsonRequest vcodeCheckJsonRequest);
    /**
     *验证码登录
     * **/
    Observable<BaseResponse> postUnitLoginWithCode(@Body VcodeCheckJsonRequest checkJsonRequest);
    /***
     * app更新
     * **/
    Observable<BaseResponse> app_update(@Body CheckUpdateJsonRequst checkUpdateJsonRequst);
    /***
     * 推送的token处理
     * **/
    Observable<BaseResponse> pluginPush(@Body PluginPushJsonRequest pluginPushJsonRequest);
    /**
     * 家庭共享消息数量
     * */
    Observable<BaseResponse> postUnitFamilyNewCount(@Body UnitFamilyListRequest familyListRequest);
    /**
     * 获取家庭列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitFamilyList(@Body UnitFamilyListRequest unitFamilyListRequest);
    /**
     *获取全部报警设备列表
     * **/
    Observable<BaseResponse> postUnitAlertDevList(@Body UnitDevShareRequest shareRequest);
    /**
     * 获取家庭设备列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitFamilyDevList(@Body UnitHomeFamilyDevRequest unitHomeFamilyDevRequest);
    /**
     * 获取房间设备列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitRoomDevList(@Body UnitRoomDevRequest roomDevRequest);
    /**
     *获取设备列表
     * **/
    Observable<BaseResponse> postUnitDevList(@Body UnitDevListRequest devListRequest);
    /**
     * 获取设备详情
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitDevInfo(@Body UnitDevDetailRequest unitDevDetailRequest);
    /**
     * 家庭设备信息统计
     * @param
     * @return
     */
    Observable<BaseResponse> postFamilyDevStatusNum(@Body UnitHomeDevStatusRequest devStatusRequest);
    /**
     * 家庭房间列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitFamilyRoomList(@Body UnitFamilyRoomRequest roomRequest);

    /**
     * 历史消息
     * @param messageHistoryRequest
     * @return
     */
    Observable<BaseResponse> postUnitMessageHistorylist(UnitMessageHistoryRequest messageHistoryRequest);

    /**
     * 历史消息详情
     * @param messageHistoryRequest
     * @return
     */
    Observable<BaseResponse> postUnitMessageHistoryDetail(DevHistoryJsonRequst messageHistoryRequest);
    /**
     * 家庭共享 -消息列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitMessageFamilyShareList(@Body UnitMessageFamilyRequest messageFamilyRequest);
    /**
     * 家庭共享 -同意邀请
     * @param
     * @return
     */
    Observable<BaseResponse> postAgreeUnitMessageFamilyShare(@Body UnitMessageFamilyAgreeRequest familyAgreeRequest);

    /**
     * 设备共享 -消息列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitMessageDevShareList(@Body UnitMessageFamilyRequest messageFamilyRequest);

    /**
     * 删除家庭共享
     * */
    Observable<BaseResponse> postUnitDeleteFamilyMessage(@Body UnitMsgDeleteFamilyRequest familyRequest);

    /**
     * 删除设备共享
     * */
    Observable<BaseResponse> postUnitDeleteDevMessage(@Body UnitMsgDeleteDevRequest devRequest);

    /**
     * 查询用户未读公告数据
     * @param historyRequest
     * @return
     */
    Observable<BaseResponse> queryNoReadNotice(@Body UnitNotificationListRequest historyRequest);

    /**
     * 查询用户已读公告内容
     * @param historyRequest
     * @return
     */
    Observable<BaseResponse> queryReadNotice(@Body UnitNotificationListRequest historyRequest);
    /**
     *查询用户所有公告
     * @param
     * @return
     */
    Observable<BaseResponse> queryAllNotice(@Body UnitNotificationListRequest historyRequest);

    /**
     * 设置已读接口
     * @param setNoticeReadJsonRequst
     * @return
     */
    Observable<BaseResponse> setReadNotice(@Body SetNoticeReadJsonRequst setNoticeReadJsonRequst);
    /**
     * 获取用户信息
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitMineUserInfo(@Body UnitMineRequest unitMineRequest);

    /**
     * 后台服务版本号
     * @return
     */
    Observable<BaseResponse> getVersion();

    /**
     * 联系我们
     * @return
     */
    Observable<Response<BaseResponse>> getEnterpriseInfo();
    /**
     * 维修
     * @return
     */
    Observable<BaseResponse> devRepair(@Body RepairJsonRequest repairJsonRequest);
    /**设置报警**/
    Observable<BaseResponse> modifyMark(@Body ModifyMarkJsonRequest modifyMarkJsonRequest);
    /**用户反馈**/
    Observable<BaseResponse> feedback(@Body FeedbackJsonRequest feedbackJsonRequest);
    /**
     *新的 消音 mute传2  我知道了 mute传1
     * **/
    Observable<BaseResponse> postUnitDevCloseVoice(@Body UnitDevCloseVoiceRequest closeVoiceRequest);
    /**
     *旧的 消音 mute传11000  我知道了 mute传10000
     * **/
    Observable<BaseResponse> modifyOnlineDevDoStat(@Body ModifyOnlineDevJsonRequest modifyOnlineDevJsonRequest);

    /****商用控制器****************/
    /**
     * 192设备权限
     * */
    Observable<BaseResponse> postUnitDevPermission(@Body CommonDevLowJsonRequest devLowJsonRequest);
    /**
     * 判断设备是否注册
     * */
    Observable<BaseResponse> IsDevReg_V902(@Body CommonDevLowJsonRequest commonDevJsonRequest);
    /**
     * 注册并绑定设备
     * @param
     * @return
     */
    Observable<BaseResponse> postAddAndBindUnitDev(@Body UnitDevAddBindRequest devAddBindRequest);
    /**
     * 修改并绑定设备
     * @param
     * @return
     */
    Observable<BaseResponse> postUpdateAndBindUnitDev(@Body UnitDevUpdateBindRequest updateBindRequest);

    /***获取设备绑定信息**/
    Observable<BaseResponse> getDevBindInfo(@Body CommonDevLowJsonRequest devLowJsonRequest);

    /**
     *关注设备
     * **/
    Observable<BaseResponse> postUnitDevBinder(@Body UnitDevDetailRequest singleDevInfoJsonRequest);
    /**
     *取消关注设备
     * **/
    Observable<BaseResponse> postUnitDevUnAttention(@Body UnitDevDetailRequest singleDevInfoJsonRequest);
    /**
     *解绑设备
     * **/
    Observable<BaseResponse> postUnitDevUnBinder(@Body UnitDevDetailRequest singleDevInfoJsonRequest);

    /**
     *编辑设备
     * **/
    Observable<BaseResponse> postUnitDevEdit(@Body UnitDevEditRequest devEditRequest);

    /**
     *移动设备
     * **/
    Observable<BaseResponse> postUnitDevRemovePlace(@Body UnitDevRemovePlaceRequest removePlaceRequest);

    /**
     * 182屏蔽设备
     * */
    Observable<BaseResponse> postUnitCloseClicket(@Body CommonDevLowJsonRequest devLowJsonRequest);

    /**
     *获取商用控制器回路列表
     * **/
    Observable<BaseResponse> postUnitDevHAddr(@Body UnitDevDetailRequest  singleDevInfoJsonRequest);
    /**
     *获取商用控制器节点列表
     * **/
    Observable<BaseResponse> postUnitDevNodeAddr(@Body UnitDevDetailNodeRequest devDetailNodeRequest);
    /***
     * 节点屏蔽/开启
     * */
    Observable<BaseResponse> postUnitNodeOpenCode(@Body UnitDevNodeOpenRequest nodeOpenRequest);
    Observable<BaseResponse> postUnitNodeCloseCode(@Body UnitDevNodeOpenRequest nodeOpenRequest);
    /***
     * 回路屏蔽/开启
     * */
    Observable<BaseResponse> postUnitHArrOpenCode(@Body UnitDevNodeOpenRequest nodeOpenRequest);
    Observable<BaseResponse> postUnitHArrCloseCode(@Body UnitDevNodeOpenRequest nodeOpenRequest);
    /**
     * 开启/关闭节点设备
     * */
    Observable<BaseResponse> postUnitNodeProductCloseCode(@Body UnitDevNodeOpenRequest nodeOpenRequest);
    /**
     *获取商用控制器趋势图
     * **/
    Observable<BaseResponse> postUnitDevChart(@Body UnitDevDetailChartRequest chartRequest);
    /**
     * 燃气趋势图
     * @param
     * @return
     */
    Observable<BaseResponse> postDevGasAlarm(@Body UnitDevDetailRequest singleDevInfoJsonRequest);
    /**
     * 查询192设备低高限告警值
     * */
    Observable<BaseResponse> postDevChartAlarmGasvalue(@Body CommonDevLowJsonRequest devRequest);
    /**
     * 查看联系人列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitDevMemberList(@Body UnitDevDetailRequest devUperJsonRequest);
    /**
     * 删除联系人
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitRemoveMemberList(@Body UnitMemberRemoveRequest memberRemoveRequest);
    /**
     * 添加联系人
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitAddMemberList(@Body UnitMemberAddRequest memberAddRequest);

    /**********家庭共享、设备共享*********/
    /**
     * 获取成员信息
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitMemberInfo(@Body UnitMemberInfoRequest memberInfoRequest);
    /**
     * 家庭共享
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitFamilyShare(@Body UnitFamilyShareRequest familyShareRequest);
    /**
     * 获取共享的设备列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitDevShareList(@Body UnitDevShareRequest devShareRequest);


    /***高德地图API**/
    Observable<GaoDeGetRegionAreaJsonResp> queryRegionArea(@Query("keywords") String keywords);

    /**
     * 删除房间
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitDeleteRoom(@Body UnitRoomDeleteRequest roomDeleteRequest);
    /**
     * 添加房间的公共列表
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitPublicRoomList(@Body UnitFamilyListRequest roomAddRequest);

    /**
     * 核对房间名称重复
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitCheckRoom(@Body UnitCheckRoomNameRequest checkRoomNameRequest);
    /**
     * 创建房间
     * @param
     * @return
     */
    Observable<BaseResponse> postUnitCreateRoom(@Body UnitCheckRoomNameRequest checkRoomNameRequest);



    /******设备续费*********************/
    /**
     * 获取设备续费列表
     * @param shoppingDeviceListJsonRequest
     * @return
     */
    Observable<BaseResponse> postShoppingDeviceList(@Body ShoppingDeviceListJsonRequest shoppingDeviceListJsonRequest);

    /**
     * 折扣开关
     * @param
     * @return
     */
    Observable<BaseResponse>postOrderSwitch(@Body ShoppingOrderSwitchRequest commonJsonRequst);
    /**
     * 折扣列表
     * @param
     * @return
     */
    Observable<BaseResponse<ShoppingOrderDiscountListModel>>postOrderDiscountList(@Body ShoppingOrderDiscountRequest shoppingOrderDiscountRequest);

    /**
     * 订单列表查询
     * @param orderInfoJsonRequst
     * @return
     */
    Observable<BaseListResponse<List<OrderInfo>>> postOrderlist(@Body OrderInfoJsonRequst orderInfoJsonRequst);

    /**
     * 订单结算
     * @param
     * @return
     */
    Observable<BaseResponse<ShoppingDeviceSubmitInfo>> postOrderAccountDetail(@Body ShoppingDeviceSubmitRequest shoppingDeviceSubmitRequest);

    /**
     * 创建订单
     * @param
     * @return
     */
    Observable<BaseResponse> postCreateOrder(@Body ShoppingDeviceSubmitRequest shoppingDeviceSubmitRequest);

    /**
     * 支付宝支付
     * @param
     * @return
     */
    Observable<AliPayJsonResp> postApiPay(@Body ShoppingOrderApiPayRequest shoppingDeviceSubmitRequest);
    /**
     * 微信支付
     * @param
     * @return
     */
    Observable<PayJsonResp> postWeixinPay(@Body ShoppingOrderApiPayRequest shoppingDeviceSubmitRequest);

    /**
     * 订单列表
     * @param
     * @return
     */
    Observable<BaseResponse<ShoppingOrderListResp>> postNewOrderList(@Body ShoppingOrderListRequest shoppingOrderListRequest);

    /**
     * 订单详情
     * @param
     * @return
     */
    Observable<BaseResponse<ShoppingOrderDetailResp>> postNewOrderDetail(@Body ShoppingOrderDetailRequest shoppingOrderDetailRequest);

    /**
     * 取消订单
     * @param
     * @return
     */
    Observable<BaseResponse> postCancelOrderWithTradeNo(@Body ShoppingOrderDetailRequest shoppingOrderDetailRequest);

}
