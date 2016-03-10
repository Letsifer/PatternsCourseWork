/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watkinsalgo.Geometry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import watkinsalgo.util.DoublePair;

/**
 *
 * @author gea
 */
public class SegmentTest {
    
    public SegmentTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMinimumAndMaximumY method, of class Segment.
     */
    @Test
    public void testGetMinimumAndMaximumY() {
        System.out.println("getMinimumAndMaximumY");
        Point a = new Point(2, 5, 3), b = new Point(3, 4, 6);
        Segment instance = new Segment(a, b);
        DoublePair expResult = new DoublePair(4, 5);
        DoublePair result = instance.getMinimumAndMaximumY();
        assertEquals(expResult, result);
        
        System.out.println("min and max y passed");
    }

    /**
     * Test of getIntersectionWithY method, of class Segment.
     */
    @Test
    public void testGetIntersectionWithY() {
        System.out.println("getIntersectionWithY");
        double currentY = 0.0;
        int testsNumber = 5;
        Segment[] tests = new Segment[testsNumber];
        
        Point[][] results = new Point[testsNumber][];
        Point a = new Point(2, 5, 3), b = new Point(3, 4, 6);
        tests[0] = new Segment(a, b);
        results[0] = null;
        
        a = new Point(5, 0, 3); b = new Point(5, 4, 7);
        tests[1] = new Segment(a, b);
        results[1] = new Point[]{new Point(5, 0, 3)};
        
        a = new Point(5, -7, 3); b = new Point(5, 0, 7);
        tests[2] = new Segment(a, b);
        results[2] = new Point[]{new Point(5, 0, 7)};
        
        a = new Point(5, -5, 0); b = new Point(5, 5, 0);
        tests[3] = new Segment(a, b);
        results[3] = new Point[]{new Point(5, 0, 0)};
        
        a = new Point(5, 0, 0); b = new Point(12, 0, 7);
        tests[4] = new Segment(a, b);
        results[4] = new Point[]{new Point(5, 0, 0), new Point(12, 0, 7)};
        
        for (int i = 0; i < testsNumber; i++) {
            Point[] expResult = results[i];
            Point[] result = tests[i].getIntersectionWithY(currentY);
            assertArrayEquals(expResult, result);
        }       
        System.out.println("inter with y passed");
    }

    /**
     * Test of getIntersectionWithSegment method, of class Segment.
     */
    @Test
    public void testGetIntersectionWithSegment() {
        System.out.println("getIntersectionWithSegment");
        Segment first = new Segment(new Point(1, 0, 1), new Point(5, 0, 3));
        Segment second = new Segment(new Point(4, 0, 1), new Point(2, 0, 3));
        Point[] expResult = new Point[]{new Point(3, 0, 2)};
        Point[] result = first.getIntersectionWithSegment(second);
        System.out.println(result.length + " result1: " + result[0]);
        assertArrayEquals(expResult, result);
        
        first = new Segment(new Point(0, 0, 0), new Point(3, 0, 3));
        second = new Segment(new Point(2, 0, 2), new Point(4, 0, 4));
        
        expResult = new Point[]{new Point(2, 0, 2), new Point(3, 0, 3)};
        result = first.getIntersectionWithSegment(second);
        System.out.println("result2: " + result[0] + ", " + result[1]);
        assertArrayEquals(expResult, result);
        
        System.out.println("inter segments passed");
    }


    
}
