package pt.isel.ls.http;

import pt.isel.ls.common.Writable;

public interface HttpContent extends Writable {
    String getMediaType();    
}
