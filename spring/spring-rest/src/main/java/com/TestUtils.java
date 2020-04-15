package com;

import model.Test;

public class TestUtils {
  public static void check(Test test) {
    if (test == null) {
      throw new RuntimeException("해당 값이 존재 하지 않습니다");
    }
  }
}
