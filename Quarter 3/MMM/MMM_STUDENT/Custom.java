 //Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
 //Entity <- Player <- (abstract) Monster <- Custom
 
public class Custom extends Monster
{
   public Custom(int r, int c, String[] customInfo, String[][][] image)
   {  //ARGS:name,row, col, image collection, animation delay,             stomp power,                    speed penalty,                    reload time,                     autoStompPower,                  projectileType, burnDamage 
      super (customInfo[0],  r, c, image, Integer.parseInt(customInfo[2]), Integer.parseInt(customInfo[3]), Integer.parseInt(customInfo[4]), Integer.parseInt(customInfo[5]), Integer.parseInt(customInfo[6]), customInfo[7], Integer.parseInt(customInfo[8]));
      setHead360	(Boolean.parseBoolean(customInfo[9]));
      setIsSwimmer(Boolean.parseBoolean(customInfo[10]));
      setIsFlyer	(Boolean.parseBoolean(customInfo[11]));
      setIsDigger	(Boolean.parseBoolean(customInfo[12]));
      setIsThrower(Boolean.parseBoolean(customInfo[13]));
      setIsShooter(Boolean.parseBoolean(customInfo[14]));
      setCanSplit		(Boolean.parseBoolean(customInfo[15]));
      setHealInWater	(Boolean.parseBoolean(customInfo[16]));
      setIsAmbidextorous(Boolean.parseBoolean(customInfo[17]));
      setDamageInWater	(Boolean.parseBoolean(customInfo[18]));
      setLessAppetite	(Boolean.parseBoolean(customInfo[19]));
      setImperviousToBullets(Boolean.parseBoolean(customInfo[20]));
      setIsFragile      (Boolean.parseBoolean(customInfo[21]));
      setSlimeTrail		(Boolean.parseBoolean(customInfo[22]));
      setEnergyAbsorber	(Boolean.parseBoolean(customInfo[23]));
      setCanEatAll      (Boolean.parseBoolean(customInfo[24]));
      setIsMindControl	(Boolean.parseBoolean(customInfo[25]));	
      setCantGrab	      (Boolean.parseBoolean(customInfo[26]));		
   }

//row, col, custom infor array, anim images, stomp power, speed penalty, reload time
   public Custom(int r, int c, String[] customInfo, String[][][] image, int stp, int spp, int rt)
   {  //ARGS:name,row, col, image collection, animation delay,    stomp power,speed penalty, reload time,autoStompPower, projectileType, burnDamage 
      super (customInfo[0],  r, c, image, Integer.parseInt(customInfo[2]), stp, spp, rt, Integer.parseInt(customInfo[6]), customInfo[7], Integer.parseInt(customInfo[8]));
      setHead360	(Boolean.parseBoolean(customInfo[9]));
      setIsSwimmer(Boolean.parseBoolean(customInfo[10]));
      setIsFlyer	(Boolean.parseBoolean(customInfo[11]));
      setIsDigger	(Boolean.parseBoolean(customInfo[12]));
      setIsThrower(Boolean.parseBoolean(customInfo[13]));
      setIsShooter(Boolean.parseBoolean(customInfo[14]));
      setHealInWater	(Boolean.parseBoolean(customInfo[15]));
      setCanSplit		(Boolean.parseBoolean(customInfo[16]));
      setIsAmbidextorous(Boolean.parseBoolean(customInfo[17]));
      setDamageInWater	(Boolean.parseBoolean(customInfo[18]));
      setLessAppetite	(Boolean.parseBoolean(customInfo[19]));
      setImperviousToBullets(Boolean.parseBoolean(customInfo[20]));
      setIsFragile		(Boolean.parseBoolean(customInfo[21]));
      setSlimeTrail		(Boolean.parseBoolean(customInfo[22]));
      setEnergyAbsorber	(Boolean.parseBoolean(customInfo[23]));
      setCanEatAll	   (Boolean.parseBoolean(customInfo[24]));
      setIsMindControl	(Boolean.parseBoolean(customInfo[25]));
      setCantGrab	      (Boolean.parseBoolean(customInfo[26]));				
   }

 //returns true if the monster can grab the unit of type specified by name
   public boolean canGrabUnit(String name)
   {
      if(energyAbsorber())
      {	//energy absorbers can only grab trains
         if(name.startsWith("TRAIN"))
            return true;
         return false;
      }
      if(canEatAll())
         return true;
      if(isSwimmer() && name.startsWith("BOAT"))		//all swimmers can grab boats
         return true;
      if(isFlying() && name.startsWith("AIR"))//all flyers can grab airplanes when flying
         return true;
      if(name.startsWith("CYCLE") || name.startsWith("CAR") || name.startsWith("CROWD") || name.equals("AIR newscopter"))
         return true;
      return false;
   }

//grabs a unit of type specified by name
   public void grabUnit(String name)
   {
      if(isAmbidextorous())
      {
         String[] contents = super.getClawContents();
         if(contents[0].equals("empty"))
            contents[0] = name;
         else
            contents[1] = name;
         super.setClawContents(contents);   
      }
      else
         super.setClawContents(name);
   }

 //post:  returns the type that the monster shoots
   public String projectileType()
   {
      if(isThrower())
      {
         String[] contents = super.getClawContents();
         if(!contents[0].startsWith("CROWD") && !contents[0].startsWith("empty"))
            return contents[0];
         if(!contents[1].startsWith("CROWD") && !contents[1].startsWith("empty"))
            return contents[1];
         return "empty";
      }
      else
         return super.projectileType();
   }
   
	   //eats a unit and updates health and claw contents
   public void eatUnit()
   {
      String[] contents = super.getClawContents();
      int index = -1;		//index of claw contents that are full
      if(!contents[0].equals("empty"))
         index = 0;
      else
         if(!contents[1].equals("empty"))
            index = 1;
      if(isAmbidextorous())
      {       
      //if they are holding a CROWD, prioritize eating them
         if(contents[0].startsWith("CROWD"))
            index = 0;
         else
            if(contents[1].startsWith("CROWD"))
               index = 1;
      }
      if(index >= 0)
      {
         if(contents[index].startsWith("CROWD"))		//more health for eating a crowd of people
         {
            super.setHealth(super.getHealth()+10);
            super.setHunger(0); 
         }
         else
            if(contents[index].endsWith("bus"))			//satisfy hunger more for eating a bus
            {
               super.setHealth(super.getHealth()+8);
               super.setHunger(super.getHunger()-2);
            }
            else
            {
               super.setHealth(super.getHealth()+5);
               super.setHunger(super.getHunger()-1);
            }
         contents[index] = "empty";
         super.setClawContents(contents);     
      }
   }

   public String reloadingMessage()
   {
      if(isThrower())
         return "Nothing to throw!";
      if(isMindControl())
         return "Out of focus!";
      return "Out of breath!";
   }
}