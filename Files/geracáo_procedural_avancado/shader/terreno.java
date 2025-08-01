package JAVARuntime;

// Useful imports
import java.util.*;

/** @Author */
public class terreno extends MaterialShader {

  /// Define shader configs
  @Override
  public String getShaderName() {
    return "CustomShaders/terreno";
  }

  @Override
  public float getMinimalSupportedOGL() {
    return MaterialShader.OGL3;
  } 

  public Color cor = new Color().White();
  public Texture img1, noise;
  private float time = 0;
  private Shader shader;

  @Override
  void start() {
    Shader.Builder sh = new Shader.Builder();
    sh.createProgram();
    VertexShader v = VertexShader.loadFile(this, "water");
    sh.setVertexCode(v);
    FragmentShader f = FragmentShader.loadFile(this, "Grass");
    sh.setFragmentCode(f);
    sh.compileVertex();
    sh.compileFragment();

    shader = sh.create();
  }

  @Override
  void render(OGLES ogles, Camera camera, MSRenderData renderData) {
    OGLES3 ogl = (OGLES3) ogles;
    ogl.withShader(shader);
    ogl.setIgnoreAttributeException(true);

    ogl.uniformMatrix4("viewMatrix", camera.getViewMatrix());
    ogl.uniformMatrix4("projectionMatrix", camera.getProjectionMatrix());
    ogl.uniformColor("diffuse", cor);
    time += 0.01f;
    ogl.uniformFloat("time",time);
    
    ogl.uniformTexture("water", img1 != null ? img1 : img1.White());
    ogl.uniformTexture("texnoise", noise != null ? noise : noise.White());

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
