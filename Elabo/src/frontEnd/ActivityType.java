package frontEnd;

import java.util.Map;
import java.util.TreeMap;

public enum ActivityType {
	Ritiro,
	RilascioNew,
	RilascioOld,
	Furto,
	Smarrito,
	Deteriorato;
	
	public Map<ActivityType,String> getNames() {
		Map<ActivityType,String> names = new TreeMap<>();
		for (var n : ActivityType.values()) {
			switch (n) {
				case RilascioNew:
					names.put(n,"Rilascio per la prima volta");
					break;
				case RilascioOld:
					names.put(n,"Rilascio per scadenza del precedente");
					break;
				default:
					names.put(n,n.toString());
			}
				
		}
		return names;
		
	}
	
}
