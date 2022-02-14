package com.nwalsh.gradle.saxon

import net.sf.saxon.Transform

/**
 * Isolate the call to Transform().doTransform in one place.
 * 
 * <p>Use reflection to determine if this is a Saxon 11.x or later
 * transformer or a Saxon 10.x or earlier one.</p>
 */
class SaxonTranform {
  public static void doTransform(String[] parameters) {
    // FIXME: what about PE/EE?
    Class transformClass = Class.forName("net.sf.saxon.Transform")
    Class[] param = new Class[] { String[].class, String.class };
    try {
      def method = transformClass.getDeclaredMethod("doTransform", param);
      method.invoke(transformClass.newInstance(), new Object[] { parameters, '' })
    } catch (NoSuchMethodException ex) {
      param = new Class[] { String[].class };
      def method = transformClass.getDeclaredMethod("doTransform", param);
      method.invoke(transformClass.newInstance(), new Object[] { parameters })
    }
  }
}
