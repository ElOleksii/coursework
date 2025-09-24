package org.cruises.model.logic;

import org.cruises.model.Cruise;
import org.cruises.model.Stateroom;


public class Calc {
    public double calculateTotalPrice(Stateroom stateroom, Cruise cruise) {
        long days = Math.max(1,
                (cruise.getEndDate().getTime() - cruise.getStartDate().getTime()) / (1000 * 60 * 60 * 24));
        return days * stateroom.getPrice() + cruise.getBasicPrice();
    }
}



