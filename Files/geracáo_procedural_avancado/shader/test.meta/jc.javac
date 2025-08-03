package JAVARuntime;

// Useful imports
import java.util.*;

/** @Author */
public class test extends MaterialShader {
  @Override
  public String getShaderName() {
    return "CustomShaders/test";
  }

  @Override
  public float getMinimalSupportedOGL() {
    return MaterialShader.OGL3;
  }

  @Order(idx = -1)
  public Texture t;

  @Order(idx = 0)
  public Color cor = new Color(255, 255, 255);

  private Shader shade;
  @Order(idx = 1)
  public Vector2 mapUV = new Vector2(1, 1), tile = new Vector2(0, 0);

  @Override
  void start() {
    Shader.Builder b = new Shader.Builder();
    b.createProgram();
    VertexShader vert = VertexShader.loadFile(this, "test");
    b.setVertexCode(vert);
    FragmentShader fra = FragmentShader.loadFile(this, "test");
    b.setFragmentCode(fra);
    b.compileVertex();
    b.compileFragment();
    shade = b.create();
  }

  @Override
  void render(OGLES ogles, Camera camera, MSRenderData renderData) {
    OGLES3 ogl = (OGLES3) ogles;
    ogl.withShader(shade);
    ogl.setIgnoreAttributeException(true);

    ogl.uniformMatrix4("viewMatrix", camera.getViewMatrix());
    ogl.uniformMatrix4("projectionMatrix", camera.getProjectionMatrix());

    ogl.uniformColor("diffuse", cor);
    ogl.uniformTexture("albedo", t != null ? t : Texture.white());
    ogl.uniformVector2("mapuv", mapUV);
    ogl.uniformVector2("o_tile", tile);

    for (int i = 0; i < renderData.vertexCount(); i++) {
      RenderableVertex rvert = renderData.RenderableVertexAt(i);
      Vertex vertex = rvert.vertex;

      if (vertex.getVerticesBuffer() != null) ogl.attributeVector3("position", vertex.getVerticesBuffer());
      if (vertex.getUVsBuffer() != null) ogl.attributeVector2("texCoord", vertex.getUVsBuffer());

      for (int j = 0; j < rvert.objectCount(); j++) {
        RenderableObject rObj = rvert.objectAt(j);
        if (!rObj.isVisibleByCamera()) continue;
        if (rObj.getRenderMatrix() != null) ogl.uniformMatrix4("modelMatrix", rObj.getRenderMatrix());
        ogl.drawTriangles(vertex.getTrianglesBuffer());
      }
    } 
    ogl.releaseAttributes();
    ogl.releaseShader();
  }
}
