package gstores.merchandiser_beta.components.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TargetTotalView implements Parcelable{
    private Double TotalTargetPerc;
    private Double TotalIncAmt ;
    private Integer LineAch ;
    private Double TotalLineAchAccAmt ;
    private Double totalBonusLinePerc ;
    private Double TotalBonusLineAmt ;

    public TargetTotalView(Double totalTargetPerc,
                           Double totalIncAmt,
                           Integer lineAch,
                           Double totalLineAchAccAmt,
                           Double totalBonusLinePerc,
                           Double totalBonusLineAmt) {
        TotalTargetPerc = totalTargetPerc;
        TotalIncAmt = totalIncAmt;
        LineAch = lineAch;
        TotalLineAchAccAmt = totalLineAchAccAmt;
        this.totalBonusLinePerc = totalBonusLinePerc;
        TotalBonusLineAmt = totalBonusLineAmt;
    }



    protected TargetTotalView(Parcel in) {
        if (in.readByte() == 0) {
            TotalTargetPerc = null;
        } else {
            TotalTargetPerc = in.readDouble();
        }
        if (in.readByte() == 0) {
            TotalIncAmt = null;
        } else {
            TotalIncAmt = in.readDouble();
        }
        if (in.readByte() == 0) {
            LineAch = null;
        } else {
            LineAch = in.readInt();
        }
        if (in.readByte() == 0) {
            TotalLineAchAccAmt = null;
        } else {
            TotalLineAchAccAmt = in.readDouble();
        }
        if (in.readByte() == 0) {
            totalBonusLinePerc = null;
        } else {
            totalBonusLinePerc = in.readDouble();
        }
        if (in.readByte() == 0) {
            TotalBonusLineAmt = null;
        } else {
            TotalBonusLineAmt = in.readDouble();
        }
    }

    public Double getTotalIncAmt() {
        return TotalIncAmt;
    }

    public static final Creator<TargetTotalView> CREATOR = new Creator<TargetTotalView>() {
        @Override
        public TargetTotalView createFromParcel(Parcel in) {
            return new TargetTotalView(in);
        }

        @Override
        public TargetTotalView[] newArray(int size) {
            return new TargetTotalView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.TotalTargetPerc);
        dest.writeDouble(this.TotalIncAmt);
        dest.writeInt(this.LineAch);
        dest.writeDouble(this.TotalLineAchAccAmt);
        dest.writeDouble(this.TotalBonusLineAmt);
        dest.writeDouble(this.TotalBonusLineAmt);
    }
}
