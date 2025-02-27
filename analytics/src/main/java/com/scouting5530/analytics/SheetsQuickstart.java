package com.scouting5530.analytics;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SheetsQuickstart {
  private static final String APPLICATION_NAME = "Green Goggles";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";
  private static final String SPREADSHEET_ID  = "1fmYuVSqP3JfEZXwsBNbUWnL-HnkDAWfgd_0JHj8GOzQ";
  Sheets service;
  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES =
      Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
  private static final String CREDENTIALS_FILE_PATH = "/client_secret_385220316235-dviq43cq8k960vjjotc3jjk0lv8sl87b.apps.googleusercontent.com.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }


  public void initialize() {
    try {
     // Build a new authorized API client service.
     final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
     
     Sheets service =
         new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
             .setApplicationName(APPLICATION_NAME)
             .build();
     this.service = service;
  }

  catch (Exception ex) {
    ex.printStackTrace();
  }
}

  public ValueRange getValues(String range) throws IOException {
  
    return service.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
  }


//the function assumes that the C column is the TEAM NUMBER and B column is the QUAL/MATCH NUMBER bc thats what the sheet says rn, change if needed

public List<Object> findRowByTeamAndMatch(int teamNumber, int matchNumber) throws IOException {
  // Step 1: Get the entire sheet's used range dynamically
  String sheetName = "Scouting Data 2025"; // Change if needed
  String range = getDynamicRange(sheetName);

  // Step 2: Fetch data from the dynamically determined range
  ValueRange response = service.spreadsheets().values()
          .get(SPREADSHEET_ID, range)
          .execute();

  List<List<Object>> values = response.getValues();
  if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
      return null;
  }

  // Step 3: Search for the matching row
  for (List<Object> row : values) {
      if (row.size() > 2) { // Ensure the row has enough columns
          try {
              int rowMatchNumber = Integer.parseInt(row.get(1).toString()); // Column B (Index 1)
              int rowTeamNumber = Integer.parseInt(row.get(2).toString()); // Column C (Index 2)

              if (rowMatchNumber == matchNumber && rowTeamNumber == teamNumber) {
                  return row; // Return the matching row
              }
          } catch (NumberFormatException e) {
              System.err.println("Skipping invalid row: " + row);
          }
      }
  }

  return null; // No matching row found
}




//changes the range based on the sheet
private String getDynamicRange(String sheetName) throws IOException {
  // Fetch metadata about the sheet's dimensions
  String metaRange = sheetName + "!A1:Z1000"; // Fetch a broad range
  ValueRange response = service.spreadsheets().values()
          .get(SPREADSHEET_ID, metaRange)
          .execute(); 

  List<List<Object>> values = response.getValues();
  if (values == null || values.isEmpty()) {
      throw new IOException("No data found in sheet: " + sheetName);
  }

  // Find last non-empty row
  int lastRow = values.size();
  int lastColumn = 0;

  for (List<Object> row : values) {
      lastColumn = Math.max(lastColumn, row.size()); // Get max column index
  }

  if (lastRow == 0 || lastColumn == 0) {
    return sheetName + "!A1:A1"; // Fallback if sheet is empty
}

  // Convert last column index to letter (e.g., 3 → "C", 26 → "Z")
  String lastColumnLetter = columnNumberToLetter(lastColumn);

  // Construct the range dynamically
  return sheetName + "!A1:" + lastColumnLetter + lastRow;
}




//converts the column index into the letter 
private String columnNumberToLetter(int colNum) {
  StringBuilder columnLetter = new StringBuilder();
  while (colNum > 0) {
      colNum--; // Convert to 0-based index
      columnLetter.insert(0, (char) ('A' + (colNum % 26)));
      colNum /= 26;
  }
  return columnLetter.toString();
}



}








