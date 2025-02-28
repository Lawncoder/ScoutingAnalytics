package com.scouting5530.analytics;

import java.util.List;

import com.scouting5530.analytics.Data.Events;

public class Data {


    //#region General
        public Events event;
        public int teamNumber;
        public int qualificationNumber;
        public String userName;
        //#endregion
    //#region Autonomous
        public int autonCoralFails;
        public int autonAlgaeFails;
        public int autonIntakeFails;
        public int autonL1;
        public int autonL2;
        public int autonL3;
        public int autonL4;
        public int algaeDescoredInAuto;
 
       
        
        //#endregion
    
    //#region Tele-Operated
        public int l1;
        public int l2;
        public int l3;
        public int l4;
        public int algaeInProcessor;
        public int algaeInBarge;
        public int l1Fails;
        public int l2Fails;
        public int l3Fails;
        public int l4fails;
        public int algaeFails;
        public int intakeFails;
        public int algaeThrowFails;
        public int majorFouls;
        public int minorFouls;
        public int algaeDescored;
    
        public Cages cage;
        
        //#endregion
        //#region Post Match
        public String failureInfo = "null";
        public String driverInfo = "null";
        public String importantInfo = "null";
        public String badHumanFailures = "null";
        public String driverRating = "N/A";
        public String driverDefRating = "N/A";
        public int l1autofails;
        public int l2autofails;
        public int l3autofails;
        public int l4autofails;
        public boolean leftAuton;
        public boolean brokeDown;
    
        //#endregion
        public enum Cages {
            PARK, SHALLOW, DEEP, FAILED_DEEP, FAILED_SHALLOW, DID_NOT_ATTEMPT
        }
       public enum Events {
            FERNDALE, RENAISSANCE, STATES, WORLDS, TEST
       }
       
       public static Data deserialize(List<Object> data) {
        if (data == null || data.size() < 40) {
            throw new IllegalArgumentException("Invalid data list: must contain at least 40 elements");
        }
    
        Data out = new Data();
        
        // General
        out.event = Events.FERNDALE;
        out.teamNumber = Integer.parseInt((String) data.get(0));
        out.qualificationNumber = Integer.parseInt((String) data.get(1));
        out.userName = (String) data.get(2);
        
        // Autonomous
        out.autonCoralFails = getFromObject(data.get(3));
        out.autonAlgaeFails = getFromObject(data.get(4));
        out.autonIntakeFails = getFromObject(data.get(5));
        out.autonL1 = getFromObject(data.get(6));
        out.autonL2 = getFromObject(data.get(7));
        out.autonL3 = getFromObject(data.get(8));
        out.autonL4 = getFromObject(data.get(9));
        out.algaeDescoredInAuto = getFromObject(data.get(10));
        
        // Tele-Operated
        out.l1 = getFromObject(data.get(11));
        out.l2 = getFromObject(data.get(12));
        out.l3 = getFromObject(data.get(13));
        out.l4 = getFromObject(data.get(14));
        out.algaeInProcessor = getFromObject(data.get(15));
        out.algaeInBarge = getFromObject(data.get(16));
        out.l1Fails = getFromObject(data.get(17));
        out.l2Fails = getFromObject(data.get(18));
        out.l3Fails = getFromObject(data.get(19));
        out.l4fails = getFromObject(data.get(20));
        out.algaeFails = getFromObject(data.get(21));
        out.intakeFails = getFromObject(data.get(22));
        out.algaeThrowFails = getFromObject(data.get(23));
        out.majorFouls = getFromObject(data.get(24));
        out.minorFouls = getFromObject(data.get(25));
        out.algaeDescored = getFromObject(data.get(26));
        out.cage = Cages.valueOf((String) data.get(27)); // Convert string to enum
        
        // Post Match
        out.failureInfo = (String) data.get(28);
        out.driverInfo = (String) data.get(29);
        out.importantInfo = (String) data.get(30);
        out.badHumanFailures = (String) data.get(31);
        out.driverRating = (String) data.get(32);
        out.driverDefRating = (String) data.get(33);
        out.leftAuton = Boolean.parseBoolean((String) data.get(34));
        out.l1autofails = getFromObject(data.get(35));
        out.l2autofails = getFromObject(data.get(36));
        out.l3autofails = getFromObject(data.get(37));
        out.l4autofails = getFromObject(data.get(38));
     
        out.brokeDown = Boolean.parseBoolean((String) data.get(39));
        
        return out;
    }
    
       public static int getFromObject(Object o) {
        return Integer.parseInt((String) o);
       }
       public int calculateTeleScore() {
        int out = 0;
        out += 5 * l4;
        out += 4 * l3;
        out += 3 * l2;
        out += 2 * l1;
        out += 2 * algaeInProcessor;
        out += 4 * algaeInBarge;
        return out;

       }

     
        
        
    
    }
    
    
    
    
    
