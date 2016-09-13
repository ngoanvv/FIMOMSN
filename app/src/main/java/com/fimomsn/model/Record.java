package com.fimomsn.model;

/**
 * Created by Diep_Chelsea on 25/08/2016.
 */
public class Record {
        private String status;
        private String temp;
        private String time;

    @Override
    public String toString() {
        return "Record{" +
                "status='" + status + '\'' +
                ", temp='" + temp + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
