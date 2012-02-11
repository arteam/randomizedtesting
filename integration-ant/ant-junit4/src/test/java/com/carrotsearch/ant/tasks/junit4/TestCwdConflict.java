package com.carrotsearch.ant.tasks.junit4;


import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;


public class TestCwdConflict extends AntBuildFileTestBase {

  @Before
  public void setUp() throws Exception {
    URL resource = getClass().getClassLoader().getResource("junit4.xml");
    super.setupProject(new File(resource.getFile()));
  }

  @Test 
  public void cwdconflict() {
    super.executeTarget("cwdconflict");
  }
}
