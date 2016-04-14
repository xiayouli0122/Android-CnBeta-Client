package com.yuri.cnbeta.http.response;

import java.io.Serializable;

/**
 * Bead Content(News Content)
 * Created by Wayne on 2016/2/21.
 */
public class Content implements Comparable<Content>,Serializable {
    public String sid;
    public String topic;
    public String aid;
    public String title;
    public String hometext;
    public String bodytext;
    public String source;
    public String comments;
    public String counter;
    public String inputtime;
    public String time;//已格式化过的日期

    @Override
    public int compareTo(Content another) {
        int aSid = Integer.parseInt(this.sid);
        int bSid = Integer.parseInt(another.sid);
        return bSid - aSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Content content = (Content) o;
        return this.sid.equals(content.sid);
    }

    @Override
    public int hashCode() {
        return 7 * sid.hashCode()
                + 11 * hometext.hashCode()
                + 13 * bodytext.hashCode();
    }

    @Override
    public String toString() {
        return title +
                "\n " + hometext +
                "\n " + bodytext;
    }
}
