package com.tnt.ibazaar;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.ServerResponse;
import network.webconnection.WebConnection;
import tools.ImageFilePath;

public class Act_Edit_Profile extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_photo)
    CircularImageView img_photo;
    @BindView(R.id.txt_birthdate)
    TextView txt_birthdate;
    @BindView(R.id.btn_save_edit)
    Button btn_save_edit;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_family)
    EditText et_family;
    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;
    @BindView(R.id.et_reagent_code)
    EditText et_reagent_code;

    private Act_Edit_Profile mAct;
    private Bitmap photo;
    private File sdImageMainDirectory;
    private Uri mImageCaptureUri;
    private Uri mImageCropedUri;
    private boolean image_changed = false;
    private android.app.AlertDialog alertDialog;
    private String mobile;
    private String name;
    private String family;
    private String reagent;
    private String image_name;
    private Dialog mDialog;

    private ProgressView progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(
                getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__edit__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;
        ButterKnife.bind(mAct);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);


        mobile = Application.NormalizeString(
                prefs.getString(Application.getCustomerId() + "mobile", ""));

        name = Application.NormalizeString(
                prefs.getString(Application.getCustomerId() + "name", ""));

        family = Application.NormalizeString(
                prefs.getString(Application.getCustomerId() + "family", ""));

        reagent = Application.NormalizeString(
                prefs.getString(Application.getCustomerId() + "reagent", ""));

        et_name.setText(name);
        et_family.setText(family);
        et_reagent_code.setText(reagent);

        image_name = prefs.getString(Application.getCustomerId() + "image", "");
        String image = prefs.getString("Download_IP", "") + ":" +
                prefs.getInt("Download_Port", 6000) + "/" +
                prefs.getString("Download_Folder_Customers", "") + "/" + image_name;
        if (!image_name.isEmpty())
            Picasso.with(mAct)
                    .load(image)
                    .placeholder(R.drawable.placeholder)
                    .into(img_photo);
        else img_photo.setImageResource(R.drawable.contactoutline);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        captureImageInitialization();
        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissions()) {
                    // Checking camera availability
                    if (!isDeviceSupportCamera()) {
                        Toast.makeText(getApplicationContext(),
                                "Sorry! Your device doesn't support camera",
                                Toast.LENGTH_LONG).show();
                        // will close the app if the device does't have camera
                        return;
                    }
                    alertDialog.show();
                } else
                    requestPermissions(mAct, 96);
            }


        });

        btn_save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                family = et_family.getText().toString();
                reagent = et_reagent_code.getText().toString();
                new UploadImage().execute();

            }
        });
    }

    private void captureImageInitialization() {


        final String[] items = new String[]{getString(R.string.camera),
                getString(R.string.gallery)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.select_dialog_item, items);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.select_image));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {
                    /**
                     * To take a photo from camera, pass intent action
                     * �MediaStore.ACTION_IMAGE_CAPTURE� to open the camera app.
                     */
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    /**
                     * Also specify the Uri to save the image on specified path
                     * and file name. Note that this Uri variable also used by
                     * gallery app to hold the selected image path.
                     */
                    final File root = new File(Environment.getExternalStorageDirectory() +
//                            File.separator + "android/data/" + getPackageName() + File.separator);
                            File.separator + getString(R.string.app_name) + File.separator);
                    root.mkdirs();

                    String id = Application.getCustomerId();

//                    final String fname = "img_" + System.currentTimeMillis() + ".jpg";
                    final String fname = id + "_" + System.currentTimeMillis() + ".jpg";
                    sdImageMainDirectory = new File(root, fname);
                    mImageCaptureUri = Uri.fromFile(sdImageMainDirectory);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    // pick from file
                    /**
                     * To select an image from existing files, use
                     * Intent.createChooser to open image chooser. Android will
                     * automatically display a list of supported applications,
                     * such as image gallery or file manager.
                     */
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        alertDialog = builder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.e("code", requestCode + "");
//        if (resultCode != RESULT_OK) {
//            return;
//        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                /**
                 * After taking a picture, do the crop
                 */
                doCrop();
                break;

            case PICK_FROM_FILE:
                /**
                 * After selecting image from files, save the selected path
                 */
                if (data != null)
                    mImageCaptureUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                image_changed = true;
                try {
                    Bundle extras = data.getExtras();
                    ImageView img_photo = (ImageView) findViewById(R.id.img_photo);
                    if (extras != null) {
                        photo = extras.getParcelable("data");

                    } else {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inDither = true;
                        Log.e("cropped path", mImageCaptureUri.getPath());
                        String path;

                        if (mImageCaptureUri.getPath().contains("content://"))
                            path = ImageFilePath.getPath(mAct, mImageCaptureUri);
                        else
                            path = mImageCaptureUri.getPath();

                        photo = BitmapFactory.decodeFile(path, options);
                    }
                    img_photo.setImageBitmap(photo);
                } catch (Exception e) {
                    Log.e("errorr", " " + e.getMessage());
                }
                break;
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                image_changed = true;
                Uri resultUri = result.getUri();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;
                Log.e("cropped path", mImageCaptureUri.getPath());
                String path;

//                if (mImageCaptureUri.getPath().contains("content://"))
                path = ImageFilePath.getPath(mAct, mImageCaptureUri);
//                else
//                    path = mImageCaptureUri.getPath();

                photo = BitmapFactory.decodeFile(path, options);
                ImageView img_photo = (ImageView) findViewById(R.id.img_photo);
                img_photo.setImageBitmap(photo);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("crop error", " " + error.getMessage());
            }
        }
    }

    private void doCrop() {
//        final ArrayList<CropOption> cropOptions = new ArrayList<>();
////        String path = mImageCaptureUri.getPath();
        if (mImageCaptureUri == null)
            return;
        String path = ImageFilePath.getPath(getApplicationContext(), mImageCaptureUri);
//
        if (!path.contains(getString(R.string.app_name))) {
            final File root = new File(Environment.getExternalStorageDirectory() +
                    File.separator + getString(R.string.app_name) + File.separator);
            root.mkdirs();
            final String fname = Application.getCustomerId() + "_" + System.currentTimeMillis() + ".jpg";
            sdImageMainDirectory = new File(root, fname);
            mImageCropedUri = Uri.fromFile(sdImageMainDirectory);
        } else
            mImageCropedUri = mImageCaptureUri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setType("image/*");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropedUri);
