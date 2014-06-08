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
        throw new IllegalArgumentException("Controller name must have exactly one ':'.");
      } else {
        className = parts[0];
        methodName = parts[1];
      }
    }

    try {
      Controller controller = controllers.get(className);
      Method method = controller.getClass().getMethod(methodName, Map.class);

      Output results = (Output) (method.invoke(controller, call.getArgs()));
      return results;
    } catch (ReflectiveOperationException ex) {
      throw new IllegalArgumentException("Specified controller method not found.", ex);
    }
  }

  public static void permit(String[] required, Map<String, String> args) {
    for (String arg : required) {
      if (args.get(arg) == null) {
        throw new IllegalArgumentException("Invalid parameters.");
      }
    }
  }
}