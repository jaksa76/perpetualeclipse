/*
* We reserve all rights in this document and in the information contained 
* therein. Reproduction, use or disclosure to third parties without express
* authority is strictly forbidden.
* 
* Copyright(c) ALSTOM (Switzerland) Ltd. 2007
*/


package perpetualeclipse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jaksa Vuckovic
 *
 */
public class TestReport2 {
    String name;
    public List<TestCase> tests = new ArrayList<TestCase>();
    public int numberOfFailures;
    public int numberOfErrors;

    public TestReport2(String name) {
        this.name = name;
    }
}
