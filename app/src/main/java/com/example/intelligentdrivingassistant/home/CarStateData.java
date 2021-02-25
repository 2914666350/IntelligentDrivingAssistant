package com.example.intelligentdrivingassistant.home;

public class CarStateData {
    private boolean LeftFrontDoor;
    private boolean LeftRealDoor;
    private boolean RightFrontDoor;
    private boolean RightRealDoor;
    private boolean RealDoor;
    private boolean CarState;
    private boolean CarWindow;
    private boolean CarLock;
    private  int Power;
    private int Kilometer;


    public CarStateData() {
        LeftFrontDoor = true;
        LeftRealDoor = true;
        RightFrontDoor = true;
        RightRealDoor = true;
        RealDoor = true;
        CarState = true;
        CarWindow = true;
        CarLock = true;
        Power = 60;
        Kilometer = 80;
    }

    public int getPower() {
        return Power;
    }

    public void setPower(int power) {
        Power = power;
    }

    public int getKilometer() {
        return Kilometer;
    }

    public void setKilometer(int kilometer) {
        Kilometer = kilometer;
    }


    public boolean isLeftFrontDoor() {
        return LeftFrontDoor;
    }

    public void setLeftFrontDoor(boolean leftFrontDoor) {
        LeftFrontDoor = leftFrontDoor;
    }

    public boolean isLeftRealDoor() {
        return LeftRealDoor;
    }

    public void setLeftRealDoor(boolean leftRealDoor) {
        LeftRealDoor = leftRealDoor;
    }

    public boolean isRightFrontDoor() {
        return RightFrontDoor;
    }

    public void setRightFrontDoor(boolean rightFrontDoor) {
        RightFrontDoor = rightFrontDoor;
    }

    public boolean isRightRealDoor() {
        return RightRealDoor;
    }

    public void setRightRealDoor(boolean rightRealDoor) {
        RightRealDoor = rightRealDoor;
    }

    public boolean isRealDoor() {
        return RealDoor;
    }

    public void setRealDoor(boolean realDoor) {
        RealDoor = realDoor;
    }

    public boolean isCarState() {
        return CarState;
    }

    public void setCarState(boolean carState) {
        CarState = carState;
    }

    public boolean isCarWindow() {
        return CarWindow;
    }

    public void setCarWindow(boolean carWindow) {
        CarWindow = carWindow;
    }

    public boolean isCarLock() {
        return CarLock;
    }

    public void setCarLock(boolean carLock) {
        CarLock = carLock;
    }
}
