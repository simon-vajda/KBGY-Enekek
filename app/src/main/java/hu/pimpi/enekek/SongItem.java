package hu.pimpi.enekek;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class SongItem implements Parcelable {
    private String title;
    private String filename;

    public SongItem(String title, String filename) {
        this.title = title;
        this.filename = filename;
    }

    protected SongItem(Parcel in) {
        title = in.readString();
        filename = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(filename);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SongItem> CREATOR = new Creator<SongItem>() {
        @Override
        public SongItem createFromParcel(Parcel in) {
            return new SongItem(in);
        }

        @Override
        public SongItem[] newArray(int size) {
            return new SongItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongItem songItem = (SongItem) o;
        return title.equals(songItem.title) &&
                filename.equals(songItem.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, filename);
    }

    @Override
    public String toString() {
        return title;
    }
}
