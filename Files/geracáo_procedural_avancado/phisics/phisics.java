public class phisics extends Component {
  private float gravity = 5f;
  private Vector3 velocity;
  private float collider = 1f;
  private float eixoX, eixoZ;

  void start() {
    velocity = new Vector3();
  }

  void repeat() {
    Vector3 pos = myObject.position;
    float delta = Time.deltatime();
    velocity.y = velocity.y + (-gravity * delta);

    float blockInsite = getBlock(pos.x, pos.z);
    float blockAltura = getBlock(pos.x + velocity.x * delta, pos.z * velocity.z * delta);
    float checkAlto = Math.abs(blockAltura - blockInsite);
    float mult = 5f;
    //Console.log("abs: "+ checkAlto);
     if (checkAlto >20f ) mult = 5f;
     else if (checkAlto > 6f) mult = 2.5f;
    eixoX = mult;
    eixoZ = mult;

    float altura = blockInsite + collider;
    float posmy = pos.y + velocity.y * delta;
    if (posmy <= altura && velocity.y < 0) {
      pos.y = Math.lerpInSeconds(pos.y, altura, 4f);
      velocity.y = 0;
    } 
    pos.set(pos.x + velocity.x * delta, pos.y + velocity.y * delta, pos.z + velocity.z * delta);
  }

  public float moveX() {
    return eixoX;
  }

  public float moveZ() {
    return eixoZ;
  }

  public float getBlock(float x, float z) {
    TerreController terreno = getTerreno(x, z);
    if (terreno == null) return 0f;
    return terreno.getHeight(x, z);
  }

  public TerreController getTerreno(float x, float z) {
    chunkgen chunck = (chunkgen) myObject.findComponent("chunkgen");
    if (chunck == null) return null;
    int coodX = (int) Math.floor(x / chunck.width);
    int coodZ = (int) Math.floor(z / chunck.width);
    long codekey = chunck.CodificKey(coodX, coodZ);
    SpatialObject ObjChunck = chunck.chunck.get(codekey);
    if (ObjChunck == null || !ObjChunck.exists()) return null;
    return (TerreController) ObjChunck.findComponent("TerreController");
  }
}
