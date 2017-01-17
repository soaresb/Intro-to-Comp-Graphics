/**
 * 
 */


import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since Spring 2011
 */
public class TestCases extends CyclicIterator<Map<String, Angled>> {

  Map<String, Angled> stop() {
    return this.stop;
  }

  private final Map<String, Angled> stop;

  @SuppressWarnings("unchecked")
  TestCases() {
    this.stop = new HashMap<String, Angled>();
    final Map<String, Angled> first = new HashMap<String, Angled>();
    final Map<String, Angled> second = new HashMap<String, Angled>();
    final Map<String, Angled> jump = new HashMap<String, Angled>();
    final Map<String, Angled> fly = new HashMap<String, Angled>();
    final Map<String, Angled> cripwing = new HashMap<String, Angled>();

    super.add(stop, first, second, jump, fly, cripwing);

    // the upper arm, forearm, and hand angles do not change through any of the
    // test cases
    

    stop.put(PA2.HAND_NAME, new BaseAngled(0, 0, 0));
    first.put(PA2.HAND_NAME, new BaseAngled(0, 0, 0));
    second.put(PA2.HAND_NAME, new BaseAngled(0, 0, 0));
    jump.put(PA2.HAND_NAME, new BaseAngled(0, 0, 0));
    fly.put(PA2.HAND_NAME, new BaseAngled(0, 0, 0));
    cripwing.put(PA2.HAND_NAME, new BaseAngled(0, 0, 0));

    // the stop test case
    stop.put(PA2.PINKY_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.PINKY_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.PINKY_PALM_NAME, new BaseAngled(0, 0, 0));
    stop.put(PA2.FOOT1_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.RING_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.RING_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.RING_PALM_NAME, new BaseAngled(0, 0, 0));
    stop.put(PA2.FOOT2_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.MIDDLE_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.MIDDLE_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.MIDDLE_PALM_NAME, new BaseAngled(0, 0, 0));
    stop.put(PA2.FOOT3_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.INDEX_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.INDEX_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.INDEX_PALM_NAME, new BaseAngled(0, 0, 0));
    stop.put(PA2.FOOT4_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEG_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.LEG_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEG_PALM_NAME, new BaseAngled(0, 180, 0));
    stop.put(PA2.FOOT5_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEGBOT2_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.LEGBOT2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEGBOT2_PALM_NAME, new BaseAngled(0, 180, 0));
    stop.put(PA2.FOOT6_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEGTOP2_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.LEGTOP2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEGTOP2_PALM_NAME, new BaseAngled(0, 180, 0));
    stop.put(PA2.FOOT7_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEGTOP_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    stop.put(PA2.LEGTOP_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    stop.put(PA2.LEGTOP_PALM_NAME, new BaseAngled(0, 180, 0));
    stop.put(PA2.FOOT8_NAME, new BaseAngled(45, 0, 0));

    // the first sign test case
    first.put(PA2.PINKY_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    first.put(PA2.PINKY_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.PINKY_PALM_NAME, new BaseAngled(0, 0, 0));
    first.put(PA2.FOOT1_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.RING_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    first.put(PA2.RING_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.RING_PALM_NAME, new BaseAngled(0, 0, 0));
    first.put(PA2.FOOT2_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.MIDDLE_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    first.put(PA2.MIDDLE_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.MIDDLE_PALM_NAME, new BaseAngled(0, 0, 0));
    first.put(PA2.FOOT3_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.INDEX_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    first.put(PA2.INDEX_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.INDEX_PALM_NAME, new BaseAngled(0, 0, 0));
    first.put(PA2.FOOT4_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.LEG_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    first.put(PA2.LEG_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.LEG_PALM_NAME, new BaseAngled(0, 180, 0));
    first.put(PA2.FOOT5_NAME, new BaseAngled(25, 0, 0));
    first.put(PA2.LEGBOT2_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    first.put(PA2.LEGBOT2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.LEGBOT2_PALM_NAME, new BaseAngled(0, 180, 0));
    first.put(PA2.FOOT6_NAME, new BaseAngled(25, 0, 0));
    first.put(PA2.LEGTOP2_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    first.put(PA2.LEGTOP2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.LEGTOP2_PALM_NAME, new BaseAngled(0, 180, 0));
    first.put(PA2.FOOT7_NAME, new BaseAngled(25, 0, 0));
    first.put(PA2.LEGTOP_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    first.put(PA2.LEGTOP_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    first.put(PA2.LEGTOP_PALM_NAME, new BaseAngled(0, 180, 0));
    first.put(PA2.FOOT8_NAME, new BaseAngled(25, 0, 0));

    // the second test case
    second.put(PA2.PINKY_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    second.put(PA2.PINKY_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.PINKY_PALM_NAME, new BaseAngled(0, 0, 0));
    second.put(PA2.FOOT1_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.RING_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    second.put(PA2.RING_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.RING_PALM_NAME, new BaseAngled(0, 0, 0));
    second.put(PA2.FOOT2_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.MIDDLE_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    second.put(PA2.MIDDLE_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.MIDDLE_PALM_NAME, new BaseAngled(0, 0, 0));
    second.put(PA2.FOOT3_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.INDEX_DISTAL_NAME, new BaseAngled(-90, 0, 0));
    second.put(PA2.INDEX_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.INDEX_PALM_NAME, new BaseAngled(0, 0, 0));
    second.put(PA2.FOOT4_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.LEG_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    second.put(PA2.LEG_MIDDLE_NAME, new BaseAngled(15, 0, 0));
    second.put(PA2.LEG_PALM_NAME, new BaseAngled(0, 180, 0));
    second.put(PA2.FOOT5_NAME, new BaseAngled(65, 0, 0));
    second.put(PA2.LEGBOT2_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    second.put(PA2.LEGBOT2_MIDDLE_NAME, new BaseAngled(15, 0, 0));
    second.put(PA2.LEGBOT2_PALM_NAME, new BaseAngled(0, 180, 0));
    second.put(PA2.FOOT6_NAME, new BaseAngled(65, 0, 0));
    second.put(PA2.LEGTOP2_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    second.put(PA2.LEGTOP2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.LEGTOP2_PALM_NAME, new BaseAngled(0, 180, 0));
    second.put(PA2.FOOT7_NAME, new BaseAngled(65, 0, 0));
    second.put(PA2.LEGTOP_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    second.put(PA2.LEGTOP_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    second.put(PA2.LEGTOP_PALM_NAME, new BaseAngled(0, 180, 0));
    second.put(PA2.FOOT8_NAME, new BaseAngled(65, 0, 0));

    // the jump test case
    jump.put(PA2.PINKY_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    jump.put(PA2.PINKY_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    jump.put(PA2.PINKY_PALM_NAME, new BaseAngled(0, 0, 0));
    jump.put(PA2.FOOT1_NAME, new BaseAngled(65, 0, 0));
    jump.put(PA2.RING_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    jump.put(PA2.RING_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    jump.put(PA2.RING_PALM_NAME, new BaseAngled(0, 0, 0));
    jump.put(PA2.FOOT2_NAME, new BaseAngled(65, 0, 0));
    jump.put(PA2.MIDDLE_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    jump.put(PA2.MIDDLE_MIDDLE_NAME, new BaseAngled(15, 0, 0));
    jump.put(PA2.MIDDLE_PALM_NAME, new BaseAngled(0, 0, 0));
    jump.put(PA2.FOOT3_NAME, new BaseAngled(65, 0, 0));
    jump.put(PA2.INDEX_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    jump.put(PA2.INDEX_MIDDLE_NAME, new BaseAngled(15, 0, 0));
    jump.put(PA2.INDEX_PALM_NAME, new BaseAngled(0, 0, 0));
    jump.put(PA2.FOOT4_NAME, new BaseAngled(65, 0, 0));
    jump.put(PA2.LEG_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    jump.put(PA2.LEG_MIDDLE_NAME, new BaseAngled(15, 0, 0));
    jump.put(PA2.LEG_PALM_NAME, new BaseAngled(0, 180, 0));
    jump.put(PA2.FOOT5_NAME, new BaseAngled(65, 0, 0));
    jump.put(PA2.LEGBOT2_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    jump.put(PA2.LEGBOT2_MIDDLE_NAME, new BaseAngled(15, 0, 0));
    jump.put(PA2.LEGBOT2_PALM_NAME, new BaseAngled(0, 180, 0));
    jump.put(PA2.FOOT6_NAME, new BaseAngled(65, 0, 0));
    jump.put(PA2.LEGTOP2_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    jump.put(PA2.LEGTOP2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    jump.put(PA2.LEGTOP2_PALM_NAME, new BaseAngled(0, 180, 0));
    jump.put(PA2.FOOT7_NAME, new BaseAngled(65, 0, 0));
    jump.put(PA2.LEGTOP_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    jump.put(PA2.LEGTOP_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    jump.put(PA2.LEGTOP_PALM_NAME, new BaseAngled(0, 180, 0));
    jump.put(PA2.FOOT8_NAME, new BaseAngled(65, 0, 0));

    // the fly test case
    fly.put(PA2.PINKY_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.PINKY_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.PINKY_PALM_NAME, new BaseAngled(0, 0, 0));
    fly.put(PA2.FOOT1_NAME, new BaseAngled(25, 0, 0));
    fly.put(PA2.RING_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.RING_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.RING_PALM_NAME, new BaseAngled(0, 0, 0));
    fly.put(PA2.FOOT2_NAME, new BaseAngled(25, 0, 0));
    fly.put(PA2.MIDDLE_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.MIDDLE_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.MIDDLE_PALM_NAME, new BaseAngled(0, 0, 0));
    fly.put(PA2.FOOT3_NAME, new BaseAngled(25, 0, 0));
    fly.put(PA2.INDEX_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.INDEX_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.INDEX_PALM_NAME, new BaseAngled(0, 0, 0));
    fly.put(PA2.FOOT4_NAME, new BaseAngled(25, 0, 0));
    fly.put(PA2.LEG_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.LEG_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.LEG_PALM_NAME, new BaseAngled(0, 180, 0));
    fly.put(PA2.FOOT5_NAME, new BaseAngled(25, 0, 0));
    fly.put(PA2.LEGBOT2_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.LEGBOT2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.LEGBOT2_PALM_NAME, new BaseAngled(0, 180, 0));
    fly.put(PA2.FOOT6_NAME, new BaseAngled(25, 0, 0));
    fly.put(PA2.LEGTOP2_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.LEGTOP2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.LEGTOP2_PALM_NAME, new BaseAngled(0, 180, 0));
    fly.put(PA2.FOOT7_NAME, new BaseAngled(25, 0, 0));
    fly.put(PA2.LEGTOP_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    fly.put(PA2.LEGTOP_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    fly.put(PA2.LEGTOP_PALM_NAME, new BaseAngled(0, 180, 0));
    fly.put(PA2.FOOT8_NAME, new BaseAngled(25, 0, 0));

    // the cripwing test case
    cripwing.put(PA2.PINKY_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    cripwing.put(PA2.PINKY_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.PINKY_PALM_NAME, new BaseAngled(15, 10, 0));
    cripwing.put(PA2.FOOT1_NAME, new BaseAngled(25, 0, 0));
    cripwing.put(PA2.RING_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    cripwing.put(PA2.RING_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.RING_PALM_NAME, new BaseAngled(15, 10, 0));
    cripwing.put(PA2.FOOT2_NAME, new BaseAngled(25, 0, 0));
    cripwing.put(PA2.MIDDLE_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    cripwing.put(PA2.MIDDLE_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.MIDDLE_PALM_NAME, new BaseAngled(15, 10, 0));
    cripwing.put(PA2.FOOT3_NAME, new BaseAngled(25, 0, 0));
    cripwing.put(PA2.INDEX_DISTAL_NAME, new BaseAngled(-45, 0, 0));
    cripwing.put(PA2.INDEX_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.INDEX_PALM_NAME, new BaseAngled(15, 10, 0));
    cripwing.put(PA2.FOOT4_NAME, new BaseAngled(25, 0, 0));
    cripwing.put(PA2.LEG_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    cripwing.put(PA2.LEG_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.LEG_PALM_NAME, new BaseAngled(-10, 180, 0));
    cripwing.put(PA2.FOOT5_NAME, new BaseAngled(25, 0, 0));
    cripwing.put(PA2.LEGBOT2_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    cripwing.put(PA2.LEGBOT2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.LEGBOT2_PALM_NAME, new BaseAngled(-10, 180, 0));
    cripwing.put(PA2.FOOT6_NAME, new BaseAngled(25, 0, 0));
    cripwing.put(PA2.LEGTOP2_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    cripwing.put(PA2.LEGTOP2_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.LEGTOP2_PALM_NAME, new BaseAngled(-10, 180, 0));
    cripwing.put(PA2.FOOT7_NAME, new BaseAngled(25, 0, 0));
    cripwing.put(PA2.LEGTOP_DISTAL_NAME, new BaseAngled(-150, 0, 0));
    cripwing.put(PA2.LEGTOP_MIDDLE_NAME, new BaseAngled(45, 0, 0));
    cripwing.put(PA2.LEGTOP_PALM_NAME, new BaseAngled(-10, 180, 0));
    cripwing.put(PA2.FOOT8_NAME, new BaseAngled(25, 0, 0));
  }
}
