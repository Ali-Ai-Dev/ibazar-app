package database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.tnt.ibazaar.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import models.Advertisement;
import models.Brand;
import models.Category;
import models.Product;
import models.Staff;
import models.shop.Shop;
import models.shop.ShopInfo;

/**
 * Created by omid' on 5/9/2017.
 */

public class DataBase {

    private static final String dirsd = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String dirdb = dirsd + "/android/data/" + Application.getPackageName2();


    private static SQLiteDatabase db;


    private static void openOrCreate() {
        File filedb = new File(dirdb);
        filedb.mkdirs();
        filedb.setWritable(true);
        filedb.setReadable(true);
        File db2 = new File(dirdb + "/db.db");
        Log.e("dp path", dirdb + " exists? " + db2.exists());
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dirdb + "/db.db", null);
        } catch (Exception e) {
            Log.e("db open error", "" + e.getMessage());
        }
    }

    private static long insert(String tableName, String columnNames, ContentValues values) {
        if (db == null)
            openOrCreate();
        return db.insert(tableName, columnNames, values);

    }

    private static void update(String tableName, ContentValues values, String whereCluase, String[] whereArgs) {
        if (db == null)
            openOrCreate();
        db.update(tableName, values, whereCluase, whereArgs);

    }

    private static void delete(String tableName, String whereCluase, String[] whereArgs) {
        if (db == null)
            openOrCreate();
        db.delete(tableName, whereCluase, whereArgs);

    }

    private static Cursor select(String query) {
        if (db == null)
            openOrCreate();
        return db.rawQuery(query, null);
    }

    private static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static String getColumns(Set<String> strings) {
        String columns = "";
        if (strings != null)
            for (String s : strings)
                columns += s;

        return columns;
    }

//    public static String selectLastModifiedCategoryTime() {
//        String query = "select max(last_modified) from categories";
//        Cursor select = select(query);
//        if (select == null || select.moveToFirst() || select.getCount() == 0) {
//            return "0";
//        }
//        return select.getString(0) == null ? "0" : select.getString(0);
//    }

//    public static void InsertOrUpdateCategory(Category category) {
//        if (!isCategoryAvailable(category.getId()))
//            insertCategory(category);
//        else
//            updateCategories(category);
//    }

//    private static void updateCategories(Category category) {
//        ContentValues values = new ContentValues();
////        values.put("code", category.getId());
//        values.put("parent", category.getParent());
//        values.put("title", category.getTitle());
//        values.put("image_url", category.getImage_name());
//        values.put("last_modified", category.getLast_modified());
//
//        update("categories", values, " id=" + category.getId(), null);
//    }

    public static void insertCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("id", category.getId());
        values.put("parent", category.getParent());
        values.put("title", category.getTitle());
        values.put("image_url", category.getImage_name());
        values.put("last_modified", category.getLast_modified());

        String columns = getColumns(values.keySet());
        insert("categories", columns, values);
    }

