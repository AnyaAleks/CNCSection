package com.example.cncsection;

public class People {

    private String fio; // ФИО
    private int role; // Добавлено поле для роли
    private int id;

    public People(String fio, int role, int id) { // Добавлен параметр для роли
        this.fio = fio;
        this.role = role; // Инициализация роли
        this.id = id;
    }

    public String getfio() {
        return this.fio;
    }

    public int getRole() { // Добавлен геттер для роли
        return this.role;
    }

    public int getId() { // Добавлен геттер для роли
        return this.id;
    }
}
