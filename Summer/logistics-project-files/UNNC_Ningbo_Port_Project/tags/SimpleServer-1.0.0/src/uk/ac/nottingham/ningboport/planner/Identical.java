package uk.ac.nottingham.ningboport.planner;

public abstract interface Identical<T> {
	public abstract boolean geographicallyIdenticalTo(T t);
}
