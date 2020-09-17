package com.example.systemize;

public class TaskItem {
    private String title;
    private int categoryImage;


    public TaskItem(String title, int categoryImage){
        this.title = title;
        this.categoryImage = categoryImage;
    }

    public int getCategoryImage(){
        return categoryImage;
    }

    public String getTitle(){
        return title;
    }
}
