package network.webconnection;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Ali_Ai on 5/20/2018.
 */

public class WebConnection {
    //    private boolean isLocalUrls = true;
//    private ArrayList<String> localOrServerUrls = new ArrayList<>();
    public static ArrayList<String> savedUrlsAtSP;

/*    public static String[] localUrls = new String[]{
            "http://185.237.86.74/ibazar/",
            "http://185.237.86.74/ibazar/",
            "http://185.237.86.74/ibazar/",
            "http://185.237.86.74/ibazar/",
            "http://185.237.86.74/ibazar/"};*/

    private final String webservicebuyers = "webservicebuyers/";
    private final String WebserviceSeller = "WebserviceSellers/";
    private final String WebservicePayment = "WebservicePayment/";

/*    String[] localUrls = new String[]{
            "http://192.168.1.16/ibazar/",
            "http://192.168.1.16/ibazar/",
            "http://192.168.1.16/ibazar/",
            "http://192.168.1.16/ibazar/"};*/

    String[] localUrls = new String[]{
            "http://ibazar.alibozorgzadarbab.ir/",
            "http://ibazar.alibozorgzadarbab.ir/",
            "http://ibazar.alibozorgzadarbab.ir/",
            "http://ibazar.alibozorgzadarbab.ir/"};

    private ConnectionResponse connectionResponse = null;


    public WebConnection(ConnectionResponse connectionResponse) {
        this.connectionResponse = connectionResponse;
    }

    public void connect(Uri.Builder builder, String api_method, String httpMethod, int isFromSeller) {

        //for now i'm using static urls for server connection

        savedUrlsAtSP = new ArrayList<>(Arrays.asList(localUrls));
        callPostAndGetConnection(0, httpMethod, isFromSeller, builder, api_method);
    }

    private void callPostAndGetConnection(
            final int index, final String method,
            final int isFromSeller,
            final Uri.Builder builderSend,
            final String api_method) {
        String webservice;
        if (isFromSeller == 0) {
            webservice = webservicebuyers;
        } else if (isFromSeller == 1)
            webservice = WebserviceSeller;
        else
            webservice = WebservicePayment;

        new WebHttpConnection(savedUrlsAtSP.get(index) + webservice + api_method,
                method, builderSend) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (result != null) {
                    int maxLogSize = 1000;
                    for (int i = 0; i <= result.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > result.length() ? result.length() : end;
                        Log.e("result " + api_method, result.substring(start, end));
                    }
                    connectionResponse.connectionFinish(result,
                            savedUrlsAtSP.get(index) + api_method);
                } else {
                    callPostConnectionAgain(index, isFromSeller, builderSend, api_method, method);
                }
            }
        }.execute();
    }

    private void callPostConnectionAgain(int index, int isFromSeller, Uri.Builder builderSend, String api_method, String method) {
        int tempIndex = index;
        if (++tempIndex != savedUrlsAtSP.size()) {
            callPostAndGetConnection(tempIndex, method, isFromSeller, builderSend, api_method);
        } else {
            // call Get or TCP
            //callPostAndGetConnection(0, "GET", builderSend, api_method);
            connectionResponse.connectionFinish(null, null);
        }
    }

    // you may separate this or combined to caller class.
    public interface ConnectionResponse {
        void connectionFinish(String result, String urlCanConnect);
    }


//    void checkVersionAndUpdateUrl(Context context) {
//        if (isLocalUrls) {
//            Collections.addAll(localOrServerUrls, localUrls);
//        } else {
//            Collections.addAll(localOrServerUrls, serverUrls);
//        }
//
//        // Get SharedPreferences And Editor
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        spEditor = sharedPreferences.edit();
//
//        String currentVersionNumber = sharedPreferences.getString(CURRENT_VERSION_NUMBER, "");
//        if (!currentVersionNumber.equals(BuildConfig.VERSION_NAME)) {
//            spEditor.putString(CURRENT_VERSION_NUMBER, BuildConfig.VERSION_NAME);
//            spEditor.apply();
//
//            getUrlsFromServer(localOrServerUrls);
//            fillSavedUrlsAtSP();
//        } else {
////            fillSavedUrlsAtSP();
////            getUrlsFromServer(savedUrlsAtSP);
//
//            /* just for test (delete later)*/
//            /*int size = savedUrlsAtSP.size();
//            for(int i = 0 ; i < size; i++){
//                savedUrlsAtSP.remove(0);
//            }*/
////            savedUrlsAtSP.add(httpDomain+":"+httpPort);
//            /* test end */
//
//            getUrlsFromServer(localOrServerUrls);
//        }
//    }
//
//    private void fillSavedUrlsAtSP() {
//        savedUrlsAtSP = new ArrayList<>();
//
//        String superUrl = sharedPreferences.getString(SUPER_URL, "");
//        String urlArray = sharedPreferences.getString(URLS_ARRAY, "");
//        savedUrlsAtSP = convertUrlsFromJsonToArrayList(urlArray);
//        savedUrlsAtSP.add(0, superUrl);
//    }

//    private void getUrlsFromServer(ArrayList<String> urlsCanGetOtherUrls) {
//        Uri.Builder builder = new Uri.Builder()
//                .appendQueryParameter("id", String.valueOf(100))
//                .appendQueryParameter("seller_id", String.valueOf(Application.getSellerId()));
//
//        new WebConnection(new ConnectionResponse() {
//            @Override
//            public void connectionFinish(String result, String urlCanConnect) {
//                if (result != null) {
//                    try {
//                        JSONObject object = new JSONObject(result);
//
//                        JSONObject objectData = object.getJSONObject("Data");
//                        JSONArray arrayUrls = objectData.getJSONArray("UrlBase");
//
//                        // save the urls as json
//                        spEditor.putString(URLS_ARRAY, arrayUrls.toString());
//                        spEditor.putString(SUPER_URL, urlCanConnect);
//                        spEditor.apply();
//
//                        //* just for test (delete later)*//
//                        httpDomain = objectData.getString("HttpDomain");
//                        httpPort = objectData.getString("HttpPort");
//                        //* test end *//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).connect(builder, urlsCanGetOtherUrls);
//    }


//    private ArrayList<String> convertUrlsFromJsonToArrayList(String urlArray) {
//        ArrayList<String> urls = new ArrayList<>();
//
//        try {
//            JSONArray jsonArray = new JSONArray(urlArray);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject object = jsonArray.getJSONObject(i);
//                urls.add(object.getString("UrlBase"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return urls;
//    }

//    private String codeBase64(String text) {
//        byte[] data = new byte[0];
//        try {
//            data = text.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return Base64.encodeToString(data, Base64.NO_WRAP);
//    }
//
//    private String decodeBase64(String base64) {
//        byte[] data = Base64.decode(base64, Base64.NO_WRAP);
//        String decode = null;
//        try {
//            decode = new String(data, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        return decode;
//    }
}
