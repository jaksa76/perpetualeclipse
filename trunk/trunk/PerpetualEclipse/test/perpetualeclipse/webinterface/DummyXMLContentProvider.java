package perpetualeclipse.webinterface;

import java.util.Map;

public class DummyXMLContentProvider implements ContentProvider {

	public String invoke(Map parameters) {
		return "<artwork>" +
				"    <piece>" +
				"        <name>The Wall</name>" +
				"        <image>artwork1.jpg</image>" +
				"        <price>250</price>" +
				"        <quantity>5</quantity>" +
				"    </piece>" +
				"    <piece>" +
				"        <name>Blue Flake</name>" +
				"        <image>artwork5.jpg</image>" +
				"        <price>400</price>" +
				"        <quantity>3</quantity>" +
				"    </piece>" +
				"    <piece>" +
				"        <name>Butterfly</name>" +
				"        <image>artwork6.jpg</image>" +
				"        <price>375</price>" +
				"        <quantity>17</quantity>" +
				"    </piece>" +
				"</artwork>";
	}
}
