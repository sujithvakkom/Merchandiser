package gstores.merchandiser2.components.models.homedelivery;

import android.support.annotation.NonNull;

public class Brand {
    public String BrandID;
    public String BrandCode;

    @NonNull
    @Override
    public String toString() {
        return BrandCode;
    }
}