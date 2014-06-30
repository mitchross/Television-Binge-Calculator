package com.vanillax.televisionbingecalculator.app.Dagger;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by mitch on 6/22/14.
 */
@Qualifier
@Retention( RUNTIME )
public @interface ForApplication
{
}