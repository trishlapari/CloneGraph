package CloneGraph;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static CloneGraph.PreetyPrintClass.PreetyPrint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Graph {

    HashMap<Integer, List<Integer>> graph;
    HashMap<Integer, Entity> EntityIdMap;
    List<Entity> entities = new ArrayList<>();
    List<Link> links = new ArrayList<>();
    Set<Integer> visited;

    public Graph() {
        this.visited = new HashSet<>();
        this.EntityIdMap = new HashMap<>();
        this.graph = new HashMap<>();
    }

    public void dfs(Integer Id, Integer copyID) {
        if (graph.get(Id) != null) {
            List<Integer> children = graph.get(Id);
            for (Integer child : children) {
                if (!visited.contains(child)) {  
                    visited.add(child);
                    Integer newId = getNewId();
                    Entity newEntity = new Entity(newId, EntityIdMap.get(child).Name, EntityIdMap.get(child).Description);
                    entities.add(newEntity);
                    Link newLink = new Link(copyID, newId);
                    links.add(newLink);
                    dfs(child, newId);
                }
            }
        }
    }

    public Integer getNewId() {
        Random rand = new Random();
        Integer newId = rand.nextInt(50); //50000
        while (EntityIdMap.containsKey(newId)) {
            newId = rand.nextInt(50);
        }
        return newId;
    }

    public void clone(Integer idToClone) {
        if(EntityIdMap.containsKey(idToClone) == false){
            System.out.println("The given Id " + idToClone + " does not exits. Try again..");
            return;
        }
        Integer newId = getNewId();
        Entity newEntity = new Entity(newId, EntityIdMap.get(idToClone).Name, EntityIdMap.get(idToClone).Description);
        visited.add(idToClone);
        entities.add(newEntity);
        dfs(idToClone, newId);

        Set<Integer> keys = graph.keySet();
        Iterator<Integer> itr = keys.iterator();

        while (itr.hasNext()) {
            Integer id = itr.next();
            if (!Objects.equals(id, idToClone) && graph.get(id).contains(idToClone)) {
                Link newLink = new Link(id, newId);
                links.add(newLink);
            }
        }
    }
    
    public static void main(String args[]) {
        
        Scanner sc = new Scanner(System.in); 
        /*String program_name = sc.nextLine(); 
        String inputfile = sc.nextLine();
        String entityid1 = sc.nextLine();*/
        
        String program_name = args[0];
        if(!program_name.equals("clone")){
            System.out.println("Enter a valid program name. Try again..");
            return;
        }
        String inputfile = args[1]; //"C:\\Users\\Dell\\Documents\\test2.txt"; 
        Integer entityid = Integer.valueOf(args[2]); //5; 
        
        JSONRead jSONReadExample = new JSONRead();
        try {
            jSONReadExample.getParsedJSON(inputfile);
            
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
        Graph myClass = new Graph();
        myClass.entities = jSONReadExample.getEntities();
        myClass.links = jSONReadExample.getLinks();

        for (Entity entitie : myClass.entities) {
            myClass.EntityIdMap.put(entitie.Id, entitie);
            myClass.graph.put(entitie.Id, new ArrayList<>(Arrays.asList()));
        }

        for (Link link : myClass.links) {
            myClass.graph.get(link.FromEntityID).add(link.ToEntityID);
        }
        
        myClass.clone(entityid);
        
        // convert to JSON obejct
        JSONObject jo = new JSONObject(); 
        JSONArray jae = new JSONArray(); 
        JSONArray jal = new JSONArray(); 
        
        for (Entity entitie : myClass.entities) {
            JSONObject item = new JSONObject();
            item.put("entity_id", entitie.Id);
            item.put("name", entitie.Name);
            if(entitie.Description != "")
                item.put("description", entitie.Description);
            jae.add( item );
        }
        
        for (Link link : myClass.links) {
            JSONObject item = new JSONObject();
            item.put("from", link.FromEntityID);
            item.put("to", link.ToEntityID);
            jal.add( item );
        }
        jo.put("entities", jae); 
        jo.put("links", jal);
        
        System.out.println(PreetyPrint(jo.toString()));
    }
}
