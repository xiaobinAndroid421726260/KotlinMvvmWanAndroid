package com.kotlin.mvvm.api

import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResponse
import com.kotlin.mvvm.ui.home.bean.BannerBean
import com.kotlin.mvvm.ui.home.bean.HomeBean
import com.kotlin.mvvm.ui.project.bean.ProjectBean
import com.kotlin.mvvm.ui.project.bean.ProjectPagerBean
import com.kotlin.mvvm.ui.square.SquareBean
import com.kotlin.mvvm.ui.system.bean.Article
import com.kotlin.mvvm.ui.system.bean.KnowBean
import com.kotlin.mvvm.ui.system.bean.NaviBean
import com.kotlin.mvvm.ui.system.bean.SystemBean
import com.kotlin.mvvm.ui.wechat.bean.WechatBean
import com.kotlin.mvvm.ui.wechat.bean.WechatPagerBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/1 17:06
 */
interface Api {

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
}