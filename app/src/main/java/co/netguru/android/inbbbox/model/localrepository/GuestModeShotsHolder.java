package co.netguru.android.inbbbox.model.localrepository;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.model.ui.Shot;

public class GuestModeShotsHolder {

    private List<Shot> shotList;
    private List<Long> idsList;

    public GuestModeShotsHolder() {
        shotList = new ArrayList<>();
        idsList = new ArrayList<>();
    }

    public List<Shot> getShotList() {
        return shotList;
    }

    public void setShotList(List<Shot> shotList) {
        this.shotList = shotList;
    }

    public List<Long> getIdsList() {
        return idsList;
    }

    public void setIdsList(List<Long> idsList) {
        this.idsList = idsList;
    }
}
