public class chunkgen extends Component {
  public class object {
    public SpatialObject TerrCriate(float posx, float posy) {
      SpatialObject Obj = new SpatialObject("chuck ");
      Obj.setPosition(posx, 0, posy);
      Obj.addComponent(new ModelRenderer());
      Obj.addComponent(new TerreController());
      Obj.removeComponent(new TerreController());
      Obj.setStatic(true);
      return Obj;
    }
  }

  @Hide public float waterlevel = .5f;
  @Hide public int chunks = 4, width = 16, seed;
  public float heightscale = 7.5f, valuelog = .5f;
  public MaterialFile TerrMate, WaterMate;
  @Hide public chunkgen.object object;
  public ObjectFile[] trees = new ObjectFile[1];
  private Vector3 myposGlobal;
  private int lastChuckX = Integer.MIN_VALUE, lastChuckZ = Integer.MIN_VALUE;
  private int myposxs, myposzs;
  public HashMap<Long, SpatialObject> chunck = new HashMap<Long, SpatialObject>();
  private float time = 0, timechunk = 0;
  private int gerax, geraz, startx, startz, endx, endz;
  private boolean gerado = false;

  @Override
  void start() {
    object = new chunkgen.object();
    seed = Random.range(0, 500);
    spawobjT();
    processotime(16 * 16);
  } 

  @Override
  void repeat() {
    posplayermy();
    if ((timechunk += Math.bySecond()) > 2f) {
      spawobjT();
      timechunk = 0;
    }
    processotime(4);
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

    startx = -chunks;
    startz = -chunks;
    endx = chunks;
    endz = chunks;

    gerax = startx;
    geraz = startz;
    gerado = true;
  }

  private void processotime(int timeDaley) {
    if (!gerado) return;
    int gerandor = 0;
    while (gerandor < timeDaley) {
      if (gerax >= endx) {
        gerax = startx;
        geraz++;
        if (geraz >= endz) {
          gerado = false;
          break;
        }
      }
      int px = gerax + myposxs;
      int pz = geraz + myposzs;
      long poskey = CodificKey(px, pz);
      if (!chunck.containsKey(poskey)) {
        SpatialObject TerrObj = object.TerrCriate(px * width, pz * width);
        if (TerrObj != null && TerrObj.exists()) chunck.put(poskey, TerrObj);
      }
      gerax++;
      gerandor++;
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
      if ((time += Math.bySecond()) > 3f && (dX > (chunks) || dZ > (chunks))) {
        entregrar.getValue().destroy();
        item.remove();
        time = 0;
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
