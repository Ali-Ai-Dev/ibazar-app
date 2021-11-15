package network.serverinterface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tnt.ibazaar.Application;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import models.ServerResponse;
import network.NetworkConnection;
import network.webservice.WebService;



public class ServerInterface {
    private static final String url_Geocode = "http://maps.googleapis.com/" +
            "maps/api/geocode/json?latlng=";
    private static final String URL_FREE_GEO_IP = "http://freegeoip.net/json/";
    private static final String URL_IP_INFO = "http://ipinfo.io/json";

    public static String getAddress(Context context, double lat, double lng) {
        String result;
        String url = url_Geocode + lat + "," + lng + "&language=fa";
        try {

            result = WebService.readUrl(url, context);
            Log.e("geocode result", " " + result);
            if (result == null)
                return null;

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            JSONArray address_components =
                    new JSONObject(jsonArray.get(1).toString())
                            .getJSONArray("address_components");
//            Log.e("long_name",
//                    ((JSONObject) address_components.get(0)).getString("long_name") +
//                            ((JSONObject) address_components.get(1)).getString("long_name"));

            return ((JSONObject) address_components.get(0)).getString("long_name") + " " +
                    ((JSONObject) new JSONObject(jsonArray.get(0).toString())
                            .getJSONArray("address_components").get(0)).getString("long_name");

        } catch (Exception e) {
            Log.e("ERROR getAddress", " " + e.getMessage());
            return null;
        }

    }

    public static ServerResponse getCountryCode(Context context) {
        String result;

        ServerResponse response;
        try {
            result = WebService.readUrl(URL_FREE_GEO_IP, context);
            Log.e("getCountryCode", " " + result);

            if (result == null) {
                result = WebService.readUrl(URL_IP_INFO, context);
                if (result == null)
                    return null;
            }

            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("country_code"))
                Application.setCountryCode(
                        Application.NormalizeString(
                                jsonObject.getString("country_code")));
            else if (jsonObject.has("country"))
                Application.setCountryCode(
                        Application.NormalizeString(
                                jsonObject.getString("country")));
            response = new ServerResponse();
            response.setStatus(100);
            return response;
        } catch (Exception e) {
            Log.e("getCountryCode error", " " + e.getMessage());
            return new ServerResponse();
        }
    }

//    private static ServerResponse signUp(
//            Context context, String mobile, String country_code,
//            boolean post, String url) {
//        String result;
//        ServerResponse response = new ServerResponse();
//        try {
//            String p = "customer_id=" + Application.getCustomerId() +
//                    "&id=11" +
//                    "&mobile=" + mobile +
//                    "&country_code=" + country_code;
//
//
//            result = request(context, p, post, url);
//            Log.e("signUp", " " + result);
//
//            if (result == null)
//                return null;
//
//            JSONObject jsonObject = new JSONObject(result);
//            response.setStatus(jsonObject.getInt("Status"));
//            response.setMessage(jsonObject.getString("MSG"));
////            if (response.getStatus() == 100) {
////                try {
////
////                } catch (Exception e) {
////                    Log.e("error", " " + e.getMessage());
////                }
////            }
//        } catch (Exception e) {
//            Log.e("signUp error", " " + e.getMessage());
//            return new ServerResponse();
//        }
//        return response;
//    }

//    private static ServerResponse checkVerificationCode(
//            Context context, String code,
//            String country_code, String mobile, boolean post, String url) {
//        String result;
//        ServerResponse response = new ServerResponse();
//        try {
//            String p = "customer_id=" + Application.getCustomerId() +
//                    "&id=12" +
//                    "&mobile=" + mobile +
//                    "&verify_code=" + code +
//                    "&country_code=" + country_code;
//
//            result = request(context, p, post, url);
//
//            Log.e("checkVerificationCode", " " + result);
//
//            if (result == null)
//                return null;
//
//            JSONObject jsonObject = new JSONObject(result);
//            response.setStatus(jsonObject.getInt("Status"));
//            response.setMessage(jsonObject.getString("MSG"));
//            if (response.getStatus() == 100)
//                try {
//                    JSONObject data = jsonObject.getJSONObject("Data");
//                    Application.setCustomerId(data.getString("CustomerId"));
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Application.getContext());
//                    SharedPreferences.Editor mEditor = prefs.edit();
//                    String customer_id = data.getString("CustomerId");
//                    mEditor.putString("customer_id", customer_id);
//
//                    String name = null;
//                    if (data.has("Name"))
//                        name = Application.NormalizeString(data.getString("Name"));
//                    String family = null;
//                    if (data.has("Family"))
//                        family = Application.NormalizeString(data.getString("Family"));
//                    String reagent = null;
//                    if (data.has("ReagentID"))
//                        reagent = Application.NormalizeString(data.getString("ReagentID"));
//                    String image = null;
//                    if (data.has("Image"))
//                        image = Application.NormalizeString(data.getString("Image"));
//
//                    mEditor.putString(customer_id + "name", "");
//
//                    mEditor.commit();
//                    Intent i = new Intent(BROADCAST_FILTER_Act_Verify);
//                    i.putExtra("verifyCodeResponse", response);
//                    if (name != null)
//                        i.putExtra("name", name);
//                    if (family != null)
//                        i.putExtra("family", family);
//                    if (reagent != null)
//                        i.putExtra("reagent", reagent);
//                    if (image != null)
//                        i.putExtra("image", image);
//                    Application.getContext().sendBroadcast(i);
//                } catch (Exception e) {
//                    Log.e("error case 12", " " + e.getMessage());
//                }
//        } catch (Exception e) {
//            Log.e("VerificationCode error", " " + e.getMessage());
//            return new ServerResponse();
//        }
//        return response;
//    }

