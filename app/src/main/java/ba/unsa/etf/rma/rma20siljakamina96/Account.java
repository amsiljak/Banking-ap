package ba.unsa.etf.rma.rma20siljakamina96;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    double budget;
    double totalLimit;
    double monthLimit;

    public Account(double budget, double totalLimit, double monthLimit) {
        this.budget = budget;
        this.totalLimit = totalLimit;
        this.monthLimit = monthLimit;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(double totalLimit) {
        this.totalLimit = totalLimit;
    }

    public double getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(double monthLimit) {
        this.monthLimit = monthLimit;
    }

    protected Account(Parcel in) {
        budget = in.readDouble();
        totalLimit = in.readDouble();
        monthLimit = in.readDouble();
    }
    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(budget);
        dest.writeDouble(totalLimit);
        dest.writeDouble(monthLimit);
    }
    @Override
    public int describeContents() {
        return 0;
    }
}
