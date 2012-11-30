package com.hartmann.lumen.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.hartmann.lumen.core.LumenGame;

public class LumenGameActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/hartmann/lumen/resources");
    PlayN.run(new LumenGame());
  }
}
