package com.pifactorial.ebpm.data;
import android.os.Parcelable;
import org.daveware.passwordmaker.Profile;
import android.os.Parcel;

public class ParcelableProfile extends Profile implements Parcelable {

    private int mData;

    public ParcelableProfile() {

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<ParcelableProfile> CREATOR
        = new Parcelable.Creator<ParcelableProfile>() {
            public ParcelableProfile createFromParcel(Parcel in) {
                return new ParcelableProfile(in);
            }

            public ParcelableProfile[] newArray(int size) {
                return new ParcelableProfile[size];
            }
        };

    private ParcelableProfile(Parcel in) {

        mData = in.readInt();
    }
}
