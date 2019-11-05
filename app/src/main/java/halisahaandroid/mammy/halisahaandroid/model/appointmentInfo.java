package halisahaandroid.mammy.halisahaandroid.model;

import java.util.ArrayList;

public class appointmentInfo
{
    private String calendarID;
    private String calendarName;
    private String calendarAddress;
    private String startDate;
    private String finishDate;
    private int actionType;
    private int fieldID;
    private int calendarLastID;

    public appointmentInfo(String fieldIdin,int actionTypein,String startDatein,String finishDatein ,int fieldIDin,int calendarLastIDin) {
        calendarID = fieldIdin;
        actionType =actionTypein;
        startDate=startDatein;
        finishDate=finishDatein;
        fieldID=fieldIDin;
        calendarLastID=calendarLastIDin;
    }

    public String getfieldName() {
        return calendarName;
    }

    public String getfieldAddress() {
        return calendarAddress;
    }

    public String getFieldID() {return calendarID;}

    public int getActionType(){return actionType;}

    public String getStartDate() {
        return startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public int getfieldID(){return fieldID;}

    public int getcalendarLastID(){return calendarLastID;}

    private static int lastContactId = 0;

    public static ArrayList<calendarInfo> createContactsList(int numUsers) {
        ArrayList<calendarInfo> contacts = new ArrayList<>();

        for (int i = 1; i <= numUsers; i++) {
            contacts.add(new calendarInfo("Person " + ++lastContactId,"",0,0));
        }

        return contacts;
    }
}
