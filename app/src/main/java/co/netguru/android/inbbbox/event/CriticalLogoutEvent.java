package co.netguru.android.inbbbox.event;

public class CriticalLogoutEvent implements Event {

    private final String reason;

    public CriticalLogoutEvent(String reason) {

        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}
