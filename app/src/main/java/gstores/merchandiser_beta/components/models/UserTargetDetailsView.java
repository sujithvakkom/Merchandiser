package gstores.merchandiser_beta.components.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class UserTargetDetailsView
        implements Parcelable , Serializable {
    @SerializedName("UserName")
    public String UserName ;
    @SerializedName("FullName")
    public String FullName ;
    @SerializedName("Location")
    public String Location;
    @SerializedName("Period")
    public String Period;
    @SerializedName("Catogery")
    public String Catogery;
    @SerializedName("Target")
    public Double Target;
    @SerializedName("Achievement")
    public Double Achievement;

    @SerializedName("Lines")
    public List<UserTargetDetailsView> Lines;

    public UserTargetDetailsView(String userName,String fullName ,String location,String period, String catogery,Double target,Double achievement){

        UserName = userName;
        FullName = fullName;
        Location = location;
        Period = period;
        Catogery = catogery;
        Target = target;
        Achievement = achievement;
    }

    protected UserTargetDetailsView(Parcel in) {
        UserName = in.readString();
        FullName = in.readString();
        Location = in.readString();
        Period = in.readString();
        Catogery = in.readString();
        Target = in.readDouble();
        Achievement = in.readDouble();
    }

    public static final Creator<UserTargetDetailsView> CREATOR = new Creator<UserTargetDetailsView>() {
        @Override
        public UserTargetDetailsView createFromParcel(Parcel in) {
            return new UserTargetDetailsView(in);
        }

        @Override
        public UserTargetDetailsView[] newArray(int size) {
            return new UserTargetDetailsView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserName);
        dest.writeString(FullName);
        dest.writeString(Location);
        dest.writeString(Period);
        dest.writeString(Catogery);
        dest.writeDouble(Target);
        dest.writeDouble(Achievement);
    }
}
