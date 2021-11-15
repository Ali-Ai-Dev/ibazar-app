package models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Driver;
import java.util.ArrayList;

/**
 * Created by Omid on 4/26/2018.
 */

public class Product implements Parcelable {


    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private int id;
    private String title;
    private String main_image;
    private int price;
    private int discount;
    private int discount_type;
    private String description;
    private ArrayList<UserComment> comments;
    private ArrayList<String> images;
    private int shop_id;
    private int cat_id = -1;
    private String categoryTitle;
    private String brandTitle;
    private Color productColor;

    public Product() {
    }

    protected Product(Parcel in) {
        id = in.readInt();
        title = in.readString();
        main_image = in.readString();
        price = in.readInt();
        discount = in.readInt();
        discount_type = in.readInt();
        description = in.readString();
        images = in.createStringArrayList();
        shop_id = in.readInt();
        cat_id = in.readInt();
        categoryTitle = in.readString();
        brandTitle = in.readString();
    }

    public Color getProductColor() {
        return productColor;
    }

    public void setProductColor(Color productColor) {
        this.productColor = productColor;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(main_image);
        dest.writeInt(price);
        dest.writeInt(discount);
        dest.writeInt(discount_type);
        dest.writeString(description);
        dest.writeStringList(images);
        dest.writeInt(shop_id);
        dest.writeInt(cat_id);
        dest.writeString(categoryTitle);
        dest.writeString(brandTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(int discount_type) {
        this.discount_type = discount_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<UserComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<UserComment> comments) {
        this.comments = comments;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getBrandTitle() {
        return brandTitle;
    }

    public void setBrandTitle(String brandTitle) {
        this.brandTitle = brandTitle;
    }
}
