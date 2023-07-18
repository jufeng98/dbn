package com.dci.intellij.dbn.debugger.jdwp.process.tunnel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NSTunnelConnectionProxyHandler implements InvocationHandler {

	private Object realNSTunnelConnection;
	private Map<String, Method> methodsByName = new HashMap<>();
	
	public NSTunnelConnectionProxyHandler(Object realNSTunnelConection) {
		this.realNSTunnelConnection = realNSTunnelConection;
		for(Method method : this.realNSTunnelConnection.getClass().getDeclaredMethods()) {
            this.methodsByName.put(method.getName(), method);
        }
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Method realMethod = this.methodsByName.get(method.getName());
		realMethod.setAccessible(true);
		return realMethod.invoke(this.realNSTunnelConnection, args);
	}

}
