package gstores.merchandiser_beta.customviews;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import gstores.merchandiser_beta.R;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryLine;

import static gstores.merchandiser_beta.components.AppLiterals.DISPLAY_NUMBER_FORMAT_0_00;

public class DeliveryLineAdaptor extends ArrayAdapter<DeliveryLine> {
    private final Context mContext;
    private final int mResource;
    private DeliveryLineAdaptor.onDeleteItem onDeleteItem;

    public DeliveryLineAdaptor(Context context, int resource, List<DeliveryLine> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DeliveryLine line = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);
        AppCompatButton textViewHeadding = convertView.findViewById(R.id.textViewHeader);
        AppCompatTextView textViewQuantity = convertView.findViewById(R.id.textViewQuantity);
        AppCompatTextView textViewValue = convertView.findViewById(R.id.textViewValue);
        AppCompatImageButton buttonDelete = convertView.findViewById(R.id.appCompatImageButtonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onDeleteItem != null)
                    onDeleteItem.deleteItem(position);
            }
        });
        textViewHeadding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteItem.editItem(position);
            }
        });
        textViewHeadding.setText(line.Description);
        textViewQuantity.setText(
                DISPLAY_NUMBER_FORMAT_0_00.format(line.OrderQuantity)+" NOS");
        textViewValue.setText(
                DISPLAY_NUMBER_FORMAT_0_00.format(line.SelleingPrice)+" AED");
        return convertView;
        //return super.getView(position, convertView, parent);
    }

    public void setOnDeleteItem(DeliveryLineAdaptor.onDeleteItem onDeleteItem) {
        this.onDeleteItem = onDeleteItem;
    }

    public interface onDeleteItem{
        void deleteItem(int position);
        void editItem(int position);
    }
}
