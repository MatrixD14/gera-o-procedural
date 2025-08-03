public class playe extends Component {
  private Vector2 joy = null, slid = null, slidmouse = null;
  private float speed = 5f, camx, camy, camrota = 1f;
  private SUIText fps;
  public WorldFile world, menu;
  private boolean playstop = false;
  private phisics fisica;

  void start() {
    fps = WorldController.findObject("see").findComponent("suitext");
    fisica = myObject.findComponent("phisics");
    joy = Input.getAxisValue("joy");
    slid = Input.getAxisValue("slid");
    slidmouse = new Vector2(Input.mouse.getSlideX(), Input.mouse.getSlideY());
  }

  void repeat() {
    sairgame();
    mudamudo();
    if (playstop) return;
    fps.setText("FPS: " + (int) (1 / Time.deltatime()));
    cameMouse(slid);
    move(joy.x, joy.y);
  }

  private void cameMouse(Vector2 slide) {
    camx += slide.x * camrota;
    camy = Math.clamp(-90, camy += slide.y * camrota, 90);
    myObject.getRotation().selfLookTo(new Vector3(Math.sin(-camx), 0, Math.cos(-camx)));
    myObject.findChildObject("see_player").getRotation().selfLookTo(new Vector3(0, Math.sin(-camy), Math.cos(-camy)));
  }

  private void move(float x, float y) {
    myObject.moveInSeconds(-(x * fisica.moveX()), 0, y * fisica.moveZ());
  } 

  private void sairgame() {
    if (Input.isKeyDown("sair")) GameController.quit();
    if (Input.isKeyDown("playStop")) playstop = !playstop;
    if (playstop) {
      Time.setTimeScale(0);
    } else Time.setTimeScale(1);
  }

  private void mudamudo() {
    if (Input.isKeyDown("reload")) WorldController.loadWorld(world);
    //if (Input.isKeyDown("menu")) WorldController.loadWorld(menu);
  }
}
