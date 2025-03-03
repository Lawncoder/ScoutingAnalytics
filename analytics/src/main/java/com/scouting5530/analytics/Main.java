package com.scouting5530.analytics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scouting5530.analytics.BlueAllianceFetcher.Match;


public class Main {
    static int teamToCalculate = 8160;
    public static void main(String[] args) {
        BlueAllianceFetcher fetcher = new BlueAllianceFetcher();
        try {
            SheetsQuickstart quickstart = new SheetsQuickstart();
            quickstart.initialize();
            int last = 121;
            int first = 2;

            String range = "FERNDALE!B" + Integer.toString(first) + ":fAO" + Integer.toString(last);
            List<List<Object>> values = quickstart.getValues(range).getValues();
            Map<BlueAllianceFetcher.Match, Data> matchData = new HashMap<>();
            for (List<Object> o : values) {
              Data d = Data.deserialize(o);
              BlueAllianceFetcher.Match m = fetcher.new Match(d.teamNumber, d.qualificationNumber);
              matchData.put(m, d);
            }
            List<BlueAllianceFetcher.Match> matches = Arrays.asList(
              fetcher.new Match(4693, 12),
              fetcher.new Match(7054, 12),
              fetcher.new Match(6926, 12),
              fetcher.new Match(4836, 14),
              fetcher.new Match(6541, 14),
              fetcher.new Match(2142, 14),
              fetcher.new Match(2941, 15),
              fetcher.new Match(6213, 15),
              fetcher.new Match(4693, 15),
              fetcher.new Match(7365, 17),
              fetcher.new Match(5635, 17),
              fetcher.new Match(5886, 17),
              fetcher.new Match(5635, 19),
              fetcher.new Match(5261, 19),
              fetcher.new Match(8337, 19)
          ); //fetcher.getOpps(teamToCalculate); Cannot use this because no matches

            
            Map<Integer, Double> oppsAverageScore = new HashMap<>();
            for (BlueAllianceFetcher.Match m : matches) {
                int key = m.team;
                int averageScore = 0;
                int numOfMatches = 0;
                for (BlueAllianceFetcher.Match mm : matchData.keySet()) {
                  if (mm.team == key) {
                    Data specific = matchData.get(mm);
                    if (!specific.brokeDown) {
                      averageScore += specific.calculateTeleScore();
                      numOfMatches ++;
                    }
                  }
                }
                oppsAverageScore.put(key, averageScore * 1.0/numOfMatches);
            }
            System.out.println(oppsAverageScore);
            Map<Match, Integer> oppsScores = new HashMap<>();
            for (Match m : matches) {
                if ( ! matchData.get(m).brokeDown && !matchData.get(fetcher.new Match(teamToCalculate, m.qual)).driverDefRating.equals("N/A") && !matchData.get(m).driverRating.equals("N/A")) oppsScores.put(m, matchData.get(m).calculateTeleScore());
            }
            double deviation = 0;
            double deviationPercent = 0;
            int count = 0;
            for (Match m : oppsScores.keySet()) {
              deviation += (oppsAverageScore.get(m.team) - oppsScores.get(m));
             
              deviationPercent += ( oppsScores.get(m)/ oppsAverageScore.get(m.team) );
              count += 1;
            }
            deviation  *= 1.0/count;
            deviationPercent *= 1.0/count;
            System.out.println(deviation);
            System.out.println(deviationPercent);


            
            






        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void print(Object key, Object val) {
        System.out.println(key.toString() + " " + val.toString());
    }


}
/*
{
    "actual_time": 1710692166,
    "alliances": {
      "blue": {
        "dq_team_keys": [],
        "score": 73,
        "surrogate_team_keys": [],
        "team_keys": [
          "frc2960",
          "frc5530",
          "frc6615"
        ]
      },
      "red": {
        "dq_team_keys": [],
        "score": 68,
        "surrogate_team_keys": [],
        "team_keys": [
          "frc3322",
          "frc1502",
          "frc9209"
        ]
      }
    },
    "comp_level": "qm",
    "event_key": "2024miann",
    "key": "2024miann_qm80",
    "match_number": 80,
    "post_result_time": 1710692362,
    "predicted_time": 1710703390,
    "score_breakdown": {
      "blue": {
        "adjustPoints": 0,
        "autoAmpNoteCount": 0,
        "autoAmpNotePoints": 0,
        "autoLeavePoints": 2,
        "autoLineRobot1": "No",
        "autoLineRobot2": "No",
        "autoLineRobot3": "Yes",
        "autoPoints": 22,
        "autoSpeakerNoteCount": 4,
        "autoSpeakerNotePoints": 20,
        "autoTotalNotePoints": 20,
        "coopNotePlayed": true,
        "coopertitionBonusAchieved": true,
        "coopertitionCriteriaMet": true,
        "endGameHarmonyPoints": 0,
        "endGameNoteInTrapPoints": 0,
        "endGameOnStagePoints": 3,
        "endGameParkPoints": 2,
        "endGameRobot1": "Parked",
        "endGameRobot2": "Parked",
        "endGameRobot3": "StageRight",
        "endGameSpotLightBonusPoints": 0,
        "endGameTotalStagePoints": 5,
        "ensembleBonusAchieved": false,
        "ensembleBonusOnStageRobotsThreshold": 2,
        "ensembleBonusStagePointsThreshold": 10,
        "foulCount": 0,
        "foulPoints": 5,
        "g206Penalty": false,
        "g408Penalty": false,
        "g424Penalty": false,
        "melodyBonusAchieved": true,
        "melodyBonusThreshold": 15,
        "melodyBonusThresholdCoop": 15,
        "melodyBonusThresholdNonCoop": 18,
        "micCenterStage": false,
        "micStageLeft": false,
        "micStageRight": false,
        "rp": 3,
        "techFoulCount": 1,
        "teleopAmpNoteCount": 5,
        "teleopAmpNotePoints": 5,
        "teleopPoints": 46,
        "teleopSpeakerNoteAmplifiedCount": 4,
        "teleopSpeakerNoteAmplifiedPoints": 20,
        "teleopSpeakerNoteCount": 8,
        "teleopSpeakerNotePoints": 16,
        "teleopTotalNotePoints": 41,
        "totalPoints": 73,
        "trapCenterStage": false,
        "trapStageLeft": false,
        "trapStageRight": false
      },
      "red": {
        "adjustPoints": 0,
        "autoAmpNoteCount": 0,
        "autoAmpNotePoints": 0,
        "autoLeavePoints": 2,
        "autoLineRobot1": "Yes",
        "autoLineRobot2": "No",
        "autoLineRobot3": "No",
        "autoPoints": 22,
        "autoSpeakerNoteCount": 4,
        "autoSpeakerNotePoints": 20,
        "autoTotalNotePoints": 20,
        "coopNotePlayed": true,
        "coopertitionBonusAchieved": true,
        "coopertitionCriteriaMet": true,
        "endGameHarmonyPoints": 0,
        "endGameNoteInTrapPoints": 0,
        "endGameOnStagePoints": 0,
        "endGameParkPoints": 2,
        "endGameRobot1": "None",
        "endGameRobot2": "Parked",
        "endGameRobot3": "Parked",
        "endGameSpotLightBonusPoints": 0,
        "endGameTotalStagePoints": 2,
        "ensembleBonusAchieved": false,
        "ensembleBonusOnStageRobotsThreshold": 2,
        "ensembleBonusStagePointsThreshold": 10,
        "foulCount": 0,
        "foulPoints": 5,
        "g206Penalty": false,
        "g408Penalty": false,
        "g424Penalty": false,
        "melodyBonusAchieved": true,
        "melodyBonusThreshold": 15,
        "melodyBonusThresholdCoop": 15,
        "melodyBonusThresholdNonCoop": 18,
        "micCenterStage": false,
        "micStageLeft": false,
        "micStageRight": true,
        "rp": 1,
        "techFoulCount": 1,
        "teleopAmpNoteCount": 5,
        "teleopAmpNotePoints": 5,
        "teleopPoints": 41,
        "teleopSpeakerNoteAmplifiedCount": 6,
        "teleopSpeakerNoteAmplifiedPoints": 30,
        "teleopSpeakerNoteCount": 2,
        "teleopSpeakerNotePoints": 4,
        "teleopTotalNotePoints": 39,
        "totalPoints": 68,
        "trapCenterStage": false,
        "trapStageLeft": false,
        "trapStageRight": false
      }
    },
    "set_number": 1,
    "time": 1710691020,
    "videos": [
      {
        "key": "b8Lw2SCzlJU",
        "type": "youtube"
      }
    ],
    "winning_alliance": "blue"
  },
  {
    "actual_time": 1710700564,
    "alliances": {
      "blue": {
        "dq_team_keys": [],
        "score": 71,
        "surrogate_team_keys": [],
        "team_keys": [
          "frc4327",
          "frc5530",
          "frc6344"
        ]
      },
      "red": {
        "dq_team_keys": [],
        "score": 73,
        "surrogate_team_keys": [],
        "team_keys": [
          "frc1502",
          "frc6615",
          "frc1076"
        ]
      }
    },
 */