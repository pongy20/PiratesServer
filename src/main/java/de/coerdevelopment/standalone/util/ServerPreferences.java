package de.coerdevelopment.standalone.util;

import java.util.prefs.Preferences;

public class ServerPreferences {

    private static ServerPreferences instance;

    public static ServerPreferences getInstance() {
        if (instance == null) {
            instance = new ServerPreferences();
        }
        return instance;
    }

    private final String preferencesNode = "Coer_Server";
    private final Preferences preferences;

    // console message preferences

    public boolean sendErrorMessage;
    public boolean sendWarningMessage;
    public boolean sendDebugMessage;
    public boolean sendInfoMessage;
    public boolean sendJobMessage;

    private ServerPreferences() {
        preferences = Preferences.userRoot().node(preferencesNode);
    }

    public void savePreferences() {
        preferences.putBoolean("consoleError", sendErrorMessage);
        preferences.putBoolean("consoleWarning", sendWarningMessage);
        preferences.putBoolean("consoleDebug", sendDebugMessage);
        preferences.putBoolean("consoleInfo", sendInfoMessage);
        preferences.putBoolean("consoleJob", sendJobMessage);
    }
    public void loadSettings() {
        sendErrorMessage = preferences.getBoolean("consoleError", true);
        sendWarningMessage = preferences.getBoolean("consoleWarning", true);
        sendDebugMessage = preferences.getBoolean("consoleDebug", false);
        sendInfoMessage = preferences.getBoolean("consoleInfo", true);
        sendJobMessage = preferences.getBoolean("consoleJob", false);
    }

    public void updateConsoleMessagePreferences(boolean error, boolean warning, boolean debug, boolean info, boolean job) {
        sendErrorMessage = error;
        sendWarningMessage = warning;
        sendDebugMessage = debug;
        sendInfoMessage = info;
        sendJobMessage = job;
    }

}
