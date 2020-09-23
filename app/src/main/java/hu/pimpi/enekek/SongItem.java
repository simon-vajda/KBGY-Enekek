package hu.pimpi.enekek;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SongItem implements Parcelable {
    private String filename;
    private String title;
    private String lyrics;

    public SongItem(String filename, String title, String lyrics) {
        this.filename = filename;
        this.title = title;
        this.lyrics = lyrics;
    }

    protected SongItem(Parcel in) {
        filename = in.readString();
        title = in.readString();
        lyrics = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filename);
        dest.writeString(title);
        dest.writeString(lyrics);
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongItem songItem = (SongItem) o;
        return filename.equals(songItem.filename) &&
                title.equals(songItem.title) &&
                Objects.equals(lyrics, songItem.lyrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, title, lyrics);
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
