package parser;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.*;

public class Caller {

  private Map<String, Class<?>> classes;

  public Caller() {
    this.classes = new HashMap<>();
  }

  public void registerClass(String name, Class<?> clazz) {
    classes.put(name, clazz);
  }

  public Output call(Call[] calls, Scope scope) {
    Output results = new Output();
    for (Call call : calls) {
      results.append(call(call, scope));
    }

    return results;
  }
  
  public Output call(Call call, Scope scope) {
    String controller = call.getKeyword().getController();
    
    String className, methodName;
    {
      String[] parts = controller.split(":");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Controller name must have exactly one ':'.");
      } else {
        className = parts[0];
        methodName = parts[1];
      }
    }

    try {
      Class<?> clazz = classes.get(className);
      Method method = clazz.getMethod(methodName, Map.class, Scope.class);

      Output results = (Output) (method.invoke(call.getArgs(), scope));
      return results;
    } catch (ReflectiveOperationException ex) {
      throw new IllegalArgumentException("Specified controller method not found.", ex);
    }
  }

  public static void permit(String[] required, HashMap<String, String> args) {
    for (String arg : required) {
      if (args.get(arg) == null) {
        throw new IllegalArgumentException("Invalid parameters.");
      }
    }
  }
}