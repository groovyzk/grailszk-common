// 
// Decompiled by Procyon v0.6-prerelease
// 

package org.zkoss.web.util.resource;

import grails.core.GrailsApplication;
import grails.plugins.GrailsPlugin;
import grails.plugins.GrailsPluginManager;
import org.apache.commons.logging.LogFactory;
import java.io.FileInputStream;
import java.net.URL;
import org.grails.core.io.DefaultResourceLocator;
import org.grails.gsp.GroovyPagesTemplateEngine;
import org.zkoss.web.servlet.Servlets;
import java.io.Reader;
import java.io.InputStreamReader;
import org.zkoss.zk.ui.metainfo.Parser;
import org.zkoss.zk.ui.metainfo.PageDefinitions;
import org.zkoss.util.resource.Locator;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import groovy.lang.Writable;
import groovy.text.Template;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.core.io.ByteArrayResource;
import java.io.BufferedInputStream;
import java.io.StringReader;
import java.io.InputStream;
import java.util.Map;
import java.net.URI;
import java.io.FileNotFoundException;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
import org.codehaus.groovy.runtime.InvokerHelper;
import grails.util.GrailsNameUtils;
import grails.util.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.context.ApplicationContext;
import org.zkoss.zk.ui.WebApp;
import org.apache.commons.logging.Log;
import org.zkoss.zk.ui.metainfo.PageDefinition;

public class GrailsContentLoader extends ResourceLoader<PageDefinition>
{
    private static final String GROOVY_PAGES_TEMPLATE_ENGINE = "groovyPagesTemplateEngine";
    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String CONFIG_OPTION_GSP_ENCODING = "grails.views.gsp.encoding";
    private static final String CONFIG_ZKGRAILS_TAGLIB_DISABLE = "grails.zk.taglib.disabled";
    private static final Log LOG;
    private final WebApp webApp;
    private final ApplicationContext appCtx;
    private GrailsApplication grailsApplication;
    
    public GrailsContentLoader(final WebApp wapp) {
        this.webApp = wapp;
        final WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(wapp.getServletContext());
        this.grailsApplication = (GrailsApplication)ctx.getBean("grailsApplication", (Class)GrailsApplication.class);
        (this.appCtx = this.grailsApplication.getMainContext()).getBean("grailsResourceLocator", (Class) DefaultResourceLocator.class);
    }
    
    public PageDefinition load(final ResourceInfo si) throws Exception {
        org.springframework.core.io.Resource springResource = null;
        if (!Environment.isWarDeployed()) {
            final String path = "file:./grails-app/zul" + si.path;
            springResource = this.grailsApplication.getMainContext().getResource(path);
            if (springResource != null && !springResource.exists()) {
                final GrailsPluginManager pluginManager = (GrailsPluginManager)this.appCtx.getBean("pluginManager", (Class)GrailsPluginManager.class);
                for (final GrailsPlugin p : pluginManager.getAllPlugins()) {
                    final org.grails.io.support.Resource pluginDir = p.getPluginDir();
                    if (pluginDir != null) {
                        final File file = (File)InvokerHelper.getProperty(pluginDir, "file");
                        springResource = new FileSystemResource(file.getAbsolutePath() + "/grails-app/zul" + si.path);
                        GrailsContentLoader.LOG.debug((Object)(">>> Try Resource ::: " + springResource));
                        if (springResource.exists()) {
                            break;
                        }
                        springResource = null;
                        GrailsContentLoader.LOG.debug((Object)("NOT found ::: " + springResource));
                    }
                }
            }
            GrailsContentLoader.LOG.debug((Object)("Get Spring Resource from: " + springResource));
        }
        else {
            final URI uri = new File(((ServletContextLocator)si.extra).getServletContext().getRealPath("WEB-INF") + "/grails-app/zul" + si.path).toURI();
            springResource = this.grailsApplication.getMainContext().getResource(uri.toString());
            GrailsContentLoader.LOG.debug((Object)("Get Spring Resource in WAR mode from: " + uri.toString()));
        }
        if (springResource != null) {
            GrailsContentLoader.LOG.debug((Object)("Load from Spring Resource: " + springResource));
            try {
                return this.parse(si.path, (File) springResource, si.extra);
            }
            catch (Throwable e) {
                GrailsContentLoader.LOG.debug((Object)"Cannot parse ZUL from a Spring Resource", e);
            }
        }
        if (si.url != null) {
            GrailsContentLoader.LOG.debug((Object)("Load from URL: " + si.url));
            return this.parse(si.path, si.url, si.extra);
        }
        if (!si.file.exists()) {
            GrailsContentLoader.LOG.debug((Object)("File " + si.file + " not found"));
            return null;
        }
        try {
            GrailsContentLoader.LOG.debug((Object)("Load from File: " + si.file));
            return this.parse(si.path, si.file, si.extra);
        }
        catch (FileNotFoundException ex) {
            return null;
        }
    }
    
