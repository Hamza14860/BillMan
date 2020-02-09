package com.hamzaazam.fyp_frontend;

import android.os.Parcel;
import android.os.Parcelable;


public class UserInfo implements Parcelable {
    public String Name, Email, ProfileURL;

    public UserInfo(){

    }

    public UserInfo(String name, String email, String profileURL) {
        Name = name;
        Email = email;
        ProfileURL = profileURL;
    }

    protected UserInfo(Parcel in) {
        Name = in.readString();
        Email = in.readString();
        ProfileURL = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };


    public static Creator<UserInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Email);
        dest.writeString(ProfileURL);
    }
}
