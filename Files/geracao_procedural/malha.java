public class malha {
  private PerlinNoise perlin = new PerlinNoise(75);

  public void trianguloNT(int value, IntBuffer triang, int passo) {
    for (int z = 0; z < value; z += passo) {
      for (int x = 0; x < value; x += passo) {
        //if (z + passo > value || x + passo > value) continue;
        int v0 = z * (value + 1) + x;
        int v1 = (z + passo) * (value + 1) + x;
        triang.put(v0).put(v1).put(v0 + passo);
        triang.put(v0 + passo).put(v1).put(v1 + passo);
      } 
    }
  }

  public Vertex meshupN(boolean onoff, ModelRenderer model, MaterialFile mateFile, int[] trianglo, Vector3Buffer vertices, Vector3Buffer normal, Vector2Buffer uv) {
    Vertex vertexs = new Vertex();
    vertexs.setVertices(vertices);
    vertexs.setNormals(normal);
    vertexs.setUVs(uv);
    vertexs.setTriangles(trianglo);
    vertexs.apply();

    model.setMaterialFile(mateFile);
    model.setVertex(vertexs);
    if (onoff) model.material.setReceiveLight(false);
    model.setCastShadowEnabled(false);
    return vertexs;
  }

  public void generationlog(chunkgen tama, Vector3 mypos, SpatialObject obj, float x, float y, float z) {
    float worldx = x + mypos.x;
    float worldz = z + mypos.z;
    int chunkX = (int) obj.getGlobalPosition().x;
    int chunkZ = (int) obj.getGlobalPosition().z;
    if (worldx < chunkX || worldx >= chunkX + tama.width || worldx < chunkZ || worldz >= chunkZ + tama.width) return;
    int space = Random.range(3, 5);
    if (((int) worldx % space != 0) || ((int) worldz % space != 0)) return;
    float addspaw = perlin.noise(worldx + tama.seed, worldz + tama.seed) - perlin.noise(worldx * 50f + tama.seed, worldz * 50f + tama.seed);
    if (addspaw >= tama.valuelog && y > tama.waterlevel + tama.heightscale) {
      int quemspaw = Random.range(0, tama.trees.length - 1);
      Vector3 positobj = new Vector3(worldx, y + mypos.y, worldz) - obj.getGlobalPosition();
      SpatialObject log = obj.instantiate(tama.trees[quemspaw], positobj);
      log.setParent(obj);
    }
  }

  public float perlinnoises(chunkgen tama, SpatialObject myObj, float x, float z) {
    float calcu = 0;
    float pi = 22f / 7f;
    float valuex = (x + tama.seed) + myObj.position.x;
    float valuez = (z + tama.seed) + myObj.position.z;
    calcu += perlin.noise(valuex, valuez) * tama.heightscale;
    calcu += perlin.noise(valuex * pi, valuez * pi) * tama.heightscale * 0.5f;
    calcu += perlin.noise(valuex / pi, valuez / pi) * tama.heightscale / 0.5f;
    calcu += perlin.noise(valuex - pi, valuez - pi) * tama.heightscale - 0.1f;
    return calcu;
  }
}
