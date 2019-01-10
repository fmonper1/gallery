package uk.ac.shef.oak.com4510.pojo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class FormData extends BaseObservable {
    private String title;
    private String description;

    public FormData() {
        this.title = "";
        this.description = "";
    }

    public FormData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
