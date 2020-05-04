package gstores.merchandiser_beta.customviews;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.Date;
import java.util.List;


import gstores.merchandiser_beta.R;
import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.models.Item;
import gstores.merchandiser_beta.components.models.ItemModel;
import gstores.merchandiser_beta.components.models.Model;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryLine;
import gstores.merchandiser_beta.components.viewhelpers.ItemModelArrayAdapter;
import gstores.merchandiser_beta.components.viewhelpers.SelectItemDialogListener;
import gstores.merchandiser_beta.components.web.BusinessExcelService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static gstores.merchandiser_beta.components.AppLiterals.ISRETURN;
import static gstores.merchandiser_beta.components.AppLiterals.MODEL;
import static gstores.merchandiser_beta.components.AppLiterals.QUANTITY;
import static gstores.merchandiser_beta.components.AppLiterals.SELECTDATE;
import static gstores.merchandiser_beta.components.AppLiterals.VALUE;


public class SelectItemActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SelectItemDialogListener {
    private ListView itemModelList;
    private Model selectedModel;
    private ProgressBar progressBar;
    private int page=1;
    private String EXTENDED_FILTER_OUT_STATE="EXTENDED_FILTER_OUT_STATE";
    private boolean extendedFilter=false;
    private String FILTER_OUT_STATE ="FILTER_OUT_STATE";
    private String filter ="";
    private ArrayAdapter<ItemModel> adaptor;
    List<ItemModel> resultList;
    private View parentView;
    private FloatingActionButton extendedFilterButton;

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int threshold = 1;
                int count = itemModelList.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (itemModelList.getLastVisiblePosition() >= count - threshold) {

                        // Execute LoadMoreDataTask AsyncTask
                        page += 1;
                        setupModel(filter, page,itemModelList.getLastVisiblePosition());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            try{
                filter = savedInstanceState.getString(FILTER_OUT_STATE);
            }
            catch (Exception ex){ex.printStackTrace();}
            try{
                extendedFilter = savedInstanceState.getBoolean(EXTENDED_FILTER_OUT_STATE);
            }
            catch (Exception ex){ex.printStackTrace();}
        }

        setContentView(R.layout.activity_select_item);

        Toolbar toolbar = findViewById(R.id.selectItemDialogToolBar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getStatusBarColor();
        }

        itemModelList = (ListView) findViewById(R.id.itemListView);
        itemModelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        progressBar = (ProgressBar)findViewById(R.id.item_load_progress) ;
        extendedFilterButton = (FloatingActionButton)findViewById(R.id.extended_filter) ;
        extendedFilterButton.setSupportBackgroundTintList(ColorStateList.valueOf(
                extendedFilter==true?Color.BLUE:Color.GRAY));
        extendedFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extendedFilter = !extendedFilter;
                extendedFilterButton.setSupportBackgroundTintList(ColorStateList.valueOf(
                        extendedFilter==true?
            getResources().getColor(R.color.success_background)
            :Color.LTGRAY));
                page   =1;
                setupModel(filter, page,0);
            }
        });
        page   =1;
        filter = "";
        setupModel(filter,page,0);

        itemModelList.setOnScrollListener(onScrollListener());

        itemModelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ItemModel temp = (ItemModel)itemModelList.getItemAtPosition(position);
                    Model item = new Model(temp.getModel_id(),temp.getModel_description());
                    item.setItem(new Item(
                            (int)temp.getInventory_item_id(),
                            -1,
                            (int)temp.getModel_id(),
                            -1,
                            temp.getItem_code(),
                            temp.getDescription()
                    ));
                    item.getItem().setModel(
                            new Model(temp.getModel_id(),temp.getModel_description())
                    );
                    item.setValue(temp.getPrice());
                    selectedModel = item;
                    openSalesConfigDialog();
                } catch (ClassCastException ex) {
                    ex.printStackTrace();
                }
            }
        });
        parentView = findViewById(R.id.itemListView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(FILTER_OUT_STATE,filter);
        outState.putBoolean(EXTENDED_FILTER_OUT_STATE,extendedFilter);
    }

    private void openSalesConfigDialog() {
        SaleConfigDialog dialog = new SaleConfigDialog();
        dialog.model=SelectItemActivity.this.selectedModel;
        dialog.setOnSaleConfigDialogListener(new SaleConfigDialog.SaleConfigDialogListner() {
            @Override
            public void success(double quantity, double value) {
                selectedModel.setQuantity(quantity);
                selectedModel.setValue(value);

                Intent intent = new Intent();
                DeliveryLine line = new DeliveryLine();
                line.ItemCode = SelectItemActivity.this.selectedModel.getItem().getItemCode();
                line.Description = SelectItemActivity.this.selectedModel.getItem().getDescriptionString();
                line.OrderQuantity = SelectItemActivity.this.selectedModel.getQuantity();
                line.SelleingPrice =SelectItemActivity.this.selectedModel.getValue();
                intent.putExtra(AppLiterals.ITEM,line);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(),"Item Select");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_item, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            page   =1;
            filter = s;
            setupModel(filter,page,0);
            /*
            ((ArrayAdapter<Item>) itemModelList.getAdapter())
                    .getFilter().filter(s.toString().trim());
                    */
        } catch (ClassCastException ex) {
            ex.printStackTrace();

        }
        return false;
    }

    private void setupModel(String filter, final int page, final int view) {
        progressBar.setVisibility(View.VISIBLE);
        String baseUrl = getApplicationContext().getResources().getString(
                R.string.pref_head_office_default
        );
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        BusinessExcelService client = retrofit.create(BusinessExcelService.class);

        Call<List<ItemModel>> call = client.AllProducts(filter,page,extendedFilter);
        call.enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                if(response.body()!=null) {
                    if (page == 1)
                        resultList = response.body();
                    else
                        resultList.addAll(response.body());
                    if (resultList.size() == 0) {
                        Snackbar.make(parentView, "No Items found", Snackbar.LENGTH_LONG).show();
                    }
                    ItemModel[] arrayItemModel = new ItemModel[resultList.size()];
                    arrayItemModel = resultList.toArray(arrayItemModel);
                    adaptor = new ItemModelArrayAdapter(
                            getApplicationContext(),
                            arrayItemModel);
                    itemModelList.setAdapter(adaptor);
                    itemModelList.setSelection(view);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                Exception ex = new Exception(t);
                String x =ex.getMessage();
                Util.SimpleSnackBar(parentView,"Error occurred.",Snackbar.LENGTH_LONG);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void addModel(Model model, Integer quantity, Integer value) {
        Intent intent = new Intent();
        intent.putExtra(MODEL,model);
        intent.putExtra(QUANTITY,quantity);
        intent.putExtra(VALUE,value);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void addModel(Model model, Integer quantity, Integer value, boolean isReturn, Date date) {
        Intent intent = new Intent();
        intent.putExtra(MODEL,model);
        intent.putExtra(QUANTITY,quantity);
        intent.putExtra(VALUE,value);
        intent.putExtra(SELECTDATE,date.getTime());
        intent.putExtra(ISRETURN,isReturn);
        setResult(RESULT_OK,intent);
        finish();
    }
}