package com.sys.ldk.clock;

public class ClockBean {
    private int id;
    private String time;
    private String repeat;
    private int isSwitchOn;
    private String create_time;
    private String apptype;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getIsSwitchOn() {
        return isSwitchOn;
    }

    public void setIsSwitchOn(int isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    @Override
    public String toString() {
        return "ClockBean{" + "id=" + id + ", time='" + time + '\'' + ", repeat='" + repeat + '\'' + ", isSwitchOn=" + isSwitchOn + ", apptype='" + apptype + '\'' + ", create_time='" + create_time + '\'' + '}';
    }

    public ClockBean(int id, String time, String repeat, int isSwitchOn, String apptype, String create_time) {
        this.id = id;
        this.time = time;
        this.repeat = repeat;
        this.isSwitchOn = isSwitchOn;
        this.apptype = apptype;
        this.create_time = create_time;
    }

    public ClockBean() {
    }
}
