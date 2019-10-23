package lab07_pop.central;

import java.util.Collection;

class IDGenerator {

	static Integer getNext(Collection<Integer> occupiedIDs) {
		for (Integer id = 0; id != occupiedIDs.size(); ++id) {
			if (!occupiedIDs.contains(id))
				return id;
		}
		return occupiedIDs.size();
	}
}
