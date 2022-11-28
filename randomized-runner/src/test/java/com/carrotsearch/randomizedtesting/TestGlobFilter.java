package com.carrotsearch.randomizedtesting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGlobFilter {
  @Test
  public void testBrackets() {
    GlobFilter gf = new MethodGlobFilter("ab(*)");
    Assertions.assertTrue(gf.globMatches("ab()"));
    Assertions.assertTrue(gf.globMatches("ab(foo)"));
    Assertions.assertTrue(gf.globMatches("ab(bar=xxx)"));

    gf = new MethodGlobFilter("test {yaml=resthandler/10_Foo/Bar (Hello)}");
    Assertions.assertTrue(gf.globMatches("test {yaml=resthandler/10_Foo/Bar (Hello)}"));
  }
}