//    private static ServerResponse sign_up_completion(
//            Context context, String name, String family,
//            String reagent, String image, boolean post, String url) {
//        String result;
//        ServerResponse response = new ServerResponse();
//        try {
//            String p = "customer_id=" + Application.getCustomerId() +
//                    "&id=13";
//            if (!name.isEmpty())
//                p += "&name=" + name;
//            if (!family.isEmpty())
//                p += "&family=" + family;
//            if (reagent != null && !reagent.isEmpty())
//                p += "&reagent=" + reagent;
//            if (image != null && !image.isEmpty())
//                p += "&image=" + image;
//
//            result = request(context, p, post, url);
//
//            Log.e("sign_up_completion", " " + result);
//
//            if (result == null)
//                return null;
//
//            JSONObject jsonObject = new JSONObject(result);
//            response.setStatus(jsonObject.getInt("Status"));
//            response.setMessage(jsonObject.getString("MSG"));
//
//        } catch (Exception e) {
//            Log.e("signUpCompletion error", " " + e.getMessage());
//            return new ServerResponse();
//        }
//        return response;
//    }

//    private static ServerResponse getShopDetails(
//            Context context, int shop_id, boolean post, String url) {
//        String result;
//        ServerResponse response = new ServerResponse();
//        try {
//            String p = "customer_id=" + Application.getCustomerId() +
//                    "&id=14" +
//                    "&shop_id=" + shop_id;
//
//            result = request(context, p, post, url);
//            Log.e("getShopDetails", " " + result);
//
//            if (result == null)
//                return null;
//
//            JSONObject jsonObject = new JSONObject(result);
//            response.setStatus(jsonObject.getInt("Status"));
//            response.setMessage(jsonObject.getString("MSG"));
//            if (response.getStatus() == 100) {
//                response.setData(jsonObject.getString("Data"));
//            }
//        } catch (Exception e) {
//            Log.e("getShopDetails error", " " + e.getMessage());
//            return new ServerResponse();
//        }
//        return response;
//    }

//    private static ServerResponse getShopsOnMap(
//            Context context, LatLng target, double distance,
//            int category, int zoom, boolean post, String url) {
//        String result;
//        ServerResponse response = new ServerResponse();
//        try {
//            String p = "customer_id=" + Application.getCustomerId() +
//                    "&id=15" +
//                    "&categoryId=" + category +
//                    "&lat=" + target.latitude +
//                    "&lng=" + target.longitude +
//                    "&zoom=" + zoom +
//                    "&distance=" + distance;
//
//            result = request(context, p, post, url);
//            Log.e("getShopsOnMap", " " + result);
//
//            if (result == null)
//                return null;
//
//            JSONObject jsonObject = new JSONObject(result);
//            response.setStatus(jsonObject.getInt("Status"));
//            response.setMessage(jsonObject.getString("MSG"));
//            if (response.getStatus() == 100) {
//                response.setData(jsonObject.getString("Data"));
//            } else
//                response.setData("");
//        } catch (Exception e) {
//            Log.e("getShopsOnMap error", " " + e.getMessage());
//            return new ServerResponse();
//        }
//        return response;
//    }

