public class chunkgen extends Component {
  public class object {
    public SpatialObject TerrCriate(float posx, float posy) {
      SpatialObject Obj = new SpatialObject("chuck " + posx + posy);
      Obj.setStatic(true);
      Obj.addComponent(new ModelRenderer());
      Obj.addComponent(new TerreController());
      Obj.setPosition(posx, 0, posy);
      return Obj;
    } 
  }

  @Hide public float waterlevel = .5f;
  @Hide public int chunks = 2, width = 16, seed;
  public float scale, heightscale, valuelog;
  public MaterialFile TerrMate, WaterMate;
  @Hide public chunkgen.object object;
  public List<ObjectFile> trees = new ArrayList<ObjectFile>();
  private Vector3 myposGlobal;
  private int lastChuckX = Integer.MIN_VALUE, lastChuckZ = Integer.MIN_VALUE;
  private int myposxs, myposzs;
  public HashMap<Long, SpatialObject> chunck = new HashMap<Long, SpatialObject>();
  public Queue<Long> process = new ArrayDeque<Long>();

  @Override
  void start() {
    object = new chunkgen.object();
    seed = Random.range(0, 500);
  }

  @Override
  void repeat() {
    posplayermy();
    spawobjT();
    processaChuck(1);
    removeChunck();
  }

  private void posplayermy() {
    myposGlobal = myObject.getGlobalPosition();
    myposxs = (int) Math.Floor(myposGlobal.x / width);
    myposzs = (int) Math.Floor(myposGlobal.z / width);
  }

  private void spawobjT() {
    if (myposxs == lastChuckX && myposzs == lastChuckZ) return;
    lastChuckX = myposxs;
    lastChuckZ = myposzs;
    for (int x = (-chunks); x < (chunks); x++) {
      for (int z = (-chunks); z < (chunks); z++) {
        int px = x + myposxs;
        int pz = z + myposzs;
        long poskey = CodificKey(px, pz);
        if (chunck.containsKey(poskey)) continue;
        process.add(poskey);
      }
    }
  }

  private void processaChuck(int quant) {
    if (process.isEmpty()) return;
    for (int i = 0; i < quant; i++) {
      long poss = process.remove();
      int px = (int) DescodificKeyX(poss);
      int pz = (int) DescodificKeyZ(poss);
      long poskey = CodificKey(px, pz);
      if (chunck.containsKey(poskey)) continue;
      // Vector2 pos = new Vector2(px * width, pz * width);
      SpatialObject TerrObj = object.TerrCriate(px * width, pz * width);
      if (TerrObj != null && TerrObj.exists()) chunck.put(poskey, TerrObj);
    }
  }

  private void removeChunck() {
    Iterator<Map.Entry<Long, SpatialObject>> item = chunck.entrySet().iterator();
    while (item.hasNext()) {
      Map.Entry<Long, SpatialObject> entregrar = item.next();
      int posChunckX = DescodificKeyX(entregrar.getKey());
      int posChunckZ = DescodificKeyZ(entregrar.getKey());
      int dX = Math.abs(posChunckX - myposxs);
      int dZ = Math.abs(posChunckZ - myposzs);
      if (dX > (chunks) || dZ > (chunks)) {
        entregrar.getValue().destroy();
        item.remove();
      }
    }
  }

  public long CodificKey(int x, int z) {
    return (((long) x) << 32) | (z & 0xFFFFFFFFL);
  }

  public int DescodificKeyX(long x) {
    return (int) (x >> 32);
  }

  public int DescodificKeyZ(long z) {
    return (int) z;
  }
}
