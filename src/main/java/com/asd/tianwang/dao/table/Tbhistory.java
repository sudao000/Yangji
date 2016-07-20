package com.asd.tianwang.dao.table;

/**
 * Created by ASD on 2016/7/18.
 */
public class Tbhistory {
    private int id,orp;
    private float inp ,outp ,opsp ,inf,outf ,backf;
    private String mtime,mdate;
    public Tbhistory(){}
    public Tbhistory(int id, float inp, float outp, float opsp, float inf, float outf, float backf,
                     int orp, String mtime, String mdate) {
        this.id = id;
        this.orp = orp;
        this.inp = inp;
        this.outp = outp;
        this.opsp = opsp;
        this.inf = inf;
        this.outf = outf;
        this.backf = backf;
        this.mtime = mtime;
        this.mdate = mdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrp() {
        return orp;
    }

    public void setOrp(int orp) {
        this.orp = orp;
    }

    public float getInp() {
        return inp;
    }

    public void setInp(float inp) {
        this.inp = inp;
    }

    public float getOutp() {
        return outp;
    }

    public void setOutp(float outp) {
        this.outp = outp;
    }

    public float getOpsp() {
        return opsp;
    }

    public void setOpsp(float opsp) {
        this.opsp = opsp;
    }

    public float getInf() {
        return inf;
    }

    public void setInf(float inf) {
        this.inf = inf;
    }

    public float getOutf() {
        return outf;
    }

    public void setOutf(float outf) {
        this.outf = outf;
    }

    public float getBackf() {
        return backf;
    }

    public void setBackf(float backf) {
        this.backf = backf;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }
}
