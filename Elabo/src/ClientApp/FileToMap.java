package ClientApp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
public class FileToMap {

	private Map<String, Set<String>> mymap = new HashMap<>(); 
	
	public Map<String, Set<String>> getMap() {
		try (FileReader fr = new FileReader("Elaborato_IS/src/Data/città.txt")) {
			BufferedReader reader = new BufferedReader(fr);
			String line, nation, city;
			int pos;
			while ((line = reader.readLine()) != null) {
				pos = line.indexOf("@");				
				nation = line.substring(0, pos);
				city = line.substring(pos + 1);
				mymap.putIfAbsent(nation, new TreeSet<String>());
				mymap.get(nation).add(city);
			}
		} catch (Exception e) {
			System.out.println("Il file città.txt non esiste");
		}
		return mymap;
	}
}


