package parser;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.*;

public class Caller {

  private Map<String, Controller> controllers;

  public Caller() {
    this.controllers = new HashMap<>();
  }

  public void registerClass(String name, Controller controller) {
    controllers.put(name, controller);
  }

  public void callAndPrint(Call[] calls) {
    Output output;
    if (calls == null) {
      output = new Output("Command not recognized. Type help for help.");
    } else {
      output = call(calls);
    }
    output.print(System.out);
  }

  public Output call(Call[] calls) {
    Output results = new Output();
    for (Call call : calls) {
      results.append(call(call));
    }

    return results;
  }
  
  public Output call(Call call) {
    String className, methodName;
    {
      String path = call.getKeyword().getController();
      String[] parts = path.split(":");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Controller name must have exactly one ':'");
      } else {
        className = parts[0];
        methodName = parts[1];
      }
    }

    try {
      Controller controller = controllers.get(className);

      if (controller == null) {
        throw new IllegalArgumentException("Controller class '" + className + "' not found");
      }

      Method method = controller.getClass().getMethod(methodName, Map.class);

      return (Output) (method.invoke(controller, call.getArgs()));
    } catch (IllegalAccessException ex) {
      throw new RuntimeException("Unhandled exception thrown by controller '" + className + ":" + methodName + "'", ex);
    } catch (ReflectiveOperationException ex) {
      throw new IllegalArgumentException("Controller method '" + methodName + "' not found in controller '" + className + "'", ex);
    }
  }
}