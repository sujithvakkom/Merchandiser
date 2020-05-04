package gstores.merchandiser_beta.components.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class TargetAchievementView implements Parcelable {
    private Integer user_id;
    private String Category;
    private Double Target_amt;
    private Double Achieved_Amt;
    private Double Entered_Amt;
    private boolean has_bonus;
    private String Month;
    private Date start_date;
    private Date end_date;
    private Double Base_Incentive;
    private Integer category_id;
    private Integer target_id;
    private List<TargetTotalView> TargetTotal;

    public TargetAchievementView(Integer user_id,
                                 String category,
                                 Double target_amt,
                                 Double achieved_Amt,
                                 Double entered_Amt,
                                 boolean has_bonus,
                                 String month,
                                 Date start_date,
                                 Date end_date,
                                 Double base_Incentive,
                                 Integer category_id,
                                 Integer target_id,
                                 List<TargetTotalView> targetTotal) {
        this.user_id = user_id;
        Category = category;
        Target_amt = target_amt;
        Achieved_Amt = achieved_Amt;
        Entered_Amt = entered_Amt;
        this.has_bonus = has_bonus;
        Month = month;
        this.start_date = start_date;
        this.end_date = end_date;
        Base_Incentive = base_Incentive;
        this.category_id = category_id;
        this.target_id = target_id;
        TargetTotal = targetTotal;
    }

    protected TargetAchievementView(Parcel in) {
        if (in.readByte() == 0) {
            user_id = null;
        } else {
            user_id = in.readInt();
        }
        Category = in.readString();
        if (in.readByte() == 0) {
            Target_amt = null;
        } else {
            Target_amt = in.readDouble();
        }
        if (in.readByte() == 0) {
            Achieved_Amt = null;
        } else {
            Achieved_Amt = in.readDouble();
        }
        if (in.readByte() == 0) {
            Entered_Amt = null;
        } else {
            Entered_Amt = in.readDouble();
        }
        has_bonus = in.readByte() != 0;
        Month = in.readString();
        if (in.readByte() == 0) {
            Base_Incentive = null;
        } else {
            Base_Incentive = in.readDouble();
        }
        if (in.readByte() == 0) {
            category_id = null;
        } else {
            category_id = in.readInt();
        }
        if (in.readByte() == 0) {
            target_id = null;
        } else {
            target_id = in.readInt();
        }
        in.readList(this.TargetTotal, null);
    }

    public static final Creator<TargetAchievementView> CREATOR = new Creator<TargetAchievementView>() {
        @Override
        public TargetAchievementView createFromParcel(Parcel in) {
            return new TargetAchievementView(in);
        }

        @Override
        public TargetAchievementView[] newArray(int size) {
            return new TargetAchievementView[size];
        }
    };

    public String getCategory() {
        return Category;
    }

    public Double getTarget_amt() {
        return Target_amt;
    }

    public Double getAchieved_Amt() {
        return Achieved_Amt;
    }

    public List<TargetTotalView> getTargetTotal() {
        return TargetTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id );
        dest.writeString(this.Category );
        dest.writeDouble(this.Target_amt );
        dest.writeDouble(this.Achieved_Amt );
        dest.writeDouble(this.Entered_Amt );
        dest.writeByte((byte) (this.has_bonus ? 1 : 0));
        dest.writeString(this.Month );
        dest.writeSerializable(this.start_date );
        dest.writeSerializable(this.end_date );
        dest.writeDouble(this.Base_Incentive );
        dest.writeInt(this.category_id );
        dest.writeInt(this.target_id );
        dest.writeList(this.TargetTotal );
    }
}