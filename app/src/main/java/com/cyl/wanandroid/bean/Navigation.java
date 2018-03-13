package com.cyl.wanandroid.bean;

import java.util.List;

/**
 * Created by lw on 2018/1/23.
 */

public class Navigation {

    /**
     * articles : [{"apkLink":"","author":"小编","chapterId":281,"chapterName":"公司博客","collect":false,"courseId":13,"desc":"","envelopePic":"","id":1894,"link":"https://tech.meituan.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515326436000,"title":"美团点评","visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":281,"chapterName":"公司博客","collect":false,"courseId":13,"desc":"","envelopePic":"","id":1895,"link":"https://joyrun.github.io/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515326464000,"title":"悦跑圈技术团队","visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":281,"chapterName":"公司博客","collect":false,"courseId":13,"desc":"","envelopePic":"","id":2064,"link":"http://dev.qq.com/","niceDate":"2018-01-13","origin":"","projectLink":"","publishTime":1515814947000,"title":"腾讯DevClub","visible":1,"zan":0}]
     * cid : 281
     * name : 公司博客
     */

    private int cid;
    private String name;
    private List<ArticlesBean> articles;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticlesBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesBean> articles) {
        this.articles = articles;
    }

    public static class ArticlesBean {
        /**
         * apkLink :
         * author : 小编
         * chapterId : 281
         * chapterName : 公司博客
         * collect : false
         * courseId : 13
         * desc :
         * envelopePic :
         * id : 1894
         * link : https://tech.meituan.com/
         * niceDate : 2018-01-07
         * origin :
         * projectLink :
         * publishTime : 1515326436000
         * title : 美团点评
         * visible : 0
         * zan : 0
         */

        private String apkLink;
        private String author;
        private int chapterId;
        private String chapterName;
        private boolean collect;
        private int courseId;
        private String desc;
        private String envelopePic;
        private int id;
        private String link;
        private String niceDate;
        private String origin;
        private String projectLink;
        private long publishTime;
        private String title;
        private int visible;
        private int zan;

        public String getApkLink() {
            return apkLink;
        }

        public void setApkLink(String apkLink) {
            this.apkLink = apkLink;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getChapterId() {
            return chapterId;
        }

        public void setChapterId(int chapterId) {
            this.chapterId = chapterId;
        }

        public String getChapterName() {
            return chapterName;
        }

        public void setChapterName(String chapterName) {
            this.chapterName = chapterName;
        }

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getEnvelopePic() {
            return envelopePic;
        }

        public void setEnvelopePic(String envelopePic) {
            this.envelopePic = envelopePic;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getNiceDate() {
            return niceDate;
        }

        public void setNiceDate(String niceDate) {
            this.niceDate = niceDate;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getProjectLink() {
            return projectLink;
        }

        public void setProjectLink(String projectLink) {
            this.projectLink = projectLink;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public int getZan() {
            return zan;
        }

        public void setZan(int zan) {
            this.zan = zan;
        }
    }
}
