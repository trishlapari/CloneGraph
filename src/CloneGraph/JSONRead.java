package CloneGraph;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONRead {

    private List<Entity> entities;
    private List<Link> links;
    
    public List<Entity> getEntities(){
        return entities;
    }
    
    public List<Link> getLinks(){
        return links;
    }

    public void getParsedJSON(String filePath) throws Exception {
        try {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader(filePath);
            JSONObject jo = (JSONObject) parser.parse(reader); 
            entities = new ArrayList<>();
            links = new ArrayList<>();

            JSONArray ja = (JSONArray) jo.get("entities");
            Iterator itr2 = ja.iterator();
            while (itr2.hasNext()) {
                Object slide = itr2.next();
                JSONObject jsonObject2 = (JSONObject) slide;
                Long oldLong = (Long) jsonObject2.get("entity_id");
                Integer entity_id = oldLong.intValue();
                String name = (String) jsonObject2.get("name");
                String description = "";
                if (jsonObject2.containsKey("description")) {
                    description = (String) jsonObject2.get("description");
                }
                entities.add(new Entity(entity_id, name, description));
            }

            JSONArray ja1 = (JSONArray) jo.get("links");
            Iterator itr1 = ja1.iterator();
            while (itr1.hasNext()) {
                Object slide = itr1.next();
                JSONObject jsonObject2 = (JSONObject) slide;
                Long oldFrom = (Long) jsonObject2.get("from");
                Integer from = oldFrom.intValue();
                Long oldto = (Long) jsonObject2.get("to");
                Integer to = oldto.intValue();
                links.add(new Link(from, to));
            }
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
