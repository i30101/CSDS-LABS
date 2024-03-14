public class Blop extends Monster {
    /**
     * Creates a new default Blop object
     * @param r desired row to spawn Blop
     * @param c desired column to spawn Blop
     * @param image desired image collection
     */
    public Blop(int r, int c, String[][][] image) {
        super("The-Blop", r, c, image, 15, 20, 1, 0, 90, "GLOP", 50);
        setIsSwimmer(true);
        setCanSplit(true);
        setImperviousToBullets(true);
        setSlimeTrail(true);
        setCanEatAll(true);
    }

    /**
     * Creates a new custom Blop object
     * @param n
     * @param r
     * @param c
     * @param image
     * @param sp
     * @param spp
     * @param rt
     */
    public Blop(String n, int r, int c, String[][][] image, int sp, int spp, int rt) {
        super(n, r, c, image, 15, sp, spp, rt, 90, "GLOP", 50);
        setIsSwimmer(true);
        setCanSplit(true);
        setImperviousToBullets(true);
        setSlimeTrail(true);
        setCanEatAll(true);
    }

    @Override
    public boolean canGrabUnit(String name) {
        return true;
    }

    @Override
    public void grabUnit(String name) {
        super.setClawContents(name);     
    }

    @Override
    public void eatUnit() {
        String[] contents = super.getClawContents();
        int index = -1;
        if(!contents[1].equals("empty")) {
            index = 0;
        } else {
            if(!contents[1].equals("empty")) {
                index = 1;
            }
        }
        if (index >= 0) {
            if (super.getHunger() > 0) {
                super.setHunger(super.getHunger() - 1);
                super.setHealth(super.getHealth() + (int) (Math.random() * 6) + 5);
            }
        }
        super.clearClawContents();
    }

    @Override
    public String reloadingMessage() {
        return "Generating glop!";
    }
}
