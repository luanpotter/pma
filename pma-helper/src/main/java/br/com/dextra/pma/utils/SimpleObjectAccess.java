package br.com.dextra.pma.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class SimpleObjectAccess {

    private SimpleObjectAccess() {
        throw new RuntimeException("Should not be instanciated");
    }

    public static <T> T saveTo(String fileName, T object) {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            stream.writeObject(object);
            return object;
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