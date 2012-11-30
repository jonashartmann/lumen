package com.hartmann.lumen.core.tools.bt;

import java.util.Iterator;

import playn.core.Asserts;

import com.hartmann.lumen.core.tools.bt.BehaviorObserverDelegate.BehaviorObserver;

public abstract class Sequence extends Composite {

    protected final BehaviorTree m_pBehaviorTree;
    protected Iterator<Behavior> m_Current;
    protected Behavior currentBehavior;

    public Sequence(BehaviorTree bt) {
	super();
	m_Current = null;
	currentBehavior = null;
	this.m_pBehaviorTree = bt;
    }

    public void onInitialize() {
	m_Current = m_Children.iterator();
	BehaviorObserverDelegate observer = BehaviorObserverDelegate
		.register(new BehaviorObserver() {

		    @Override
		    public void onComplete() {
			onChildComplete();
		    }
		});
	currentBehavior = m_Current.next();
	m_pBehaviorTree.insert(currentBehavior, observer);
    }

    private final void onChildComplete() {
	Behavior child = currentBehavior;
	if (child.m_eStatus == Status.BH_FAILURE) {
	    m_pBehaviorTree.terminate(this, Status.BH_FAILURE);
	}

	Asserts.check(child.m_eStatus == Status.BH_SUCCESS);
	if (!m_Current.hasNext()) {
	    m_pBehaviorTree.terminate(this, Status.BH_SUCCESS);
	} else {
	    currentBehavior = m_Current.next();
	    BehaviorObserverDelegate observer = BehaviorObserverDelegate
		    .register(new BehaviorObserver() {

			@Override
			public void onComplete() {
			    onChildComplete();
			}
		    });
	    m_pBehaviorTree.insert(currentBehavior, observer);
	}
    }

    public Status update() {
	return null;
    }

}
