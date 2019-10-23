package lab05.logic;

import java.util.Collection;

public class IDGenerator {
	
	public static Integer getNext(Collection<Integer> occupiedIDs) {
		for (Integer id = 0; id != occupiedIDs.size(); ++id) {
			if (!occupiedIDs.contains(id))
				return id;
		}
		return occupiedIDs.size();
	}
}
