public class gitCloneMult {
  private gitClone gitclone;
  private String link, token;
  private File Dir;

  public void gitPasthAll(gitClone gitclone, File Dir, String link, String token) {
    this.Dir = Dir;
    this.gitclone = gitclone;
    this.link = link;
    this.token = token;
    new AsyncTask(
        new AsyncRunnable() {
          public Object onBackground(Object input) {
            processFile();
            return null;
          }

          public void onEngine(Object result) {
            Console.log("Download finalizado");
          }
        });
  }

  public void processFile() {
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
      com.disconnect();

      FileJson(result.toString());
    } catch (IOException e) {
      Console.log("erro no json: " + e.getMessage());
    }
  }

  public void FileJson(String jsons) {
    try {
      GitCloneJson[] file = (GitCloneJson[]) Json.fromJson(jsons, GitCloneJson[].class, true);
      for (GitCloneJson json : file) {
        if (json.type.equals("file")) {
          String destino = new File(Dir, json.name).getAbsolutePath();
          gitclone.GitClone(json.download_url, destino);
          StringBuilder InforDate = new StringBuilder();
          InforDate.append("{\n \"pasth\": \"").append(Dir).append("\",\n \"NameFile\": \"").append(destino).append("\",\n \"Link\": \"").append(json.download_url).append("\"\n}");
          Console.log(InforDate.toString());
          Console.log(new File(destino).exists()?"file já existe sobrescrevendo":"");
        }
      } 
    } catch (Exception e) {
      Console.log("erro no Download file: " + e.getMessage());
    }
  }

  public static class GitCloneJson {
    public String name;
    public String download_url;
    public String type;
    public String sha;
  }
}
