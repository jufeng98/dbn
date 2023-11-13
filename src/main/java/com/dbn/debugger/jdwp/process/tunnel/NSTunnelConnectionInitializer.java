package com.dbn.debugger.jdwp.process.tunnel;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

import static com.dbn.common.util.Unsafe.cast;

public class NSTunnelConnectionInitializer {

	@SneakyThrows
	public static NSTunnelConnectionProxy newInstance(ClassLoader classLoader, String url, Properties props) {
		Class<?> tunnelClass = classLoader.loadClass("oracle.net.ns.NSTunnelConnection");

		Object tunnelConnection = invokeStaticNew(tunnelClass, url, props);
		if (tunnelConnection == null) throw new IllegalStateException("Failed to create NS Tunnel Connection");

		NSTunnelConnectionProxyHandler handler = new NSTunnelConnectionProxyHandler(tunnelConnection);
		return cast(Proxy.newProxyInstance(classLoader, new Class<?> [] {NSTunnelConnectionProxy.class}, handler));
	}

	@SneakyThrows
	private static Object invokeStaticNew(Class<?> tunnelClass, String url, Properties props) {
		try {
			Method method = tunnelClass.getMethod("newInstance", String.class, Properties.class);
			return method.invoke(null, url, props);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}
}
