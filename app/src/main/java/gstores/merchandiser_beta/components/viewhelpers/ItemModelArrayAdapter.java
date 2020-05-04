package gstores.merchandiser_beta.components.viewhelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gstores.merchandiser_beta.R;
import gstores.merchandiser_beta.components.models.ItemModel;

public class ItemModelArrayAdapter extends ArrayAdapter<ItemModel> {

    private final Context context;
    private final ItemModel[] values;

    public  ItemModelArrayAdapter(Context context, ItemModel[] arrayItemModel){
        super(context,-1, arrayItemModel);
        this.context =context;
        this.values =arrayItemModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list_view, parent, false);
        TextView textViewFirstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textViewSecondLine = (TextView) rowView.findViewById(R.id.secondLine);
        TextView textViewsecondLineItemCode = (TextView) rowView.findViewById(R.id.secondLineItemCode);
        textViewFirstLine.setText(values[position].getDescription());
        textViewSecondLine.setText(values[position].getModel());
        textViewsecondLineItemCode.setText(values[position].getItem_code());
        return rowView;
    }
}
