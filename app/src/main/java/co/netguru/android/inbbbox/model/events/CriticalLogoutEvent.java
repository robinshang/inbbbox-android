package co.netguru.android.inbbbox.model.events;

public class CriticalLogoutEvent {

    private final String reason;

    public CriticalLogoutEvent(String reason) {

        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