//        /**
//         * Check if there is image cropper app installed.
//         */
//        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
//                intent, 0);
//        int size = list.size();
//        /**
//         * If there is no image cropper app, display warning message
//         */
//        if (size == 0) {
//
//            Toast.makeText(this, R.string.no_croping_app,
//                    Toast.LENGTH_LONG).show();
//            return;
//        } else {
//
//            mImageCaptureUri = Uri.parse(path);
//            intent.setDataAndType(mImageCaptureUri, "image/*");
//
//            intent.putExtra("crop", "true");
//
//            intent.putExtra("outputX", 200);
//            intent.putExtra("outputY", 200);
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("scale", true);
//            intent.putExtra("scaleUpIfNeeded", true);
//            intent.putExtra("return-data", true);
//
//            if (size == 1) {
//                Intent i = new Intent(intent);
//                ResolveInfo res = list.get(0);
//
//                i.setComponent(new ComponentName(res.activityInfo.packageName,
//                        res.activityInfo.name));
//
//                startActivityForResult(i, CROP_FROM_CAMERA);
//            } else {
//
//                for (ResolveInfo res : list) {
//                    final CropOption co = new CropOption();
//
//                    co.title = getPackageManager().getApplicationLabel(
//                            res.activityInfo.applicationInfo);
//                    co.icon = getPackageManager().getApplicationIcon(
//                            res.activityInfo.applicationInfo);
//                    co.appIntent = new Intent(intent);
//
//                    co.appIntent
//                            .setComponent(new ComponentName(
//                                    res.activityInfo.packageName,
//                                    res.activityInfo.name));
//
//                    cropOptions.add(co);
//                }
//
//                CropOptionAdapter adapter = new CropOptionAdapter(
//                        getApplicationContext(), cropOptions);
//
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//                builder.setTitle(getString(R.string.select_crop_app));
//                builder.setAdapter(adapter,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface mDialog, int item) {
//                                startActivityForResult(
//                                        cropOptions.get(item).appIntent,
//                                        CROP_FROM_CAMERA);
//                            }
//                        });
//
//                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface mDialog) {
//
//                        if (mImageCaptureUri != null) {
//                            getContentResolver().delete(mImageCaptureUri, null,
//                                    null);
//                            mImageCaptureUri = null;
//                        }
//                    }
//                });
//
//                android.app.AlertDialog alert = builder.create();
//
//                alert.show();
//            }
//            Log.e("doCrop path", path);
//        }

//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAllowFlipping(false)
//                .setAllowRotation(false)
//                .start(this);
// start cropping activity for pre-acquired image saved on the device

