package de.coerdevelopment.standalone.util;

public class TimeCalculator {

    private static TimeCalculator instance;

    public static TimeCalculator getInstance() {
        if (instance == null) {
            instance = new TimeCalculator();
        }
        return instance;
    }

    public String convertTime(long milliseconds) {
        if (milliseconds < 1000) {
            return "00:00:00." + milliseconds;
        }
        long time = milliseconds / 1000;
        long hours = (time / 60) / 60;
        long minutes = (time / 60) % 60;
        long seconds = time % 60;

        String secondsC = seconds > 9 ? seconds + "" : "0" + seconds;
        String minutesC = minutes > 9 ? minutes + "" : "0" + minutes;
        String hoursC = hours > 9 ? hours + "" : "0" + hours;

        return hoursC + ":" + minutesC + ":" + secondsC;
    }

}
