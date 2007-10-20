package perpetualeclipse.webinterface;

import java.util.Map;

import perpetualeclipse.BuildManager;


public class BuildSummaryContentProvider implements ContentProvider {
    private BuildManager buildManager = BuildManager.getInstance();

    public String invoke(Map parameters) {
        return buildManager.getOverview().toXML();
    }

    /**
     * Used for testing.
     * @param buildManager the buildManager to be used for retriving the overview of builds
     */
    void setBuildManager(BuildManager buildManager) {
        this.buildManager = buildManager;
    }
}
