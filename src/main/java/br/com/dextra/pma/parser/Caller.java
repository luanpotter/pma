package br.com.dextra.pma.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class Caller {

    private Map<String, Controller<?>> controllers;

    public Caller() {
        this.controllers = new HashMap<>();
    }

    public void registerClass(String name, Controller<?> controller) {
        controllers.put(name, controller);
    }

    public Output call(Call[] calls) {
        Output results = new Output();
        if (calls == null) {
            results.add("Command not recognized. Type help for help.");
        } else {
            for (Call call : calls) {
                results.append(call(call));
            }
        }

        return results;
    }

    public Output call(Call call) {
        Keyword keyword = call.getKeyword();
        Method m = getControllerMethod(keyword);
        try {
            return (Output) (m.invoke(getController(keyword), call.getArgs()));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Really unexpected exception", ex);
        } catch (InvocationTargetException ex) {
            return ExceptionHandler.handleController(ex, m.getDeclaringClass().getName() + ":" + m.getName());
        }
    }

    private String[] getParts(Keyword keyword) {
        String path = keyword.getController();
        String[] parts = path.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Controller name must have exactly one ':'");
        } else {
            return parts;
        }
    }

    private Controller<?> getController(Keyword keyword) {
        String className = getParts(keyword)[0];
        return controllers.get(className);
    }

    public Method getControllerMethod(Keyword keyword) {
        String[] parts = getParts(keyword);
        String className = parts[0], methodName = parts[1];

        Controller<?> controller = controllers.get(className);

        if (controller == null) {
            throw new IllegalArgumentException("Controller class '" + className + "' not found");
        }

        try {
            Method method = controller.getClass().getMethod(methodName, Map.class);
            assertValidControllerMethod(method);
            return method;
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("Controller method '" + methodName + "' not found in controller '" + className + "'", ex);
        }
    }

    private static void assertValidControllerMethod(Method m) {
        String message = "Invalid controller method '" + m.getName() + "' in controller " + m.getDeclaringClass().getName() + ": ";
        if (!m.getReturnType().equals(Output.class)) {
            throw new RuntimeException(message + "Return type must be parser.Output");
        }
        Parameter[] args = m.getParameters();
        if (args.length != 1) {
            throw new RuntimeException(message + "Must take exactly one parameter.");
        }
        if (!args[0].getType().equals(Map.class)) {
            throw new RuntimeException(message + "The parameter must be of the type java.util.Map.");
        }
    }
}