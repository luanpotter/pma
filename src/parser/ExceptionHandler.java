package parser;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;
import java.lang.reflect.InvocationTargetException;

public final class ExceptionHandler {
  
  public static final Map<Class<? extends Throwable>, Function<Throwable, Output>> HANDLERS = new HashMap<>();

  public static Output handleController(Throwable e, String controller) {
    if (e.getClass().equals(InvocationTargetException.class)) {
      return handle(e.getCause(), controller);
    }
    return handle(e, controller);
  }

  public static Output handle(Throwable e, String controller) {
    Function<Throwable, Output> handler = HANDLERS.get(e.getClass());
    if (handler == null) {
      throw new RuntimeException("Unexpected exception thrown at '" + controller + "'", e);
    }
    return handler.apply(e);
  }
}