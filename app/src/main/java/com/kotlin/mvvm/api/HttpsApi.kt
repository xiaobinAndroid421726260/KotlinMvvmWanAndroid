package com.kotlin.mvvm.api

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/1 11:29
 */
object HttpsApi {

    private const val HTTPS = "https://"

    const val BASE_URL: String = HTTPS + "www.wanandroid.com"

    const val GANK_IO_URL = "https://gank.io"

    // 首页文章列表 banner
    const val banner: String =  "/banner/json"

    // 置顶文章
    const val article_top: String = "/article/top/json"

    // 首页文章列表
    const val article_list_json: String = "/article/list/"

    // 广场
    const val user_article_list_json: String = "/user_article/list/"

    // 获取公众号列表
    const val get_wechat_article_json: String = "/wxarticle/chapters/json"

    // 查看某个公众号历史数据 、 在某个公众号中搜索历史文章
    const val get_wechat_list_json: String = "/wxarticle/list/"

    // 体系
    const val get_tree_json: String = "/tree/json"

    // 知识体系下的文章
    const val get_tree_json_cid: String = "/article/list/"

    // 导航
    const val get_navi_json: String = "/navi/json"

    // 项目分类
    const val get_project_tree_json: String = "/project/tree/json"

    // 项目列表数据
    const val get_project_cid_json: String = "/project/list/"

}