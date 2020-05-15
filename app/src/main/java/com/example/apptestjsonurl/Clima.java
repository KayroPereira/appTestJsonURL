package com.example.apptestjsonurl;

import java.io.Serializable;
import java.util.ArrayList;

public class Clima implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<ClimaDia> climaDia;
    private String temp;
    private String date;
    private String humidity;
    private String cityName;

    public Clima(){}

    public Clima(String temp, String date, String humidity, String cityName) {
        this.temp = temp;
        this.date = date;
        this.humidity = humidity;
        this.cityName = cityName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ArrayList<ClimaDia> getClimaDia() {
        return climaDia;
    }

    public void setClimaDia(ArrayList<ClimaDia> climaDia) {
        this.climaDia = climaDia;
    }

    public static class ClimaDia{
        private String date;
        private String weekday;
        private String max;
        private String min;
        private String condition;

        public ClimaDia(){
        };

        public ClimaDia(String date, String weekday, String max, String min, String condition) {
            this.date = date;
            this.weekday = weekday;
            this.max = max;
            this.min = min;
            this.condition = condition;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeekday() {
            return weekday;
        }

        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }
}