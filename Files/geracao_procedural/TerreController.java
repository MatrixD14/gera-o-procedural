public class TerreController extends Component {
  private ModelRenderer TerrModelo;
  private Vertex TerrVertex;
  private Vector3Buffer TerrVertices = null, TerrNormal = null;
  private IntBuffer TerrTriangles = null;
  private Vector2Buffer TerrUVs = null;
  private chunkgen tama;
  private Vector3 mypos;
  private HashMap<Long, Float> HeightMap = new HashMap<Long, Float>();
  private OH2LevelIntArray block = null;
  private OH2LevelFloatArray heigth = null;
  private malha modela = new malha();
  private int geraWater = 0;
  // private Collider c;

  void start() {
    if (!myObject.exists()) return;
    tama = WorldController.findObject("player").findComponent("chunkgen");
    TerrModelo = myObject.findComponent("modelrenderer");
    // c = myObject.findComponent("collider");

    reload();
  }

  private void myposblock() {
    mypos = myObject.position;
  }

  public void reload() {
    new AsyncTask(
        new AsyncRunnable() {
          public Object onBackground(Object input) {
            TerrVertex = new Vertex();
            myposblock();
            MatrizChunck();
            createBuffer();
            HeightMap.clear();
            TerrTriangles.clear();
            generat();
            return null;
          }

          public void onEngine(Object result) {
            generatlog();
            if (geraWater == 1) WaterCriate();
            defiDados();
          }
        });
  }

  private void MatrizChunck() {
    int W = tama.width;
    block = new OH2LevelIntArray(W + 1, W + 1);
    heigth = new OH2LevelFloatArray(W + 1, W + 1);
    for (int z = 0; z <= W; z++) {
      for (int x = 0; x <= W; x++) {
        float yLocal = modela.perlinnoises(tama, myObject, x, z);
        float yWorld = yLocal + mypos.y;
        heigth.set(z, x, yLocal);
        if (yWorld >= tama.waterlevel - 2 && yWorld <= 2 + tama.waterlevel) block.set(z, x, 0);
        else if (yWorld >= tama.waterlevel - 15 && yWorld <= tama.waterlevel - 2) block.set(z, x, 12);
        else if (yWorld <= tama.waterlevel - 15) block.set(z, x, 7);
        else block.set(z, x, 1);
      }
    } 
  }

  private void generatlog() {

    for (int z = 0; z <= tama.width; z++) {
      for (int x = 0; x <= tama.width; x++) {
        modela.generationlog(tama, mypos, myObject, x, heigth.get(z, x), z);
      }
    }
  }

  private void createBuffer() {
    int W = tama.width;
    chunkSimul data = new chunkSimul();
    boolean offon = false;
    data.gerat(W);
    TerrVertices = BufferUtils.createVector3Buffer(data.VertecesCount);

    TerrTriangles = BufferUtils.createIntBuffer(data.TrianCount);

    TerrNormal = BufferUtils.createVector3Buffer(data.NormalCount);
    TerrUVs = BufferUtils.createVector2Buffer(data.UvMapCount);
    if (!offon) return;
    TerrVertices.setVboEnabled(true);
    TerrNormal.setVboEnabled(true);
    TerrUVs.setVboEnabled(true);
  }

  private void generat() {
    int W = tama.width;
    for (int z = 0; z <= W; z++) {
      for (int x = 0; x <= W; x++) {
        float y = heigth.get(z, x);
        int matriz = block.get(z, x);
        if (matriz < 0) continue;

        long codekey = CodificKey((int) (x + mypos.x), (int) (z + mypos.z));
        HeightMap.put(codekey, y + mypos.y);

        TerrVertices.put(x, y, z);
        TerrNormal.put(0, 1, 0);
        TerrUVs.put(mapuv(matriz, 0, 0));

        if (matriz != 1) geraWater = 1;
      }
    }
  }

  private void defiDados() {
    modela.trianguloNT(tama.width, TerrTriangles, 1);
    int[] TrianConvert = new int[TerrTriangles.position()];
    TerrTriangles.rewind();
    TerrTriangles.get(TrianConvert);
    TerrVertex = modela.meshupN(false, TerrModelo, tama.TerrMate, TrianConvert, TerrVertices, TerrNormal, TerrUVs);
    // c.setVertex(TerrVertex);
  }

  private Vector2 mapuv(int type, int x, int y) {
    int t = 4;
    float tilasize = 1f / t;
    int tx = type % t;
    int ty = type / t;
    float u = tx * tilasize + x * tilasize, v = ty * tilasize + y * tilasize;
    return new Vector2(u, v);
  }

  public void WaterCriate() {
    SpatialObject Obj = new SpatialObject("Water");
    Obj.setParent(myObject);
    Obj.setStatic(true);
    Obj.addComponent(new ModelRenderer());
    Obj.addComponent(new Water());
    Water gera = null;
    if (Obj.findComponent("water") != null) gera = Obj.findComponent("Water");
    if (gera != null) gera.WaterGera();
  }

  public float getHeight(float x, float z) {
    long codeKey = CodificKey((int) Math.floor(x), (int) Math.floor(z));
    return HeightMap.getOrDefault(codeKey, 0f);
  }

  public long CodificKey(int x, int z) {
    return (((long) x) << 32) | (z & 0xFFFFFFFFL);
  }
}
