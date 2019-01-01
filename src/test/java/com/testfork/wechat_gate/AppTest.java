package com.testfork.wechat_gate;

import com.testfork.wechat_gate.util.RestTemplateUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    /**
     * 
     */
    public void testWechat(){
    	String msg = "this is a message";
    	String result = RestTemplateUtil.postForObject("http://localhost:8080/wechat/msg", msg, String.class);
    	System.out.println(result);
    }
    
    /**
     * 
     */
    public void testSign(){
    	String result = RestTemplateUtil.getForObject("http://localhost:8080/wechat/{a}", String.class, "sign");
    	System.out.println(result);
    }
}
