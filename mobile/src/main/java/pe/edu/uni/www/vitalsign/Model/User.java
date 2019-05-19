package pe.edu.uni.www.vitalsign.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String id;
    String username;
    String nombres;
    String apePat;
    String apeMat;
    Point location;

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        nombres = in.readString();
        apePat = in.readString();
        apeMat = in.readString();
        location = in.readParcelable(Point.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(nombres);
        dest.writeString(apePat);
        dest.writeString(apeMat);
        dest.writeParcelable(location, flags);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
