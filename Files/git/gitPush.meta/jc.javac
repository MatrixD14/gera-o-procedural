public class gitPush {
  public void GitPush(String link, String menssage, String pasth, String token, String sha) {
    try {
      byte[] date = readFile(pasth);
      String encode = Base64.getEncoder().encodeToString(date);

      // json que sera enviado para api.github

      StringBuilder json = new StringBuilder();
      json.append("{\n  \"message\": \"").append(menssage).append("\",\n  \"content\": \"").append(encode).append("\",\n  \"branch\": \"main\"");
      if (sha != null && !sha.isEmpty()) json.append(",\n  \"sha\": \"").append(sha).append("\"");
      json.append("\n}");
      Console.log("{\n  \"message\": \""+menssage+"\",\n  \"branch\": \"main\",\n  "+((sha != null && !sha.isEmpty())?"\"sha\": \""+sha:"")+"\"\n}");

      URL url = new URL(link);

      // connect com o github

      HttpURLConnection com = (HttpURLConnection) url.openConnection();
      com.setRequestMethod("PUT");
      com.setDoOutput(true);
      com.setRequestProperty("Authorization", "token " + token);
      com.setRequestProperty("Content-Type", "application/json");

      OutputStream output = com.getOutputStream();
      output.write(json.toString().getBytes("UTF-8"));
      output.flush();
      output.close();

      /// vierifica se teve algum erro ao conectar como github

      int menss = com.getResponseCode();
      Console.log(menss == 201 || menss == 200 ? "file enviado sucess" : "erro em algum folder");

      InputStream input = (menss >= 400) ? com.getErrorStream() : com.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) result.append(line);
      Console.log("\nreposta: " + result.toString());
      input.close();
    } catch (Exception e) {
      Console.log(e);
    }
  }

  /// função que ve o tamanho do file que sera enviado para git para não manda faltando em byte

  public byte[] readFile(String pasth) {
    try {
      FileInputStream fs = new FileInputStream(pasth);
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      byte[] date = new byte[1024];
      int read;
      while ((read = fs.read(date)) != -1) buffer.write(date, 0, read);
      fs.close();
      return buffer.toByteArray();
    } catch (Exception e) {
      Console.log(e);
    }
    return null;
  }

  /// funcão vai busca no json da api.github onde esta o "sha" do file existe no github

  public String getSha(String link, String token) {
    try {
      URL url = new URL(link);

      HttpURLConnection com = (HttpURLConnection) url.openConnection();
      com.setRequestMethod("GET");
      com.setRequestProperty("Authorization", "token " + token);
      com.setRequestProperty("Accept", "application/vnd.github.v3+json");

      int menss = com.getResponseCode();
      InputStream input = (menss >= 400) ? com.getErrorStream() : com.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) result.append(line);
      input.close();

      GitPushJson json = (GitPushJson) Json.fromJson(result.toString(), GitPushJson.class, true);

      if (json != null && json.sha != null) return json.sha;
      else Console.log("falho o sha");
      input.close();
    } catch (Exception e) {
      Console.log(e);
    }
    return "";
  }

  public static class GitPushJson {
    public String sha;
  } 
}
