package com.company;

import java.util.ArrayList;

public class GameTable {
    //array cu toate elementele matricei
    private ArrayList<String> gameValues;
    //array cu pozitiile pe care se afla corp&&coada&&aripi matrice
    private ArrayList<Integer> planeABodyIndexes;
    private ArrayList<Integer> planeBBodyIndexes;
    private ArrayList<Integer> planeCBodyIndexes;

    private int planeAHeadIndex;
    private int planeBHeadIndex;
    private int planeCHeadIndex;
    private int planesDestroyed;

    private final int lineLength = 10;

    // deep copy
    private void copyArrayInt(ArrayList<Integer> dest, ArrayList<Integer> source) {
        for (int i = 0; i < source.size(); i++) {
            dest.add(source.get(i));
        }
    }

    // deep copy
    private void copyArrayString(ArrayList<String> dest, ArrayList<String> source) {
        for (int i = 0; i < source.size(); i++) {
            dest.add(new String(source.get(i)));
        }
    }

    //dupa ce avionul e distrus, setam valorile capului&corpului&cozii&aripilor cu 0 => dispare din matrice
    private void destroyPlane(int planeHeadIndex, ArrayList<Integer> bodyIndexes) {
        this.gameValues.set(planeHeadIndex, "0");

        for (Integer currentIndex : bodyIndexes) {
            this.gameValues.set(currentIndex, "0");
        }
    }

    public String shoot(int row, int column) {
        int index = ((row - 1) * lineLength) + (column - 1);
        String valueToCheck = gameValues.get(index);

        if (valueToCheck.equals("0")) {
            return "0";
        } else if (valueToCheck.equals("1") || valueToCheck.equals("2") || valueToCheck.equals("3")) {
            return "1";
        } else if (index == planeAHeadIndex) {
            planesDestroyed++;
            destroyPlane(planeAHeadIndex, this.planeABodyIndexes);
            planeAHeadIndex = -1;
            return "X";
        } else if (index == planeBHeadIndex) {
            planesDestroyed++;
            destroyPlane(planeBHeadIndex, this.planeBBodyIndexes);
            planeBHeadIndex = -1;
            return "X";
        } else if (index == planeCHeadIndex) {
            planesDestroyed++;
            destroyPlane(planeCHeadIndex, this.planeCBodyIndexes);
            planeCHeadIndex = -1;
            return "X";
        }

        return "null";
    }

    public GameTable() {
    }

    public GameTable(
            ArrayList<String> gameValues,
            ArrayList<Integer> planeABodyIndexes,
            ArrayList<Integer> planeBBodyIndexes,
            ArrayList<Integer> planeCBodyIndexes,
            int planeAHeadIndex,
            int planeBHeadIndex,
            int planeCHeadIndex) {

        this.planeAHeadIndex = planeAHeadIndex;
        this.planeBHeadIndex = planeBHeadIndex;
        this.planeCHeadIndex = planeCHeadIndex;

        this.gameValues = new ArrayList<>();
        this.planesDestroyed = 0;
        copyArrayString(this.gameValues, gameValues);

        this.planeABodyIndexes = new ArrayList<>();
        this.planeBBodyIndexes = new ArrayList<>();
        this.planeCBodyIndexes = new ArrayList<>();

        copyArrayInt(this.planeABodyIndexes, planeABodyIndexes);
        copyArrayInt(this.planeBBodyIndexes, planeBBodyIndexes);
        copyArrayInt(this.planeCBodyIndexes, planeCBodyIndexes);
    }

    public GameTable(GameTable gameTable) {
        this.planeAHeadIndex = gameTable.getPlaneAHeadIndex();
        this.planeBHeadIndex = gameTable.getPlaneBHeadIndex();
        this.planeCHeadIndex = gameTable.getPlaneCHeadIndex();

        this.gameValues = new ArrayList<>();
        this.planesDestroyed = 0;
        copyArrayString(this.gameValues, gameTable.getGameValues());

        this.planeABodyIndexes = new ArrayList<>();
        this.planeBBodyIndexes = new ArrayList<>();
        this.planeCBodyIndexes = new ArrayList<>();

        copyArrayInt(this.planeABodyIndexes, gameTable.getPlaneABodyIndexes());
        copyArrayInt(this.planeBBodyIndexes, gameTable.getPlaneBBodyIndexes());
        copyArrayInt(this.planeCBodyIndexes, gameTable.getPlaneCBodyIndexes());
    }

    public ArrayList<String> getGameValues() {
        return gameValues;
    }

    public ArrayList<Integer> getPlaneABodyIndexes() {
        return planeABodyIndexes;
    }

    public ArrayList<Integer> getPlaneBBodyIndexes() {
        return planeBBodyIndexes;
    }

    public ArrayList<Integer> getPlaneCBodyIndexes() {
        return planeCBodyIndexes;
    }

    public int getPlaneAHeadIndex() {
        return planeAHeadIndex;
    }

    public int getPlaneBHeadIndex() {
        return planeBHeadIndex;
    }

    public int getPlaneCHeadIndex() {
        return planeCHeadIndex;
    }

    public int getPlanesDestroyed() {
        return planesDestroyed;
    }
}
