package network.webservice;


import android.content.Context;
import android.util.Log;


import com.tnt.ibazaar.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class WebService {
    public static String readUrl(String url, ArrayList<NameValuePair> params, Context context) {
        HttpClient client = null;
        HttpPost method = null;
        InputStream inputStream = null;
        String result = null;
        HttpResponse response = null;
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    context.getResources().getInteger(R.integer.serverConnectionTimeout));
            HttpConnectionParams.setSoTimeout(httpParams,
                    context.getResources().getInteger(R.integer.serverConnectionTimeout));

            client = new DefaultHttpClient(httpParams);
            method = new HttpPost(url);

            if (params != null) {
                method.setEntity(new UrlEncodedFormEntity(params));
            }
            response = client.execute(method);
            inputStream = response.getEntity().getContent();
            result = convertInputStreamToString(inputStream);
        } catch (ClientProtocolException ex) {
            Log.e("ClientProtocolException", "" + ex.getMessage());
            ex.printStackTrace();

        } catch (IOException ex) {
            Log.e("IOException", "" + ex.getMessage());
        } finally {
            try {
                if (client != null) {
                    client.getConnectionManager().closeExpiredConnections();
                }
            } catch (Exception e) {
                Log.e("IOException2", "" + e.getMessage());
            }
        }
        return result;
    }

    public static String readUrl(String url, Context context) {
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    context.getResources().getInteger(R.integer.serverConnectionTimeout));
            HttpConnectionParams.setSoTimeout(httpParams,
                    context.getResources().getInteger(R.integer.serverConnectionTimeout));
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpGet method = new HttpGet(url);


            HttpResponse response = client.execute(method);

            InputStream inputStream = response.getEntity().getContent();
            String result = convertInputStreamToString(inputStream);

            return result;
        } catch (ClientProtocolException ex) {

            ex.printStackTrace();
        } catch (IOException ex) {

        }

        return null;
    }

    private static String convertInputStreamToString(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (IOException ex) {
            Log.e("InputStreamToString", "" + ex.getMessage());
        }

        return null;
    }

}
