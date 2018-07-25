package ie.demo.demost.readers;

import java.io.Closeable;

public interface Reader<T> extends Closeable {


    boolean moveNext() throws ReaderException;

    T read() throws ReaderException;


}
