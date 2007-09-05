/*
* We reserve all rights in this document and in the information contained 
* therein. Reproduction, use or disclosure to third parties without express
* authority is strictly forbidden.
* 
* Copyright(c) ALSTOM (Switzerland) Ltd. 2007
*/


package perpetualeclipse;

/**
 *
 * @author Jaksa Vuckovic
 *
 */
public class TestCase {
    public String name;
    public String classname;
    public long time;
    public boolean failed = false;
    public int status;
    public String failureMessage;

    public TestCase(String name, String classname) {
        this.name = name;
        this.classname = classname;
    }
}
