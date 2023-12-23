package com.moutamid.sprachelernen.models;

import java.util.ArrayList;

public class ExerciseListModel {
    String name;
    ArrayList<ExerciseModel> list;

    public ExerciseListModel() {
    }

    public ExerciseListModel(String name, ArrayList<ExerciseModel> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ExerciseModel> getList() {
        return list;
    }

    public void setList(ArrayList<ExerciseModel> list) {
        this.list = list;
    }
}
