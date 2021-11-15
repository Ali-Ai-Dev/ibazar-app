package com.tnt.ibazaar;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import database.DataBase;
import models.Category;
import models.ServerResponse;
import network.NetworkConnection;
import network.serverinterface.ServerInterface;
import network.webconnection.WebConnection;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Act_Splash_Screen extends AppCompatActivity {

    public static boolean active;
    private Act_Splash_Screen mAct;

    //    BroadcastReceiver timeOut = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            NetworkConnection.changeConnectionMode();
//            NetworkConnection.getData(mAct);
//        }
//    };

    private SharedPreferences prefs;
    private SharedPreferences.Editor mEditor;
    private boolean
            urls = false, country = false;

    private Handler handler;
    private Runnable r;
    private int h = -100;

    private void fetchBasicDataWhenBuyerAppStart() {

        Uri.Builder builder = new Uri.Builder();
        builder
                .appendQueryParameter("customerId", "" + Application.getCustomerId())
                .appendQueryParameter("userAccountParentId", "" + Application.getUserAccountParentId());
        Log.e("builder", builder.toString());
        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                    if (response.getStatus() == 100) {
                        try {
                            HashMap<String, ArrayList> data = new HashMap<>();
                            JSONObject Data = jsonObject.getJSONObject("Data");
                            JSONArray jsonArray = Data.getJSONArray("Category");
                            String categoriesJsonString = Application.NormalizeString(
                                    Data.getString("Category"));
                            Application.setCategoriesJsonString(categoriesJsonString);
                            String last_category =
                                    Application.NormalizeString(Data.getString("Last_Modified_Category"));
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                                    Application.getContext());
                            SharedPreferences.Editor mEditor = prefs.edit();
                            if (!last_category.equals(prefs.getString("last_category", "0"))) {

                                DataBase.deleteCategories();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    Category category = new Category();
                                    category.setId(jsonObject.getInt("ID"));
                                    category.setTitle(Application.NormalizeString(jsonObject.getString("Name")));
                                    category.setParent(jsonObject.getInt("ParnetID"));
                                    if (jsonObject.has("ImageName"))
                                        category.setImage_name(
                                                Application.NormalizeString(
                                                        jsonObject.getString("ImageName")));
                                    DataBase.insertCategory(category);
                                }

                                mEditor.putString("last_category", last_category);
                                mEditor.commit();
                            }


                            mEditor.putString("Upload_IP", Application.NormalizeString(Data.getString("Upload_IP")));
                            mEditor.putString("Download_IP", Application.NormalizeString(Data.getString("Download_Ip")));
                            mEditor.putInt("Upload_Port", Data.getInt("Upload_Port"));
                            mEditor.putInt("Download_Port", Data.getInt("Download_Port"));
//                mEditor.putString("Upload_Folder", Data.getString("Upload_Folder"));
                            mEditor.putString("Download_Folder_Customers", Application.NormalizeString(Data.getString("Download_Folder_Customers")));
                            mEditor.putString("Download_Folder_Ad", Application.NormalizeString(Data.getString("Download_Folder_Ad")));
                            mEditor.putString("Download_Folder_Category", Application.NormalizeString(Data.getString("Download_Folder_Category")));
                            mEditor.putString("Download_Folder_Brand", Application.NormalizeString(Data.getString("Download_Folder_Brand")));
                            mEditor.putString("Download_Folder_Product", Application.NormalizeString(Data.getString("Download_Folder_Product")));
                            mEditor.putString("Download_Folder_Motto", Application.NormalizeString(Data.getString("Download_Folder_Motto")));
                            mEditor.putString("Download_Folder_Shop", Application.NormalizeString(Data.getString("Download_Folder_Shop")));
                            mEditor.putString("Download_Folder_Seller", Application.NormalizeString(Data.getString("Download_Folder_Seller")));
                            Application.setCountPaymentCartOrderItems(Data.getInt("countPaymentCartOrderItems"));
                            if (Data.has("Profile")) {
                                JSONObject profile = Data.getJSONObject("Profile");
                                if (profile.has("Name"))
                                    mEditor.putString(Application.getCustomerId() + "name",
                                            Application.NormalizeString(profile.getString("Name")));
                                if (profile.has("Family"))
                                    mEditor.putString(Application.getCustomerId() + "family",
                                            Application.NormalizeString(profile.getString("Family")));
                                if (profile.has("ReagentID"))
                                    mEditor.putString(Application.getCustomerId() + "reagent",
                                            Application.NormalizeString(profile.getString("ReagentID")));
                                if (profile.has("Image"))
                                    mEditor.putString(Application.getCustomerId() + "image",
                                            Application.NormalizeString(profile.getString("Image")));
                            }
                            mEditor.commit();

                            String adsJsonString = Application.NormalizeString(Data.getString("AD"));
                            Application.setAdsJsonString(adsJsonString);

                            String brandsJsonString = Application.NormalizeString(Data.getString("Brand"));
                            Application.setBrandsJsonString(brandsJsonString);

                            Application.setCountPaymentCartOrderItems(
                                    Data.getInt("countPaymentCartOrderItems"));

                            mEditor.putString("motto", Application.NormalizeString(Data.getString("Motto")));
                            mEditor.commit();
                            Log.e("end of", "data");
                            response.setData(data);
                        } catch (JSONException e) {
                            Log.e("error", " " + e.getMessage());
                        }
                    }
                } catch (Exception e) {

                }
                if (response == null) {
//                new CheckInternet().execute();
//                    Toast.makeText(mAct, R.string.not_connected_to_internet, Toast.LENGTH_SHORT).show();
                } else {
                    if (urls && country) {
                        handler = new Handler();
                        r = new Runnable() {
                            @Override
                            public void run() {
                                if (h != -100) {
                                    Intent i = new Intent(mAct, Act_Main.class);

                                    i.putExtra("height", h);
                                    startActivity(i);
                                    finish();
                                } else handler.postDelayed(r, 100);
                            }
                        };

                        handler.postDelayed(r, 100);
                    }
                }
            }
        }).connect(builder, "fetchBasicDataWhenBuyerAppStart", "GET", 0);


    }

    private void getURLs() {
        LinearLayout no_net_layout = (LinearLayout) findViewById(R.id.no_net_layout);
        no_net_layout.setVisibility(View.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
//        NetworkConnection.getUrls();

        Uri.Builder builder = new Uri.Builder();

        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {

                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                    if (response.getStatus() == 100) {
                        JSONObject Data = jsonObject.getJSONObject("Data");
                        JSONArray jsonArray = Data.getJSONArray("UrlBase");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
                        SharedPreferences.Editor mEditor = prefs.edit();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            String UrlBase = jsonObject.getString("UrlBase");
                            mEditor.putString("UrlBase" + i, UrlBase);
                        }
                        mEditor.putInt("UrlCount", Data.getInt("UrlCount"));
                        mEditor.putString("TcpDomain", Application.NormalizeString(Data.getString("TcpDomain")));
                        mEditor.putString("HttpDomain", Application.NormalizeString(Data.getString("HttpDomain")));
                        mEditor.putInt("TcpPort", Data.getInt("TcpPort"));
                        mEditor.putInt("HttpPort", Data.getInt("HttpPort"));
                        mEditor.putString("Protocol", Application.NormalizeString(Data.getString("Protocol")));
                        if (Application.NormalizeString(Data.getString("Protocol")).equalsIgnoreCase("tcp"))
                            NetworkConnection.setConnection_mod(NetworkConnection.MODE_TCP_SOCKET);
                        else if (Application.NormalizeString(Data.getString("HttpCommand"))
                                .equalsIgnoreCase("get"))
                            NetworkConnection.setConnection_mod(NetworkConnection.MODE_HTTP_GET);
                        else if (Application.NormalizeString(Data.getString("HttpCommand"))
                                .equalsIgnoreCase("post"))
                            NetworkConnection.setConnection_mod(NetworkConnection.MODE_HTTP_POST);
                        mEditor.commit();
                    }
                } catch (Exception e) {
                    Log.e("error get url", " " + e.getMessage());
                }
                if (response == null) {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                    progressBar.setVisibility(View.GONE);

                    LinearLayout no_net_layout = (LinearLayout) findViewById(R.id.no_net_layout);
                    no_net_layout.setVisibility(View.VISIBLE);
                    TextView txt_retry = (TextView) findViewById(R.id.txt_retry);
                    txt_retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getURLs();
                        }
                    });
                } else {
                    urls = true;
                    go();
                }
            }
        }).connect(builder, "fetchUrlBase", "GET", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
//        registerReceiver(mReceiver, new IntentFilter(
//                EnumsAndStatics.BROADCAST_FILTER_Act_Splash_Screen));
//        registerReceiver(disconnect, new IntentFilter(EnumsAndStatics.BROADCAST_FILTER_All));
//        registerReceiver(timeOut, new IntentFilter("timeOut"));
    }


    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__splash__screen);
        ButterKnife.bind(this);
        mAct = this;

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
        mEditor = prefs.edit();

        try {
            boolean exit = getIntent().getExtras().getBoolean("exit");

            if (exit) {
                active = false;

                finish();
                return;
            }
        } catch (Exception e) {
            Log.e("error exiting", "" + e.getMessage());
        }

        final View view = findViewById(R.id.item);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                h = view.getHeight();
            }
        });

        if (checkPermissions()) {
            copyDB();
//            new CheckInternet().execute();
            fetchBasicDataWhenBuyerAppStart();
            getURLs();
            new getCountry().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else
            requestPermissions(mAct, 96);

    }

    private void requestPermissions(Activity mAct, int reqCode) {
        if (Build.VERSION.SDK_INT < 23)
            return;
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

//        String[] neededPermissions=new String[];
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct,
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        if (neededPermissions.size() == 0)
            return;

        String[] strings = new String[neededPermissions.size()];
        for (int i = 0; i < neededPermissions.size(); i++) {
            strings[i] = neededPermissions.get(i);
        }
        ActivityCompat.requestPermissions(mAct, strings, reqCode);

//        if (ContextCompat.checkSelfPermission(mAct,
//                permission)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(mAct,
//                    permission)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(mAct,
//                        new String[]{permission}, reqCode);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // categories of the request.
//            }
//        }
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

//        String[] neededPermissions=new String[];
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct,
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        if (neededPermissions.size() == 0)
            return true;
//        else {
//            for (String s : neededPermissions) {
//                Log.e("neededPermissions", "" + s);
//            }
//        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 96) {
            if (!checkPermissions()) {
                Toast.makeText(mAct, R.string.app_needs_permission_to_save_app_data, Toast.LENGTH_LONG).show();
                requestPermissions(mAct, 96);
            } else {
                copyDB();
//                new CheckInternet().execute();
//                new getData().execute();
                getURLs();
                new getCountry().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private void copyDB() {
        if (!prefs.getBoolean("dbcopied2", false)) {
            String path =
                    Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/android/data/" + getPackageName();

            Log.e("path", path);
            try {
                copyFromAssets("db.db", path);
                mEditor.putBoolean("dbcopied2", true);
                mEditor.commit();
            } catch (IOException e) {
                mEditor.putBoolean("dbcopied2", false);
                mEditor.commit();
                Log.e("error copy", " " + e.getMessage());
            }
        }
        Log.e("copyDB", "end");
    }

    private void copyFromAssets(String dbName, String dbPath) throws IOException {

        InputStream myInput = mAct.getAssets().open(dbName);
        File databasePath = new File(dbPath);
        if (!databasePath.exists()) {
            databasePath.mkdirs();
            databasePath.setWritable(true);
            databasePath.setReadable(true);
        }
        String outFileName = dbPath + "/" + dbName;
        Log.e("x", "x");
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void go() {
        if (country && urls & active)
            fetchBasicDataWhenBuyerAppStart();
    }

    private class getCountry extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ServerInterface.getCountryCode(mAct);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            country = true;
            go();
        }
    }
}
