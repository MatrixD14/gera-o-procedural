public class gitPushMult {
  private gitPush gitpush;
  private String base, linkNamePasth, Commit, pasth, toke;
  private File dir;
  public void UpVariaPasth(gitPush gitpush, File dir, String base, String linkNamePasth, String Commit, String pasth, String toke) {
    this.gitpush = gitpush;
    this.base = base;
    this.linkNamePasth = linkNamePasth;
    this.Commit = Commit;
    this.pasth = pasth;
    this.toke = toke;
    this.dir = dir;
    
    new AsyncTask(
        new AsyncRunnable() {
          public Object onBackground(Object input) {
            processFile();
            return null;
          }

          public void onEngine(Object result) {
            Console.log("upload Ended");
          }
        });
  }

  private void busca(File dir, List<File> resul) {
    File[] file = dir.listFiles();
    if (file == null) return;
    for (File f : file) {
      if (f.isDirectory()) busca(f, resul);
      else resul.add(f);
    }
  }

  private File[] listFile(File dir) {
    List<File> resul = new ArrayList<File>();
    busca(dir, resul);
    return resul.toArray(new File[0]);
  }

  private void processFile() {
    File[] file = listFile(dir);
    if (file == null) return;
    for (int i = 0; i < file.length; i++) {
      File f = file[i];
      if (f.isDirectory()) continue;
      if (!f.getName().startsWith(".")) {
        String name = f.getAbsolutePath().replace(base, "");
        String cominho = f.getAbsolutePath();
        String API_Url = "https://api.github.com/repos/" + linkNamePasth + "/contents/Files/" + pasth + name + "?ref=main";

        // busca o sha do file

        String shas = gitpush.getSha(API_Url, toke);
        gitpush.GitPush(API_Url, Commit, cominho, toke, shas);

        Console.log(!shas.isEmpty() ? "update" : "create");
        Console.log("Link: " + API_Url);
      }
    } 
  }
}
