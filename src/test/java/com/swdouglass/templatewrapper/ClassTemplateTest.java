/*
 * Copyright 2010, Scott Douglass <scott@swdouglass.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * on the World Wide Web for more details:
 * http://www.fsf.org/licensing/licenses/gpl.txt
 */
package com.swdouglass.templatewrapper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author scott
 */
public class ClassTemplateTest extends AbstractTemplateTest {

  private ClassTemplate template;

  public ClassTemplateTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    template = new ClassTemplate();
    template.setTemplatePath("/test.vm");
    loadExpectedResult();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testMerge() {
    merge(new ClassTemplateTest.AttributesPojo());
    assertEquals(getMergeResult(), getExpectedMergeResult());
  }

  @Test
  public void testMerge2() {
    merge(new ClassTemplateTest.AttributesPojo2());
    assertEquals(getMergeResult(), getExpectedMergeResult());
  }

  private void merge(Object inAttributes) {
    System.out.println("test merge using instance of: " +
      inAttributes.getClass().getName());
    System.out.println("Expected result: \n" + getExpectedMergeResult());
    template.setData(inAttributes);
    template.init();
    setMergeResult(template.merge());
    System.out.println("Expected result: \n" + getExpectedMergeResult());
  }

  private static class AttributesPojo {
    public String name = "swan";
    public String date = "09/21/2010";
  }

  private static class AttributesPojo2 {
    private String name = "swan";
    private String date = "09/21/2010";
    public String getName() {
      return name;
    }
    public String getDate() {
      return date;
    }
  }
}
