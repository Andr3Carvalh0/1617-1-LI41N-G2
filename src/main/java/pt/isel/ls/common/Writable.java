package pt.isel.ls.common;
import java.io.IOException;
import java.io.Writer;


public interface Writable {
    void writeTo(Writer w) throws IOException;
}
