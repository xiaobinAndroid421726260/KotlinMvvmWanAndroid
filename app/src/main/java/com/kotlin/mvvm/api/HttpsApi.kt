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

    // 登录
    const val login: String = "/user/login"

    // 注册
    const val register: String = "/user/register"

    // 退出登录
    const val logout: String = "/user/logout/json"

    // 个人信息
    const val user_info: String = "/user/lg/userinfo/json"

    // 首页文章列表 banner
    const val banner: String = "/banner/json"

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

    // 问答
    const val get_wenda_list_json: String = "/wenda/list/"

    // 获取个人积分获取列表，需要登录后访问
    const val get_my_integral_list: String = "lg/coin/list/"

    // 获取个人积分，需要登录后访问
    const val get_my_integral: String = "lg/coin/userinfo/json"

    // 积分排行榜接口
    const val get_integral_rank: String = "coin/rank/"

    // 获取收藏文章列表
    const val get_collect_list: String = "lg/collect/list/"

    // 未读消息数量
    const val get_message_count_unread: String = "message/lg/count_unread/json"

    // 已读消息列表
    const val get_message_read_list: String = "message/lg/readed_list/"

    // 未读消息列表
    const val get_message_unread_list: String = "message/lg/unread_list/"

    // 获取自己的分享列表
    const val get_user_share_list: String = "user/lg/private_articles/"

    // 搜索
    const val article_query: String = "article/query/"

    // 搜索热词
    const val search_hotkey_json: String = "hotkey/json"

    // 添加分享
    const val add_share: String = "lg/user_article/add/json"

}