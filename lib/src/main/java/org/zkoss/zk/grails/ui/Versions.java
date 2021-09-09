package org.zkoss.zk.grails.ui;

import org.zkoss.zk.fn.ZkFns;

public class Versions {
    private static final String ZK6_VERSION = "6.5";
    private static final String ZK7_VERSION = "7.0";
    
    static void versionValidator() {
        final String zkVersion = ZkFns.getVersion();
        if (zkVersion.startsWith(Versions.ZK6_VERSION)) {
            return;
        }
        if (zkVersion.startsWith(Versions.ZK7_VERSION)) {
            return;
        }
        throw new RuntimeException("ZK version " + zkVersion + " not supported.");
    }
}
