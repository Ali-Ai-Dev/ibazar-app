package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Omid on 3/13/2018.
 */

public class Category implements Parcelable{

    private int id;
    private int parent;
    private String last_modified;
    private String title;
    private String image_name;

    public Category() {
    }

    protected Category(Parcel in) {
        id = in.readInt();
        parent = in.readInt();
        last_modified = in.readString();
        title = in.readString();
        image_name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(parent);
        dest.writeString(last_modified);
        dest.writeString(title);
        dest.writeString(image_name);
    }
}
