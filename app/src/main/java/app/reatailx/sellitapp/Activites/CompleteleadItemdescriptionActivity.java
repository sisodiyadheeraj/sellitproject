package app.reatailx.sellitapp.Activites;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mukesh.OtpView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import app.reatailx.sellitapp.Models.Inprogressmodel;
import app.reatailx.sellitapp.R;
import app.reatailx.sellitapp.SharePrefrence.SessionManager;

public class CompleteleadItemdescriptionActivity extends AppCompatActivity implements View.OnClickListener {
    public RelativeLayout back_des;
    public SessionManager session;
    public String vendorid, agentid, lead_pick_date, role, lead_id, ajentassign = "", model_name, imageurl, price, location, lead_pick_time, bank_name, accountnumber, ifsccode, beneficiary_name, upiid;
    private KProgressHUD kProgressHUD;
    public LinearLayout linearLayout;
    public LinearLayout iv_captureone, iv_capturetwo, iv_capturethree, iv_capturefour, iv_capturefiveadharcard, iv_capturefiveadharcardback;
    public ImageView civ_mobileone, civ_mobiletwo, civ_mobilethree, civ_mobilefour, civ_mobilefiveadharcard, civ_mobilefiveadharcardback;
    public File file1, file2, file3, file4;
    public Uri outPutfileUri1, outPutfileUri2, outPutfileUri3, outPutfileUri4;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 = 100;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE2 = 101;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE3 = 102;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE4 = 103;
    public static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY0NzUxMTQzNiwianRpIjoiMTkxOWY3NjktNWUzYy00Mjc4LWE5ODYtMjY3OGJiOTllZjZlIiwidHlwZSI6ImFjY2VzcyIsImlkZW50aXR5IjoiZGV2LnNlbGxpdEBhYWRoYWFyYXBpLmlvIiwibmJmIjoxNjQ3NTExNDM2LCJleHAiOjE5NjI4NzE0MzYsInVzZXJfY2xhaW1zIjp7InNjb3BlcyI6WyJyZWFkIl19fQ.BLOZEPaEW8ZSUGmnllQLoF8O0EEj6ZNkfaWNf_4_LKs";

    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE4ADHARCARD = 104;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE4ADHARCARDBACK = 105;

    public Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap4adharcard, bitmap4adharcardback, bmp;

