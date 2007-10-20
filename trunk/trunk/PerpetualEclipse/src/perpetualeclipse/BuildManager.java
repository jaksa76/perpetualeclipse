package perpetualeclipse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import perpetualeclipse.report.BuildReport;
import perpetualeclipse.report.OverviewReport;
import perpetualeclipse.tasks.Build;


public class BuildManager {
    private static BuildManager instance;

    private Map<String, Build> builds = new HashMap<String, Build>();


    public void addBuild(Build build) {
        builds.put(build.getName(), build);
    }


    public Map<String, Build> getBuilds() {
        return builds;
    }
    
    
    public void removeBuild(Build build) {
        builds.remove(build.getName());
    }


    public static BuildManager getInstance() {
        synchronized (BuildManager.class) {
            if (instance == null) instance = new BuildManager();
            return instance;
        }
    }


    public Build getBuild(String buildName) {
        return builds.get(buildName);
    }


    public OverviewReport getOverview() {
        return new OverviewReport(getAllReports());
    }


    private List<BuildReport> getAllReports() {
        List<BuildReport> reports = new ArrayList<BuildReport>();
        for (Build build : builds.values()) {
            reports.add(build.getLatestReport());
        }
        return reports;
    }
}
