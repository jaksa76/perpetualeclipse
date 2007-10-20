package perpetualeclipse.webinterface;

import java.util.Map;

public class FlexSecurityConfigurationContentProvider implements
		ContentProvider {

	public String invoke(Map parameters) {
		return "<cross-domain-policy>"
				+ "    <allow-access-from domain=\"*\"/>"
				+ "</cross-domain-policy>";
	}
}
