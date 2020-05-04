package gstores.merchandiser_beta.customviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;


import gstores.merchandiser_beta.R;
import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.models.Model;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryLine;

public class SaleConfigDialog extends AppCompatDialogFragment {

    public Model model;
    private AppCompatEditText editTextItemQuantity;
    private AppCompatEditText editTextItemValue;
    private SaleConfigDialogListner onSaleConfigDialogListner;
    private int position;
    private DeliveryLine deliveryLine;
    private boolean priceOverRidden;
    private double unitSellingPrice;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));

        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sale_config,null);
        editTextItemQuantity= view.findViewById(R.id.editTextItemQuantity);
        editTextItemValue = view.findViewById(R.id.editTextItemValue);
        if(deliveryLine!=null) {
            this.unitSellingPrice =deliveryLine.SelleingPrice/deliveryLine.OrderQuantity;
            this.editTextItemQuantity.setText(Double.toString(deliveryLine.OrderQuantity));
            this.editTextItemValue.setText(Double.toString(deliveryLine.SelleingPrice));
        }else{
            this.unitSellingPrice =model.getValue();
            editTextItemValue.setText(AppLiterals.DISPLAY_NUMBER_FORMAT_0_00.format(unitSellingPrice));
        }

        editTextItemQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    editTextItemValue.setText(AppLiterals.DISPLAY_NUMBER_FORMAT_0_00.format(
                            unitSellingPrice * Double.parseDouble(s.toString())));
                }catch (Exception ex){ex.printStackTrace();}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextItemValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(Double.parseDouble(editTextItemQuantity.getText().toString())>0)
                    unitSellingPrice = Double.parseDouble(s.toString()) / Double.parseDouble(editTextItemQuantity.getText().toString());
                }catch (Exception ex){ex.printStackTrace();}
            }

            @Override
            public void afterTextChanged(Editable s) {
                SaleConfigDialog.this.priceOverRidden = true;
            }
        });

        builder.setView(view)
        .setTitle(getResources().getString( R.string.item_sale_congif)+
                System.getProperty("line.separator")
                +(deliveryLine==null ? (model.getDescriptionString().length() > 100 ?
                    model.getDescriptionString().substring(0,99):
                    model.getDescriptionString()):(deliveryLine.Description.length() > 100 ?
                deliveryLine.Description.substring(0,99):
                deliveryLine.Description)))
        .setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean valid = true;
                if(editTextItemQuantity.getText().length()==0){
                    valid =false;
                    editTextItemQuantity.setError("Please enter quantity.");}
                if(editTextItemValue.getText().length()==0){
                    valid = false;
                    editTextItemValue.setError("Please enter quantity.");}
                    if(valid){
                        double quantity = Double.parseDouble(editTextItemQuantity.getText().toString());
                        double value = Double.parseDouble(editTextItemValue.getText().toString());
                        if(onSaleConfigDialogListner!=null)
                        onSaleConfigDialogListner.success(quantity,value);
                    }
            }
        });


        return builder.create();
    }

    public void setOnSaleConfigDialogListener(SaleConfigDialogListner onSaleConfigDialogListner) {
        this.onSaleConfigDialogListner = onSaleConfigDialogListner;
    }

    public void setDeliveryLine (DeliveryLine line){
        this.deliveryLine = line;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public interface SaleConfigDialogListner{
        void success(double quantity,double value);
    }
}
