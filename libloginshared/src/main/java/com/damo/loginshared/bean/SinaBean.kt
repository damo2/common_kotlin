package com.damo.loginshared.bean

import com.google.gson.annotations.SerializedName


/**
 * Created by wr
 * Date: 2018/12/10  15:30
 * describe:
 */

//data class SinaUserBean(

data class SinaUserBean(
        @SerializedName("id") var id: Long?,
        @SerializedName("idstr") var idstr: String?,
        @SerializedName("class") var classX: Int?,
        @SerializedName("screen_name") var screenName: String?,
        @SerializedName("name") var name: String?,
        @SerializedName("province") var province: String?,
        @SerializedName("city") var city: String?,
        @SerializedName("location") var location: String?,
        @SerializedName("description") var description: String?,
        @SerializedName("url") var url: String?,
        @SerializedName("profile_image_url") var profileImageUrl: String?,
        @SerializedName("cover_image_phone") var coverImagePhone: String?,
        @SerializedName("profile_url") var profileUrl: String?,
        @SerializedName("domain") var domain: String?,
        @SerializedName("weihao") var weihao: String?,
        @SerializedName("gender") var gender: String?,
        @SerializedName("followers_count") var followersCount: Int?,
        @SerializedName("friends_count") var friendsCount: Int?,
        @SerializedName("pagefriends_count") var pagefriendsCount: Int?,
        @SerializedName("statuses_count") var statusesCount: Int?,
        @SerializedName("video_status_count") var videoStatusCount: Int?,
        @SerializedName("favourites_count") var favouritesCount: Int?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("following") var following: Boolean?,
        @SerializedName("allow_all_act_msg") var allowAllActMsg: Boolean?,
        @SerializedName("geo_enabled") var geoEnabled: Boolean?,
        @SerializedName("verified") var verified: Boolean?,
        @SerializedName("verified_type") var verifiedType: Int?,
        @SerializedName("remark") var remark: String?,
        @SerializedName("insecurity") var insecurity: Insecurity?,
        @SerializedName("status") var status: Status?,
        @SerializedName("ptype") var ptype: Int?,
        @SerializedName("allow_all_comment") var allowAllComment: Boolean?,
        @SerializedName("avatar_large") var avatarLarge: String?,
        @SerializedName("avatar_hd") var avatarHd: String?,
        @SerializedName("verified_reason") var verifiedReason: String?,
        @SerializedName("verified_trade") var verifiedTrade: String?,
        @SerializedName("verified_reason_url") var verifiedReasonUrl: String?,
        @SerializedName("verified_source") var verifiedSource: String?,
        @SerializedName("verified_source_url") var verifiedSourceUrl: String?,
        @SerializedName("follow_me") var followMe: Boolean?,
        @SerializedName("like") var like: Boolean?,
        @SerializedName("like_me") var likeMe: Boolean?,
        @SerializedName("online_status") var onlineStatus: Int?,
        @SerializedName("bi_followers_count") var biFollowersCount: Int?,
        @SerializedName("lang") var lang: String?,
        @SerializedName("star") var star: Int?,
        @SerializedName("mbtype") var mbtype: Int?,
        @SerializedName("mbrank") var mbrank: Int?,
        @SerializedName("block_word") var blockWord: Int?,
        @SerializedName("block_app") var blockApp: Int?,
        @SerializedName("credit_score") var creditScore: Int?,
        @SerializedName("user_ability") var userAbility: Int?,
        @SerializedName("urank") var urank: Int?,
        @SerializedName("story_read_state") var storyReadState: Int?,
        @SerializedName("vclub_member") var vclubMember: Int?
)

data class Status(
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("id") var id: Long?,
        @SerializedName("idstr") var idstr: String?,
        @SerializedName("mid") var mid: String?,
        @SerializedName("can_edit") var canEdit: Boolean?,
        @SerializedName("show_additional_indication") var showAdditionalIndication: Int?,
        @SerializedName("text") var text: String?,
        @SerializedName("textLength") var textLength: Int?,
        @SerializedName("source_allowclick") var sourceAllowclick: Int?,
        @SerializedName("source_type") var sourceType: Int?,
        @SerializedName("source") var source: String?,
        @SerializedName("favorited") var favorited: Boolean?,
        @SerializedName("truncated") var truncated: Boolean?,
        @SerializedName("in_reply_to_status_id") var inReplyToStatusId: String?,
        @SerializedName("in_reply_to_user_id") var inReplyToUserId: String?,
        @SerializedName("in_reply_to_screen_name") var inReplyToScreenName: String?,
        @SerializedName("pic_urls") var picUrls: List<PicUrl?>?,
        @SerializedName("thumbnail_pic") var thumbnailPic: String?,
        @SerializedName("bmiddle_pic") var bmiddlePic: String?,
        @SerializedName("original_pic") var originalPic: String?,
        @SerializedName("geo") var geo: Any?,
        @SerializedName("is_paid") var isPaid: Boolean?,
        @SerializedName("mblog_vip_type") var mblogVipType: Int?,
        @SerializedName("annotations") var annotations: List<Annotation?>?,
        @SerializedName("reposts_count") var repostsCount: Int?,
        @SerializedName("comments_count") var commentsCount: Int?,
        @SerializedName("attitudes_count") var attitudesCount: Int?,
        @SerializedName("pending_approval_count") var pendingApprovalCount: Int?,
        @SerializedName("isLongText") var isLongText: Boolean?,
        @SerializedName("reward_exhibition_type") var rewardExhibitionType: Int?,
        @SerializedName("hide_flag") var hideFlag: Int?,
        @SerializedName("mlevel") var mlevel: Int?,
        @SerializedName("visible") var visible: Visible?,
        @SerializedName("biz_feature") var bizFeature: Long?,
        @SerializedName("hasActionTypeCard") var hasActionTypeCard: Int?,
        @SerializedName("darwin_tags") var darwinTags: List<Any?>?,
        @SerializedName("hot_weibo_tags") var hotWeiboTags: List<Any?>?,
        @SerializedName("text_tag_tips") var textTagTips: List<Any?>?,
        @SerializedName("mblogtype") var mblogtype: Int?,
        @SerializedName("rid") var rid: String?,
        @SerializedName("userType") var userType: Int?,
        @SerializedName("more_info_type") var moreInfoType: Int?,
        @SerializedName("positive_recom_flag") var positiveRecomFlag: Int?,
        @SerializedName("content_auth") var contentAuth: Int?,
        @SerializedName("gif_ids") var gifIds: String?,
        @SerializedName("is_show_bulletin") var isShowBulletin: Int?,
        @SerializedName("comment_manage_info") var commentManageInfo: CommentManageInfo?
)

data class PicUrl(
        @SerializedName("thumbnail_pic") var thumbnailPic: String?
)

data class CommentManageInfo(
        @SerializedName("comment_permission_type") var commentPermissionType: Int?,
        @SerializedName("approval_comment_type") var approvalCommentType: Int?
)

data class Visible(
        @SerializedName("type") var type: Int?,
        @SerializedName("list_id") var listId: Int?
)

data class Annotation(
        @SerializedName("client_mblogid") var clientMblogid: String?,
        @SerializedName("mapi_request") var mapiRequest: Boolean?
)

data class Insecurity(
        @SerializedName("sexual_content") var sexualContent: Boolean?
)