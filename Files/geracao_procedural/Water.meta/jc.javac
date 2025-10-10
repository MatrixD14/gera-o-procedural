public class Water extends Component {
  private ModelRenderer WaterModelo = null;
  private chunkgen tama = null;

  void start() {
    if (!myObject.exists()) return;
    tama = WorldController.findObject("player").findComponent("chunkgen");
    WaterModelo = myObject.findComponent("modelrenderer");
  } 

  public void WaterGera() {
    start();
    if(tama == null || WaterModelo == null) return;
    Vertex WaterVertex = new Vertex();
    float y = tama.waterlevel;
    int mult = 16;
    float[] WaterVertices = new float[] {0, y, 0, 0, y, mult, mult, y, 0, mult, y, mult};
    float[] WaterNormal = new float[] {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};
    float[] WaterUV = new float[] {0, 0, 0, 1, 1, 0, 1, 1};
    int[] WaterTriangle = new int[] {0, 1, 2, 2, 1, 3};

    WaterVertex.setVertices(WaterVertices);
    WaterVertex.setNormals(WaterNormal);
    WaterVertex.setUVs(WaterUV);
    WaterVertex.setTriangles(WaterTriangle);
    WaterVertex.apply();

    if (tama.WaterMate != null) WaterModelo.setMaterialFile(tama.WaterMate);
    /*WaterModelo.setMaterial(new Material());
    WaterModelo.material.setShader("PBR/Standard");
    WaterModelo.material.setDiffuse(new Color(0,170,255));*/
    WaterModelo.material.setReceiveLight(false);

    if (WaterVertex != null) WaterModelo.setVertex(WaterVertex);
    WaterModelo.setCastShadowEnabled(false);
  }
}
