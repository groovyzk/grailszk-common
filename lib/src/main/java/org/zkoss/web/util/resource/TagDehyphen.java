// 
// Decompiled by Procyon v0.6-prerelease
// 

package org.zkoss.web.util.resource;

import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import java.util.Iterator;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import groovy.lang.Closure;
import org.codehaus.groovy.runtime.GeneratedClosure;
import java.lang.ref.SoftReference;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import groovy.lang.GroovyObject;

public class TagDehyphen implements GroovyObject
{
    public static /* synthetic */ long __timeStamp;
    public static /* synthetic */ long __timeStamp__239_neverHappen1398723745267;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public TagDehyphen() {
        $getCallSiteArray();
    }
    
    public static String dehyphen(final String content) {
        return (String)ScriptBytecodeAdapter.castToType($getCallSiteArray()[0].call((Object)content, (Object)"(</?)(\\w+:?)?([\\w-]+)(/?>)", (Object)new GeneratedClosure(TagDehyphen.class, TagDehyphen.class) {
            public static transient /* synthetic */ boolean __$stMC;
            private static /* synthetic */ SoftReference $callSiteArray;
            
            public Object doCall(final Object all, final Object op, final Object ns, final Object tag, final Object cp) {
                final CallSite[] $getCallSiteArray = $getCallSiteArray();
                final StringBuilder buf = (StringBuilder)ScriptBytecodeAdapter.castToType($getCallSiteArray[0].callConstructor((Object)StringBuilder.class), (Class)StringBuilder.class);
                final String[] tokens = (String[])ScriptBytecodeAdapter.castToType($getCallSiteArray[1].call(tag, (Object)"-"), (Class)String[].class);
                boolean first = true;
                String token = null;
                final Iterator iterator = (Iterator)ScriptBytecodeAdapter.castToType($getCallSiteArray[2].call((Object)tokens), (Class)Iterator.class);
                while (iterator.hasNext()) {
                    token = (String)ScriptBytecodeAdapter.castToType(iterator.next(), (Class)String.class);
                    if (ScriptBytecodeAdapter.compareEqual((Object)token, (Object)null) || ScriptBytecodeAdapter.compareEqual($getCallSiteArray[3].call((Object)token), (Object)0)) {
                        continue;
                    }
                    if (first) {
                        $getCallSiteArray[4].call((Object)buf, (Object)token);
                        first = false;
                    }
                    else {
                        $getCallSiteArray[5].call($getCallSiteArray[6].call((Object)buf, $getCallSiteArray[7].call($getCallSiteArray[8].call((Object)token, (Object)0, (Object)1))), $getCallSiteArray[9].call((Object)token, (Object)1));
                    }
                }
                if (BytecodeInterface8.isOrigZ() && !TagDehyphen$_dehyphen_closure1.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
                    return new GStringImpl(new Object[] { op, ScriptBytecodeAdapter.compareEqual(ns, (Object)null) ? "" : ns, $getCallSiteArray[11].call((Object)buf), cp }, new String[] { "", "", "", "", "" });
                }
                return new GStringImpl(new Object[] { op, ScriptBytecodeAdapter.compareEqual(ns, (Object)null) ? "" : ns, $getCallSiteArray[10].call((Object)buf), cp }, new String[] { "", "", "", "", "" });
            }
            
            public Object call(final Object all, final Object op, final Object ns, final Object tag, final Object cp) {
                return $getCallSiteArray()[12].callCurrent((GroovyObject)this, ArrayUtil.createArray(all, op, ns, tag, cp));
            }
            
            public static /* synthetic */ void __$swapInit() {
                $getCallSiteArray();
                TagDehyphen$_dehyphen_closure1.$callSiteArray = null;
            }
            
            static {
                __$swapInit();
            }
            
            private static /* synthetic */ CallSiteArray $createCallSiteArray() {
                final String[] array = new String[13];
                $createCallSiteArray_1(array);
                return new CallSiteArray((Class)TagDehyphen$_dehyphen_closure1.class, array);
            }
            
            private static /* synthetic */ CallSite[] $getCallSiteArray() {
                CallSiteArray $createCallSiteArray;
                if (TagDehyphen$_dehyphen_closure1.$callSiteArray == null || ($createCallSiteArray = TagDehyphen$_dehyphen_closure1.$callSiteArray.get()) == null) {
                    $createCallSiteArray = $createCallSiteArray();
                    TagDehyphen$_dehyphen_closure1.$callSiteArray = new SoftReference($createCallSiteArray);
                }
                return $createCallSiteArray.array;
            }
        }), (Class)String.class);
    }
    
    public static /* synthetic */ void __$swapInit() {
        $getCallSiteArray();
        TagDehyphen.$callSiteArray = null;
    }
    
    static {
        __$swapInit();
        TagDehyphen.__timeStamp__239_neverHappen1398723745267 = 0L;
        TagDehyphen.__timeStamp = 1398723745267L;
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = { null };
        $createCallSiteArray_1(array);
        return new CallSiteArray((Class)TagDehyphen.class, array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (TagDehyphen.$callSiteArray == null || ($createCallSiteArray = TagDehyphen.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            TagDehyphen.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
