package com.example.cncsection;

public class Status {
    private final String application; // название заявки
    private final String status;  // статус заявки

    public Status(String application, String status){
        this.application=application;
        this.status=status;
    }

    public String getApplication() {return this.application;}
    public String getStatus() {return this.status;}

}
