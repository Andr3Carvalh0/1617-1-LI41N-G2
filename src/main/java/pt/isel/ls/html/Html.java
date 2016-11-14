package pt.isel.ls.html;

import pt.isel.ls.http.HttpContent;

import java.io.IOException;
import java.io.Writer;

import pt.isel.ls.common.CompositeWritable;
import pt.isel.ls.common.Writable;

public class Html implements HttpContent {

    private Writable _content;
    
    protected Html(Writable... cs) {
        _content = new CompositeWritable(cs);
    }
           
    @Override
    public void writeTo(Writer w) throws IOException {
        _content.writeTo(w);        
    }
    
    @Override
    public String getMediaType() {
        return "text/html";
    }
    
    public static Writable text(String s) { return new HtmlText(s);}
    public static Writable h1(Writable... c) { return new HtmlElem("h1",c);}
    public static Writable h2(Writable... c) { return new HtmlElem("h2",c);}
    public static Writable h3(Writable... c) { return new HtmlElem("h3",c);}
    public static Writable form(String method, String url, Writable... c) {
        return new HtmlElem("form",c)
            .withAttr("method", method)
            .withAttr("action", url);
    }
    public static Writable label(String to, String text) {
        return new HtmlElem("label", new HtmlText(text))
            .withAttr("for", to);
    }
    public static Writable textInput(String name) {
        return new HtmlElem("input")
            .withAttr("type", "text")
            .withAttr("name", name);            
    }
    public static Writable ul(Writable... c) {
        return new HtmlElem("ul",c);
    }
    public static Writable li(Writable...c) {
        return new HtmlElem("li",c);
    }
    public static Writable a(String href, String t) {
        return new HtmlElem("a", text(t))
            .withAttr("href", href);
    }
}
