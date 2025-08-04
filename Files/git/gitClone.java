public class gitClone {

  public void GitClone(String link, String path) {
    InputStream in = null;
    BufferedInputStream bs = null;
    FileOutputStream fs = null;
    File addPasth = new File(path);
    File paretDir = addPasth.getParentFile();
    if (paretDir != null && !paretDir.exists()) paretDir.mkdirs();

    try {
      in = new URL(link).openStream();
      bs = new BufferedInputStream(in);
      fs = new FileOutputStream(addPasth);
      byte[] date = new byte[1024];
      int count;
      while ((count = bs.read(date, 0, 1024)) != -1) {
        fs.write(date, 0, count);
      }
      if (addPasth.length() == 0) Console.log("falho file void or no exists");
    } catch (IOException e) {
      Console.log("erro no Download file: " + e.getMessage());
    } finally {
      try {
        if (fs != null) fs.close();
      } catch (IOException e) {
        Console.log("erro no FileOutputStream: " + e.getMessage());
      }
      try {
        if (bs != null) bs.close();
      } catch (IOException e) {
        Console.log("erro no BufferedInputStream: " + e.getMessage());
      }
      try {
        if (in != null) in.close();
      } catch (IOException e) {
        Console.log("erro no InputStream: " + e.getMessage());
      } 
    }
  }
}
