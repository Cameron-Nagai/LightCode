// Generates palettes based on images of the sky from webcams around the world.

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/** A source of sky photos from a public webcam. */
class SkyPhotoSource {
  public final String name;
  public final String id;
  
  SkyPhotoSource(String name, String id) {
    this.name = name;
    this.id = id;
  }
  
  public BufferedImage fetchImage() throws IOException {
    String url = "http://api.deckchair.com/v1/viewer/camera/" + id +
        "?width=960&height=540&format=png&panelMode=false";
    println("Reading \"" + name + "\" image from " + url);
    BufferedImage image = ImageIO.read(new URL(url));
    println("Finished reading " + image.getWidth() + " x " + image.getHeight() + " image.");
    return image;
  }
}

interface ColorPalette {
  int getColor(double p);  // p is a parameter between 0 and 1
}

interface PaletteExtractor {
  ColorPalette getPalette(BufferedImage image);
}

/** A palette the samples from an array of color values. */
class ArrayPalette implements ColorPalette {
  int[] colors;
  
  ArrayPalette(int[] colors) {
    this.colors = colors;
  }
  
  public int getColor(double p) {
    double index = p * (colors.length - 1);
    int low = (int) Math.floor(index);
    int high = (low + 1) < colors.length ? low + 1 : low;
    double fraction = index - low;    
    return LXColor.lerp(colors[low], colors[high], fraction);
  }
}

/** Extracts a palette from an image by sampling colours along an arc from
  * rising from a point on the left edge of the image, to the center of the
  * top edge, falling to a point on the right edge of the image.  The "height"
  * parameter specifies how low to start at the left edge and end at the right
  * edge, as a fraction (e.g. height = 0.25 means a quarter of the way down).
  */
class ArcPaletteExtractor implements PaletteExtractor {
  float height;
  
  ArcPaletteExtractor(float height) {  // a fraction of the image's height, from 0 to 1
    this.height = height;
  }
  
  public ColorPalette getPalette(BufferedImage image) {
    int[] colors = new int[101];
    double xMax = image.getWidth() - 1;
    double yMax = image.getHeight() - 1;
    for (int i = 0; i <= 100; i++) {
      double t = i / 100.0;
      double x = xMax * t;
      double y = yMax * height * (0.5 + Math.cos(2 * Math.PI * t) * 0.5);
      int xl = (int) Math.floor(x);
      int yl = (int) Math.floor(y);
      int xh = (xl + 1) <= xMax ? xl + 1 : xl;
      int yh = (yl + 1) <= yMax ? yl + 1 : yl;
      int nw = image.getRGB(xl, yl);
      int ne = image.getRGB(xh, yl);
      int sw = image.getRGB(xl, yh);
      int se = image.getRGB(xh, yh);
      int n = LXColor.lerp(nw, ne, x - xl);
      int s = LXColor.lerp(sw, se, x - xl);
      colors[i] = LXColor.lerp(n, s, y - yl);
    }
    return new ArrayPalette(colors);
  }
}

/** A collection of named palettes drawn from various sky photo sources. */
class SkyPaletteLibrary {
  Map<String, SkyPhotoSource> sources;
  Map<String, PaletteExtractor> extractors;
  Map<String, Long> lastFetchMillis;
  Map<String, ColorPalette> palettes;
  
  final int CACHE_TTL_SEC = 60;  // length of time to cache retrieved photos
  
  SkyPaletteLibrary() {
    sources = new HashMap<String, SkyPhotoSource>();
    extractors = new HashMap<String, PaletteExtractor>();
    lastFetchMillis = new HashMap<String, Long>();
    palettes = new HashMap<String, ColorPalette>();
  }
  
  void addSky(String name, String id, PaletteExtractor extractor) {
    sources.put(name, new SkyPhotoSource(name, id));
    extractors.put(name, extractor);
  }
  
  ColorPalette getPalette(String name) {
    long nowMillis = new Date().getTime();
    long cacheAge = (nowMillis - lastFetchMillis.getOrDefault(name, 0L))/1000;
    ColorPalette palette = palettes.get(name);
    if (palette != null && cacheAge < CACHE_TTL_SEC) {
      return palette;
    }
    
    SkyPhotoSource source = sources.get(name);
    PaletteExtractor extractor = extractors.get(name);
    if (source != null && extractor != null) {
      try {
        palette = extractor.getPalette(source.fetchImage());
        palettes.put(name, palette);
        lastFetchMillis.put(name, nowMillis);
        return palette;
      } catch (IOException e) {
        println("Failed to fetch \"%s\" sky photo (id=\"%s\")", name, source.id);
      }
    }
    return null;
  }
}