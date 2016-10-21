package co.netguru.android.inbbbox.data.models;

import java.io.Serializable;

public class Settings implements Serializable{
    private StreamSourceState streamSourceState;
    private NotificationSettings notificationSettings;

    public Settings() {
        this.streamSourceState = new StreamSourceState();
        this.notificationSettings = new NotificationSettings();
    }

    public Settings(StreamSourceState streamSourceState, NotificationSettings notificationSettings) {
        this.streamSourceState = streamSourceState;
        this.notificationSettings = notificationSettings;
    }

    public StreamSourceState getStreamSourceState() {
        return streamSourceState;
    }

    public void setStreamSourceState(StreamSourceState streamSourceState) {
        this.streamSourceState = streamSourceState;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }
}
