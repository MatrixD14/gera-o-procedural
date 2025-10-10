package JAVARuntime;
import java.util.*;

public class test extends MaterialShader {
  @Override
  public String getShaderName() {
    return "CustomShaders/Simple";
  }

  @Override
  public float getMinimalSupportedOGL() {
    return MaterialShader.OGL3;
  }
  public Texture t;
  public Color cor = null;

  private Shader shade;

  @Override
  void start() {
    if(shade !=null)return;
    cor= new Color();
    Shader.Builder b = new Shader.Builder();
    b.createProgram();
    b.setVertexCode(VertexShader.loadFile(this, "test")); 
    b.setFragmentCode(FragmentShader.loadFile(this, "test"));
    b.compileVertex();
    b.compileFragment();
    shade = b.create();
  }

  @Override
  void render(OGLES ogles, Camera camera, MSRenderData renderData) {
    if(shade == null)return;
    OGLES3 ogl = (OGLES3) ogles;
    ogl.withShader(shade);
    ogl.setIgnoreAttributeException(true);

    ogl.uniformMatrix4("viewMatrix", camera.getViewMatrix());
    ogl.uniformMatrix4("projectionMatrix", camera.getProjectionMatrix());

    ogl.uniformColor("diffuse", cor);
    ogl.uniformTexture("albedo", t != null ? t : Texture.white());
    RenderableVertex rvert;
    RenderableObject rObj;
    Vertex vertex;
    for (int i = 0; i < renderData.vertexCount(); i++) {
       rvert = renderData.RenderableVertexAt(i);
       vertex = rvert.vertex;
      
      if (vertex.getVerticesBuffer() != null) ogl.attributeVector3("position", vertex.getVerticesBuffer());
      if (vertex.getUVsBuffer() != null) ogl.attributeVector2("texCoord", vertex.getUVsBuffer());
      
      for (int j = 0; j < rvert.objectCount(); j++) {
         rObj = rvert.objectAt(j);
        if (!rObj.isVisibleByCamera()) continue;
        if (rObj.getRenderMatrix() != null) ogl.uniformMatrix4("modelMatrix", rObj.getRenderMatrix());
        ogl.drawTriangles(vertex.getTrianglesBuffer());
      }
    } 
    ogl.releaseAttributes();
    ogl.releaseShader();
  }
}
