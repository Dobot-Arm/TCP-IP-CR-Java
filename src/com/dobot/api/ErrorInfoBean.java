package com.dobot.api;

public class ErrorInfoBean {
    private int id;
    private int level;
    private String Type;

    private Description en;
    private Description zh_CN;

    class Description
    {
        public String description;
        public String cause;
        public String solution;

        @Override
        public String toString() {
            return "Description{" +
                    "description='" + description + '\'' +
                    ", cause='" + cause + '\'' +
                    ", solution='" + solution + '\'' +
                    '}';
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Description getEn() {
        return en;
    }

    public void setEn(Description en) {
        this.en = en;
    }

    public Description getZh_CN() {
        return zh_CN;
    }

    public void setZh_CN(Description zh_CN) {
        this.zh_CN = zh_CN;
    }

    @Override
    public String toString() {
        return "ErrorInfoBean{" +
                "id=" + id +
                ", level=" + level +
                ", Type='" + Type + '\'' +
                ", en=" + en +
                ", zh_CN=" + zh_CN +
                '}';
    }
}