//    private static String request(Context context, String p, boolean post, String url) {
//
//        String result = null;
//        try {
//
////            byte[] msgBytes = Base64.encodeBase64(p.getBytes());
////            p = new String(msgBytes, "UTF-8").trim();
//            Log.e("url", url);
//            Log.e("P", "" + p);
//            byte[] msgBytes = Base64.encodeBase64(p.getBytes());
//            p = new String(msgBytes, "UTF-8").trim();
//            if (post) {
//                ArrayList<NameValuePair> params = new ArrayList<>();
//                params.add(new BasicNameValuePair("params", "" + p));
//
//                result = WebService.readUrl(url, params, context);
//            } else {
//                url += "?params=" + p;
//                result = WebService.readUrl(url, context);
//            }
//            if (result == null) {
//                NetworkConnection.changeConnectionMode();
//                return null;
//            }
//            if (result.isEmpty()) {
//                NetworkConnection.changeConnectionMode();
//                return null;
//            }
//            Log.e("request result", " " + result);
//            byte[] decode = Base64.decodeBase64(result.getBytes());
//            result = new String(decode, "UTF-8");
//
//        } catch (Exception e) {
//        }
//        return result;
//    }

//    public static class checkVerificationCode extends AsyncTask<Void, Void, Void> {
//
//        private ServerResponse response;
//
//        private Context context;
//        private String code;
//        private String country_code;
//        private String mobile;
//        private boolean post;
//
//        public checkVerificationCode(
//                Context context, String code,
//                String country_code, String mobile, boolean post) {
//            this.context = context;
//            this.code = code;
//            this.country_code = country_code;
//            this.mobile = mobile;
//            this.post = post;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            response = checkVerificationCode(
//                    context, code, country_code, mobile, post,
//                    prefs.getString("HttpDomain", "")
////                            + ":" + prefs.getInt("HttpPort", 80)
//            );
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (response == null)
//                NetworkConnection.checkVerificationCode(context, code, country_code, mobile);
//        }
//    }

//    public static class sign_up_completion extends AsyncTask<Void, Void, Void> {
//
//        private ServerResponse response;
//
//        private Context context;
//        private String name;
//        private String family;
//        private String reagent;
//        private String image;
//        private boolean post;
//
//        public sign_up_completion(
//                Context context, String name, String family,
//                String reagent, String image, boolean post) {
//            this.context = context;
//            this.name = name;
//            this.family = family;
//            this.reagent = reagent;
//            this.image = image;
//            this.post = post;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            response = sign_up_completion(context, name, family, reagent, image, post,
//                    prefs.getString("HttpDomain", "")
////                            + ":" + prefs.getInt("HttpPort", 80)
//            );
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (response == null) {
//                NetworkConnection.sign_up_completion(context, name, family, reagent, image);
//            } else {
////                Intent i = new Intent(BROADCAST_FILTER_Act_Sign_Up_Completion);
////                i.putExtra("CompletionResponse", response);
////                Application.getContext().sendBroadcast(i);
//            }
//        }
//    }

//    public static class getShopDetails extends AsyncTask<Void, Void, Void> {
//
//        private ServerResponse response;
//
//        private Context context;
//        private int shop_id;
//        private boolean post;
//
//        public getShopDetails(Context context, int shop_id, boolean post) {
//            this.context = context;
//            this.shop_id = shop_id;
//            this.post = post;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            response = getShopDetails(context, shop_id, post,
//                    prefs.getString("HttpDomain", "")
////                            + ":" + prefs.getInt("HttpPort", 80)
//            );
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (response == null) {
//                NetworkConnection.getShopDetails(context, shop_id);
//            } else {
////                Intent i = new Intent(BROADCAST_FILTER_Act_Shop);
////                if (response.getStatus() == 100) {
////                    String responseShopDetails = (String) response.getData();
////                    i.putExtra("responseShopDetails", responseShopDetails);
////                }
////                Application.getContext().sendBroadcast(i);
//            }
//        }
//    }

//    public static class getShopsOnMap extends AsyncTask<Void, Void, Void> {
//
//        private ServerResponse response;
//
//        private Context context;
//        private LatLng target;
//        private double distance;
//        private int category, zoom;
//        private boolean post;
//
//        public getShopsOnMap(Context context, LatLng target,
//                             double distance, int category, int zoom, boolean post) {
//            this.context = context;
//            this.target = target;
//            this.distance = distance;
//            this.category = category;
//            this.post = post;
//            this.zoom = zoom;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            response = getShopsOnMap(context, target, distance, category, zoom, post,
//                    prefs.getString("HttpDomain", "")
////                            + ":" + prefs.getInt("HttpPort", 80)
//            );
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (response == null) {
//                NetworkConnection.getShopsOnMap(context, target, distance, category, zoom);
//            } else {
////                Intent i = new Intent(BROADCAST_FILTER_Frg_Location);
////                String data = (String) response.getData();
////                i.putExtra("responseLocation", data);
////                Application.getContext().sendBroadcast(i);
//            }
//        }
//    }
}
