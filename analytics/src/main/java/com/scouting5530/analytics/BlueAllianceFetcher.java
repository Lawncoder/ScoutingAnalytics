package com.scouting5530.analytics;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;




public class BlueAllianceFetcher {
    public final String api_url = "https://www.thebluealliance.com/api/v3";
    public final String api_key = "ME9K7byqRTWn6vvWDvzCncK0Io80Q4UvUt2Kbddp9dhUyW7QN5ex0POQmjvVnnfp";
    public final String event_id = "2024miann";
    private File ferndaleTeams;


    /* /team/{team_key}/event/{event_key}/matches */
    public JSONArray getMatches(int team) throws URISyntaxException, IOException, InterruptedException{
        String url = api_url + "/team/" + getTeamId(team) + "/event/" + event_id + "/matches";
        URI uri = new URI(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
        .header("X-TBA-Auth-Key", api_key)
        .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray out = new JSONArray();
        if (response.statusCode()==200) {
            out = new JSONArray(response.body());
        }
       

        return out;


    }
    public List<Match> getOpps(JSONArray matches, int teamNum) {
        List<Match> out = new ArrayList<>();
        for (int i = 0; i < matches.length(); i++) {
            JSONObject obj = (JSONObject) matches.get(i);
            int matchNum = obj.getInt("match_number");
            JSONObject red = obj.getJSONObject("alliances").getJSONObject("red");
            JSONObject blue = obj.getJSONObject("alliances").getJSONObject("blue");
            JSONArray keys = red.getJSONArray("team_keys");
            boolean useRed = true;
            for (Object o : keys) {
                if (((String)o).equals(getTeamId(teamNum))) {
                    useRed = false;
                }
            }
            keys = useRed ? keys : blue.getJSONArray("team_keys");
            for (Object o : keys) {
                String id = (String)o;
                int num = Integer.parseInt(id.substring(3));
                Match m = new Match(num, matchNum);
                out.add(m);
            }


        }
   
        return out;
    }
    public List<Match> getOpps(int teamNum)throws URISyntaxException, IOException, InterruptedException {
        return getOpps(getMatches(teamNum), teamNum);
    }
    public class Match {
        int team;
        int qual;
        public Match() {
            team = 0;
            qual = 0;
        }
        public Match(int team, int qual) {
            this.team = team;
            this.qual = qual;
        }
        @Override
        public String toString() {
            
            return "(" + Integer.toString(team) + "," + Integer.toString(qual) + ")";
        }
        @Override 
        public boolean equals(Object m) {
            if (!(m instanceof Match)) {
                return false;
            }
            Match o = (Match) m;
            
            return (o.team == this.team)&&(o.qual==this.qual);
        }
        @Override
        public int hashCode() {
            return this.team * 1000 + this.qual;
        }
        

        
    }
  
    /* /event/{event_key}/teams */
    public String getTeamId(int teamId){
        return "frc" + Integer.toString(teamId);

 
    }
    
}


 