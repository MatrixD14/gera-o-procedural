public class phisics extends Component {
  private float gravity = 5f;
  private Vector3 velocity;
  private float ColliderSize = 1f;
  private float eixoX, eixoZ;

  private TerreController correntChunck = null;
  private int correntX = Integer.MIN_VALUE;
  private int correntZ = Integer.MIN_VALUE;

  void start() {
    ;
    velocity = new Vector3();
  }

  void repeat() {
    Vector3 pos = myObject.position;
    float delta = Time.deltatime();

    velocity.y = velocity.y + (-gravity * delta);
    pos.y = pos.y + velocity.y * delta;

    float HeightMax = 1.5f;
    float veloDX = velocity.x * delta, veloDZ = velocity.z * delta;

    float blockInic = safeFormBlock(pos.x, pos.z);
    float blockZ = safeFormBlock(pos.x, pos.z + veloDZ);
    float blockX = safeFormBlock(pos.x + veloDX, pos.z);

    eixoX = Math.abs(blockZ - blockInic) > HeightMax ? 0 : 1f;
    eixoZ = Math.abs(blockX - blockInic) > HeightMax ? 0 : 1f;

    float HeightDistY = blockInic + ColliderSize;
    
    //HeightDistY = Math.max(HeightDistY,0);
    float diffY = HeightDistY - pos.y;

    if (eixoX != 0 && eixoZ != 0) {
      float stepSpeed = 5f;
      if (diffY > 0 && diffY <= HeightMax) {
        pos.y = Math.min(pos.y + stepSpeed * delta, HeightDistY);
        velocity.y = 0;
      } else if (pos.y < HeightDistY) {
        pos.y = HeightDistY;
        velocity.y = 0;
      }
    } 
  }

  public float safeFormBlock(float x, float z) {
    TerreController terren = getCachTerreno(x, z);
    return (terren != null) ? terren.getHeight(x, z) : 0f;
  }

  public TerreController getCachTerreno(float x, float z) {
    chunkgen chunck = (chunkgen) myObject.findComponent("chunkgen");
    if (chunck == null) return null;
    int W = chunck.width;
    int coodX = (int) Math.floor(x / W);
    int coodZ = (int) Math.floor(z / W);
    if (correntChunck == null || coodX != correntX || coodZ != correntZ) {

      long codekey = chunck.CodificKey(coodX, coodZ);
      SpatialObject ObjChunck = chunck.chunck.get(codekey);
      if (ObjChunck == null || !ObjChunck.exists()) correntChunck = null;
      else {
        correntChunck = (TerreController) ObjChunck.findComponent("TerreController");
        correntX = coodX;
        correntZ = coodZ;
      }
    }
    return correntChunck;
  }

  public float moveX() {
    return eixoX;
  }

  public float moveZ() {
    return eixoZ;
  }
}
