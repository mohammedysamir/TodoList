package util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import model.*;
import handlers.*;

public class Utilities {
    public static Priorities mapStringToPriority(String p) {
        if (p.equalsIgnoreCase("Low"))
            return Priorities.LOW;
        if (p.equalsIgnoreCase("Medium"))
            return Priorities.MEDIUM;
        return Priorities.HIGH;
    }
}

