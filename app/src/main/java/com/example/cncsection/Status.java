package com.example.cncsection;

import java.util.ArrayList;

public class Status {

    private String application; // название заявки
    private String status; // статус заявки

    public Status(String application, String status) {
        this.application = application;
        this.status = status;
    }

    public String getApplication() {
        return this.application;
    }

    public String getStatus() {
        return this.status;
    }



    //Потом стереть - просто для проверки заполнения данными
    private static int lastStatusId = 0;
    public static ArrayList<Status> createStatusesList(int numContacts) {
        ArrayList<Status> statuses = new ArrayList<Status>();

        for (int i = 1; i <= numContacts; i++) {
            statuses.add(new Status("Status " + ++lastStatusId, "blabla"));
        }

        return statuses;
    }
}
