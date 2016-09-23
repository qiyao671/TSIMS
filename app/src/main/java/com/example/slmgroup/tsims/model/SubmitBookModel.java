package com.example.slmgroup.tsims.model;

import java.util.List;

/**
 * Created by BruceYu on 2016/9/20 11:29.
 * Email: 13738725570@163.com
 * Description:
 */
public class SubmitBookModel {

    /**
     * user_num : sample string 1
     * textBookInfoList : [{"isbn":"sample string 1","isSelect":2},{"isbn":"sample string 1","isSelect":2},{"isbn":"sample string 1","isSelect":2}]
     */

    private String user_num;
    /**
     * isbn : sample string 1
     * isSelect : 2
     */

    private List<TextBookInfo> textBookInfoList;

    public String getUser_num() {
        return user_num;
    }

    public void setUser_num(String user_num) {
        this.user_num = user_num;
    }

    public List<TextBookInfo> getTextBookInfoList() {
        return textBookInfoList;
    }

    public void setTextBookInfoList(List<TextBookInfo> textBookInfoList) {
        this.textBookInfoList = textBookInfoList;
    }

    public static class TextBookInfo {
        private String isbn;
        private int isSelect;

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public int getIsSelect() {
            return isSelect;
        }

        public void setIsSelect(int isSelect) {
            this.isSelect = isSelect;
        }
    }
}
