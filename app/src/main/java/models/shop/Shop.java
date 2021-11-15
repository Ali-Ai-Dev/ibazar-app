package models.shop;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Omid on 3/26/2018.
 */

public class Shop implements Parcelable{
    private int id;
    private String title;
    private int cat_id;
    private String address;
    private int medal;
    private String img_main;
    private String lat;
    private String lng;
    private float score;
    private int vote_count;
    private String webSite;
    private String email;
    private String tell;

    public Shop() {
    }

    protected Shop(Parcel in) {
        id = in.readInt();
        title = in.readString();
        cat_id = in.readInt();
        address = in.readString();
        medal = in.readInt();
        img_main = in.readString();
        lat = in.readString();
        lng = in.readString();
        score = in.readFloat();
        vote_count = in.readInt();
        webSite = in.readString();
        email = in.readString();
        tell = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(cat_id);
        dest.writeString(address);
        dest.writeInt(medal);
        dest.writeString(img_main);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeFloat(score);
        dest.writeInt(vote_count);
        dest.writeString(webSite);
        dest.writeString(email);
        dest.writeString(tell);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMedal() {
        return medal;
    }

    public void setMedal(int medal) {
        this.medal = medal;
    }

    public String getImg_main() {
        return img_main;
    }

    public void setImg_main(String img_main) {
        this.img_main = img_main;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }
}
