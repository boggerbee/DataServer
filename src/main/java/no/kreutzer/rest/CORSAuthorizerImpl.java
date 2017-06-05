package no.kreutzer.rest;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import restx.factory.Module;
import restx.factory.Provides;
import restx.security.CORSAuthorizer;
import restx.security.StdCORSAuthorizer;


@Module
public class CORSAuthorizerImpl {
    @Provides
    public CORSAuthorizer allowAllAuthorizer() {
        return StdCORSAuthorizer.builder()
                .setOriginMatcher(Predicates.<CharSequence>alwaysTrue())
                .setPathMatcher(Predicates.containsPattern("^/*"))
                .setAllowedMethods(ImmutableList.of("GET", "HEAD", "POST"))
                .build();
    }
}