    private Bitmap bitmapmobilephoto1, bitmapmobilephoto2, bitmapmobilephoto3, bitmapmobilephoto4, bitmapmobilephoto4back;
    public String mobilepicturePath1, mobilepicturePath2, mobilepicturePath3, mobilepicturePath4, mobilepicturePath5, mobilepicturePath4adharcrad, mobilepicturePath4adharcradback;
    public ImageView image_descld;
    public TextView tv_productname_descld, tv_pickup_categorycld, tv_pickup_conditioncld, tv_phone_price_descld, tv_pickup_locationcld, tv_pickup_timedateyearcld, tv_beneficiaryname_descld, tv_accountnumber_descld, tv_ifsccode_descld, tv_bankname_descld, tv_upiid_cld, tv_payableamountcld;
    public Button btn_completedcld, btn_addextraamount;
    public EditText et_addextraamount_cld, et_enterimeinumber;
    public String jsonString, otpno;
    public String extraamount = "", imeinumber, flag, client_id;
    public CardView cv_beneficiarydetail, cv_upiiddetail;
    public TextView tv_amountcldone, tv_extraamountcldone;
    public PopupWindow pwindoselfie;
    public EditText et_cardNumberEditText;
    public Button btn_verifyadharcard, btn_verifyadharcardotp;
    public OtpView pinview;
    public CardView cv_verifyotpno;
    public String sellerhouseno, sellercity, sellerstate, sellerlandmark;
    public String vendoraddress, vendorcity;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completelead_item_list_detail);
        session = new SessionManager(getApplicationContext());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (shouldAskPermissions()) {
            askPermissions();
        }

        try {
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserdata();
            vendorid = user.get(SessionManager.KEY_VERDORID);
            agentid = user.get(SessionManager.KEY_AGENTID);
            role = user.get(SessionManager.KEY_ROLE);
            System.out.println("User_Session_Data_Vendorid:::::" + vendorid + "  " + agentid + "  " + role);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Bundle intent = getIntent().getExtras();
        if (intent != null) {

          /*  intent.putExtra("lead_id", lead_id);
            intent.putExtra("ajentassign", ajentassign);
            intent.putExtra("imagebmp", imagebmp);
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
            intent.putExtra("flagdata", flagdata);*/

            /*flag = intent.get("flag").toString();
            System.out.println("flag_datahere " + flag); //clead*/
            lead_pick_date = intent.get("lead_pick_date").toString();
            lead_id = intent.get("lead_id").toString();
            //ajentassign = intent.get("ajentassign").toString(); // old
            ajentassign = intent.get("ajentassign").toString();
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
            System.out.println("getting_lead_id " + lead_id + "::::::::" + ajentassign + "  ::::::::: " + lead_pick_time + "    " + imageurl);

           /* Intent intent1 = getIntent();
            Bitmap bitmap = (Bitmap) intent1.getParcelableExtra("imagebmp");*/
           /* Intent intent = getIntent();
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra("Image");*/
            // System.out.println("bitmapgetting_bmpfilename" + String.valueOf(bitmap));//android.graphics.Bitmap@a07fb0c

            // New code execution

           /* String filename = getIntent().getStringExtra("imagebmp");
            System.out.println("bitmapgetting_bmpfilename" + filename);//android.graphics.Bitmap@a07fb0c
            bmp = StringToBitMap(filename);
            Bitmap bmphere = BitmapFactory.decodeFile(filename);
            System.out.println("bitmapgetting_bmpfilename" + String.valueOf(bmp) + ":::::::::::::::::");//android.graphics.Bitmap@a07fb0c
            System.out.println("bitmapgetting_bmpfilenamebmp" + String.valueOf(bmphere) + ":::::::::::::::::");//android.graphics.Bitmap@a07fb0c*/

           /* FileInputStream is = null;
            try {
                is = this.openFileInput(filename);
                bmp = BitmapFactory.decodeStream(is);
                mobilepicturePath5 = String.valueOf(bmp);
                System.out.println("bitmapgetting_bmpfilename" + String.valueOf(bmp) + ":::::::::::::::::" + mobilepicturePath5);//android.graphics.Bitmap@a07fb0c
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }*/

         /*   try {
                FileInputStream is = this.openFileInput(filename);
                bmp = BitmapFactory.decodeStream(is);
                System.out.println("bitmapgetting_bmpfilename" + String.valueOf(bmp));//android.graphics.Bitmap@a07fb0c
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            /*String filename = getIntent().getStringExtra("imagebmp");
            System.out.println("bitmapgetting_bmpfilename" + filename);//android.graphics.Bitmap@a07fb0c*/
            /*Bitmap b = StringToBitMap(filename);
            System.out.println("bitmapgetting_bmp" + String.valueOf(b));//android.graphics.Bitmap@a07fb0c*/

          /*  byte[] byteArray = getIntent().getByteArrayExtra("imagebmp");
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            String bitmapdata= String.valueOf(bmp);
            System.out.println("bitmapgetting_bmp" + bitmapdata);*/

            /*try {
                byte[] encodeByte = Base64.decode(filename, Base64.DEFAULT);

                InputStream inputStream = new ByteArrayInputStream(encodeByte);
                bmp = BitmapFactory.decodeStream(inputStream);
                System.out.println("bitmapgetting_bmp" + String.valueOf(bmp));//android.graphics.Bitmap@a07fb0c
                *//*FileInputStream is = this.openFileInput(filename);
                bmp = BitmapFactory.decodeStream(is);
                System.out.println("bitmapgetting_bmp" + bmp.toString());
                is.close();*//*
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        et_cardNumberEditText = findViewById(R.id.et_cardNumberEditText);//aadhar card
        btn_verifyadharcard = findViewById(R.id.btn_verifyadharcard);//aadhar card submit
        pinview = findViewById(R.id.pinview);//aadhar card submit
        btn_verifyadharcardotp = findViewById(R.id.btn_verifyadharcardotp);//aadhar card submit
        cv_verifyotpno = findViewById(R.id.cv_verifyotpno);//aadhar card submit

        image_descld = findViewById(R.id.image_descld);
        cv_beneficiarydetail = findViewById(R.id.cv_beneficiarydetail);
        cv_upiiddetail = findViewById(R.id.cv_upiiddetail);

        iv_captureone = findViewById(R.id.iv_captureone);
        iv_capturetwo = findViewById(R.id.iv_capturetwo);
        iv_capturethree = findViewById(R.id.iv_capturethree);
        iv_capturefour = findViewById(R.id.iv_capturefour);

        iv_capturefiveadharcard = findViewById(R.id.iv_captureadharcardimage);
        iv_capturefiveadharcardback = findViewById(R.id.iv_captureadharcardimageback);

        civ_mobileone = findViewById(R.id.civ_mobileone);
        civ_mobiletwo = findViewById(R.id.civ_mobiletwo);
        civ_mobilethree = findViewById(R.id.civ_mobilethree);
        civ_mobilefour = findViewById(R.id.civ_mobilefour);

        civ_mobilefiveadharcard = findViewById(R.id.civ_adharcardimage);
        civ_mobilefiveadharcardback = findViewById(R.id.civ_adharcardimageback);

        tv_productname_descld = findViewById(R.id.tv_productname_descld);
        tv_pickup_categorycld = findViewById(R.id.tv_pickup_categorycld);
        tv_pickup_conditioncld = findViewById(R.id.tv_pickup_conditioncld);
        tv_phone_price_descld = findViewById(R.id.tv_phone_price_descld);
        tv_pickup_locationcld = findViewById(R.id.tv_pickup_locationcld);
        tv_pickup_timedateyearcld = findViewById(R.id.tv_pickup_timedateyearcld);
        tv_beneficiaryname_descld = findViewById(R.id.tv_beneficiaryname_descld);
        tv_accountnumber_descld = findViewById(R.id.tv_accountnumber_descld);
        tv_ifsccode_descld = findViewById(R.id.tv_ifsccode_descld);

        tv_bankname_descld = findViewById(R.id.tv_bankname_descld);
        tv_upiid_cld = findViewById(R.id.tv_upiid_cld);
        tv_payableamountcld = findViewById(R.id.tv_payableamountcld);
        btn_addextraamount = findViewById(R.id.btn_addextraamount);
        btn_completedcld = findViewById(R.id.btn_completedcld);
        et_addextraamount_cld = findViewById(R.id.et_addextraamount_cld);
        et_enterimeinumber = findViewById(R.id.et_enterimeinumber);

        tv_amountcldone = findViewById(R.id.tv_amountcldone);
        tv_extraamountcldone = findViewById(R.id.tv_extraamountcldone);

        tv_amountcldone.setText(price);
        tv_productname_descld.setText("");
        //image_descld.setImageBitmap(bmp);
        Picasso.with(getApplicationContext())
                .load(imageurl)
                .resize(300, 300)
                .into(image_descld);

        tv_productname_descld.setText(model_name);
        tv_pickup_categorycld.setText("Mobile");
        tv_pickup_conditioncld.setText("NA");
        tv_phone_price_descld.setText("Rs. " + price);
        tv_pickup_locationcld.setText(location);
        tv_pickup_timedateyearcld.setText(lead_pick_time);
        tv_beneficiaryname_descld.setText(beneficiary_name);
      /*  tv_accountnumber_descld.setText(accountnumber);
        tv_ifsccode_descld.setText(ifsccode);*/

        String accountnumberlastFourDigits = accountnumber.substring(accountnumber.length() - 4);
        tv_accountnumber_descld.setText("*********" + accountnumberlastFourDigits);
        String ifsccodelastFourDigits = ifsccode.substring(ifsccode.length() - 4);
        tv_ifsccode_descld.setText("*****" + ifsccodelastFourDigits);

        tv_bankname_descld.setText(bank_name);
        if (upiid == null || upiid.equals("null") || upiid.isEmpty()) {//000000000000000000
            tv_upiid_cld.setText("");
            cv_upiiddetail.setVisibility(View.GONE);
            cv_beneficiarydetail.setVisibility(View.VISIBLE);
        } else {
            tv_upiid_cld.setText(upiid);
            cv_upiiddetail.setVisibility(View.VISIBLE);
            cv_beneficiarydetail.setVisibility(View.GONE);
        }

        tv_payableamountcld.setText("Rs. " + price);

        back_des = findViewById(R.id.back_descld);
        back_des.setOnClickListener(this);
        btn_completedcld.setOnClickListener(this);
        btn_verifyadharcard.setOnClickListener(this);
        btn_verifyadharcardotp.setOnClickListener(this);

        et_addextraamount_cld.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                btn_addextraamount.setEnabled(true);
                btn_addextraamount.setBackgroundColor(getResources().getColor(R.color.dim_blue));
                return false;
            }
        });

        btn_addextraamount.setOnClickListener(new View.OnClickListener() {//111111111111111111111111111111111111111
            @Override
            public void onClick(View v) {

                btn_addextraamount.setEnabled(false);
                btn_addextraamount.setBackgroundColor(getResources().getColor(R.color.dim_foreground_dark));

                btn_addextraamount.setText("ADD");
                btn_addextraamount.setTextColor(Color.BLACK);

                if (et_addextraamount_cld.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter extra amount !!", Toast.LENGTH_SHORT).show();
                } else {
                    btn_addextraamount.setEnabled(false);
                    btn_addextraamount.setBackgroundColor(getResources().getColor(R.color.dim_foreground_dark));
                    extraamount = et_addextraamount_cld.getText().toString();
                    tv_extraamountcldone.setText(extraamount);
                    int sum = Integer.parseInt(price) + Integer.parseInt(extraamount);
                    System.out.println("getting_amountsumdata " + sum);
                    tv_payableamountcld.setText(String.valueOf(sum));
                }
            }
        });

        /* if (isNetworkAvailable()) {
            progressDialog();
            return;
        } else {
            dismissDialog();
            Toast.makeText(CompleteleadItemdescriptionActivity.this, getResources().getString(R.string.connecttointenet), Toast.LENGTH_LONG).show();
        }*/

        iv_captureone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE1);
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file1 = new File(Environment.getExternalStorageDirectory(), "MyPhoto1.jpg");
                outPutfileUri1 = Uri.fromFile(file1);
                System.out.println("getting_captureprofle  " + outPutfileUri1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri1);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE1);*/
            }
        });
        iv_capturetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE2);
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file2 = new File(Environment.getExternalStorageDirectory(), "MyPhoto2.jpg");
                outPutfileUri2 = Uri.fromFile(file2);
                System.out.println("getting_captureprofle  " + outPutfileUri2);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri2);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE2);*/
            }
        });
        iv_capturethree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE3);
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file3 = new File(Environment.getExternalStorageDirectory(), "MyPhoto3.jpg");
                outPutfileUri3 = Uri.fromFile(file3);
                System.out.println("getting_captureprofle  " + outPutfileUri3);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri3);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE3);*/
            }
        });
        iv_capturefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4);
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file4 = new File(Environment.getExternalStorageDirectory(), "MyPhoto4.jpg");
                outPutfileUri4 = Uri.fromFile(file4);
                System.out.println("getting_captureprofle  " + outPutfileUri4);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri4);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4);*/
            }
        });
        iv_capturefiveadharcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4ADHARCARD);
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file4 = new File(Environment.getExternalStorageDirectory(), "MyPhoto4.jpg");
                outPutfileUri4 = Uri.fromFile(file4);
                System.out.println("getting_captureprofle  " + outPutfileUri4);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri4);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4);*/
            }
        });
        iv_capturefiveadharcardback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4ADHARCARDBACK);
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file4 = new File(Environment.getExternalStorageDirectory(), "MyPhoto4.jpg");
                outPutfileUri4 = Uri.fromFile(file4);
                System.out.println("getting_captureprofle  " + outPutfileUri4);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri4);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4);*/
            }
        });

        //showwinpopupofselfie();
    }

    public void showwinpopupofselfie() {//00000000000000000000000
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.androidcameraexample,
                            findViewById(R.id.rlt_selfiecameralyt));
                    pwindoselfie = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
                    pwindoselfie.showAtLocation(layout, Gravity.CENTER, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
             /*   PopupMenu popuMenu=new PopupMenu(SplashScreen.this,binding.progressBar);
                popuMenu.inflate(R.menu.bottom_nav_menu);
                popuMenu.show();*/
            }
        }, 1000);
    }

    private Bitmap StringToBitMap(String filename) {
        try {
            /*byte[] encodeByte = Base64.decode(filename, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
*/
            byte[] encodeByte = Base64.decode(filename, Base64.DEFAULT);
            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /*create end*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("getting_requestCode  " + requestCode);
        System.out.println("getting_resultCode  " + resultCode);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 && resultCode == -1) {
            try {
                //bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri1);
                bitmap1 = (Bitmap) data.getExtras().get("data");

                /*int compressionRatio = 4; //1 == originalImage, 2 = 50% compression, 4=25% compress
                File file = new File(String.valueOf(bitmap1));
                try {
                    bitmapmobilephoto1 = BitmapFactory.decodeFile(file.getPath());
                    bitmapmobilephoto1.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
                    getResizedBitmap(bitmapmobilephoto1, 200);
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }*/
                //mobilepicturePath1 = getResizedBitmap(bitmapmobilephoto1, 100); // old code
                //mobilepicturePath1 = String.valueOf(bitmapmobilephoto1);
                mobilepicturePath1 = String.valueOf(bitmap1);
                System.out.println("getting_mobilepicturePath1 " + mobilepicturePath1);
               /* BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file1.toString(), bounds);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(file1.toString(), opts);
                ExifInterface exif = new ExifInterface(file1.toString());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                bitmapmobilephoto1 = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);*/
                ImageView imageView = findViewById(R.id.civ_mobileone);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE2 && resultCode == -1) {
            try {
                //bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri2);
                bitmap2 = (Bitmap) data.getExtras().get("data");
/*
                int compressionRatio = 4; //1 == originalImage, 2 = 50% compression, 4=25% compress
                File file = new File(String.valueOf(bitmap2));

                try {
                    bitmapmobilephoto2 = BitmapFactory.decodeFile(file.getPath());
                    bitmapmobilephoto2.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
                    getResizedBitmap(bitmapmobilephoto2, 200);
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }*/
                //mobilepicturePath2 = getResizedBitmap(bitmapmobilephoto2, 100); // old code
                //mobilepicturePath2 = String.valueOf(bitmapmobilephoto2);
                mobilepicturePath2 = String.valueOf(bitmap2);
              /*  BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file2.toString(), bounds);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(file2.toString(), opts);
                ExifInterface exif = new ExifInterface(file2.toString());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                bitmapmobilephoto2 = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);*/
                ImageView imageView = findViewById(R.id.civ_mobiletwo);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE3 && resultCode == -1) {
            try {
                // bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri3);
                bitmap3 = (Bitmap) data.getExtras().get("data");
/*

                int compressionRatio = 4; //1 == originalImage, 2 = 50% compression, 4=25% compress
                File file = new File(String.valueOf(bitmap3));
                try {
                    bitmapmobilephoto3 = BitmapFactory.decodeFile(file.getPath());
                    bitmapmobilephoto3.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
                    getResizedBitmap(bitmapmobilephoto3, 200);
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }
*/
                // bitmapprofilephoto = getResizedBitmap(bitmap1, 200); // old code
                //mobilepicturePath3 = getResizedBitmap(bitmapmobilephoto3, 100); // old code
                //mobilepicturePath3 = String.valueOf(bitmapmobilephoto3);
                mobilepicturePath3 = String.valueOf(bitmap3);
                /*BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file3.toString(), bounds);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(file3.toString(), opts);
                ExifInterface exif = new ExifInterface(file3.toString());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                bitmapmobilephoto3 = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);*/
                ImageView imageView = findViewById(R.id.civ_mobilethree);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE4 && resultCode == -1) {
            try {
                //bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri4);
                bitmap4 = (Bitmap) data.getExtras().get("data");

               /* int compressionRatio = 4; //1 == originalImage, 2 = 50% compression, 4=25% compress
                File file = new File(String.valueOf(bitmap2));
                try {
                    bitmapmobilephoto4 = BitmapFactory.decodeFile(file.getPath());
                    bitmapmobilephoto4.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
                    getResizedBitmap(bitmapmobilephoto4, 200); // old code
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }*/
                // bitmapprofilephoto = getResizedBitmap(bitmap1, 200); // old code
                //mobilepicturePath4 = getResizedBitmap(bitmapmobilephoto4, 100); // old code
                mobilepicturePath4 = String.valueOf(bitmap4);
               /* BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file4.toString(), bounds);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(file4.toString(), opts);
                ExifInterface exif = new ExifInterface(file4.toString());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                bitmapmobilephoto4 = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);*/
                ImageView imageView = findViewById(R.id.civ_mobilefour);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE4ADHARCARD && resultCode == -1) {
            try {
                //bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri4);
                bitmap4adharcard = (Bitmap) data.getExtras().get("data");

               /* int compressionRatio = 4; //1 == originalImage, 2 = 50% compression, 4=25% compress
                File file = new File(String.valueOf(bitmap2));
                try {
                    bitmapmobilephoto4 = BitmapFactory.decodeFile(file.getPath());
                    bitmapmobilephoto4.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
                    getResizedBitmap(bitmapmobilephoto4, 200); // old code
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }*/
                // bitmapprofilephoto = getResizedBitmap(bitmap1, 200); // old code
                //mobilepicturePath4 = getResizedBitmap(bitmapmobilephoto4, 100); // old code
                mobilepicturePath4adharcrad = String.valueOf(bitmap4adharcard);
               /* BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file4.toString(), bounds);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(file4.toString(), opts);
                ExifInterface exif = new ExifInterface(file4.toString());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                bitmapmobilephoto4 = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);*/
                ImageView imageView = findViewById(R.id.civ_adharcardimage);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap4adharcard);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE4ADHARCARDBACK && resultCode == -1) {
            try {
                //bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri4);
                bitmap4adharcardback = (Bitmap) data.getExtras().get("data");

               /* int compressionRatio = 4; //1 == originalImage, 2 = 50% compression, 4=25% compress
                File file = new File(String.valueOf(bitmap2));
                try {
                    bitmapmobilephoto4 = BitmapFactory.decodeFile(file.getPath());
                    bitmapmobilephoto4.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
                    getResizedBitmap(bitmapmobilephoto4, 200); // old code
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }*/
                // bitmapprofilephoto = getResizedBitmap(bitmap1, 200); // old code
                //mobilepicturePath4 = getResizedBitmap(bitmapmobilephoto4, 100); // old code
                mobilepicturePath4adharcradback = String.valueOf(bitmap4adharcardback);
               /* BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file4.toString(), bounds);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(file4.toString(), opts);
                ExifInterface exif = new ExifInterface(file4.toString());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                bitmapmobilephoto4 = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);*/
                ImageView imageView = findViewById(R.id.civ_adharcardimageback);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap4adharcardback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "File Not Found , Please Re-Capture !!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getResizedBitmap(Bitmap bitmap1, int i) {
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = i;
            height = (int) (width / bitmapRatio);
        } else {
            height = i;
            width = (int) (height * bitmapRatio);
        }
        return String.valueOf(Bitmap.createScaledBitmap(bitmap1, width, height, true));
    }
    /*create end*/

    private void progressDialog() {
        kProgressHUD = new KProgressHUD(CompleteleadItemdescriptionActivity.this);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {//
            case R.id.btn_verifyadharcardotp:
                verifiyaadharcardotpno();
                break;
            case R.id.back_descld:
                finish();
                break;
            case R.id.btn_verifyadharcard:
                verifiyaadharcard();
                break;
            case R.id.btn_completedcld:

                extraamount = et_addextraamount_cld.getText().toString();
                imeinumber = et_enterimeinumber.getText().toString();
                System.out.println("getting_extraamount " + extraamount);
                System.out.println("getting_imeinumber " + imeinumber);
                /*if (et_enterimeinumber.getText().toString().length() == 0) {
                    et_addextraamount_cld.setError("Please enter extra amount");
                    et_addextraamount_cld.requestFocus();
                } else*/
                if (mobilepicturePath1 == null || mobilepicturePath1.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "Please capture all images", Toast.LENGTH_LONG).show();
                } else if (mobilepicturePath2 == null || mobilepicturePath2.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "Please capture all images", Toast.LENGTH_LONG).show();
                } else if (mobilepicturePath3 == null || mobilepicturePath3.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "Please capture all images", Toast.LENGTH_LONG).show();
                } else if (mobilepicturePath4 == null || mobilepicturePath4.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "Please capture all images", Toast.LENGTH_LONG).show();
                } else if (mobilepicturePath4adharcrad == null || mobilepicturePath4adharcrad.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "Please capture front image of adharcard", Toast.LENGTH_LONG).show();
                } else if (mobilepicturePath4adharcradback == null || mobilepicturePath4adharcradback.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "Please capture back images of adharcard", Toast.LENGTH_LONG).show();
                } else if (et_enterimeinumber.getText().toString().length() == 0) {
                    et_enterimeinumber.setError("Please enter IMEI number");
                    et_enterimeinumber.requestFocus();
                } else {
                    progressDialog();
                    senddataforcomplete();
                }
                break;
            default:
                break;
        }
    }

    private void verifiyaadharcardotpno() {
        otpno = pinview.getText().toString().trim();
        if (pinview.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please Enter OTP ", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog();
            verifiyaadharcardnumberotp(otpno);
        }
    }

    private void verifiyaadharcard() {
        String aadharcard = et_cardNumberEditText.getText().toString().trim();
        if (et_cardNumberEditText.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please Enter Aadharcard No.", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog();
            verifiyaadharcardnumber(aadharcard);
        }
    }

    private void verifiyaadharcardnumber(String aadharcard) {
        System.out.println("getting_aadharcardnumber::::::::: " + aadharcard);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id_number", aadharcard);
        JsonObjectRequest stringRequest = new JsonObjectRequest("https://kyc-api.aadhaarkyc.io/api/v1/aadhaar-v2/generate-otp", new JSONObject(params),
                //JsonObjectRequest stringRequest = new JsonObjectRequest("https://postman-echo.com/post", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Result_verify_fragment_onemain:::::" + response);
                        if (response == null) {
                            dismissDialog();
                        } else {
                            String s = response.toString();
                            System.out.println("msg_fonemain:::" + s);
                            dismissDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String status = jsonObject.getString("status_code");
                                String message = jsonObject.getString("message");
                                System.out.println("msg_status_message:::" + status + "  " + message);
                                if (status.equals("200")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    client_id = jsonObject1.get("client_id").toString();
                                    cv_verifyotpno.setVisibility(View.VISIBLE);
                                    System.out.println("msg_client_id:::" + client_id);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();
                        Toast.makeText(getApplicationContext(), "Something went wrong ," + getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                    }
                }) {
          /*  @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_number", aadharcard);
                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramsheader = new HashMap<String, String>();
                paramsheader.put("Content-Type", "application/json");
                paramsheader.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return paramsheader;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void verifiyaadharcardnumberotp(String otpno) {
        System.out.println("getting_aadharcardnumberotpnoclient_id::::::::: " + otpno + "        " + client_id);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", client_id);
        params.put("otp", otpno);
        JsonObjectRequest stringRequest = new JsonObjectRequest("https://kyc-api.aadhaarkyc.io/api/v1/aadhaar-v2/submit-otp", new JSONObject(params),
                //JsonObjectRequest stringRequest = new JsonObjectRequest("https://postman-echo.com/post", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Result_verify_fragment_onemain_otpverified:::::" + response);
                        if (response == null) {
                            dismissDialog();
                        } else {
                            String s = response.toString();
                            System.out.println("msg_fonemainotpverified:::" + s);
                            dismissDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String status = jsonObject.getString("status_code");
                                String message = jsonObject.getString("message");

                                System.out.println("msg_status_messageotpverified:::" + status + "  " + message);
                                if (status.equals("200")) {//1111111111111111111111111111111111111111111111
                                    cv_verifyotpno.setVisibility(View.GONE);
                                    //btn_verifyadharcard.setEnabled(false);
                                    et_cardNumberEditText.setEnabled(false);
                                    btn_verifyadharcard.setEnabled(false);
                                    btn_verifyadharcard.setBackgroundColor(getResources().getColor(R.color.darkgreen));
                                    btn_verifyadharcard.setText("VERIFIED");
                                    btn_verifyadharcard.setTextColor(Color.WHITE);
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), "OTP Verified", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramsheader = new HashMap<String, String>();
                paramsheader.put("Content-Type", "application/json");
                paramsheader.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return paramsheader;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void senddataforcomplete() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://sellit.co.in/logisticapi/v1/completeleads.php",
                //VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://postman-echo.com/post",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            jsonString = new String(response.data, "UTF-8");
                            System.out.println("getting_data_from_server_datacomplition" + jsonString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jsonString == null) {
                            dismissDialog();
                        } else {
                            System.out.println("getting_response_datacomplition" + jsonString);
                            dismissDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                String customeraddress = jsonObject.getString("customeraddress");
                                String vendoraddress = jsonObject.getString("vendoraddress");

                            /*    JSONObject jarray = jsonObject.getJSONObject("customeraddress");
                                sellercity = jarray.getString("city");
                                sellerlandmark = jarray.getString("landmark");
                                sellerstate = jarray.getString("state");
                                sellerhouseno = jarray.getString("houseno");

                                JSONObject jarray1 = jsonObject.getJSONObject("vendoraddress");
                                vendorcity = jarray1.getString("city");
                                vendoraddress = jarray1.getString("address");*/

                               /* JSONArray jarray = jsonObject.getJSONArray("customeraddress");
                                for (int h = 0; h < jarray.length(); h++) {
                                    JSONObject d = jarray.getJSONObject(h);
                                    sellercity = d.getString("city");
                                    sellerlandmark = d.getString("landmark");
                                    sellerstate = d.getString("state");
                                    sellerhouseno = d.getString("houseno");

                                }
                                JSONArray jarray1 = jsonObject.getJSONArray("vendoraddress");
                                for (int hk = 0; hk < jarray1.length(); hk++) {
                                    JSONObject d1 = jarray1.getJSONObject(hk);
                                    vendorcity = d1.getString("city");
                                    vendoraddress = d1.getString("address");
                                }*/
                                if ((status.equals("1"))) {
                                    dismissDialog();
                                    //Intent intent = new Intent(getApplicationContext(), CompleteLeadActivity.class);
                                    Intent intent = new Intent(getApplicationContext(), LeadCompeteReceiptActivity.class);
                                    intent.putExtra("lead_pick_date", lead_pick_date);
                                    intent.putExtra("lead_id", lead_id);
                                    intent.putExtra("model_name", model_name);
                                    intent.putExtra("price", price);
                                    intent.putExtra("lead_pick_time", lead_pick_time);

                                    intent.putExtra("customeraddress", customeraddress);//seller
                                    intent.putExtra("vendoraddress", vendoraddress);//vendor
                                   /* intent.putExtra("sellercity", sellercity);
                                    intent.putExtra("sellerlandmark", sellerlandmark);
                                    intent.putExtra("sellerstate", sellerstate);
                                    intent.putExtra("sellerhouseno", sellerhouseno);
                                    intent.putExtra("vendorcity", vendorcity);
                                    intent.putExtra("vendoraddress", vendoraddress);*/
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                } else {
                                    dismissDialog();
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
                        dismissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>(); // candidatename
                params.put("vendorid", vendorid);
                params.put("lead_id", lead_id);
                //params.put("flag", "inprogress");
                params.put("flag", "Complete");
                params.put("IMEI", imeinumber);
                params.put("ajent_id", ajentassign);
                params.put("extraamount", extraamount);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic1", new DataPart(mobilepicturePath1 + ".png", getFileDataFromDrawable(bitmap1)));
                params.put("pic2", new DataPart(mobilepicturePath2 + ".png", getFileDataFromDrawable(bitmap2)));
                params.put("pic3", new DataPart(mobilepicturePath3 + ".png", getFileDataFromDrawable(bitmap3)));
                params.put("pic4", new DataPart(mobilepicturePath4 + ".png", getFileDataFromDrawable(bitmap4)));
                //params.put("selfie", new DataPart(imagename + ".png", getFileDataFromDrawable(bmp)));
                params.put("aadharfront", new DataPart(mobilepicturePath4adharcrad + ".png", getFileDataFromDrawable(bitmap4adharcard)));
                params.put("aadharback", new DataPart(mobilepicturePath4adharcradback + ".png", getFileDataFromDrawable(bitmap4adharcardback)));
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
        VolleySingleton.getInstance(this).
                addToRequestQueue(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        System.out.println("getting_bitmapdatahere   " + bitmap.toString());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}

