package com.anyonavinfo.bluetoothphone.bpclient.bean;

/**
 * Created by shijj on 2016/10/20.
 */

public class TelPlace {
    private int errNum;
    private String errMsg;
    private RetDataBean retData;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public RetDataBean getRetData() {
        return retData;
    }

    public void setRetData(RetDataBean retData) {
        this.retData = retData;
    }

    public static class RetDataBean {
        private String telString;
        private String province;
        private String carrier;

        public String getTelString() {
            return telString;
        }

        public void setTelString(String telString) {
            this.telString = telString;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }
    }
}
