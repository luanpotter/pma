package parser;

import java.io.Serializable;
import java.util.Map;

public interface Callable extends Serializable {

  public Call[] parse(String[] args, Map<String, String> aliases);

}