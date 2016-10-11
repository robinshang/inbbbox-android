/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.application.dagger;


import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import dagger.Component;
import dagger.Module;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent extends BaseComponent {

    
}
