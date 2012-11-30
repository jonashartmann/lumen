package com.hartmann.lumen.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.hartmann.lumen.core.LumenGame;

public class LumenGameJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/hartmann/lumen/resources");
    PlayN.run(new LumenGame());
  }
}
