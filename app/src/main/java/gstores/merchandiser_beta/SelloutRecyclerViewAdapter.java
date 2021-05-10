package gstores.merchandiser_beta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gstores.merchandiser_beta.SelloutFragment.OnListFragmentInteractionListener;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;
import gstores.merchandiser_beta.dummy.DummyContent.DummyItem;

import java.util.List;

import static gstores.merchandiser_beta.components.AppLiterals.APPLICATION_AMOUNT_FORMAT;
import static gstores.merchandiser_beta.components.AppLiterals.APPLICATION_DATE_MONTH_YEAR_FORMAT;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SelloutRecyclerViewAdapter extends RecyclerView.Adapter<SelloutRecyclerViewAdapter.ViewHolder> {

    private final List<DeliveryHeader> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SelloutRecyclerViewAdapter(List<DeliveryHeader> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).Receipt);
        holder.mContentView.setText(APPLICATION_AMOUNT_FORMAT.format(mValues.get(position).Price));
        try {
            if (mValues.get(position).SaleDate == null)
                holder.mSaleDate.setText("");
            else
                holder.mSaleDate.setText(APPLICATION_DATE_MONTH_YEAR_FORMAT.format(mValues.get(position).SaleDate));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.CustomerName.setText(mValues.get(position).CustomerName.length() > 0 ?
                "Customer Name : " + mValues.get(position).CustomerName : "No customer details.");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mValues==null)
            return 0;
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mSaleDate;
        public final TextView mSaleStatus;
        public final TextView mContentView;
        public final TextView CustomerName;
        public DeliveryHeader mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            CustomerName = (TextView) view.findViewById(R.id.CustomerName);
            mSaleDate = (TextView) view.findViewById(R.id.salesDate);
            mSaleStatus = (TextView) view.findViewById(R.id.StatusDesc);
            mSaleStatus.setText("");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
