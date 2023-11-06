package com.dci.intellij.dbn.debugger.jdwp.process.tunnel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Properties;

public class NSTunnelIO {

	public static NSTunnelConnectionProxy newInstance(ClassLoader realNSTunnelClassLoader, String url, Properties props) {
		Class<?> realNSTunnelClass;
		try {
			realNSTunnelClass = realNSTunnelClassLoader.loadClass("oracle.net.ns.NSTunnelConnection");
			Object readNSTunnelObj = invokeStaticNew(realNSTunnelClass, url, props);
			if (readNSTunnelObj == null) {
			    return null;
			}
			NSTunnelConnectionProxyHandler handler = new NSTunnelConnectionProxyHandler(readNSTunnelObj);
			NSTunnelConnectionProxy newProxyInstance = (NSTunnelConnectionProxy) 
					Proxy.newProxyInstance(realNSTunnelClassLoader,
							new Class<?> [] {NSTunnelConnectionProxy.class}, handler);
			return newProxyInstance;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object invokeStaticNew(Class<?> realNSTunnelClass, String url, Properties props) {
		for (Method m : realNSTunnelClass.getDeclaredMethods()) {
			if ("newInstance".equals(m.getName()) && Modifier.isStatic(m.getModifiers())) {
				try {
					return m.invoke(null, new Object[] {url, props});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					return null;
				}
			}
		}
		return null;
	}
}
