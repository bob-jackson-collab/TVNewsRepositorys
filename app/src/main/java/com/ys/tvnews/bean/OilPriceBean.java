package com.ys.tvnews.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/15.
 */
public class OilPriceBean {

    private String resultCode;
    private String reason;
    private List<CityBean> result;
    private String error_code;

    public class CityBean{
        private String city;
        private String b90;
        private String b93;
        private String b97;
        private String b0;


        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getB90() {
            return b90;
        }

        public void setB90(String b90) {
            this.b90 = b90;
        }

        public String getB93() {
            return b93;
        }

        public void setB93(String b93) {
            this.b93 = b93;
        }

        public String getB97() {
            return b97;
        }

        public void setB97(String b97) {
            this.b97 = b97;
        }

        public String getB0() {
            return b0;
        }

        public void setB0(String b0) {
            this.b0 = b0;
        }
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<CityBean> getList_cityBean() {
        return result;
    }

    public void setList_cityBean(List<CityBean> list_cityBean) {
        this.result = list_cityBean;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
