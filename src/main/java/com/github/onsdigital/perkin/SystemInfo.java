package com.github.onsdigital.perkin;

import com.github.davidcarboni.restolino.framework.Startup;
import com.github.onsdigital.ConfigurationManager;
import lombok.extern.slf4j.Slf4j;

/**
 * Prints out build information on startup so that a deployed container can be identified.
 */
@Slf4j
public class SystemInfo implements Startup {
    @Override
    public void init() {
        String[] keys = new String[]{"GIT_URL", "GIT_BRANCH", "GIT_COMMIT", "BUILD_TAG", "BUILD_URL"};
        for (String key : keys) {
            log.info("Build identification: {}: {}", key, ConfigurationManager.get(key));
        }
    }
}