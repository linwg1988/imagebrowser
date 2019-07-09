package linwg;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageRectFInfo implements Parcelable {
    public RectF rectF;
    public String scaleType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.rectF, flags);
        dest.writeString(this.scaleType);
    }

    public ImageRectFInfo() {
    }

    protected ImageRectFInfo(Parcel in) {
        this.rectF = in.readParcelable(RectF.class.getClassLoader());
        this.scaleType = in.readString();
    }

    public static final Parcelable.Creator<ImageRectFInfo> CREATOR = new Parcelable.Creator<ImageRectFInfo>() {
        @Override
        public ImageRectFInfo createFromParcel(Parcel source) {
            return new ImageRectFInfo(source);
        }

        @Override
        public ImageRectFInfo[] newArray(int size) {
            return new ImageRectFInfo[size];
        }
    };
}
