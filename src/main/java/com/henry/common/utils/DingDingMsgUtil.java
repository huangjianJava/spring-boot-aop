package com.henry.common.utils;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * @Title: TODO
 * @Description: TODO 钉钉自定义机器人发送消息工具类
 * @author Steven Liu 刘洋
 * @date 2018年8月9日 下午5:58:27
 * @version V1.0
 **/
public class DingDingMsgUtil {

    /**
     * 发送
     * 
     * @param url
     *            机器人webhook连接
     * @param dto
     * @see TextDto TextDto普通文本
     * @see LinkDto
     * @see MarkdownDto
     * @see ActionCardDto
     * @see ActionCardsDto
     * @see FeedCardDto
     * @return { "errmsg": "ok", "errcode": "0" }
     */
    public static DingDingResult sendMsg(MsgDto dto) {
        try {
            if (dto == null) {
                return DingDingResult.builder().errmsg("参数异常").errcode("-1").build();
            }
            String checkParam = dto.checkParam();
            if (checkParam != null) {
                return DingDingResult.builder().errmsg(checkParam).errcode("-1").build();
            }

            String post = HttpUtil.post(dto.getUrl(), dto.buildJson());
            return JsonUtil.fromJson(post, DingDingResult.class);
        } catch (Exception e) {
            return DingDingResult.builder().errmsg("请求异常" + e.getMessage()).errcode("-1").build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingDingResult {

        String errmsg;
        String errcode;
    }

    @Data
    public static abstract class MsgDto {

        protected String url;

        public abstract String buildJson();

        public String checkParam() {
            if (StringUtils.isBlank(url)) {
                return "url 不能空";
            } else if (StringUtils.isBlank(buildJson())) {
                return "param 异常";
            }
            return null;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TextDto extends MsgDto {

        /**
         * 消息内容(必传)
         */
        String content;
        /**
         * 被@人的手机号
         */
        String atMobiles;
        /**
         * @所有人时:true,否则为:false
         */
        boolean atAll;

        @Override
        public String buildJson() {
            return "{'msgtype': 'text','text': {'content': '" + content + "'},'at': {'atMobiles': ['" + atMobiles
                    + "'], 'isAtAll': " + atAll + "}}";
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LinkDto extends MsgDto {

        /**
         * 消息标题(必传)
         */
        String title;

        /**
         * 消息内容。如果太长只会部分展示(必传)
         */
        String text;
        /**
         * 点击消息跳转的URL(必传)
         */
        String messageUrl;
        /**
         * 图片URL
         */
        String picUrl;

        @Override
        public String buildJson() {
            return " {'msgtype': 'link','link': {'text':'" + text + "', 'title': '" + title + "','picUrl': '" + picUrl
                    + "','messageUrl': '" + messageUrl + "'}}";
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class MarkdownDto extends MsgDto {

        /**
         * 首屏会话透出的展示内容(必传)
         */
        String title;

        /**
         * markdown格式的消息(必传)
         */
        String text;
        /**
         * 被@人的手机号(在text内容里要有@手机号)
         */
        String[] atMobiles;
        /**
         * @所有人时:true,否则为:false
         */
        boolean atAll;

        @Override
        public String buildJson() {
            return "{'msgtype': 'markdown','markdown': {'title': '" + title + "','text':'" + text
                    + "'},'at': {'atMobiles': " + toString(atMobiles) + ", 'isAtAll': " + atAll + "}}";
        }

        public static String toString(Object[] a) {
            if (a == null)
                return "[]";
            int iMax = a.length - 1;
            if (iMax == -1)
                return "[]";

            StringBuilder b = new StringBuilder();
            b.append("['");
            for (int i = 0;; i++) {
                b.append(String.valueOf(a[i]));
                if (i == iMax)
                    return b.append("']").toString();
                b.append("','");
            }
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ActionCardDto extends MsgDto {

        /**
         * 首屏会话透出的展示内容(必传)
         */
        String title;

        /**
         * markdown格式的消息(必传)
         */
        String text;

        /**
         * 点击singleTitle按钮触发的URL(必传)
         */
        String singleTitle;

        /**
         * 点击singleTitle按钮触发的URL(必传)
         */
        String singleURL;

        /**
         * 0-按钮竖直排列，1-按钮横向排列
         */
        String btnOrientation;

        /**
         * 0-正常发消息者头像,1-隐藏发消息者头像
         */
        String hideAvatar;

        @Override
        public String buildJson() {
            return "{'msgtype': 'actionCard','actionCard': {'title': '" + title + "','text':'" + text
                    + "','hideAvatar':'" + hideAvatar + "','btnOrientation':'" + btnOrientation + "','singleTitle':'"
                    + singleURL + "','':'" + singleURL + "'}}";
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ActionCardsDto extends MsgDto {

        @Data
        public static class Btn {

            String title;
            String actionURL;

            @Override
            public String toString() {
                return "{'title': '" + title + "','actionURL':'" + actionURL + "'}";
            }

        }

        /**
         * 首屏会话透出的展示内容(必传)
         */
        String title;

        /**
         * markdown格式的消息(必传)
         */
        String text;

        ArrayList<Btn> btns;

        /**
         * 0-按钮竖直排列，1-按钮横向排列
         */
        String btnOrientation;

        /**
         * 0-正常发消息者头像,1-隐藏发消息者头像
         */
        String hideAvatar;

        @Override
        public String buildJson() {
            return "{'msgtype': 'actionCard','actionCard': {'title': '" + title + "','text':'" + text
                    + "','hideAvatar':'" + hideAvatar + "','btnOrientation':'" + btnOrientation + "','btns':"
                    + DingDingMsgUtil.toString(btns) + "}}";
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class FeedCardDto extends MsgDto {

        @Data
        public static class Item {

            /**
             * 单条信息文本(必传)
             */
            String title;

            /**
             * 点击单条信息到跳转链接(必传)
             */
            String messageURL;
            /**
             * 单条信息后面图片的URL(必传)
             */
            String picURL;

            @Override
            public String toString() {
                return "{'title': '" + title + "','messageURL':'" + messageURL + "','picURL':'" + picURL + "'}";
            }

        }

        ArrayList<Item> links;

        @Override
        public String buildJson() {
            return "{'msgtype': 'feedCard','feedCard': {'links': " + DingDingMsgUtil.toString(links) + "}}";
        }

    }

    public static <T> String toString(ArrayList<T> a) {
        if (a == null)
            return "[]";
        int iMax = a.size() - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0;; i++) {
            b.append(String.valueOf(a.get(i)));
            if (i == iMax)
                return b.append("]").toString();
            b.append(",");
        }
    }

}
