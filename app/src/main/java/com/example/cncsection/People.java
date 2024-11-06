package com.example.cncsection;

public class People {

    private String fio; // ФИО
    private int role; // Добавлено поле для роли

    public People(String fio, int role) { // Добавлен параметр для роли
        this.fio = fio;
        this.role = role; // Инициализация роли
    }

    public String getfio() {
        return this.fio;
    }

    public int getRole() { // Добавлен геттер для роли
        return this.role;
    }
}
