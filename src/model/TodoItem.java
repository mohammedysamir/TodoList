package model;

import java.time.LocalDate;

public class TodoItem {
    String title, description, category;
    LocalDate startDate, endDate;
    Boolean isFavorite;
    Priorities priority;

    public TodoItem(String title, String description, String category, LocalDate startDate, LocalDate endDate, Boolean isFavorite, Priorities priority) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isFavorite = isFavorite;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Priorities getPriority() {
        return priority;
    }

    public void setPriority(Priorities priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "{" +
                title + '\n' +
                "," + description + '\n' +
                "," + category + '\n' +
                "," + startDate + '\n' +
                "," + endDate + '\n' +
                "," + isFavorite + '\n' +
                "," + priority + '\n' +
                '}' + '\n';
    }
}
