package models;

/**
 * Created by Omid on 4/30/2018.
 */

public class ReportItem {
    private String title = "";
    private boolean selected = false;

    public ReportItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
