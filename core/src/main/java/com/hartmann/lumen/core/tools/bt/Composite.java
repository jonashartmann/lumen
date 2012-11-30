package com.hartmann.lumen.core.tools.bt;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Composite extends Behavior {
    public Collection<Behavior> m_Children;

    public Composite() {
	m_Children = new ArrayList<Behavior>();
    }
}
