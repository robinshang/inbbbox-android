package co.netguru.android.inbbbox.feature.project;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.ProjectsController;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectPresenterTest {

    private static final List<Shot> shots = Statics.generateShots();
    private static final ProjectWithShots PROJECT_WITH_SHOTS = ProjectWithShots.create
            (Statics.PROJECT_ENTITY, shots, false);
    private static final int PER_PAGE = PROJECT_WITH_SHOTS.shotList().size();

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ProjectContract.View view;

    ProjectPresenter presenter;

    @Mock
    ProjectsController projectsControllerMock;

    @Mock
    ErrorController errorControllerMock;

    @Before
    public void setUp() throws Exception {
        presenter = new ProjectPresenter(PROJECT_WITH_SHOTS, projectsControllerMock, errorControllerMock);
        presenter.attachView(view);
    }

    @Test
    public void whenDetachView_thenUnsubscribeSubscribers() throws Exception {
        //given
        TestSubscriber refreshTestSubscriber = TestSubscriber.create();
        TestSubscriber loadMoreTestSubscriber = TestSubscriber.create();
        presenter.refreshSubscription = refreshTestSubscriber;
        presenter.loadMoreSubscription = loadMoreTestSubscriber;
        //when
        presenter.detachView(false);
        //then
        loadMoreTestSubscriber.assertUnsubscribed();
        loadMoreTestSubscriber.assertUnsubscribed();
    }

    @Test
    public void whenRefreshShots_thenShowItems() {
        // given
        when(projectsControllerMock.getShotsFromProject(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.just(shots));
        // when
        presenter.refreshShots();

        // then
        verify(view).setData(shots);
        verify(view).showContent();
    }

    @Test
    public void whenRefreshShotsError_thenShowMessageOnServerError() {
        // given
        when(projectsControllerMock.getShotsFromProject(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.error(new Throwable()));

        // when
        presenter.refreshShots();

        // then
        verify(view).showMessageOnServerError(anyString());
    }

}