package network.webconnection;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CookieManager;

import com.tnt.ibazaar.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/*
 * Created by Ali_Dev on 7/9/2017.
 */

class WebHttpConnection extends AsyncTask<String, Integer, String> {
    private URL url = null;
    private Uri.Builder builder = null;
    private String method;
    private String cookieUrl;

    WebHttpConnection(String urlConnect, String sendMethod, Uri.Builder builderConnect) {
        try {
            cookieUrl = urlConnect;
            url = new URL(urlConnect);
            method = sendMethod;

            if (builderConnect != null) {
                builder = builderConnect;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... sUrl) {
        try {

            if (builder != null && method.equals("GET")) {
                url = new URL(url.toString() + builder.toString());
            }
            Log.e("url", url.toString());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(1300);
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoInput(true);

            if (builder != null && method.equals("POST")) {
                httpURLConnection.setDoOutput(true);
                String query = builder.build().getEncodedQuery();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                outputStream.close();
            }

            //// WE use cookie for transferring same session between user and server for when a user not signIn yet(For example for paymentCart)
            //-- Check the sharedPreferences for set previous session or not --//
            // get session form cookie
            // Fetch and set cookies in requests
            CookieManager cookieManager = CookieManager.getInstance();
            if (!Application.isLog_out()) {
                String cookie = cookieManager.getCookie(cookieUrl);
                if (cookie != null) {
                    httpURLConnection.setRequestProperty("Cookie", cookie);
                }
            } else {
                Application.setLog_out(false);
            }

            httpURLConnection.connect();

            // set session form cookie
            // Get cookies from responses and save into the cookie manager
            List cookieList = httpURLConnection.getHeaderFields().get("Set-Cookie");
            if (cookieList != null) {
                for (Object cookieTemp : cookieList) {
                    cookieManager.setCookie(cookieUrl, (String) cookieTemp);
                }
            }

            InputStream inputStream = httpURLConnection.getInputStream();
            return convertInputStreamToString(inputStream);
        } catch (IOException e) {
            Log.e("error method", " " + e.getMessage());
        }

        return null;
    }


    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}

