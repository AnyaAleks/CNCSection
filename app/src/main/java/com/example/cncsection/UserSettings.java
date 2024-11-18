package com.example.cncsection;

public class UserSettings {
    public int idUser;
    public int roleUser;

    public int modeUser;
    public int savingPhotoUser;
    public int notificationUser;
    public int localisationUser;
    public String passwordUser;

    public UserSettings(int idUser, String passwordUser){
        this.idUser = idUser;
        this.passwordUser = passwordUser;
        this.modeUser=1; //светлая тема
        this.localisationUser=1; //английская локализация
        this.savingPhotoUser=1; //автоматическое сохранение фоток в галерею
        this.notificationUser=1; //разрешен приход сообщений
    }

    public UserSettings(){
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setRoleUser(int roleUser) {
        this.roleUser = roleUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public void setModeUser(int modeUser) {
        this.modeUser = modeUser;
    }

    public void setLocalisationUser(int localisationUser) {
        this.localisationUser = localisationUser;
    }

    public void setNotificationUser(int notificationUser) {
        this.notificationUser = notificationUser;
    }

    public void setSavingPhotoUser(int savingPhotoUser) {
        this.savingPhotoUser = savingPhotoUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getRoleUser() {
        return roleUser;
    }

    public int getModeUser() {
        return modeUser;
    }

    public int getLocalisationUser() {
        return localisationUser;
    }

    public int getNotificationUser() {
        return notificationUser;
    }

    public int getSavingPhotoUser() {
        return savingPhotoUser;
    }
}