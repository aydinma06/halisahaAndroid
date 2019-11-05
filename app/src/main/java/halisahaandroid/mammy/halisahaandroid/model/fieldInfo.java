package halisahaandroid.mammy.halisahaandroid.model;

import java.util.ArrayList;

public class fieldInfo {
    private int fieldID;
    private String fieldName;
    private String fieldAddress;
    private String fieldPhone;

    public fieldInfo(String fieldNamein, String fieldAddressin,int fieldIdin,String fieldPhonein) {
        fieldName = fieldNamein;
        fieldAddress = fieldAddressin;
        fieldID = fieldIdin;
        fieldPhone=fieldPhonein;
    }

    public String getfieldName() {
        return fieldName;
    }

    public String getfieldAddress() {
        return fieldAddress;
    }

    public int getFieldID() {return fieldID;}
    public String getFieldPhone(){return fieldPhone;}

    private static int lastContactId = 0;

    public static ArrayList<fieldInfo> createContactsList(int numUsers) {
        ArrayList<fieldInfo> contacts = new ArrayList<fieldInfo>();

        for (int i = 1; i <= numUsers; i++) {
            contacts.add(new fieldInfo("Person " + ++lastContactId,"",0,""));
        }

        return contacts;
    }
}