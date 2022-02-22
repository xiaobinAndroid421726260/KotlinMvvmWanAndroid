package com.kotlin.mvvm.api

import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResponse
import com.kotlin.mvvm.common.UserInfoBean
import com.kotlin.mvvm.ui.collect.CollectBean
import com.kotlin.mvvm.ui.home.bean.BannerBean
import com.kotlin.mvvm.ui.home.bean.HomeBean
import com.kotlin.mvvm.ui.integral.bean.IntegralBean
import com.kotlin.mvvm.ui.integral.bean.RankBean
import com.kotlin.mvvm.ui.integral.bean.UserIntegralBean
import com.kotlin.mvvm.ui.login.bean.LoginBean
import com.kotlin.mvvm.ui.message.MsgBean
import com.kotlin.mvvm.ui.my.bean.WendBean
import com.kotlin.mvvm.ui.project.bean.ProjectBean
import com.kotlin.mvvm.ui.project.bean.ProjectPagerBean
import com.kotlin.mvvm.ui.search.bean.SearchBean
import com.kotlin.mvvm.ui.share.bean.HotKeyBean
import com.kotlin.mvvm.ui.share.bean.Share
import com.kotlin.mvvm.ui.share.bean.ShareBean
import com.kotlin.mvvm.ui.square.SquareBean
import com.kotlin.mvvm.ui.system.bean.KnowBean
import com.kotlin.mvvm.ui.system.bean.NaviBean
import com.kotlin.mvvm.ui.system.bean.SystemBean
import com.kotlin.mvvm.ui.wechat.bean.WechatBean
import com.kotlin.mvvm.ui.wechat.bean.WechatPagerBean
import retrofit2.http.*

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/1 17:06
 */
