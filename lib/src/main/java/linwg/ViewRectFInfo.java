package linwg;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author linwg
 * @date 2018/3/3
 */

 class ViewRectFInfo implements Parcelable {
     ImageRectFInfo[] imgLocations;
     float leftOffset;
     float topOffset;
     float bottomOffset;
     float rightOffset;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.imgLocations, flags);
        dest.writeFloat(this.leftOffset);
        dest.writeFloat(this.topOffset);
        dest.writeFloat(this.bottomOffset);
        dest.writeFloat(this.rightOffset);
    }

     ViewRectFInfo() {
    }

    protected ViewRectFInfo(Parcel in) {
        this.imgLocations = in.createTypedArray(ImageRectFInfo.CREATOR);
        this.leftOffset = in.readFloat();
        this.topOffset = in.readFloat();
        this.bottomOffset = in.readFloat();
        this.rightOffset = in.readFloat();
    }

    public static final Parcelable.Creator<ViewRectFInfo> CREATOR = new Parcelable.Creator<ViewRectFInfo>() {
        @Override
        public ViewRectFInfo createFromParcel(Parcel source) {
            return new ViewRectFInfo(source);
        }

        @Override
        public ViewRectFInfo[] newArray(int size) {
            return new ViewRectFInfo[size];
        }
    };
}
