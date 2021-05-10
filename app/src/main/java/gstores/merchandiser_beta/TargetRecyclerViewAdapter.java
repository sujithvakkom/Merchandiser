package gstores.merchandiser_beta;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.models.UserTargetDetailsView;

import static gstores.merchandiser_beta.components.AppLiterals.APPLICATION_AMOUNT_FORMAT;

public class TargetRecyclerViewAdapter extends RecyclerView.Adapter<TargetRecyclerViewAdapter.ViewHolder> {
    private List<UserTargetDetailsView> mValues;

    public TargetRecyclerViewAdapter(List<UserTargetDetailsView> items) {
        mValues = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_target_line, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mItem = mValues.get(i);

        //Target details
        viewHolder.targeDesc.setText(viewHolder.mItem.Period);
        viewHolder.targeSite.setText(viewHolder.mItem.Location);
        viewHolder.targetTable.removeAllViews();
        String[] targetHeadings = {"Category", "Target", "Achievement", "%"};
        viewHolder.targetTable.addView(
                createRowForHeader(viewHolder.mView.getContext(),
                        targetHeadings
                ), new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
        for (UserTargetDetailsView x : viewHolder.mItem.Lines
        ) {
            TableRow row = createRowForTarget(x, viewHolder.mView.getContext());
            viewHolder.targetTable.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }

        //Incentive details
        try {
            viewHolder.incentiveTable.removeAllViews();
            String[] inventiveHeadings = {"Category", "Incentive", "Acc.", "Tot."};
            viewHolder.incentiveTable.addView(
                    createRowForHeader(viewHolder.mView.getContext(),
                            inventiveHeadings
                    ), new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
            for (UserTargetDetailsView x : viewHolder.mItem.Lines
            ) {
                TableRow row = createRowForIncentive(x, viewHolder.mView.getContext());
                viewHolder.incentiveTable.addView(row, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        TableRow row = createRowForTarget(viewHolder.mItem, viewHolder.mView.getContext());
        viewHolder.targetTable.addView(row, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        try {
            row = createRowForIncentive(viewHolder.mItem, viewHolder.mView.getContext());
            viewHolder.incentiveTable.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TableRow createRowForTarget(UserTargetDetailsView x, Context  context) {
        int padding=8;
        int fontSize=8;
        TableRow row = new TableRow(context);
        row.setId(View.NO_ID);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.dividerVertical, typedValue, true);
            row.setDividerDrawable(context.getResources().getDrawable(typedValue.resourceId));
        }catch (Exception ex){ex.printStackTrace();}

        if(x.Catogery.equals("Total")){
            row.setBackground(context.getDrawable(R.drawable.border_top));
        }

        TextView label_cat = new TextView(context);
        label_cat.setId(View.NO_ID);
        label_cat.setText(x.Catogery);
        label_cat.setTextColor(Color.BLACK);          // part2
        label_cat.setPadding(padding, padding, padding, padding);
        ((View) label_cat).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                3.0f));
        if(x.Catogery.equals("Total")){
            label_cat.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            label_cat.setTypeface(label_cat.getTypeface(), Typeface.BOLD);}
        row.addView(label_cat);

        TextView label_target = new TextView(context);
        label_target.setId(View.NO_ID);
        label_target.setText(APPLICATION_AMOUNT_FORMAT.format(x.Target));
        label_target.setTextColor(Color.BLACK);          // part2
        label_target.setPadding(padding, padding, padding, padding);
        ((View) label_target).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f));
        label_target.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        if(x.Catogery.equals("Total")){
            label_target.setTypeface(label_cat.getTypeface(), Typeface.BOLD);}
        row.addView(label_target);

        TextView label_achievement = new TextView(context);
        label_achievement.setId(View.NO_ID);
        label_achievement.setText(APPLICATION_AMOUNT_FORMAT.format(x.Achievement));
        label_achievement.setTextColor(Color.BLACK);          // part2
        label_achievement.setPadding(padding, padding, padding, padding);
        ((View) label_achievement).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f));
        label_achievement.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        if(x.Catogery.equals("Total")){
            label_achievement.setTypeface(label_cat.getTypeface(), Typeface.BOLD);}
        row.addView(label_achievement);

        TextView label_pct = new TextView(context);
        label_pct.setId(View.NO_ID);
        try {
            Double pct =x.Achievement * 100 / x.Target;
            label_pct.setText(APPLICATION_AMOUNT_FORMAT.format(pct) + "%");
            if(pct < 50)
                label_pct.setTextColor(context.getResources().getColor(R.color.error_background));
            else if (pct < 75)
                label_pct.setTextColor(context.getResources().getColor(R.color.warning_background));
            else if (pct < 100)
                label_pct.setTextColor(context.getResources().getColor(R.color.warning_background2));
            else
                label_pct.setTextColor(context.getResources().getColor(R.color.success_background));

        }catch (Exception ex){ex.printStackTrace();
            label_pct.setTextColor(Color.BLACK); }         // part2
        label_pct.setPadding(padding, padding, padding, padding);
        ((View) label_pct).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.5f));
        label_pct.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        if(x.Catogery.equals("Total")){
            label_pct.setTypeface(label_cat.getTypeface(), Typeface.BOLD);}
        row.addView(label_pct);
        return  row;
    }

    private TableRow createRowForIncentive(UserTargetDetailsView x, Context  context) {
        int padding = 8;
        int fontSize = 8;

        if (x.TotalIncAmt == null) x.TotalIncAmt = 0.0;
        if (x.TotalAmountAcc == null) x.TotalAmountAcc = 0.0;
        TableRow row = new TableRow(context);
        row.setId(View.NO_ID);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.dividerVertical, typedValue, true);
            row.setDividerDrawable(context.getResources().getDrawable(typedValue.resourceId));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (x.Catogery.equals("Total")) {
            row.setBackground(context.getDrawable(R.drawable.border_top));
        }
        //Column 1
        TextView label_cat = new TextView(context);
        label_cat.setId(View.NO_ID);
        if (x.Catogery != null)
            label_cat.setText(x.Catogery);
        label_cat.setTextColor(Color.BLACK);          // part2
        label_cat.setPadding(padding, padding, padding, padding);
        ((View) label_cat).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                3.0f));
        if (x.Catogery.equals("Total")) {
            label_cat.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            label_cat.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        }
        row.addView(label_cat);
        //Column 2
        TextView label_target = new TextView(context);
        label_target.setId(View.NO_ID);
        if (x.Catogery.equals("Total")) {
            label_target.setText("");
        } else {
            label_target.setText(APPLICATION_AMOUNT_FORMAT.format(x.TotalIncAmt < 0 ? 0 : x.TotalIncAmt));
        }
        label_target.setTextColor(Color.BLACK);          // part2
        label_target.setPadding(padding, padding, padding, padding);
        ((View) label_target).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f));
        label_target.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        if (x.Catogery.equals("Total")) {
            label_target.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        }
        row.addView(label_target);

        //Column 3
        TextView label_achievement = new TextView(context);
        label_achievement.setId(View.NO_ID);
        if (x.Catogery.equals("Total")) {
            label_achievement.setText("");
        } else {
            label_achievement.setText(APPLICATION_AMOUNT_FORMAT.format(x.TotalAmountAcc < 0 ? 0 : x.TotalAmountAcc));
        }
        label_achievement.setTextColor(Color.BLACK);          // part2
        label_achievement.setPadding(padding, padding, padding, padding);
        ((View) label_achievement).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f));
        label_achievement.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        if (x.Catogery.equals("Total")) {
            label_achievement.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        }
        row.addView(label_achievement);

        //Column 4
        TextView label_pct = new TextView(context);
        label_pct.setId(View.NO_ID);
        try {
            Double pct = x.TotalAmountAcc + x.TotalIncAmt < 0 ? 0 : x.TotalAmountAcc + x.TotalIncAmt;
            label_pct.setText(APPLICATION_AMOUNT_FORMAT.format(pct));
        } catch (Exception ex) {
            ex.printStackTrace();
            label_pct.setTextColor(Color.BLACK);
        }         // part2
        label_pct.setPadding(padding, padding, padding, padding);
        ((View) label_pct).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.5f));
        label_pct.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        if (x.Catogery.equals("Total")) {
            label_pct.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        }
        row.addView(label_pct);
        return row;
    }


    private TableRow createRowForHeader(Context  context, String[] headers) {
        int padding=8;
        int fontSize=8;
        TableRow row = new TableRow(context);
        row.setId(View.NO_ID);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.dividerVertical, typedValue, true);
            row.setDividerDrawable(context.getResources().getDrawable(typedValue.resourceId));
        }catch (Exception ex){ex.printStackTrace();}
            row.setBackground(context.getDrawable(R.drawable.line_divider));

        TextView label_cat = new TextView(context);
        label_cat.setId(View.NO_ID);
        label_cat.setText(headers[0]);
        label_cat.setTextColor(Color.GRAY);          // part2
        label_cat.setPadding(padding, padding, padding, padding);
        ((View) label_cat).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                3.0f));
            label_cat.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            label_cat.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        row.addView(label_cat);

        TextView label_target = new TextView(context);
        label_target.setId(View.NO_ID);
        label_target.setText(headers[1]);
        label_target.setTextColor(Color.GRAY);          // part2
        label_target.setPadding(padding, padding, padding, padding);
        ((View) label_target).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f));
        label_target.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            label_target.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        row.addView(label_target);

        TextView label_achievement = new TextView(context);
        label_achievement.setId(View.NO_ID);
        label_achievement.setText(headers[2]);
        label_achievement.setTextColor(Color.GRAY);          // part2
        label_achievement.setPadding(padding, padding, padding, padding);
        ((View) label_achievement).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f));
        label_achievement.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            label_achievement.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        row.addView(label_achievement);

        TextView label_pct = new TextView(context);
        label_pct.setId(View.NO_ID);
        label_pct.setText(headers[3]);
            label_pct.setTextColor(Color.GRAY);
        label_pct.setPadding(padding, padding, padding, padding);
        ((View) label_pct).setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.5f));
        label_pct.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            label_pct.setTypeface(label_cat.getTypeface(), Typeface.BOLD);
        row.addView(label_pct);
        return  row;
    }

    @Override
    public int getItemCount() {
        if(mValues==null)
            return 0;
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView targeDesc;
        public final TextView targeSite;
        public final TableLayout targetTable;
        public final TableLayout incentiveTable;
        public UserTargetDetailsView mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            targeDesc = (TextView) itemView.findViewById(R.id.target_desc);
            targeSite = (TextView) itemView.findViewById(R.id.target_site);
            targetTable = (TableLayout) itemView.findViewById(R.id.target_table);
            incentiveTable = (TableLayout) itemView.findViewById(R.id.incentive_table);
        }
    }
}
