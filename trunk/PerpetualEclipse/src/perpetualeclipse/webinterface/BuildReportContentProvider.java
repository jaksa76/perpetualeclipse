package perpetualeclipse.webinterface;

import java.util.Map;

import perpetualeclipse.Build;
import perpetualeclipse.BuildManager;


public class BuildReportContentProvider implements ContentProvider {
    private BuildManager buildManager = BuildManager.getInstance();

    public String invoke(Map parameters) {
        String buildId = (String) parameters.get("build-id");
        Build build = buildManager.getBuild(buildId);

        return build.getLatestReport().toXML();
    }

    /**
     * Used for testing.
     * @param buildManager the buildManager to be used for retriving the overview of builds
     */
    void setBuildManager(BuildManager buildManager) {
        this.buildManager = buildManager;
    }
}
