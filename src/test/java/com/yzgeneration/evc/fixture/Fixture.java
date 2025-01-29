package com.yzgeneration.evc.fixture;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

import java.util.List;

public abstract class Fixture {

    public static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(new FailoverIntrospector(
                    List.of(BuilderArbitraryIntrospector.INSTANCE, FieldReflectionArbitraryIntrospector.INSTANCE)
            ))
            .defaultNotNull(true)
            .build();
}