    private StringReader preprocessGSP(final Map<?, ?> config, final long length, final InputStream in) throws IOException, UnsupportedEncodingException {
        GrailsContentLoader.LOG.debug((Object)"Enter :: preprocessGSP");
        final GroovyPagesTemplateEngine gsp = (GroovyPagesTemplateEngine)this.appCtx.getBean("groovyPagesTemplateEngine");
        GrailsContentLoader.LOG.debug((Object)("Got GSP Template bean: " + gsp));
        byte[] buffer = null;
        final UnicodeBOMInputStream ubomIn = new UnicodeBOMInputStream(in);
        if (ubomIn.getBOM() != UnicodeBOMInputStream.BOM.NONE) {
            GrailsContentLoader.LOG.debug((Object)"BOM detected");
            ubomIn.skipBOM();
            buffer = new byte[(int)length - ubomIn.getBOM().getBytes().length];
        }
        else {
            buffer = new byte[(int)length];
        }
        final BufferedInputStream bis = new BufferedInputStream(ubomIn);
        bis.read(buffer);
        String encoding = (String)config.get("grails.views.gsp.encoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
        String bufferStr = new String(buffer, encoding).replaceAll("@\\{", "\\$\\{'@'\\}\\{");
        bufferStr = TagDehyphen.dehyphen(bufferStr);
        final Template template = gsp.createTemplate((Resource)new ByteArrayResource(bufferStr.getBytes(encoding)), false);
        final Writable w = template.make();
        final StringWriter sw = new StringWriter();
        w.writeTo((Writer)new PrintWriter(sw));
        final String zulSrc = sw.toString().replaceAll("\\#\\{", "\\$\\{");
        final StringReader reader = new StringReader(zulSrc);
        GrailsContentLoader.LOG.debug((Object)("Returning pre-processed ::: " + reader));
        return reader;
    }
    
    private PageDefinition parse(final String path, final Resource resource, final Object extra) throws Throwable {
        final Map<?, ?> config = this.grailsApplication.getConfig().flatten();
        final Boolean disable = (Boolean)config.get("grails.zk.taglib.disabled");
        final Locator locator = (Locator)((extra != null) ? extra : PageDefinitions.getLocator(this.webApp, path));
        if (disable != null && disable) {
            return new Parser(this.webApp, locator).parse((Reader)new InputStreamReader(resource.getInputStream()), path);
        }
        final StringReader reader = this.preprocessGSP(config, resource.contentLength(), resource.getInputStream());
        final PageDefinition pgdef = new Parser(this.webApp, locator).parse((Reader)reader, Servlets.getExtension(path));
        pgdef.setRequestPath(path);
        return pgdef;
    }
    
    protected PageDefinition parse(final String path, final URL url, final Object extra) throws Exception {
        final Locator locator = (Locator)((extra != null) ? extra : PageDefinitions.getLocator(this.webApp, path));
        return new Parser(this.webApp, locator).parse(url, path);
    }
    
    protected PageDefinition parse(final String path, final File file, final Object extra) throws Exception {
        final GrailsApplication grailsApplication = (GrailsApplication)this.appCtx.getBean("grailsApplication");
        final Map<?, ?> config = grailsApplication.getConfig().flatten();
        final Boolean disable = (Boolean)config.get("grails.zk.taglib.disabled");
        final Locator locator = (Locator)((extra != null) ? extra : PageDefinitions.getLocator(this.webApp, path));
        if (disable != null && disable) {
            return new Parser(this.webApp, locator).parse(file, path);
        }
        final StringReader reader = this.preprocessGSP(config, file.length(), new FileInputStream(file));
        final PageDefinition pgdef = new Parser(this.webApp, locator).parse((Reader)reader, Servlets.getExtension(path));
        pgdef.setRequestPath(path);
        return pgdef;
    }
    
    static {
        LOG = LogFactory.getLog((Class)GrailsContentLoader.class);
    }
}
