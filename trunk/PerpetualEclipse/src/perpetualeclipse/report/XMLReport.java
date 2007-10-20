package perpetualeclipse.report;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;

public abstract class XMLReport implements Report {
    protected static XStream xstream = new XStream();
    { Annotations.configureAliases(xstream, this.getClass()); }
    
    public String toXML() {
        return xstream.toXML(this);
    }
}
