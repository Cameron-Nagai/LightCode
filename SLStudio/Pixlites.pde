Pixlite[] setupPixlites(LX lx) {
  return new Pixlite[] {
    // Sun 4 (One Half)
    new Pixlite(lx, "10.200.1.36", model.getSliceById("sun4_top_front")), // A
    new Pixlite(lx, "10.200.1.35", model.getSliceById("sun4_top_back")),  // B
  };
}
 
public class Pixlite extends LXOutputGroup {
  private Slice slice;
  public final String ipAddress;

  public Pixlite(LX lx, String ipAddress, Slice slice) {
    super(lx);
    this.slice = slice;
    this.ipAddress = ipAddress;
    this.slice = slice;
    try {
      setupOutputs(lx);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setupOutputs(LX lx) throws SocketException {
    // get slice type and add PixliteOutputs with appropriate points

    if (slice.type != Slice.Type.BOTTOM_ONE_THIRD) {
      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("1")
          .addPoints(slice.getStripById("1").points,  PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("2").points)
          .addPoints(slice.getStripById("3").points,  PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("4").points)
          .addPoints(slice.getStripById("5").points,  PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("6").points)
          .addPoints(slice.getStripById("7").points,  PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("8").points)
          .addPoints(slice.getStripById("9").points,  PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("10").points)
          .addPoints(slice.getStripById("11").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("12").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("2")
          .addPoints(slice.getStripById("13").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("14").points)
          .addPoints(slice.getStripById("15").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("16").points)
          .addPoints(slice.getStripById("17").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("18").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("3")
          .addPoints(slice.getStripById("19").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("20").points)
          .addPoints(slice.getStripById("21").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("22").points)
          .addPoints(slice.getStripById("23").points, PointsGrouping.REVERSE_ORDERING)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("4")
          .addPoints(slice.getStripById("24").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("25").points)
          .addPoints(slice.getStripById("26").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("27").points)
          .addPoints(slice.getStripById("28").points, PointsGrouping.REVERSE_ORDERING)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("5")
          .addPoints(slice.getStripById("29").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("30").points)
          .addPoints(slice.getStripById("31").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("32").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("6")
          .addPoints(slice.getStripById("33").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("34").points)
          .addPoints(slice.getStripById("35").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("36").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("7")
          .addPoints(slice.getStripById("37").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("38").points)
          .addPoints(slice.getStripById("39").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("40").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("8")
          .addPoints(slice.getStripById("41").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("42").points)
          .addPoints(slice.getStripById("43").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("44").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("9")
          .addPoints(slice.getStripById("45").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("46").points)
          .addPoints(slice.getStripById("47").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("48").points)
      ));
    }

    if (slice.type == Slice.Type.BOTTOM_ONE_THIRD || slice.type == Slice.Type.FULL) {
      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("10")
          .addPoints(slice.getStripById("49").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("50").points)
          .addPoints(slice.getStripById("51").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("52").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress, 
        new PointsGrouping("11")
          .addPoints(slice.getStripById("53").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("54").points)
          .addPoints(slice.getStripById("55").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("56").points)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("12")
          .addPoints(slice.getStripById("57").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("58").points)
          .addPoints(slice.getStripById("59").points, PointsGrouping.REVERSE_ORDERING)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("13")
          .addPoints(slice.getStripById("60").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("61").points)
          .addPoints(slice.getStripById("62").points, PointsGrouping.REVERSE_ORDERING)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("14")
          .addPoints(slice.getStripById("63").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("64").points)
          .addPoints(slice.getStripById("65").points, PointsGrouping.REVERSE_ORDERING)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("15")
          .addPoints(slice.getStripById("66").points, PointsGrouping.REVERSE_ORDERING)
          .addPoints(slice.getStripById("67").points)
          .addPoints(slice.getStripById("68").points, PointsGrouping.REVERSE_ORDERING)
      ));

      addChild(new PixliteOutput(lx, ipAddress,
        new PointsGrouping("16")
          .addPoints(slice.getStripById("69").points, PointsGrouping.REVERSE_ORDERING)  
      ));
    }
  }

  private class PixliteOutput extends LXDatagramOutput {
    private final int MAX_NUM_POINTS_PER_UNIVERSE = 170;
    private final int outputIndex;
    private final int firstUniverseOnOutput;

    private PixliteOutput(LX lx, String ipAddress, PointsGrouping pointsGrouping) throws SocketException {
      super(lx);
      this.outputIndex = Integer.parseInt(pointsGrouping.id);
      this.firstUniverseOnOutput = outputIndex * 10;
      setupDatagrams(ipAddress, pointsGrouping);
    }

    private void setupDatagrams(String ipAddress, PointsGrouping pointsGrouping) {
      // the points for one pixlite output have to be spread across multiple universes
      int numPoints = pointsGrouping.size();
      int numUniverses = (numPoints / MAX_NUM_POINTS_PER_UNIVERSE) + 1;
      
      int counter = 0;

      for (int i = 0; i < numUniverses; i++) {
        int universe = firstUniverseOnOutput + i;
        int numIndices = ((i+1) * MAX_NUM_POINTS_PER_UNIVERSE) > numPoints ? (numPoints % MAX_NUM_POINTS_PER_UNIVERSE) : MAX_NUM_POINTS_PER_UNIVERSE;
        int[] indices = new int[numIndices];

        for (int i1 = 0; i1 < numIndices; i1++) {
          indices[i1] = pointsGrouping.getPoint(counter++).index;
        }

        addDatagram(new ArtNetDatagram(ipAddress, indices, universe-1));
      }
    }
  }
}

public class PointsGrouping {
  public final static boolean REVERSE_ORDERING = true;
  public String id;
  private final List<LXPoint> points = new ArrayList<LXPoint>();

  public PointsGrouping(String id) {
    this.id = id;
  }

  public List<LXPoint> getPoints() {
    return points;
  }

  public LXPoint getPoint(int i) {
    return points.get(i);
  }

  public int size() {
    return points.size();
  }

  public int[] getIndices() {
    int[] indices = new int[points.size()];

    for (int i = 0; i < points.size(); i++) {
      indices[i] = points.get(i).index;
    }
    return indices;
  }

  public PointsGrouping reversePoints() {
    Collections.reverse(Arrays.asList(points));
    return this;
  }

  public PointsGrouping addPoints(LXPoint[] pointsToAdd) {
    for (LXPoint p : pointsToAdd) {
      this.points.add(p);
    }
    return this;
  }

  public PointsGrouping addPoints(LXPoint[] pointsToAdd, boolean reverseOrdering) {
    LXPoint[] localPointsToAdd = pointsToAdd.clone();

    if (reverseOrdering) {
      Collections.reverse(Arrays.asList(localPointsToAdd));
    }
    addPoints(localPointsToAdd);
    return this;
  }
}

public class ArtNetDatagram extends LXDatagram {

  private final static int DEFAULT_UNIVERSE = 0;
  private final static int ARTNET_HEADER_LENGTH = 18;
  private final static int ARTNET_PORT = 6454;
  private final static int SEQUENCE_INDEX = 12;

  private final int[] pointIndices;
  private boolean sequenceEnabled = false;
  private byte sequence = 1;

  public ArtNetDatagram(String ipAddress, int[] indices, int universeNumber) {
    this(ipAddress, indices, 3*indices.length, universeNumber);
  }

  public ArtNetDatagram(String ipAddress, int[] indices, int dataLength, int universeNumber) {
    super(ARTNET_HEADER_LENGTH + dataLength + (dataLength % 2));
    this.pointIndices = indices;

    try {
        setAddress(ipAddress);
        setPort(ARTNET_PORT);
    } catch (UnknownHostException e) {
        System.out.println("Pixlite with ip address (" + ipAddress + ") is not on the network.");
    }

    this.buffer[0] = 'A';
    this.buffer[1] = 'r';
    this.buffer[2] = 't';
    this.buffer[3] = '-';
    this.buffer[4] = 'N';
    this.buffer[5] = 'e';
    this.buffer[6] = 't';
    this.buffer[7] = 0;
    this.buffer[8] = 0x00; // ArtDMX opcode
    this.buffer[9] = 0x50; // ArtDMX opcode
    this.buffer[10] = 0; // Protcol version
    this.buffer[11] = 14; // Protcol version
    this.buffer[12] = 0; // Sequence
    this.buffer[13] = 0; // Physical
    this.buffer[14] = (byte) (universeNumber & 0xff); // Universe LSB
    this.buffer[15] = (byte) ((universeNumber >>> 8) & 0xff); // Universe MSB
    this.buffer[16] = (byte) ((dataLength >>> 8) & 0xff);
    this.buffer[17] = (byte) (dataLength & 0xff);

    for (int i = ARTNET_HEADER_LENGTH; i < this.buffer.length; ++i) {
     this.buffer[i] = 0;
    }
  }

  public ArtNetDatagram setSequenceEnabled(boolean sequenceEnabled) {
    this.sequenceEnabled = sequenceEnabled;
    return this;
  }

  @Override
  public void onSend(int[] colors) {
    copyPoints(colors, this.pointIndices, ARTNET_HEADER_LENGTH);

    if (this.sequenceEnabled) {
      if (++this.sequence == 0) {
        ++this.sequence;
      }
      this.buffer[SEQUENCE_INDEX] = this.sequence;
    }
  }
}