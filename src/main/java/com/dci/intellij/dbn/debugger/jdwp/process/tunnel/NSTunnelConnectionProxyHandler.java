package com.dci.intellij.dbn.debugger.jdwp.process.tunnel;

import java.io.IOException;
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
		Object result = null;
		try {
		    result = realMethod.invoke(this.realNSTunnelConnection, args);
		    return result;
		} catch (Throwable t) {
		    // for some reason, method.invoke doesn't recognize that NetException potentally
		    // thrown by the NSTunnelConnection as being an instance of IOException which
		    // is declared as a checked exception on some of its methods.  So we're going
		    // to unwrap the NetException and rethrow if it's a sub-class of IOException
		    // or IOException.  Else throw the InvocationTargetException thrown by invoke
		    if (IOException.class.isAssignableFrom(t.getCause().getClass()) ) {
	            throw t.getCause();
		    }
		    else {
		        throw t;
		    }
		}
	}

}
