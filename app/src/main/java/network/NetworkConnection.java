package network;

/**
 * Created by Omid on 5/15/2018.
 */

public class NetworkConnection {
    public static final int MODE_TCP_SOCKET = 0;
    public static final int MODE_HTTP_GET = 1;
    public static final int MODE_HTTP_POST = 2;


    private static int connection_mod = MODE_HTTP_POST;



//    public static void checkVerificationCode(Context context, String code,
//                                             String country_code, String mobile) {
//        switch (connection_mod) {
//            case MODE_TCP_SOCKET:
////                TCPSocketInterface.checkVerificationCode(context, code, country_code, mobile);
//                break;
//            case MODE_HTTP_GET:
//                new ServerInterface.checkVerificationCode(context, code, country_code, mobile, false)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//            case MODE_HTTP_POST:
//                new ServerInterface.checkVerificationCode(context, code, country_code, mobile, true)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//        }
//    }

//    public static void sign_up_completion(Context context, String name, String family,
//                                          String reagent, String image) {
//        switch (connection_mod) {
//            case MODE_TCP_SOCKET:
////                TCPSocketInterface.sign_up_completion(context, name, family, reagent, image);
//                break;
//            case MODE_HTTP_GET:
//                new ServerInterface.sign_up_completion(context, name, family, reagent, image, false)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//            case MODE_HTTP_POST:
//                new ServerInterface.sign_up_completion(context, name, family, reagent, image, true)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//        }
//    }

//    public static void getShopDetails(Context context, int shop_id) {
//        switch (connection_mod) {
//            case MODE_TCP_SOCKET:
////                TCPSocketInterface.getShopDetails(context, shop_id);
//                break;
//            case MODE_HTTP_GET:
//                new ServerInterface.getShopDetails(context, shop_id, false)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//            case MODE_HTTP_POST:
//                new ServerInterface.getShopDetails(context, shop_id, true)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//        }
//    }


//    public static void getShopsOnMap(Context context, LatLng target,
//                                     double distance, int category, int zoom) {
//        switch (connection_mod) {
//            case MODE_TCP_SOCKET:
////                TCPSocketInterface.getShopsOnMap(context, target, distance);
//                break;
//            case MODE_HTTP_GET:
//                new ServerInterface.getShopsOnMap(context, target, distance, category, zoom, false)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//            case MODE_HTTP_POST:
//                new ServerInterface.getShopsOnMap(context, target, distance, category, zoom, true)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                break;
//        }
//    }

//    public static void changeConnectionMode() {
////        if (connection_mod == MODE_TCP_SOCKET) {
////            TCPSocketInterface.disconnect();
////        }
////        connection_mod++;
////        connection_mod %= 3;
//    }

//    public static int getConnection_mod() {
//        return connection_mod;
//    }

    public static void setConnection_mod(int mode) {
        connection_mod = mode;
    }
}
