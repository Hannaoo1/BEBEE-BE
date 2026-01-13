package com.lgcns.bebee.match.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {
        "com.lgcns.bebee.match.domain",
        "com.lgcns.bebee.common.data.event"
})
@EnableJpaRepositories(basePackages = {
        "com.lgcns.bebee.match",
        "com.lgcns.bebee.common.data.event",
})
public class PersistenceConfig {
}
