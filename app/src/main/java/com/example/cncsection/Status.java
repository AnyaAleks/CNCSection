package com.example.cncsection;

import java.util.ArrayList;

public class Status {

    private String application; // название заявки
    private int status; // статус заявки
    private int id_order;

    public Status(String application, int status, int id_order) {
        this.application = application;
        this.status = status;
        this.id_order = id_order;
    }

    public String getApplication() {
        return this.application;
    }

    public int getStatus() {
        return this.status;
    }

    public int getIdOrder() {
        return this.id_order;
    }



    //Потом стереть - просто для проверки заполнения данными
    private static int lastStatusId = 0;
    public static ArrayList<Status> createStatusesList(int numContacts) {


        ArrayList<Status> statuses = new ArrayList<Status>();

        for (int i = 1; i <= numContacts; i++) {
            statuses.add(new Status("Status " + ++lastStatusId, 1, 2));
        }

        return statuses;
    }
}
