package halisahaandroid.mammy.halisahaandroid.model;


import java.util.ArrayList;

public class calendarInfo {
    private int calendarID;
    private String calendarName;
    private String calendarAddress;
    private int actionType;

    public calendarInfo(String fieldNamein, String fieldAddressin,int fieldIdin,int actionTypein) {
        calendarName = fieldNamein;
        calendarAddress = fieldAddressin;
        calendarID = fieldIdin;
        actionType =actionTypein;
    }

    public String getfieldName() {
        return calendarName;
    }

    public String getfieldAddress() {
        return calendarAddress;
    }

    public int getFieldID() {return calendarID;}

    public int getActionType(){return actionType;}

    private static int lastContactId = 0;

    public static ArrayList<calendarInfo> createContactsList(int numUsers) {
        ArrayList<calendarInfo> contacts = new ArrayList<>();

        for (int i = 1; i <= numUsers; i++) {
            contacts.add(new calendarInfo("Person " + ++lastContactId,"",0,0));
        }

        return contacts;
    }
}