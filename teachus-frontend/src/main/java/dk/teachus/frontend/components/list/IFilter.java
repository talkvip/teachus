package dk.teachus.frontend.components.list;

import java.io.Serializable;

public interface IFilter<T> extends Serializable {

	boolean include(T o1, T o2);
	
}
