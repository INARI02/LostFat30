package com.nhn.fitness.data.model;

import com.google.android.gms.maps.model.LatLng;

public class PlaceGymEntity {

        private final String name,address,content;
        private final int photoBG;
        private LatLng location;

        public PlaceGymEntity(LatLng location,String name, String address, String content, int photoBG) {
            this.location=location;
            this.name = name;
            this.address = address;
            this.content = content;
            this.photoBG = photoBG;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }


        public String getContent() {
            return content;
        }


        public int getPhotoBG() {
            return photoBG;
        }

        public LatLng getLocation() {
            return location;
        }

}
