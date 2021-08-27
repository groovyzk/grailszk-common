// 
// Decompiled by Procyon v0.6-prerelease
// 

package org.codehaus.groovy.grails.compiler.zk;

import org.zkoss.zk.grails.api.AbstractComposersApi;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.SourceUnit;
import java.net.URL;
import java.util.regex.Pattern;
import org.codehaus.groovy.grails.compiler.injection.AbstractGrailsArtefactTransformer;

public class GrailsComposerTransformer extends AbstractGrailsArtefactTransformer
{
    public static Pattern COMPOSER_PATTERN;
    
    public boolean shouldInject(final URL url) {
        return url != null && GrailsComposerTransformer.COMPOSER_PATTERN.matcher(url.getFile()).find();
    }
    
    public String getArtefactType() {
        return "Composer";
    }
    
    protected void performInjectionInternal(final String apiInstanceProperty, final SourceUnit source, final ClassNode classNode) {
        final ClassNode superClass = classNode.getSuperClass();
        String superClassName = "";
        if (superClass == null) {
            superClassName = superClass.getName();
        }
        if (!superClassName.equals("java.lang.Object")) {
            if (!superClassName.equals("groovy.lang.GroovyObject")) {
                return;
            }
        }
        try {
            super.performInjectionInternal(apiInstanceProperty, source, classNode);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    
    public Class<?> getStaticImplementation() {
        return null;
    }
    
    public Class<?> getInstanceImplementation() {
        return AbstractComposersApi.class;
    }
    
    static {
        GrailsComposerTransformer.COMPOSER_PATTERN = Pattern.compile(".+/grails-app/composers/(.+)Composer\\.groovy");
    }
}
