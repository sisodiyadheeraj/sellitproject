package app.reatailx.sellitapp.Activites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.reatailx.sellitapp.R;

public class AndroidCameraExample extends Activity {
    private KProgressHUD kProgressHUD;
    public String jsonString;
    private Camera mCamera;
    private CameraPreview mPreview;
    private PictureCallback mPicture;
    private Button capture, switchCamera;
    private AndroidCameraExample myContext;
    private LinearLayout cameraPreview;
    public String mobilepicturePath1;
    public Bitmap decodedByte;
    private boolean cameraFront = false;
    public ProgressBar progressBar;
    public String mobileimagebmp = "", lead_id, ajentassign = "", flag, imagebmp, ajentid, flagdata, lead_pick_date;
    public String vendorid, agentid, role, model_name, imageurl, price, location, lead_pick_time, bank_name, accountnumber, ifsccode, beneficiary_name, upiid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidcameraexample);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (shouldAskPermissions()) {
            askPermissions();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        final Bundle intent = getIntent().getExtras();
        if (intent != null) {
            lead_pick_date = intent.get("lead_pick_date").toString();
            lead_id = intent.get("lead_id").toString();
            ajentassign = intent.get("ajentassign").toString();
            vendorid = intent.get("vendorid").toString();
            model_name = intent.get("model_name").toString();
            imageurl = intent.get("imageurl").toString();
            price = intent.get("price").toString();
            location = intent.get("location").toString();
            lead_pick_time = intent.get("lead_pick_time").toString();
            bank_name = intent.get("bank_name").toString();
            accountnumber = intent.get("accountnumber").toString();
            ifsccode = intent.get("ifsccode").toString();
            beneficiary_name = intent.get("beneficiary_name").toString();
            upiid = intent.get("upiid").toString();
            flagdata = intent.get("flagdata").toString();
            System.out.println("getting_lead_id " + lead_id + "::::::::" + ajentassign + "  ::::::::: " + lead_pick_time);
            System.out.println("getting_lead_id " + lead_id + "::::::::" + ajentassign + ":::::::::" + flagdata + "  " + imageurl);
        }
        initialize();
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {
            //if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findFrontFacingCamera());
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {
        progressBar = (ProgressBar) findViewById(R.id.progressBarPostpicklistdatadaikin);

        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        capture = (Button) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);

        switchCamera = (Button) findViewById(R.id.button_ChangeCamera);
        switchCamera.setOnClickListener(switchCameraListener);

        openfrontcamera();
    }

    public void openfrontcamera() {
        releaseCamera();
        chooseCamera();
    }

    OnClickListener switchCameraListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mPreview.refreshCamera(mCamera);
        }
    };

    public void chooseCamera() {
        if (cameraFront) {

        }
        int cameraId = findFrontFacingCamera();
        System.out.println("getting_cameraId_else  " + cameraId);
        if (cameraId == 0) {
            mCamera = Camera.open(cameraId);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
        }
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.CAMERA",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        //check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private PictureCallback getPictureCallback() {
        //Toast.makeText(myContext, "Picture saved: " + "getPictureCallback", Toast.LENGTH_LONG).show();
        PictureCallback picture = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //make a new picture file
                System.out.println("getting_data " + data.toString());

                //Bitmap bitmap = BitmapFactory.decodeFile("/path/images/image.jpg");
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                // bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
                //data = blob.toByteArray();
                decodedByte = BitmapFactory.decodeByteArray(data, 0, data.length);
                System.out.println("getting_bitmapdata  " + decodedByte);
                mobilepicturePath1 = String.valueOf(decodedByte);

                if (ajentassign.equals("")) {
                    ajentassign = "11";
                    System.out.println("ajent_idhere::::::::::::::" + ajentassign);
                }
                //progressDialog();
                progressBar.setVisibility(View.VISIBLE);
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://sellit.co.in/logisticapi/v1/ajantselfie.php",
                        //VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://postman-echo.com/post",
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                //dismissDialog();
                                progressBar.setVisibility(View.GONE);
                                try {
                                    jsonString = new String(response.data, "UTF-8");
                                    System.out.println("getting_data_from_server" + jsonString);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (jsonString == null) {
                                    //dismissDialog();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    System.out.println("getting_response" + jsonString);
                                    //dismissDialog();
                                    progressBar.setVisibility(View.GONE);
                                    try {
                                        JSONObject jsonObject = new JSONObject(jsonString);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");
                                        if ((status.equals("1"))) {
                                            //dismissDialog();
                                            if (flagdata.equalsIgnoreCase("clead")) {
                                                Intent intent = new Intent(AndroidCameraExample.this, CompleteleadItemdescriptionActivity.class);
                                                intent.putExtra("lead_id", lead_id);
                                                intent.putExtra("ajentassign", ajentassign);
                                                //intent.putExtra("imagebmp", imagebmp);
                                                intent.putExtra("lead_id", lead_id);
                                                //intent.putExtra("ajentassign", ajentid);
                                                intent.putExtra("model_name", model_name);
                                                intent.putExtra("imageurl", imageurl);
                                                intent.putExtra("price", price);
                                                intent.putExtra("location", location);
                                                intent.putExtra("lead_pick_date", lead_pick_date);
                                                intent.putExtra("lead_pick_time", lead_pick_time);
                                                intent.putExtra("bank_name", bank_name);
                                                intent.putExtra("accountnumber", accountnumber);
                                                intent.putExtra("ifsccode", ifsccode);
                                                intent.putExtra("beneficiary_name", beneficiary_name);
                                                intent.putExtra("upiid", upiid);
                                                intent.putExtra("flagdata", flagdata);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(AndroidCameraExample.this, InprogressItemdescriptionActivity.class);
                                                intent.putExtra("flag", "inprogress");
                                                intent.putExtra("lead_id", lead_id);
                                                intent.putExtra("ajentassign", ajentassign);
                                                //intent.putExtra("imagebmp", imagebmp);
                                                intent.putExtra("lead_id", lead_id);
                                                //intent.putExtra("ajentassign", ajentid);
                                                intent.putExtra("model_name", model_name);
                                                intent.putExtra("imageurl", imageurl);
                                                intent.putExtra("price", price);
                                                intent.putExtra("location", location);
                                                intent.putExtra("lead_pick_time", lead_pick_time);
                                                intent.putExtra("bank_name", bank_name);
                                                intent.putExtra("accountnumber", accountnumber);
                                                intent.putExtra("ifsccode", ifsccode);
                                                intent.putExtra("beneficiary_name", beneficiary_name);
                                                intent.putExtra("upiid", upiid);
                                                intent.putExtra("flagdata", flagdata);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            // dismissDialog();
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Please Try again !", Toast.LENGTH_SHORT).show();
                                //dismissDialog();
                                progressBar.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>(); // candidatename
                        params.put("vendorid", vendorid);
                        params.put("lead_id", lead_id);
                        params.put("ajent_id", ajentassign);
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        //long imagename = System.currentTimeMillis();
                        params.put("selfie", new DataPart(mobilepicturePath1 + ".png", getFileDataFromDrawable(decodedByte)));
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        return params;
                    }
                };
                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleySingleton.getInstance(getApplicationContext()).
                        addToRequestQueue(volleyMultipartRequest);

                //refresh camera to continue preview
                mPreview.refreshCamera(mCamera);


             /*   File pictureFile = getOutputMediaFile();

                if (pictureFile == null) {
                    return;
                }
                try {
                    //write the file
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    decodedByte = BitmapFactory.decodeFile(pictureFile.getPath());
                    System.out.println("getting_imagebmp " + decodedByte.toString());
                    mobilepicturePath1 = String.valueOf(decodedByte);
                    System.out.println("getting_mobilepicturePath1 " + mobilepicturePath1);
                    if (ajentassign.equals("")) {
                        ajentassign = "11";
                        System.out.println("ajent_idhere::::::::::::::" + ajentassign);
                    }
                    //progressDialog();
                    progressBar.setVisibility(View.VISIBLE);
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://sellit.co.in/logisticapi/v1/ajantselfie.php",
                            //VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://postman-echo.com/post",
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    //dismissDialog();
                                    progressBar.setVisibility(View.GONE);
                                    try {
                                        jsonString = new String(response.data, "UTF-8");
                                        System.out.println("getting_data_from_server" + jsonString);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (jsonString == null) {
                                        //dismissDialog();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        System.out.println("getting_response" + jsonString);
                                        //dismissDialog();
                                        progressBar.setVisibility(View.GONE);
                                        try {
                                            JSONObject jsonObject = new JSONObject(jsonString);
                                            String status = jsonObject.getString("status");
                                            String message = jsonObject.getString("message");
                                            if ((status.equals("1"))) {
                                                //dismissDialog();
                                                if (flagdata.equalsIgnoreCase("clead")) {
                                                    Intent intent = new Intent(AndroidCameraExample.this, CompleteleadItemdescriptionActivity.class);
                                                    intent.putExtra("lead_id", lead_id);
                                                    intent.putExtra("ajentassign", ajentassign);
                                                    //intent.putExtra("imagebmp", imagebmp);
                                                    intent.putExtra("lead_id", lead_id);
                                                    //intent.putExtra("ajentassign", ajentid);
                                                    intent.putExtra("model_name", model_name);
                                                    intent.putExtra("imageurl", imageurl);
                                                    intent.putExtra("price", price);
                                                    intent.putExtra("location", location);
                                                    intent.putExtra("lead_pick_time", lead_pick_time);
                                                    intent.putExtra("bank_name", bank_name);
                                                    intent.putExtra("accountnumber", accountnumber);
                                                    intent.putExtra("ifsccode", ifsccode);
                                                    intent.putExtra("beneficiary_name", beneficiary_name);
                                                    intent.putExtra("upiid", upiid);
                                                    intent.putExtra("flagdata", flagdata);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Intent intent = new Intent(AndroidCameraExample.this, InprogressItemdescriptionActivity.class);
                                                    intent.putExtra("flag", "inprogress");
                                                    intent.putExtra("lead_id", lead_id);
                                                    intent.putExtra("ajentassign", ajentassign);
                                                    //intent.putExtra("imagebmp", imagebmp);
                                                    intent.putExtra("lead_id", lead_id);
                                                    //intent.putExtra("ajentassign", ajentid);
                                                    intent.putExtra("model_name", model_name);
                                                    intent.putExtra("imageurl", imageurl);
                                                    intent.putExtra("price", price);
                                                    intent.putExtra("location", location);
                                                    intent.putExtra("lead_pick_time", lead_pick_time);
                                                    intent.putExtra("bank_name", bank_name);
                                                    intent.putExtra("accountnumber", accountnumber);
                                                    intent.putExtra("ifsccode", ifsccode);
                                                    intent.putExtra("beneficiary_name", beneficiary_name);
                                                    intent.putExtra("upiid", upiid);
                                                    intent.putExtra("flagdata", flagdata);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } else {
                                                // dismissDialog();
                                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Please Try again !", Toast.LENGTH_SHORT).show();
                                    //dismissDialog();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>(); // candidatename
                            params.put("vendorid", vendorid);
                            params.put("lead_id", lead_id);
                            params.put("ajent_id", ajentassign);
                            return params;
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            //long imagename = System.currentTimeMillis();
                            params.put("selfie", new DataPart(mobilepicturePath1 + ".png", getFileDataFromDrawable(decodedByte)));
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            return params;
                        }
                    };
                    volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                            60000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance(getApplicationContext()).
                            addToRequestQueue(volleyMultipartRequest);

                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }

                //refresh camera to continue preview
                mPreview.refreshCamera(mCamera);*/
            }
        };
        return picture;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        System.out.println("getting_bitmapdatahere   " + bitmap.toString());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void progressDialog() {
        kProgressHUD = new KProgressHUD(AndroidCameraExample.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    private void dismissDialog() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    OnClickListener captrureListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mCamera.takePicture(null, null, mPicture);
            //mCamera.stopPreview();
        }
    };

    //make picture and save to a folder
    private static File getOutputMediaFile() {
        //make a new file directory inside the "sdcard" folder
        File mediaStorageDir = new File("/sdcard/", "JCG Camera");

        //if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
            //if you cannot make this folder return
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        //take the current timeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //and make a media file:
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}