interface Api {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(HttpsApi.login)
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseResponse<LoginBean>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(HttpsApi.register)
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): BaseResponse<LoginBean>

    /**
     * 退出登录
     */
    @GET(HttpsApi.logout)
    suspend fun logout(): BaseResponse<String>

    /**
     * 个人信息
     */
    @POST(HttpsApi.user_info)
    suspend fun userInfo(): BaseResponse<UserInfoBean>

    /**
     * 首页banner
     */
    @GET(HttpsApi.banner)
    suspend fun getBannerJson(): BaseResponse<List<BannerBean>>

    /**
     * 首页置顶文章
     */
    @GET(HttpsApi.article_top)
    suspend fun getTopJson(): BaseResponse<List<HomeBean>>

    /**
     * 首页文章列表
     */
    @GET(HttpsApi.article_list_json + "{page}/json")
    suspend fun getArticleJson(@Path("page") page: Int): BaseResponse<BaseListResponse<MutableList<HomeBean>>>

    /**
     * 广场
     */
    @GET(HttpsApi.user_article_list_json + "{page}/json")
    suspend fun getUserArticleJson(@Path("page") page: Int): BaseResponse<BaseListResponse<MutableList<SquareBean>>>

    /**
     * 获取公众号列表
     */
    @GET(HttpsApi.get_wechat_article_json)
    suspend fun getWechatArticleJson(): BaseResponse<MutableList<WechatBean>>

    /**
     * 查看某个公众号历史数据
     * https://wanandroid.com/wxarticle/list/408/1/json
     */
    @GET(HttpsApi.get_wechat_list_json + "{user_id}/{page}/json")
    suspend fun getUserWechatArticleJson(
        @Path("user_id") user_id: Int?,
        @Path("page") page: Int
    ): BaseResponse<BaseListResponse<MutableList<WechatPagerBean>>>

    /**
     * 体系数据
     * https://www.wanandroid.com/tree/json
     */
    @GET(HttpsApi.get_tree_json)
    suspend fun getTreeJson(): BaseResponse<MutableList<SystemBean>>

    /**
     * 知识体系下的文章
     * https://www.wanandroid.com/article/list/0/json?cid=60
     */
    @GET(HttpsApi.get_tree_json_cid + "{page}/json")
    suspend fun getKnowledgeTreeJson(
        @Path("page") page: Int,
        @Query("cid") id: Int?
    ): BaseResponse<BaseListResponse<MutableList<KnowBean>>>

    /**
     * 导航数据
     */
    @GET(HttpsApi.get_navi_json)
    suspend fun getNaviJson(): BaseResponse<MutableList<NaviBean>>

    /**
     * 项目分类
     */
    @GET(HttpsApi.get_project_tree_json)
    suspend fun getProjectTreeJson(): BaseResponse<MutableList<ProjectBean>>

    /**
     * 项目列表数据
     */
    @GET(HttpsApi.get_project_cid_json + "{page}/json")
    suspend fun getProjectCidJson(
        @Path("page") page: Int,
        @Query("cid") cid: Int?
    ): BaseResponse<BaseListResponse<MutableList<ProjectPagerBean>>>

    /**
     * 问答
     */
    @GET(HttpsApi.get_wenda_list_json + "{page}/json")
    suspend fun getWendListJson(
        @Path("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<BaseListResponse<MutableList<WendBean>>>

    /**
     * 获取个人积分获取列表，需要登录后访问
     */
    @GET(HttpsApi.get_my_integral_list + "{page}/json")
    suspend fun getMyIntegralList(@Path("page") page: Int): BaseResponse<BaseListResponse<MutableList<IntegralBean>>>

    /**
     * 获取个人积分，需要登录后访问
     */
    @GET(HttpsApi.get_my_integral)
    suspend fun getMyIntegral(): BaseResponse<UserIntegralBean>

    /**
     * 积分排行榜接口
     */
    @GET(HttpsApi.get_integral_rank + "{page}/json")
    suspend fun getIntegralRank(@Path("page") page: Int): BaseResponse<BaseListResponse<MutableList<RankBean>>>

    /**
     * 获取收藏文章列表
     */
    @GET(HttpsApi.get_collect_list + "{page}/json")
    suspend fun getCollectList(@Path("page") page: Int): BaseResponse<BaseListResponse<MutableList<CollectBean>>>

    /**
     * 收藏站内文章
     */
    @POST(HttpsApi.collect_list + "{id}/json")
    suspend fun collectList(@Path("id") id: Int): BaseResponse<String>

    /**
     * 取消收藏 我的收藏页面
     */
    @FormUrlEncoded
    @POST(HttpsApi.unCollect + "{id}/json")
    suspend fun unCollect(@Path("id") id: Int, @Field("originId") originId: Int): BaseResponse<String>

    /**
     * 取消收藏 文章列表
     */
    @POST(HttpsApi.unCollect_list + "{id}/json")
    suspend fun unCollectList(@Path("id") id: Int): BaseResponse<String>

    /**
     * 获取自己的分享列表
     */
    @GET(HttpsApi.get_user_share_list + "{page}/json")
    suspend fun getUserShareList(@Path("page") page: Int): BaseResponse<ShareBean<BaseListResponse<MutableList<Share>>>>

    /**
     * 获取未读消息数量
     */
    @GET(HttpsApi.get_message_count_unread)
    suspend fun getMessageCountUnread(): BaseResponse<Int>

    /**
     * 已读消息列表
     */
    @GET(HttpsApi.get_message_read_list + "{page}/json")
    suspend fun getMessageReadList(@Path("page") page: Int): BaseResponse<BaseListResponse<MutableList<MsgBean>>>

    /**
     * 未读消息列表
     */
    @GET(HttpsApi.get_message_unread_list + "{page}/json")
    suspend fun getMessageUnreadList(@Path("page") page: Int): BaseResponse<BaseListResponse<MutableList<MsgBean>>>

    /**
     * 搜索
     */
    @POST(HttpsApi.article_query + "{page}/json")
    suspend fun getArticleQuery(
        @Path("page") page: Int,
        @Query("k") k: String = ""
    ): BaseResponse<BaseListResponse<MutableList<SearchBean>>>

    /**
     * 搜索热词
     */
    @GET(HttpsApi.search_hotkey_json)
    suspend fun getSearchHotKeyJson(): BaseResponse<MutableList<HotKeyBean>>
}