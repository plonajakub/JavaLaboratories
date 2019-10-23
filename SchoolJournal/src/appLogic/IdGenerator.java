package appLogic;

import java.util.Set;

public class IdGenerator {
	
	public static Integer getNext(Set<Integer> occupiedJournalNumbers) {
		for (Integer id = 1; id != occupiedJournalNumbers.size() + 1; ++id) {
			if (!occupiedJournalNumbers.contains(id))
				return id;
		}
		return occupiedJournalNumbers.size() + 1;
	}
}