//grantUriPermission();
        try {
            CropImage.activity(mImageCaptureUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(false)
                    .setAllowRotation(false)
                    .setAspectRatio(1, 1)
                    .setAutoZoomEnabled(true)
                    .setRequestedSize(200, 200)
                    .setOutputUri(mImageCropedUri)
                    .start(this);
        } catch (Exception e) {
            Log.e("crop error", "" + e.getMessage());
        }
// for fragment (DO NOT use `getActivity()`)
//        CropImage.activity()
//                .start(mAct);
    }

    private void requestPermissions(Activity mAct, int reqCode) {
        if (Build.VERSION.SDK_INT < 23)
            return;
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

//        String[] neededPermissions=new String[];
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct, perm)
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
                Manifest.permission.CAMERA
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

    private boolean isDeviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 96) {
            if (!checkPermissions()) {
//                requestPermissions(mAct, 96);

            } else {
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
                    // will close the app if the device does't have camera
                    return;
                }
                alertDialog.show();
            }
        }
    }

    private void sign_up_completion(String name, String family, String reagent, final String image_name) {

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("customerId", "" + Application.getCustomerId())
                .appendQueryParameter("customerName", name)
                .appendQueryParameter("customerFamily", family);
        if (reagent != null && !reagent.isEmpty()) {
            builder.appendQueryParameter("customerReagent", reagent);
        }
        if (image_name != null && !image_name.isEmpty()) {
            builder.appendQueryParameter("customerImage", image_name);
        }

        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                } catch (Exception e) {
                    Log.e("signUpCompletion error", " " + e.getMessage());
                }
                if (response == null) {
                    Toast.makeText(mAct, R.string.not_connected_check_internet, Toast.LENGTH_SHORT).show();
                    return;
                }
                dismissDialog();
                if (response.getStatus() == 100) {
                    SharedPreferences prefs =
                            PreferenceManager.getDefaultSharedPreferences(mAct);
                    SharedPreferences.Editor mEditor = prefs.edit();
                    mEditor.putString(Application.getCustomerId() + "mobile", mobile);

                    if (!et_name.getText().toString().isEmpty())
                        mEditor.putString(Application.getCustomerId() + "name",
                                et_name.getText().toString());

                    if (!et_family.getText().toString().isEmpty())
                        mEditor.putString(Application.getCustomerId() + "family",
                                et_family.getText().toString());

                    if (!et_reagent_code.getText().toString().isEmpty())
                        mEditor.putString(Application.getCustomerId() + "reagent",
                                et_reagent_code.getText().toString());

                    if (image_changed)
                        mEditor.putString(Application.getCustomerId() + "image", image_name);

                    mEditor.commit();
//                startActivity(new Intent(mAct, Act_Main.class));
                    finish();
                } else {
                    Toast.makeText(mAct, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).connect(builder, "editProfile", "POST",0);
    }

    private void showDialog() {
        mDialog = new Dialog(mAct);
        View view = getLayoutInflater().inflate(R.layout.dialog_please_wait_download,
                null);
        progress = (ProgressView) view.findViewById(R.id.progress);

//            progress.startAnimation(AnimationUtils.loadAnimation(mAct, R.anim.rotate));
        progress.setProgress(0);

        mDialog.setContentView(view);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    dialog.dismiss();
                }
                return false;
            }
        });
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private class UploadImage extends AsyncTask<Void, Float, String> {

        private String res;


        private UploadImage() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
            if (image_changed) {
                res = uploadFile();
            } else {
                res = "200";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (res != null && res.contains("200")) {
                sign_up_completion(
                        name, family, reagent, image_name);
            } else {
                dismissDialog();

                Snackbar snackbar = Snackbar.make(getCurrentFocus(),
                        getString(R.string.error_connecting_to_server), Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UploadImage().execute();
                    }
                });
                snackbar.getView().setBackgroundResource(R.color.colorPrimaryDark);
                snackbar.show();
            }
        }

        private String uploadFile() {
            HttpURLConnection conn;
            DataOutputStream dos;
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024;
            final File sourceFile = new File(ImageFilePath.getPath(mAct, mImageCropedUri));

            image_name = sourceFile.getName();
            Log.e("img_name", image_name);
            String serverResponseMessage;
            String response = null;
            if (!sourceFile.isFile()) {
                dismissDialog();
                return "no picture";
            } else {
                try {
                    FileInputStream fileInputStream =
                            new FileInputStream(sourceFile.getPath());
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
                    String upload_url = prefs.getString("Upload_IP", "");
                    int upload_port = prefs.getInt("Upload_Port", 6000);
                    URL url = new URL(upload_url + ":" + upload_port);

                    conn = (HttpURLConnection) url.openConnection();
                    int size = (int) sourceFile.length();
                    conn.setFixedLengthStreamingMode(size);
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("image", image_name);

                    dos = new DataOutputStream(conn.getOutputStream());
//                    dos.writeBytes(twoHyphens + boundary + lineEnd);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"" + "image" + "\";filename="
//                            + sourceFile.getName() + lineEnd);
//                    dos.writeBytes(lineEnd);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    long total_bytes = 0;
                    Log.e("start", "" + size);
                    do {
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        dos.write(buffer, 0, bufferSize);
                        total_bytes += bytesRead;
                        float f1 = total_bytes;
                        float f2 = size;
                        float f3 = f1 / f2;

                        publishProgress(f3);
                    } while (bytesRead > 0);

//                    dos.writeBytes(lineEnd);
//                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    int serverResponseCode = conn.getResponseCode();
                    serverResponseMessage = conn.getResponseMessage();
                    response = "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode;
                    Log.e("uploadFile", response);

                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (MalformedURLException ex) {
                    Log.e("MalformedURLException", "" + ex.getMessage());
                } catch (IOException e) {
                    Log.e("Upload file Exception", "Exception : "
                            + e.getMessage(), e);
                }
            }
            return response;
        }
    }
}
