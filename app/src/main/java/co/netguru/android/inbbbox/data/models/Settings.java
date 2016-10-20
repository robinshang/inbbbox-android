package co.netguru.android.inbbbox.data.models;

import java.io.Serializable;

public class Settings implements Serializable{
    private StreamSourceState streamSourceState;

    public StreamSourceState getStreamSourceState() {
        return streamSourceState;
    }

    public void setStreamSourceState(StreamSourceState streamSourceState) {
        this.streamSourceState = streamSourceState;
    }
}
