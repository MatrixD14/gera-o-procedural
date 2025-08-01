public class malha {
  private PerlinNoise perlin = new PerlinNoise(100);

  public void triangulo(int value, int[] triang) {
    int tris = 0, verts = 0;
    for (int z = 0; z < value; z++) {
      for (int x = 0; x < value; x++) {
        triang[tris + 0] = verts + 0;
        triang[tris + 1] = verts + value + 1;
        triang[tris + 2] = verts + 1;
        triang[tris + 3] = verts + 1;
        triang[tris + 4] = verts + value + 1;
        triang[tris + 5] = verts + value + 2;
        verts++;
        tris += 6;
      }
      verts++;
    }
  }

  public Vertex meshup(boolean onoff, ModelRenderer model, MaterialFile mateFile, int[] trianglo, List<Vector3> vertices, List<Vector3> normal, List<Vector2> uv) {
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

  public float perlinnoises(chunkgen tama, SpatialObject myObj, float x, float z) {
    float calcu = 0;
    float pi = 22 / 7;
    float valuex = (x + tama.seed) + myObj.position.x;
    float valuez = (z + tama.seed) + myObj.position.z;
    calcu += perlin.noise(valuex, valuez) * tama.heightscale;
    calcu += perlin.noise(valuex * pi, valuez * pi) * tama.heightscale * 0.5f;
    calcu += perlin.noise(valuex / pi, valuez / pi) * tama.heightscale / 0.5f;
    calcu += perlin.noise(valuex - pi, valuez - pi) * tama.heightscale - 0.1f;
    return calcu;
  }
}