//    private static boolean isCategoryAvailable(int code) {
//        Category shop = selectCategory(code);
//        return shop != null;
//    }

    public static Category selectCategory(int id) {
        String query = "select * from categories where id = " + id;
        ArrayList<Category> shops = select_Category(query);
        if (shops != null && shops.size() > 0)
            return shops.get(0);
        return null;
    }

    private static ArrayList<Category> select_Category(String query) {

        Cursor cursor = select(query);
        if (cursor == null)
            return null;
        if (!cursor.moveToFirst() || cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        ArrayList<Category> data = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Category category = new Category();

            category.setId(getInt(cursor, "id"));
            category.setTitle(getString(cursor, "title"));
            category.setParent(getInt(cursor, "parent"));
            category.setLast_modified(getString(cursor, "last_modified"));
            category.setImage_name(getString(cursor, "image_url"));
            data.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    public static boolean InsertOrUpdateBrand(Brand brand) {
        if (!isBrandAvailable(brand.getId())) {
            long l = insertBrand(brand);
            return l != -1;
        } else
            updateBrand(brand);
        return true;
    }

    private static void updateBrand(Brand brand) {
        ContentValues values = new ContentValues();
//        values.put("id", brand.getId());
        values.put("image_url", brand.getImage_name());
        values.put("title", brand.getTitle());

        update("brands", values, "id=" + brand.getId(), null);
    }

    private static long insertBrand(Brand brand) {
        ContentValues values = new ContentValues();
        values.put("id", brand.getId());
        values.put("image_url", brand.getImage_name());
        values.put("title", brand.getTitle());

        String columns = getColumns(values.keySet());
        return insert("brands", columns, values);
    }

    private static boolean isBrandAvailable(int id) {
        Brand ad = selectBrand(id);
        return ad != null;
    }

    private static Brand selectBrand(int id) {
        String query = "select * from brands where id = " + id;
        ArrayList<Brand> ads = select_Brands(query);
        if (ads != null && ads.size() > 0)
            return ads.get(0);
        return null;
    }

    public static String selectLastModifiedBrandTime() {
        return null;
    }

    public static String selectLastModifiedAdTime() {
        return null;
    }

    public static void InsertOrUpdateAd(Advertisement advertisement) {
        if (!isAdAvailable(advertisement.getId()))
            insertAd(advertisement);
        else
            updateAd(advertisement);
    }

    private static void updateAd(Advertisement advertisement) {
        ContentValues values = new ContentValues();
        values.put("image_url", advertisement.getImage_name());
        values.put("last_modified", advertisement.getLast_modified());
        values.put("type", advertisement.getType());
        values.put("shop_id", advertisement.getShop_id());

        update("ads", values, "id=" + advertisement.getId(), null);
    }

    private static void insertAd(Advertisement advertisement) {
        ContentValues values = new ContentValues();
        values.put("id", advertisement.getId());
        values.put("image_url", advertisement.getImage_name());
        values.put("last_modified", advertisement.getLast_modified());
        values.put("type", advertisement.getType());
        values.put("shop_id", advertisement.getShop_id());
        String columns = getColumns(values.keySet());
        insert("ads", columns, values);
    }

    private static boolean isAdAvailable(int id) {
        Advertisement ad = selectAd(id);
        return ad != null;
    }

    private static Advertisement selectAd(int id) {
        String query = "select * from ads where id = " + id;
        ArrayList<Advertisement> ads = select_Ad(query);
        if (ads != null && ads.size() > 0)
            return ads.get(0);
        return null;
    }

    private static ArrayList<Advertisement> select_Ad(String query) {

        Cursor cursor = select(query);
        if (cursor == null)
            return null;
        if (!cursor.moveToFirst() || cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        ArrayList<Advertisement> data = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Advertisement ad = new Advertisement();

            ad.setId(getInt(cursor, "id"));
            ad.setLast_modified(getString(cursor, "last_modified"));
            ad.setImage_name(getString(cursor, "image_url"));
            ad.setType(getInt(cursor, "type"));
            ad.setShop_id(getInt(cursor, "shop_id"));
            data.add(ad);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    public static ArrayList<Advertisement> selectAds() {
        String query = "select * from Ads";
        return select_Ads(query);
    }

    private static ArrayList<Advertisement> select_Ads(String query) {
        Cursor cursor = select(query);
        if (cursor == null)
            return null;
        if (!cursor.moveToFirst() || cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        ArrayList<Advertisement> data = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Advertisement advertisement = new Advertisement();

            advertisement.setId(getInt(cursor, "id"));
            advertisement.setType(getInt(cursor, "type"));
            advertisement.setImage_name(getString(cursor, "image_url"));
            advertisement.setShop_id(getInt(cursor, "shop_id"));
            advertisement.setLast_modified(getString(cursor, "last_modified"));

            data.add(advertisement);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    public static void deleteBrands() {
        delete("brands", "", null);
    }

    public static void deleteCategories() {
        delete("categories", "", null);
    }

    public static void deleteAds() {
        delete("ads", "", null);
    }

    public static ArrayList<Brand> selectBrands() {
        String query = "select * from brands";
        return select_Brands(query);
    }

    private static ArrayList<Brand> select_Brands(String query) {
        Cursor cursor = select(query);
        if (cursor == null)
            return null;
        if (!cursor.moveToFirst() || cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        ArrayList<Brand> data = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Brand brand = new Brand();

            brand.setId(getInt(cursor, "id"));
            brand.setTitle(getString(cursor, "title"));
            brand.setImage_name(getString(cursor, "image_url"));

            data.add(brand);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    public static ArrayList<Category> selectCategories() {
        String query = "select * from categories";
        return select_Category(query);
    }

    public static ArrayList<Category> selectCategories(int parent) {
        String query = "select * from categories where parent=" + parent;
        return select_Category(query);
    }

    public static ArrayList<Product> selectProducts() {
        return null;
    }

    public static ArrayList<Shop> selectShops() {
        String query = "select * from shops";
        Cursor cursor = select(query);
        if (cursor == null || cursor.getCount() < 1 || !cursor.moveToFirst())
            return null;
        ArrayList<Shop> shops = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Shop shop = new Shop();
            shop.setId(getInt(cursor, "id"));
            shop.setTitle(getString(cursor, "Title"));
            shop.setAddress(getString(cursor, "Address"));
            shop.setMedal(getInt(cursor, "Medal"));
            shop.setScore(Float.parseFloat(getString(cursor, "Score")));
            shops.add(shop);

            cursor.moveToNext();
        }
        return shops;
    }

    public static boolean isShopBookmarked(int shopId) {
        String query = "select id from shops where id=" + shopId;
        Cursor select = select(query);
        return !(select == null || select.getCount() < 1 || !select.moveToFirst());
    }

    public static void deleteShop(int shop_id) {
        delete("shops", "id=" + shop_id, null);
    }

    public static ArrayList<String> getImageNames(int shop_id) {
        String query = "select * from shopImages where shop_id=" + shop_id;
        Cursor cursor = select(query);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ArrayList<String> images = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                images.add(getString(cursor, "img_name"));
                cursor.moveToNext();
            }
            return images;
        }
        return null;
    }

    public static void insertImageName(int shop_id, String img_name) {
        ContentValues values = new ContentValues();
        values.put("shop_id", shop_id);
        values.put("img_name", img_name);
        insert("shopImages", getColumns(values.keySet()), values);
    }

    public static void deleteShopImage(int shop_id, String image_name) {
        delete("shopImages", "shop_id=" + shop_id + " and img_name='" + image_name + "'", null);
    }

    public static void insertStaff(Staff staff) {
        ContentValues values = new ContentValues();
        values.put("name", staff.getName());
        values.put("image_name", staff.getImg_name());
        values.put("post", staff.getPost());
        insert("staff", getColumns(values.keySet()), values);
    }

    public static void insertShop(ShopInfo shopInfo, int id) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("RegisterDate", shopInfo.getRegister_date());
        values.put("GuildType", shopInfo.getGuild_type());
        values.put("Title", shopInfo.getName());
        values.put("Address", shopInfo.getAddress());
        values.put("Medal", shopInfo.getMedal());
        values.put("ImgName", shopInfo.getIcon_url());
        values.put("Website", shopInfo.getWebsite());
        values.put("Email", shopInfo.getEmail());
        values.put("ShopTel", shopInfo.getPhone());
        values.put("Score", shopInfo.getScore());
        values.put("VoteCount", shopInfo.getVote_count());
        values.put("AdminName", shopInfo.getAdmin_name());
        values.put("ImgAdmin", shopInfo.getAdmin_image());
        values.put("Services", shopInfo.getService_description());
        values.put("Open", shopInfo.isOpen());
        values.put("LicenseNumber", shopInfo.getLicense_number());

        insert("shops", getColumns(values.keySet()), values);

    }

    public static boolean isProductBookmarked(int id) {
        String query = "select id from products where id=" + id;
        Cursor select = select(query);
        return !(select == null || select.getCount() < 1 || !select.moveToFirst());
    }

    public static void deleteProduct(int id) {
        delete("products", "id=" + id, null);
    }

    public static void insertProduct(
            int id,
            String title,
            String main_image,
            int price,
            int discount) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("title", title);
        values.put("main_image", main_image);
        values.put("price", price);
        values.put("discount", discount);

        insert("products", getColumns(values.keySet()), values);
    }
}
