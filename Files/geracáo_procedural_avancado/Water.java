public class Water extends Component {
  private ModelRenderer WaterModelo;
  private Vertex WaterVertex;
  private List<Vector3> WaterVertices = new LinkedList<Vector3>(), WaterNormal = new LinkedList<Vector3>();
  private List<Vector2> WaterUV = new LinkedList<Vector2>();
  private int[] WaterTriangle;
  private chunkgen tama;
  private malha modela = new malha();

  void start() {
    if (!myObject.exists()) return;
    tama = WorldController.findObject("player").findComponent("chunkgen");
    WaterModelo = myObject.findComponent("modelrenderer");
    WaterVertex = new Vertex();
    if (myObject.exists()) return;
    WaterVertices.clear();
    WaterNormal.clear();
    WaterUV.clear();
  }

  public void WaterGera() {
    start();
    for (int z = 0; z <= 4; z++) {
      for (int x = 0; x <= 4; x++) {
        WaterVertices.add(new Vector3(x * 4, tama.waterlevel, z * 4));
        WaterNormal.add(new Vector3(0, 1, 0));
        WaterUV.add(new Vector2(x,z));
      }
    }
    WaterTriangle = new int[4 * 4 * 6];
    modela.triangulo(4, WaterTriangle);
    WaterVertex =modela.meshup(false, WaterModelo, tama.WaterMate, WaterTriangle, WaterVertices, WaterNormal,WaterUV);
  }
}
