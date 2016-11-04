package co.netguru.android.inbbbox.data.api;

import rx.Observable;

public class MockedUserApi implements UserApi {

    private static final User MOCKED_USER = new User();

    static {
        MOCKED_USER.setId(123);
        MOCKED_USER.setName("name");
    }

    @Override
    public Observable<User> getAuthenticatedUser() {
        return Observable.just(MOCKED_USER);
    }
}
