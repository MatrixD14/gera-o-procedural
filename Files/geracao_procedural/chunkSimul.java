public class chunkSimul {
  public int VertecesCount, NormalCount, TrianCount, UvMapCount;

  public void gerat(int value) {
    VertecesCount = (value +1) * (value + 1);
    NormalCount = VertecesCount;
    UvMapCount = VertecesCount;
    TrianCount = value * value *6;
    
  } 
}
