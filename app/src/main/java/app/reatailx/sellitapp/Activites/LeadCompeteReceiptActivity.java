package app.reatailx.sellitapp.Activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import app.reatailx.sellitapp.R;
import app.reatailx.sellitapp.SharePrefrence.SessionManager;

public class LeadCompeteReceiptActivity extends AppCompatActivity implements View.OnClickListener {

    public SessionManager session;
    public RelativeLayout back_completereceipts;
    public String lead_id, model_name, price, lead_pick_time, lead_pick_date;
    public TextView text_leadid, text_datetime, text_modelitem, text_price, text_priceinword, text_vendoraddress, text_selleraddress;
    public String sellerhouseno, sellercity, sellerstate, sellerlandmark, customeraddress;
    public String vendoraddress, vendorcity;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completelead_deatil_receipt);
        session = new SessionManager(getApplicationContext());

        final Bundle intent = getIntent().getExtras();
        if (intent != null) {

            /*intent.putExtra("sellercity", sellercity);
            intent.putExtra("sellerlandmark", sellerlandmark);
            intent.putExtra("sellerstate", sellerstate);
            intent.putExtra("sellerhouseno", sellerhouseno);
            intent.putExtra("vendorcity", vendorcity);
            intent.putExtra("vendoraddress", vendoraddress);*/

            lead_pick_date = intent.get("lead_pick_date").toString();
            lead_id = intent.get("lead_id").toString();
            model_name = intent.get("model_name").toString();
            price = intent.get("price").toString();
            lead_pick_time = intent.get("lead_pick_time").toString();

            customeraddress = intent.get("customeraddress").toString();
            vendoraddress = intent.get("vendoraddress").toString();

            /*sellercity = intent.get("sellercity").toString();
            sellerhouseno = intent.get("sellerhouseno").toString();
            sellerlandmark = intent.get("sellerlandmark").toString();
            sellerstate = intent.get("sellerstate").toString();
            vendoraddress = intent.get("vendoraddress").toString();
            vendorcity = intent.get("vendorcity").toString();
*/
            System.out.println("getting_data_from_completelead" + "  lead_id  " + lead_id + "  model_name  " + model_name + "  price  " + price + "  lead_pick_time   " + lead_pick_time);
            System.out.println("getting_data_herevc" + customeraddress + "  " + vendoraddress);
            //  System.out.println("getting_data_seller_vendor" + "  sellercity  " + sellercity + "  sellerhouseno  " + sellerhouseno + "  sellerlandmark  " + sellerlandmark + "  sellerstate   " + sellerstate + "  vendoraddress  " + vendoraddress + "  vendorcity   " + vendorcity);

        }
       /* text_selleraddress.setText(sellerhouseno + " " + sellerlandmark + " " + sellercity + " " + sellerstate);
        text_vendoraddress.setText(vendoraddress + " " + vendorcity);*/
        text_selleraddress = findViewById(R.id.text_selleraddress);
        text_vendoraddress = findViewById(R.id.text_vendoraddress);

        text_leadid = findViewById(R.id.text_leadid);
        text_datetime = findViewById(R.id.text_datetime);
        text_modelitem = findViewById(R.id.text_modelitem);
        text_price = findViewById(R.id.text_price);
        text_priceinword = findViewById(R.id.text_priceinword);

        text_selleraddress.setText(customeraddress);
        text_vendoraddress.setText(vendoraddress);

        text_leadid.setText(lead_id);
        text_datetime.setText(lead_pick_date);
        text_modelitem.setText(model_name);
        text_price.setText(price);
        int priceint = Integer.parseInt(price);
        text_priceinword.setText(EnglishNumberToWords.convert(priceint) + " only ");


        back_completereceipts = findViewById(R.id.back_completereceipts);
        back_completereceipts.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LeadCompeteReceiptActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_completereceipts:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}

