package model;

import java.io.Serializable;

public class Abiturient implements Serializable, Comparable<Abiturient> {
    private String id;
    private String name;
    private String surname;
    private String address;
    private String phoneNumber;
    private int[] marks;

    public Abiturient(String id, String name, String surname, String address, String phoneNumber, int[] marks) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int[] getMarks() {
        return marks;
    }

    public void setMarks(int[] marks) {
        this.marks = marks;
    }

    public Integer calculateSum(){
        Integer sum = 0;
        for (Integer mark : marks)
            sum += mark;
        return sum;
    }

    public int compareTo(Abiturient o) {
        return this.calculateSum().compareTo(o.calculateSum());
    }
}