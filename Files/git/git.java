public class git extends Component {
  @Order(idx = -2)
  public String NameGitIsRepository; // "name do usuario do github/ nome do repositorio que sera alocado os arquivo-->  usuario/repositorio"

  @Order(idx = -1)
  public String path; // "caminho da pasta a pasta /Files/ e a pasta raiz que poderar ser enviado para o github "
  
  @Order(idx = 1)
  public String BranchOrCommitRecovery = "main"; /// branch e uma linha paralela do projeto que muda com o tempo 
                                                 /// CommitRecovery usa o codigo sha para voltar no tempo
  
  @Order(idx = 2)
  public String Commit = "comito"; // "etiqueta que marca o tempo de modificação do arquivo que quanda o sha"

  @Order(idx = 3)
  public String token; // "codigo toke do github"

  private String Dir;

  private gitClone gitclone = new gitClone();
  private gitPush gitpush = new gitPush();
  private gitPushMult gitpushmult = new gitPushMult();
  private gitCloneMult gitclonemult = new gitCloneMult();

  @Order(idx = 0)
  public PropertiesButton DownLoad =
      new PropertiesButton(
          new PropertiesButtonListener() {
            void onClicked() {
              DownLoad();
            }
          });

  @Order(idx = 4)
  public PropertiesButton UpLoad =
      new PropertiesButton(
          new PropertiesButtonListener() {
            void onClicked() {
              UpLoad();
            }
          });

  public void DownLoad() {
    String DownloadUrl = null;
    boolean onoffFile = false;
    if (path.contains(".")) onoffFile = true;
    if (onoffFile && verifica(false, false)) {         
      if((BranchOrCommitRecovery == null || BranchOrCommitRecovery.length() < 40) && verifica(false, true)) BranchOrCommitRecovery = "main";
      else Console.log("recuperando file no tempo");
      
      DownloadUrl = "https://raw.githubusercontent.com/" + NameGitIsRepository +"/"+ BranchOrCommitRecovery+ "/Files/" + path;
      gitclone.GitClone(DownloadUrl, Dir);

      StringBuilder InforDate = new StringBuilder();
      InforDate.append("{\n \"pasth\": \"").append(Dir).append("\",\n \"NameFile\": \"").append(path).append("\",\n \"Link\": \"").append(DownloadUrl).append("\"\n}");
      Console.log(InforDate.toString());

    } else if (verifica(true, false)) {
      DownloadUrl = "https://api.github.com/repos/" + NameGitIsRepository + "/contents/Files/" + path + "?ref=main";
      File dir = new File(Dir);
      gitclonemult.gitPasthAll(gitclone, dir, DownloadUrl, token);
    } 
  }

  public void UpLoad() {
    boolean onoffFile = false;
    if (path.contains(".")) onoffFile = true;
    if(!verifica(true, true)) return;
    if (onoffFile) {
      String API_Url = "https://api.github.com/repos/" + NameGitIsRepository + "/contents/Files/" + path + "?ref=main";
      // busca o sha do file

      String shas = gitpush.getSha(API_Url, token);
      gitpush.GitPush(API_Url, Commit, Dir, token, shas);

      Console.log(!shas.isEmpty() ? "update" : "create");
      Console.log("Link: " + API_Url);
    } else {
      File dir = new File(Dir);
      if (dir == null || !dir.exists()) return;

      // "lista todos oa file que existe"
      gitpushmult.UpVariaPasth(gitpush, dir, dir.getAbsolutePath() + "", NameGitIsRepository, Commit, path, token);
    }
  }

  public boolean verifica(boolean Token, boolean pont) {
    if (!NameGitIsRepository.contains("/") || NameGitIsRepository.isEmpty()) {
      Toast.showText("esta errado o link do \"nome do usuario do git\" / nome do repositorio", 1);
      return false;
    }
    if (path == null || path.isEmpty() || (pont && !new File(Directories.getProjectFolder() + "/Files/" + path).exists())) {
      Toast.showText("caminho para o arquivo esta faltando ou errado", 1);
      return false;
    }
    if (Token && (token == null || token.length() < 20)) {
      Toast.showText("o toke esta vazio ou faltando", 1);
      return false;
    }

    Dir = Directories.getProjectFolder() + "Files/" + path;
    return true;
  }
}
