//Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
//Entity <- Player <- (abstract) Monster <- Dinosaur

public class Dinosaur extends Monster {
    
    //ARGS:  row, col, image collection
    public Dinosaur(int r, int c, String[][][] image) {
        super("Gobzilly", r, c, image, 15, 50, 1, 100, 20, "FIRE", 5);
        //super ARGS:  name, row, col, image collection, animation delay, stomp power, speed penalty, reload time, walk damage, projectileType, burnDamage
        setIsSwimmer(true);		//we can swim fast
        setIsShooter(true);		//we can shoot a projectile (FIRE)
        setHealInWater(true);	//we can heal in water
        setCanEatAll(true);		//we can grab and eat every type of unit
    }


    //ARGS:  name, row loc, col loc, image collection, stomp power, speed penalty, reload time		
    public Dinosaur(String n, int r, int c, String[][][] image, int sp, int spp, int rt) {
        super (n,  r, c, image, 15, sp, spp, rt, 20, "FIRE", 5);
        setIsSwimmer(true);
        setIsShooter(true);
        setHealInWater(true);
        setCanEatAll(true);		//we can grab and eat every type of unit
    }

    //returns true if the monster can grab the unit of type specified by name
    //Gobzilly can grab any kind of boat
    public boolean canGrabUnit(String name) {
        if(name.startsWith("CYCLE") || name.startsWith("CAR") || name.startsWith("CROWD") || name.startsWith("BOAT") || name.equals("AIR newscopter"))
            return true;
        return false;
    }

    //grabs a unit of type specified by name
    public void grabUnit(String name) {
        super.setClawContents(name);
    }

    //eats a unit and updates health and claw contents
    public void eatUnit() {
        String[] contents = super.getClawContents();
        int index = -1;		//index of claw contents that are full
        if(!contents[0].equals("empty"))
            index = 0;
        else
            if(!contents[1].equals("empty"))
                index = 1;
        if(index >= 0) {      
            if(super.getHunger() > 0)
                super.setHunger(super.getHunger()-1);
            if(contents[index].startsWith("CROWD") || contents[index].endsWith("destroyer"))		//more satisfaction for eating a crowd of people or destroyer
                super.setHunger(0); 
            else
                if(contents[index].endsWith("bus"))			//satisfy hunger more for eating a bus
                super.setHunger(super.getHunger()-2);
                else
                super.setHunger(super.getHunger()-1);
            super.clearClawContents();   
        }  
    }    

    public String reloadingMessage() {
        return "Out of breath!";
    }

}