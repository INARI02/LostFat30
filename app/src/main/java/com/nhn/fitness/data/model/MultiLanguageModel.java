package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class MultiLanguageModel implements Parcelable {
    private HashMap<String, String> hashMap;

    public MultiLanguageModel(){
        hashMap = new HashMap<String, String>();
    }

    public MultiLanguageModel(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.hashMap);
    }

    protected MultiLanguageModel(Parcel in) {
        this.hashMap = (HashMap<String, String>) in.readSerializable();
    }

    public static final Parcelable.Creator<MultiLanguageModel> CREATOR = new Parcelable.Creator<MultiLanguageModel>() {
        @Override
        public MultiLanguageModel createFromParcel(Parcel source) {
            return new MultiLanguageModel(source);
        }

        @Override
        public MultiLanguageModel[] newArray(int size) {
            return new MultiLanguageModel[size];
        }
    };
}
