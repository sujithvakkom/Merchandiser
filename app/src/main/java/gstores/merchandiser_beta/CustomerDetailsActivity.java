package gstores.merchandiser_beta;

import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;

import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;

import static gstores.merchandiser_beta.components.AppLiterals.ExtraLabels.DELIVERY_HEADER;

public class CustomerDetailsActivity extends AppCompatActivity {

    private AppCompatEditText editTextCustomerName;
    private AppCompatSpinner spinnerCustomerEmirate;
    private AppCompatEditText editTextCustomerPhone;
    private AppCompatEditText editTextPO;
    private AppCompatEditText editTextCustomerAddress;
    private AppCompatButton buttonSaveCustomer;
    private String[] emirates;
    private View parentView;
    private DeliveryHeader deliveryHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        try{
            this.deliveryHeader = (DeliveryHeader) getIntent().getSerializableExtra(DELIVERY_HEADER);}
        catch (Exception ex) {
            ex.printStackTrace();
            if (this.deliveryHeader == null)
                this.deliveryHeader = new DeliveryHeader();
        }

        if (savedInstanceState != null) {
            this.deliveryHeader = (DeliveryHeader) savedInstanceState.getSerializable(DELIVERY_HEADER);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_customer_detail_toolbar);
        setSupportActionBar(toolbar);

        editTextCustomerName = findViewById(R.id.editTextCustomerName);
        spinnerCustomerEmirate = findViewById(R.id.spinnerCustomerEmirate);
        editTextCustomerPhone = findViewById(R.id.editTextCustomerPhone);
        editTextPO = findViewById(R.id.editTextPO);
        editTextCustomerAddress = findViewById(R.id.editTextCustomerAddress);
        buttonSaveCustomer = findViewById(R.id.buttonSaveCustomer);
        parentView = findViewById(R.id.customer_child_content_container);

        emirates =
                getResources().getStringArray(R.array.emirates);

        ArrayAdapter<String> emiratesAdaptor = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.simple_spinner_item,
                emirates
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        emiratesAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCustomerEmirate.setAdapter(emiratesAdaptor);
        //spinnerCustomerEmirate.setPopupBackgroundDrawable(getResources().getDrawable(R.color.gray));

        /*View Events*/
        buttonSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSaveCustomer();
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.deliveryHeader = (DeliveryHeader) savedInstanceState.getSerializable(DELIVERY_HEADER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(deliveryHeader!=null){
            if(deliveryHeader.CustomerName!=null) this.editTextCustomerName.setText(deliveryHeader.CustomerName);
            if(deliveryHeader.Emirate!=null) this.spinnerCustomerEmirate.setSelection(
                    Arrays.asList(emirates).indexOf(deliveryHeader.Emirate)+1);
            if(deliveryHeader.Phone!=null) this.editTextCustomerPhone.setText(deliveryHeader.Phone);
            if(deliveryHeader.Receipt!=null) this.editTextPO.setText(deliveryHeader.Receipt);
            if(deliveryHeader.DeliveryAddress!=null) this.editTextCustomerAddress.setText(deliveryHeader.DeliveryAddress);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(DELIVERY_HEADER,this.deliveryHeader);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DELIVERY_HEADER,this.deliveryHeader);
    }

    private void checkAndSaveCustomer() {
        if (validateSalesForm()) {
            CustomerDetailsActivity.this.deliveryHeader.CustomerName =
                    CustomerDetailsActivity.this.editTextCustomerName.getText().toString();
            CustomerDetailsActivity.this.deliveryHeader.Phone =
                    CustomerDetailsActivity.this.editTextCustomerPhone.getText().toString();
            CustomerDetailsActivity.this.deliveryHeader.DeliveryAddress =
                    CustomerDetailsActivity.this.editTextCustomerAddress.getText().toString();
            CustomerDetailsActivity.this.deliveryHeader.Receipt =
                    CustomerDetailsActivity.this.editTextPO.getText().toString();
            int selectedItemPosition =CustomerDetailsActivity.this.spinnerCustomerEmirate.getSelectedItemPosition();
            CustomerDetailsActivity.this.deliveryHeader.Emirate =
                    emirates[selectedItemPosition-1];
            Intent intent = new Intent();
            intent.putExtra(DELIVERY_HEADER,
                    CustomerDetailsActivity.this.deliveryHeader);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public boolean validateSalesForm()
    {
        boolean result = true;
            try {
                String custName = editTextCustomerName.getText().toString();
                result = (result && !custName.isEmpty() );
                if (custName.isEmpty())
                    editTextCustomerName.setError(getString(R.string.please_enter_customer_name));
            } catch (Exception ex) {
                result = false;
                editTextCustomerName.setError(getString(R.string.please_enter_customer_name));
            }
            try {
                result = (result && isValidPhoneNumber(editTextCustomerPhone.getText().toString()) );
                if (!isValidPhoneNumber(editTextCustomerPhone.getText().toString()))
                    editTextCustomerPhone.setError(getString(R.string.invalid_phone));
            } catch (Exception ex) {
                result = false;
                editTextCustomerPhone.setError(getString(R.string.invalid_phone));
            }
        try {
            result = (result && spinnerCustomerEmirate.getSelectedItem().toString()!=emirates[0] );
            if (!((spinnerCustomerEmirate.getSelectedItem().toString())!=emirates[0]))
            {
                Util.SimpleSnackBar(parentView,"Please select emirate.", Snackbar.LENGTH_LONG);
            }
        } catch (Exception ex) {
            result = false;
            Util.SimpleSnackBar(parentView,"Please select emirate.", Snackbar.LENGTH_LONG);
        }

        return result;
    }

    /**
     * Validation of Phone Number
     */
    public final static boolean isValidPhoneNumber(CharSequence target) {
        String patterns[] = {"05\\d{8}", "04\\d{7}", "00\\d{8,15}"};
        String phone = target.toString();
        for (String pattern : patterns
        ) {
            if (
                    phone.matches(pattern))
                return true;
        }
        return false;
        /*
        if (target == null || target.length() < 6 || target.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }*/
    }
}
