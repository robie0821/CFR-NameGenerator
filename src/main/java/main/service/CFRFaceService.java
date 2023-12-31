package main.service;

import main.vo.FaceVo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Service
public class CFRFaceService {
  private static final String clientId = "7ut9rilzfg";
  private static final String clientSecret = "cpmZYpsxRmsa01S0TSsmQuP2OLEFALkdhSnviYh0";
  private static final String apiURL = "https://naveropenapi.apigw.ntruss.com/vision/v1/face";

  public ArrayList<FaceVo> clovaFace(MultipartFile imageFile) {
    ArrayList<FaceVo> faceList = new ArrayList<>();

    try {
      String paramName = "image";
      byte[] fileBytes = imageFile.getBytes();

      URL url = new URL(apiURL);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setUseCaches(false);
      con.setDoOutput(true);
      con.setDoInput(true);

      String boundary = "---" + System.currentTimeMillis() + "---";
      con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
      con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
      con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

      OutputStream outputStream = con.getOutputStream();
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
      String LINE_FEED = "\r\n";

      String fileName = imageFile.getOriginalFilename();
      writer.append("--" + boundary).append(LINE_FEED);
      writer.append("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
      writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
      writer.append(LINE_FEED);
      writer.flush();

      outputStream.write(fileBytes, 0, fileBytes.length);
      outputStream.flush();

      writer.append(LINE_FEED).flush();
      writer.append("--" + boundary + "--").append(LINE_FEED);
      writer.close();

      BufferedReader br;
      int responseCode = con.getResponseCode();

      if (responseCode == 200) {
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }

      StringBuilder response = new StringBuilder();
      String inputLine;
      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }
      br.close();

      faceList = jsonToVoList(response.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return faceList;
  }

  public ArrayList<FaceVo> jsonToVoList(String jsonResultStr) {
    ArrayList<FaceVo> faceList = new ArrayList<>();

    try {
      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonResultStr);
      JSONArray facesArray = (JSONArray) jsonObj.get("faces");

      if (facesArray != null) {
        for (Object faceObj : facesArray) {
          JSONObject face = (JSONObject) faceObj;

          JSONObject emotions = (JSONObject) face.get("emotion");
          JSONObject age = (JSONObject) face.get("age");
          JSONObject gender = (JSONObject) face.get("gender");

          String genderValue = (String) gender.get("value");
          double genderConfidence = (double) gender.get("confidence");
          String ageValue = (String) age.get("value");
          double ageConfidence = (double) age.get("confidence");
          String emotionValue = (String) emotions.get("value");
          double emotionConfidence = (double) emotions.get("confidence");

          FaceVo vo = new FaceVo();
          vo.setGender(genderValue);
          vo.setGenderConfidence(genderConfidence);
          vo.setAge(ageValue);
          vo.setAgeConfidence(ageConfidence);
          vo.setEmotion(emotionValue);
          vo.setEmotionConfidence(emotionConfidence);

          faceList.add(vo);
        }
      } else {
        FaceVo vo = new FaceVo();
        vo.setGender("없음");
        vo.setGenderConfidence(0);
        vo.setAge("없음");
        vo.setAgeConfidence(0);
        vo.setEmotion("없음");
        vo.setEmotionConfidence(0);

        faceList.add(vo);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return faceList;
  }
}