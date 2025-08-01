public class TerreController extends Component {
  private SpatialObject armLog, Obj, voi;
  private ModelRenderer TerrModelo;
  private Vertex TerrVertex;
  private List<Vector3> TerrVertices = new LinkedList<Vector3>(), TerrNormal = new LinkedList<Vector3>();
  private List<Vector2> TerrUV = new LinkedList<Vector2>();
  private int[] TerrTriangle;
  private PerlinNoise perlin = new PerlinNoise(100);
  private chunkgen tama;
  private Vector3 mypos;
  private HashMap<Long, Float> HeightMap = new HashMap<Long, Float>();
  private int[][] block = null;
  private malha modela = new malha();

  void start() {
    if (!myObject.exists()) return;
    tama = WorldController.findObject("player").findComponent("chunkgen");
    armLog = WorldController.findObject("log");
    TerrModelo = myObject.findComponent("modelrenderer");
    TerrVertex = new Vertex();
    reload();
    if (Obj == null || !Obj.exists()) return;
    Water gera = Obj.findComponent("Water");
    gera.WaterGera();
  }

  private void myposblock() {
    mypos = myObject.position;
  }

  public void reload() {
    myposblock();
    WaterCriate();
    MatrizChunck();
    generat();
    TerrVertex = modela.meshup(false, TerrModelo, tama.TerrMate, TerrTriangle, TerrVertices, TerrNormal, TerrUV);
    if (myObject.exists()) return;
    HeightMap.clear();
    TerrVertices.clear();
    TerrNormal.clear();
    TerrUV.clear();
  } 

  private void MatrizChunck() {
    block = new int[tama.width + 1][tama.width + 1];
    for (int z = 0; z <= tama.width; z++) {
      for (int x = 0; x <= tama.width; x++) {
        float y = modela.perlinnoises(tama, myObject, x, z);
        if (y + mypos.y >= tama.waterlevel - 2 && y + mypos.y <= 2 + tama.waterlevel) block[z][x] = 1;
        else if (y + mypos.y >= tama.waterlevel - 8 && y + mypos.y <= tama.waterlevel - 2) block[z][x] = 4;
        else if (y + mypos.y <= tama.waterlevel - 8) block[z][x] = 5;
        else block[z][x] = 3;
      }
    }
  }

  private void generat() {
    TerrTriangle = new int[tama.width * tama.width * 6];
    for (int z = 0; z <= tama.width; z++) {
      for (int x = 0; x <= tama.width; x++) {
        float y = modela.perlinnoises(tama, myObject, x, z);
        long codekey = CodificKey((int) (x + mypos.x), (int) (z + mypos.z));
        HeightMap.put(codekey, y + mypos.y);
        topFace(x, y, z, block[z][x]);
        generationlog(x, y, z);
      }
    }
    modela.triangulo(tama.width, TerrTriangle);
  }

  private void topFace(float x, float y, float z, int typeblock) {
    if (typeblock < 0) return;
    TerrVertices.add(new Vector3(x, y, z));
    TerrNormal.add(new Vector3(0, 1, 0));
    Vector2 uv = mapuv(typeblock);
    TerrUV.add(new Vector2(uv.x/2,uv.y/2));
  }

  private Vector2 mapuv(int type) {
    int t = 4;
    float tilasize = 1f / t;
    int tx = type % t;
    int tz = type / t;
    tz = (int) tilasize - 1 - tz;
    float u = tx * tilasize, v = tz * tilasize;
    return new Vector2(u, v);
  }

  public void WaterCriate() {
    Obj = new SpatialObject("Water");
    Obj.setParent(myObject);
    Obj.setStatic(true);
    Obj.addComponent(new ModelRenderer());
    Obj.addComponent(new Water());
  }

  private void generationlog(float x, float y, float z) {
    float worldx = x + mypos.x;
    float worldz = z + mypos.z;
    int chunkX = (int) myObject.getGlobalPosition().x;
    int chunkZ = (int) myObject.getGlobalPosition().z;
    if (worldx < chunkX || worldx >= chunkX + tama.width || worldx < chunkZ || worldz >= chunkZ + tama.width) return;
    int space = 4;
    if (((int) worldx % space != 0) || ((int) worldz % space != 0)) return;
    float addspaw = perlin.noise(worldx + tama.seed, worldz + tama.seed);
    addspaw -= perlin.noise(worldx * 50f + tama.seed, worldz * 50f + tama.seed);
    if (addspaw >= tama.valuelog && y > tama.waterlevel + tama.heightscale) {
      int quemspaw = Random.range(0, tama.trees.size() - 1);
      Vector3 positobj = new Vector3(worldx, y + mypos.y, worldz) - myObject.getGlobalPosition();
      SpatialObject log = myObject.instantiate(tama.trees.get((int) quemspaw), positobj);
      log.setParent(myObject);
    }
  }

  public float getHeight(float x, float z) {
    long codeKey = CodificKey((int) Math.floor(x), (int) Math.floor(z));
    return HeightMap.getOrDefault(codeKey, 0f);
  }

  public long CodificKey(int x, int z) {
    return (((long) x) << 32) | (z & 0xFFFFFFFFL);
  }
}
