package com.example.systemize;

public class TaskItem {
    private int id;
    private String title;
    private String category;
    private boolean completed;
    private int categoryImage;


    public TaskItem(int id, String title, String category, boolean completed){
        this.id = id;
        this.title = title;
        this.category = category;
        this.completed = completed;
        setImage();
    }

    public void changeCompleted(){
        completed = !completed;
    }
    private void setImage(){
        switch (category){
            case "":
                categoryImage = R.drawable.ic_baseline_home_24;
                break;
            default:
                break;
        }
    }
    public String getCategory(){
        return category;
    }

    public int getCategoryImage(){
        return categoryImage;
    }

    public String getTitle(){
        return title;
    }

    public boolean getCompleted(){
        return completed;
    }
}
