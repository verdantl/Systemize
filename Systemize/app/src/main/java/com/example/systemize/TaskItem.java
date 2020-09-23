package com.example.systemize;

public class TaskItem {
    private int id;
    private String title;
    private String category;
    private boolean completed;
    private int categoryImage;
    private String date;
    private String duration;


    public TaskItem(int id, String title, String category, boolean completed, String date, String duration){
        this.id = id;
        this.title = title;
        this.category = category;
        this.completed = completed;
        this.date = date;
        this.duration = duration;
        setImage();
    }

    public void changeCompleted(){
        completed = !completed;

    }

    public String getDate() {
        return date;
    }

    public String getDuration(){
        return duration;
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
            case "Other":
                categoryImage = R.drawable.question_mark;
            default:
                break;
        }
    }

    public int getColor(){
        int color;
        switch (category) {
            case "Work":
                color = R.color.work_colour;
                break;
            case "Personal":
                color = R.color.personal_colour;
                break;
            case "Social":
                color = R.color.social_colour;
                break;
            case "Finances":
                color = R.color.finances_colour;
                break;
            case "Family":
                color = R.color.family_colour;
                break;
            case "School":
                color = R.color.school_colour;
                break;
            default:
                color = R.color.olive;
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

    public String getShortenedTitle(){
        if (title.length() > 20){
        }
        return title;
    }
}
