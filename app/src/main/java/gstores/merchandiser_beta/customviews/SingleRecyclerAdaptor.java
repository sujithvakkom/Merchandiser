package gstores.merchandiser_beta.customviews;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import gstores.merchandiser_beta.R;

public class SingleRecyclerAdaptor<T> extends RecyclerView.Adapter<SingleRecyclerAdaptor.MyViewHolder>
        implements Filterable
{
    private List<T> mDataset;
    private List<T> brandListFiltered;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public AppCompatTextView textView;
        public AppCompatCheckBox selected;
        public MyViewHolder(View v) {
            super(v);
            textView = (AppCompatTextView) v.findViewById(R.id.brand_list_item);
            selected = (AppCompatCheckBox) v.findViewById(R.id.brand_list_item_selected);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SingleRecyclerAdaptor(List<T> myDataset) {
        mDataset = myDataset;
        brandListFiltered= myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SingleRecyclerAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkbox_selection_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(brandListFiltered.get(position).toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return brandListFiltered==null?0: brandListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    brandListFiltered = mDataset;
                } else {
                    List<T> filteredList = new ArrayList<T>();
                    for (T row : mDataset) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.toString().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    brandListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = brandListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                brandListFiltered = (ArrayList<T>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}