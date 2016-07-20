package com.asd.tianwang.dao.table;

/**
 * Created by ASD on 2016/7/19.
 */
public class Tbodigital {

    private int id,jsb,fxb,csf,jqf,zjf,zpf,fjf,fpf;
    public Tbodigital(){}
    public Tbodigital(int id,int jsb, int fxb, int csf, int jqf, int zjf, int zpf, int fjf, int fpf) {
        this.id=id;
        this.jsb = jsb;
        this.fxb = fxb;
        this.csf = csf;
        this.jqf = jqf;
        this.zjf = zjf;
        this.zpf = zpf;
        this.fjf = fjf;
        this.fpf = fpf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJsb() {
        return jsb;
    }

    public void setJsb(int jsb) {
        this.jsb = jsb;
    }

    public int getFxb() {
        return fxb;
    }

    public void setFxb(int fxb) {
        this.fxb = fxb;
    }

    public int getCsf() {
        return csf;
    }

    public void setCsf(int csf) {
        this.csf = csf;
    }

    public int getJqf() {
        return jqf;
    }

    public void setJqf(int jqf) {
        this.jqf = jqf;
    }

    public int getZjf() {
        return zjf;
    }

    public void setZjf(int zjf) {
        this.zjf = zjf;
    }

    public int getZpf() {
        return zpf;
    }

    public void setZpf(int zpf) {
        this.zpf = zpf;
    }

    public int getFjf() {
        return fjf;
    }

    public void setFjf(int fjf) {
        this.fjf = fjf;
    }

    public int getFpf() {
        return fpf;
    }

    public void setFpf(int fpf) {
        this.fpf = fpf;
    }
}
