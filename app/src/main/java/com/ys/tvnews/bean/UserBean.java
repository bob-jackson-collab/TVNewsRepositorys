package com.ys.tvnews.bean;

import java.util.List;

/**
 * Created by sks on 2015/11/25.
 */
public class UserBean {

    private List<User> list_user;

    public List<User> getList_user() {
        return list_user;
    }

    public void setList_user(List<User> list_user) {
        this.list_user = list_user;
    }

    class User{
        private String headPic;
        private String id;
        private String userName;
        private String firstLogin;
        private String amount;
        private String auth_state;

        public String getAuth_state() {
            return auth_state;
        }

        public void setAuth_state(String auth_state) {
            this.auth_state = auth_state;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstLogin() {
            return firstLogin;
        }

        public void setFirstLogin(String firstLogin) {
            this.firstLogin = firstLogin;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
