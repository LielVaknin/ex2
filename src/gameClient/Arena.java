package gameClient;

import api.*;
import com.google.gson.*;
import Server.Game_Server_Ex2;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.util.*;

public class Arena {

    public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1, EPS = EPS2;
    private dw_graph_algorithms graphAlgo;
    private List<Agent> agents;
    private List<Pokemon> pokemons;
    private game_service game;

    public Arena(int level) {
        game = Game_Server_Ex2.getServer(level);
        loadAgents(game.getAgents());
        loadPokemon(game.getPokemons());
        loadGraph(game.getGraph());
    }

    void loadGraph(String json) {
       graphAlgo = new DWGraph_Algo();
        graphAlgo.load(json);
    }

    private void loadAgents(String json) {
        List<Agent> l = new ArrayList<>();
        try {
            JSONObject ja = new JSONObject(json);
            JSONArray ags = ja.getJSONArray("Agents");
            for (int i = 0; i < ags.length(); i++) {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Agent.class, new Agent());
                Gson gson = builder.create();
                FileReader newAgent = new FileReader(json);
                Agent a = gson.fromJson(newAgent, Agent.class);
                newAgent.close();
                l.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.agents = l;
    }

    private void loadPokemon(String json) {
        List<Pokemon> l = new ArrayList<>();
        try {
            JSONObject jp = new JSONObject(json);
            JSONArray pok = jp.getJSONArray("Pokemons");
            for (int i = 0; i < pok.length(); i++) {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Agent.class, new Pokemon());
                Gson gson = builder.create();
                FileReader newPokemon = new FileReader(json);
                Pokemon p = gson.fromJson(newPokemon, Pokemon.class);
                newPokemon.close();
                l.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pokemons = l;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    private Object[][] pokemonsAndEdges() {
        Object [][] pokemonsEdges = new Object [pokemons.size()][2];
        int i = 0;
        Iterator<Pokemon> it = pokemons.iterator();
        while (it.hasNext()) {
           Pokemon p =  it.next();
           pokemonsEdges [i][0] = p;
           pokemonsEdges [i][1] = p.pokemonEdge(this.graphAlgo.getGraph());
           i++;
        }
        return pokemonsEdges;
    }

    public void startPositionOfAgents(){
        PriorityQueue<List> q = new PriorityQueue<>();
        Object [][] pokemonsEdges = pokemonsAndEdges();
        int rowlength = pokemonsEdges.length;
        int colLength = pokemonsEdges[0].length;

        for (int i = 0; i < rowlength; i++){
            for (int j = 0; j < rowlength; j++){
                List<node_data> path = this.graphAlgo.shortestPath(((edge_data)pokemonsEdges[i][1]).getSrc(), ((edge_data)pokemonsEdges[j][1]).getDest());
                for (int k = 1; k <= path.size(); i++){
                    for (int a = 0; a < rowlength; a++){
                        for (int b = 0; b < colLength; b++){
                            path.get(k-1), path.get(k)
                        }
                        }
                }
                q.add(path);
        }


        }
    }
}
