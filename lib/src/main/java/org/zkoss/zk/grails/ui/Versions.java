// 
// Decompiled by Procyon v0.6-prerelease
// 

package org.zkoss.zk.grails.ui;

import org.zkoss.zk.fn.ZkFns;

public class Versions
{
    private static String ZK6_VERSION;
    private static String ZK7_VERSION;
    
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
    
    static {
        Versions.ZK6_VERSION = "6.5";
        Versions.ZK7_VERSION = "7.0";
    }
}
