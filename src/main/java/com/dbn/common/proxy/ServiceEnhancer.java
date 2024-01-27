package com.dbn.common.proxy;

import com.dbn.common.compatibility.Experimental;
import com.dbn.common.dispose.Failsafe;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dbn.common.util.Unsafe.cast;

@Experimental
public class ServiceEnhancer<T> implements InvocationHandler {
    private final T innerService;
    private static final Map<Method, Method> methodCache = new ConcurrentHashMap<>();

    public ServiceEnhancer(T innerService) {
        this.innerService = innerService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method serviceMethod = methodCache.computeIfAbsent(method, m -> getServiceMethod(m));
        if (serviceMethod.getAnnotation(Guarded.class) != null) {
            Failsafe.guarded(() -> serviceMethod.invoke(args));
        } else {
            serviceMethod.invoke(args);
        }

        return null;
    }

    @SneakyThrows
    private Method getServiceMethod(Method proxyMethod) {
        return innerService.getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
    }


    public static <T> T wrap(T service) {
        Class<?> serviceClass = service.getClass();
        ServiceEnhancer<T> serviceEnhancer = new ServiceEnhancer<>(service);
        return cast(Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, serviceEnhancer));
    }
}
