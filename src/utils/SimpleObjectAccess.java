package utils;

import java.io.*;

public final class SimpleObjectAccess {
  
  private SimpleObjectAccess() { throw new RuntimeException("Should not be instanciated"); }

  public static void saveTo(String fileName, Object object) {
    try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileName))) {
      stream.writeObject(object);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static <T> T readFrom(String fileName) {
    if (new File(fileName).exists()) {
      try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fileName))) {

        @SuppressWarnings("unchecked")
        T t = (T) stream.readObject();

        return t;
      } catch (IOException | ClassNotFoundException ex) {
        throw new RuntimeException(ex);
      }
    } else {
      return null;
    }
  }
}