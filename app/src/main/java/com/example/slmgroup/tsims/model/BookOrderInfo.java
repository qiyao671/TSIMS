package com.example.slmgroup.tsims.model;

/**
 * Created by BruceYu on 2016/9/20 11:12.
 * Email: 13738725570@163.com
 * Description:
 */
public class BookOrderInfo {
    public static final String VALUE_ORDERED = "1";
    public static final String VALUE_NOT_ORDERED = "0";

    /**
     * courseName : Java 程序设计基础
     * needBook : 1
     * isbn : 9787111342366
     * bookName : Java语言程序设计:进阶篇
     * price : 79.00
     * isSelect : 1
     */
     private String courseName;
     private String needBook;
     private String isbn;
     private String bookName;
     private String price;
     private String isSelect;

     public String getCourseName() {
         return courseName;
     }

     public void setCourseName(String courseName) {
         this.courseName = courseName;
     }

     public String getNeedBook() {
         return needBook;
     }

     public void setNeedBook(String needBook) {
         this.needBook = needBook;
     }

     public String getIsbn() {
         return isbn;
     }

     public void setIsbn(String isbn) {
         this.isbn = isbn;
     }

     public String getBookName() {
         return bookName;
     }

     public void setBookName(String bookName) {
         this.bookName = bookName;
     }

     public String getPrice() {
         return price;
     }

     public void setPrice(String price) {
         this.price = price;
     }

     public String getIsSelect() {
         return isSelect;
     }

     public void setIsSelect(String isSelect) {
            this.isSelect = isSelect;
        }

}
