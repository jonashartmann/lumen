package com.hartmann.lumen.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.hartmann.lumen.core.LumenGame;

public class LumenGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("lumen/");
    PlayN.run(new LumenGame());
  }
}
