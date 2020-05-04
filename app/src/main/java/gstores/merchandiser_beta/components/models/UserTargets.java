package gstores.merchandiser_beta.components.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserTargets implements Parcelable {

    private int target_id ;
    private Date start_date ;
    private Date end_date;
    private String Month ;
    private List<TargetAchievementView> TargetDetailsView ;
    private List<TargetTotalView> TargetTotalView;

    public UserTargets(int target_id,
                       Date start_date,
                       Date end_date,
                       String month,
                       List<TargetAchievementView> targetDetailsView,
                       List<TargetTotalView> targetTotalView) {
        this.target_id = target_id;
        this.start_date = start_date;
        this.end_date = end_date;
        Month = month;
        TargetDetailsView = targetDetailsView;
        TargetTotalView = targetTotalView;
    }

    @Override
    public String toString() {
        SimpleDateFormat month_date = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        String month_name = month_date.format(start_date);
        return  month_name;
    }

    public static final Creator<UserTargets> CREATOR = new Creator<UserTargets>() {
        @Override
        public UserTargets createFromParcel(Parcel in) {
            return new UserTargets(in);
        }

        @Override
        public UserTargets[] newArray(int size) {
            return new UserTargets[size];
        }

    };

    public List<TargetAchievementView> getTargetDetailsView() {
        return TargetDetailsView;
    }

    public List<TargetTotalView> getTargetTotalView() {
        return TargetTotalView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.target_id);
        dest.writeSerializable(this.start_date);
        dest.writeSerializable(this.end_date);
        dest.writeString(this.Month);
        dest.writeList(this.TargetDetailsView);
        dest.writeList(this.TargetTotalView);
    }

    protected UserTargets(Parcel in) {
        if (in.readByte() == 0) {
            this.target_id = 0;
        }
        else {
            this.target_id = in.readInt();
        }
        this.start_date = (Date)in.readSerializable();
        this.end_date = (Date)in.readSerializable();
        this.Month = in.readString();
        in.readList(this.TargetDetailsView, null);
        in.readList(this.TargetTotalView, null);
    }
}
