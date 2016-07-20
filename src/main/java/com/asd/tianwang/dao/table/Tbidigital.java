package com.asd.tianwang.dao.table;

/**
 * Created by ASD on 2016/7/19.
 */
public class Tbidigital {

    private int id;
    private int inl;
    private int inh;
    private int outl;

    public Tbidigital(){}
    public Tbidigital( int id, int inl, int inh, int outl,int outh) {
        this.outh = outh;
        this.id = id;
        this.inl = inl;
        this.inh = inh;
        this.outl = outl;
    }

    public int getOuth() {

        return outh;
    }

    public void setOuth(int outh) {
        this.outh = outh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInl() {
        return inl;
    }

    public void setInl(int inl) {
        this.inl = inl;
    }

    public int getInh() {
        return inh;
    }

    public void setInh(int inh) {
        this.inh = inh;
    }

    public int getOutl() {
        return outl;
    }

    public void setOutl(int outl) {
        this.outl = outl;
    }

    private int outh;
}
