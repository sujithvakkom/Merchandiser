package gstores.merchandiser_beta.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;

public final class AppLiterals {
    public static final int REQUEST_READ_INTERNET= 1000;
    public static final int REQUEST_FORGROUND_SERVICE= 1001;
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String AMOUNT_FORMAT = "#,###,##0.00";
    public static final String DATE_FORMAT_MONTH_YEAR = "MMM yyyy";
    public static final String DATE_FORMAT_DAY = "dd";
    public static final String DATE_FORMAT_SMALL = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SMALL_VIEW = "dd-MMM-yyyy";
    public static final Locale LOCALE_EN = new Locale.Builder().setLanguage("en").setRegion("US").build();
    public static final DecimalFormat APPLICATION_AMOUNT_FORMAT = new DecimalFormat(AMOUNT_FORMAT);
    public static final DateFormat APPLICATION_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);
    public static final DateFormat APPLICATION_DATE_SMALL_FORMAT = new SimpleDateFormat(DATE_FORMAT_SMALL);
    public static final DateFormat APPLICATION_DATE_MONTH_YEAR_FORMAT = new SimpleDateFormat(DATE_FORMAT_MONTH_YEAR);
    public static final DateFormat APPLICATION_DATE_DAY_FORMAT = new SimpleDateFormat(DATE_FORMAT_DAY);
    public static final DateFormat APPLICATION_DATE_SMALL_VIEW_FORMAT = new SimpleDateFormat(DATE_FORMAT_SMALL_VIEW,LOCALE_EN);

    public static final DecimalFormat DISPLAY_NUMBER_FORMAT_0_00 = new DecimalFormat("#.00");
    public static final DecimalFormat DISPLAY_NUMBER_FORMAT_0 = new DecimalFormat("#");

    public static final String CHANNEL_ID = "gstores.merchandiser2.channel";
    public static final CharSequence CHANNEL_NAME = "Merchandiser Notification";

    public static final CharSequence NOTIFICATION_TITLE = "GS Merchandiser";
    public static final String HYPHON = "-";
    public static final String ITEM = "ITEM";

    public static final Gson APPLICATION_GSON_BUILDER = new GsonBuilder()
            .setDateFormat(AppLiterals.DATE_FORMAT)
            .create();
    public static final String MOB = "MOB";
    public static final String MERCHAND = "merchand";
    public static String NULL_STRING = "EOL";
    public static Integer NULL_INTEGER = -1;
    public static String DEFAULT_USER_GROUP_ID = "2";
    final public static String MODEL = "MODEL";
    final public static String QUANTITY="QUANTITY";
    final public static String VALUE="VALUE";
    final public static String SELECTDATE="SELECTDATE";
    final public static String ISRETURN="ISRETURN";
    public static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    public static final String PROFILE_ROOT = "Profile";
    public static final String PROFILE_ROLE = "Role";
    public static final String USER_DETAIL = "UserDetails";

    public class RequestCodes {
        public static final int LOGIN_REQUEST = 1001;
        public static final int CUSTOMER_REQUEST = 1002;
        public static final int ITEM_REQUEST = 1003;
        public static final int REQUEST_IMAGE_CAPTURE = 1004;
        public static final int REQUEST_IMAGE_GALLERY = 1005;
        public static final int ADD_SALES = 1006;
    }

    public class ExtraLabels {
        public static final String DELIVERY_HEADER = "DELIVERY_HEADER";
        public static final String DELIVERY_TYPES = "saleTypes";
        public static final String SALE_TYPES = "saleTypes";
    }

    public static final String DELIVERY_CLASS_NAME = DeliveryHeader.class.getName();
}
