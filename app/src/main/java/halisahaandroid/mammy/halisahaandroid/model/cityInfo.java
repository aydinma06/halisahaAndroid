package halisahaandroid.mammy.halisahaandroid.model;


import java.util.ArrayList;

public class cityInfo {
    private String cityID;
    private String cityName;

    public cityInfo(String cityNamein) {
        cityName = cityNamein;
    }

    public cityInfo(String cityIdin, String cityNamein) {
        cityID = cityIdin;
        cityName = cityNamein;
    }

    public String getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    private static int lastContactId = 0;

    public static ArrayList<cityInfo> createContactsList(int numUsers) {
        ArrayList<cityInfo> contacts = new ArrayList<cityInfo>();

        for (int i = 1; i <= numUsers; i++) {
            contacts.add(new cityInfo("Person " + ++lastContactId, ""));
        }

        return contacts;
    }
}
