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

    public int getID(){
        return id;
    }
    private void setImage(){
        switch (category){
            case "Work":
                categoryImage = R.drawable.work;
                break;
            case "Personal":
                categoryImage = R.drawable.user;
                break;
            case "Social":
                categoryImage = R.drawable.hug;
                break;
            case "Finances":
                categoryImage = R.drawable.bill;
                break;
            case "Family":
                categoryImage = R.drawable.family;
                break;
            case "School":
                categoryImage = R.drawable.knowledge;
                break;
            default:
                break;
        }
    }

    public int getColor(){
        int color;
        switch (category) {
            case "Work":
                color = R.color.yellow;
                break;
            case "Personal":
                color = R.color.white;
                break;
            case "Social":
                color = R.color.peach;
                break;
            case "Finances":
                color = R.color.dark_peach;
                break;
            case "Family":
                color = R.color.light_peach;
                categoryImage = R.drawable.family;
                break;
            case "School":
                color = R.color.blue;
                break;
            default:
                color = R.color.light_peach;
                break;
        }
        return color;
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
