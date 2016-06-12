package com.yuri.cnbeta.newsdetial;

/**
 * 新闻详情页  视频数据
 * Created by Yuri on 2016/6/12.
 */
public class VideoInfoResponse {
    public int status;
    public String statusText;
    public VideoInfo data;

    public static class VideoInfo {
        public String cate_code;
        public int cid;
        public String clips_bytes_high;
        public String clips_duration_high;
        public long create_time;
        public String download_url;
        public int fee;
        public long file_size_high;
        public long file_size_mobile;
        public long file_size_nor;
        public long file_size_super;
        public String hor_big_pic;
        public int is_act;
        public int is_original_code;
        public String keyword;
        public String original_video_url;
        public int[] pay_type;
        public int play_count;
        public String publish_time;
        public int site;
        public String tip;
        public int total_duration;//单位s
        public long tv_id;
        public String url_high;
        public String url_high_mp4;
        public String url_html5;
        public String url_nor;
        public String url_super;
        public long vid;
        public String video_desc;
        public String video_name;
    }

}
