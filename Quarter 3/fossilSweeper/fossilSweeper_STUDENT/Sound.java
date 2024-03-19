import javax.sound.midi.*;

public class Sound
{
   protected static MidiChannel[] channels=null;		//MIDI channels
   protected static Instrument[] instr;					//MIDI instrument bank
   protected static final int PIANO = 0;              //here is the list of all instrument sounds 
   protected static final int MUTED_GT = 28;
   protected static final int TIMPANI = 47;
   protected static final int ORCH_HIT = 55;
   protected static final int FRET_NOISE = 120;       //             https://www.midi.org/specifications-old/item/gm-level-1-sound-set

  //data fields for music generation
   public static int soundOffFrame, nextNoteFrame;    //frame number to turn sound off, or go to the next note for melody notes
   public static int nextNoteFrame2;                  //frame number to go to the next note for pedal tones
   protected static final int [] majPent = {0,2,4,7,9,12};           //semi-tones between intervals in different scales
   protected static final int [] minPent = {0,3,5,7,10,12};
   protected static final int [] harmMin = {0,2,3,5,7,8,11,12};
   protected static final int [] mixolydian = {0,2,4,5,7,9,10,12};
   protected static int [] noteLengths;               //the duration of melody notes
   protected static int [] scale;                     //melody notes
   protected static int [] pedalTones;                //pedal tone notes behind the melody
   protected static int [] sustainLengths;            //the duration of pedal tones
   protected static final int OCTAVE = 12;
   protected static boolean rootPedalTone;            //if true, only uses the root as a pedal tone
   protected static int time;                         //used to vary the number of notes per second

   public static void initialize()
   {
      try 
      {
         Synthesizer synth = MidiSystem.getSynthesizer();
         synth.open();
         Sound.channels = synth.getChannels();
         Sound.instr = synth.getDefaultSoundbank().getInstruments();
      }
      catch (Exception ignored) 
      {}
      channels[0].programChange(instr[PIANO].getPatch().getProgram());
      
       //music generation code
      buildScale(harmMin, minPent);
      noteLengths = new int[9];
      int wholeNote = 1000;
      noteLengths[0] = Math.max(1,wholeNote/16);
      noteLengths[1] = Math.max(2,wholeNote/8);
      noteLengths[2] = Math.max(2,wholeNote/8);
      noteLengths[3] = Math.max(4,wholeNote/4);
      noteLengths[4] = Math.max(4,wholeNote/4);
      noteLengths[5] = Math.max(4,wholeNote/4);
      noteLengths[6] = Math.max(8,wholeNote/2);
      noteLengths[7] = Math.max(8,wholeNote/2);
      noteLengths[8] = Math.max(16,wholeNote);
   
      sustainLengths = new int[8];
      sustainLengths[0] = Math.max(16,wholeNote);
      sustainLengths[1] = Math.max(16,wholeNote);
      sustainLengths[2] = Math.max(16,wholeNote);
      sustainLengths[3] = Math.max(8,wholeNote/2);
            
      sustainLengths[4] = Math.max(16,wholeNote);
      sustainLengths[5] = Math.max(8,wholeNote/2);
      sustainLengths[6] = Math.max(8,wholeNote/2);
      sustainLengths[7] = Math.max(4,wholeNote/4);
      
      soundOffFrame = Integer.MAX_VALUE;
      nextNoteFrame = 0;
      nextNoteFrame2 = 0;
      
      rootPedalTone = false;
      time = 12;
      
      silence();		
   }

   //turn sounds off of both channels
   public static void silence()
   {
      channels[0].allNotesOff();		
      channels[1].allNotesOff();
   }
   
 //pre:  channel >= 0 && channel < channels.length
 //post: turns sound of a particular channel
   public static void silence(int channel)
   {
      if(channel>=0 && channel<channels.length)
      {
         channels[channel].allNotesOff();		//turn sounds off 
      }
   }
   
   public static void checkToClearSound()
   {
      if(GamePanel.getFrameNum() > soundOffFrame)
      {
         silence(2);						//turn sounds off 
         soundOffFrame = Integer.MAX_VALUE;
      }
   }
   
   //pre:  gameMode is either INSTRUCTIONS, GAMEOVER or STARTSCREEN
   //post: set the scale depending on the game mode
   public static void pickScale(int gameMode)
   {
      //if(gameMode == GamePanel.INSTRUCTIONS)
      Sound.buildScale(Sound.majPent, Sound.majPent);
         /*
      else if(gameMode == GamePanel.AFTERMATH)
         Sound.buildScale(Sound.minPent, Sound.minPent);
      else if(gameMode == GamePanel.STARTSCREEN)
         Sound.buildScale(Sound.mixolydian, Sound.majPent);
      else //if(gameMode == GamePanel.GAME)
         Sound.buildScale(Sound.harmMin, Sound.minPent);  
      */
      time = rollDie(7,19);
      rootPedalTone = false;
      if(Math.random() < 0.5)
      {
         rootPedalTone = true;
      }     
   }


   //sound for hitting the enter key
   public static void click()
   {
      if(GamePanel.getSoundOption() == GamePanel.ALL_OFF)
      {
         return;
      }
      channels[0].programChange(instr[FRET_NOISE].getPatch().getProgram());
      int pitch = 100;                    //pitch is how low or high the note is (1-127)  
      int velocity = 60;                  //velocity is how loud the sound is (0-127)
      channels[0].noteOn(pitch, velocity);
   }
   
   //sound for hitting a key
   public static void randomNote()
   {
      if(GamePanel.getSoundOption() == GamePanel.ALL_OFF)
      {
         return;
      }
      channels[0].programChange(instr[TIMPANI].getPatch().getProgram());
      int pitch = (int)(Math.random() * 20) + 35;          
      int velocity = 80;
      channels[0].noteOn(pitch, velocity);
   }
   
   //post: the sound of getting to the top (completing the level) - random pitch
   public static void win()     
   {
      if(GamePanel.getSoundOption() == GamePanel.ALL_OFF)
      {
         return;
      }
      channels[0].programChange(instr[ORCH_HIT].getPatch().getProgram());
      int pitch = (int)(Math.random() * 20) + 50;          
      int velocity = 100;
      channels[0].noteOn(pitch, velocity);
   }
   
      //post: the sound of revealing an ant hill
   public static void antHill()       
   {
      channels[0].programChange(instr[MUTED_GT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+35;          
      int velocity = 100;
      channels[0].noteOn(pitch, velocity);
   }
   
   //music generation code
   public static int rollDie(int min, int max)
   {
      return (int)(Math.random()*(max-min+1)) + min;
   }

   public static int rollDie(int numSides)
   {
      return rollDie(1, numSides);
   }
   
   //Music generation code
   public static void setRootPedalTone(boolean state)
   {
      rootPedalTone = state;
   }
    
   public static void buildScale(int [] baseScale, int [] pedalScale)
   {
      int randKey = rollDie(60, 71);
      
      int [] ans = new int[baseScale.length];
      for(int i=0; i<ans.length; i++)
         ans[i] = baseScale[i] + randKey;
      scale = ans;
      
      ans = new int[pedalScale.length];
      for(int i=0; i<ans.length; i++)
         ans[i] = pedalScale[i] + randKey;
      pedalTones = ans;
   }
   
   //post: plays random music using the selected scale
   public static void playMusic()
   {
      if(GamePanel.getSoundOption() == GamePanel.ALL_OFF || GamePanel.getSoundOption() == GamePanel.NO_MUSIC)
      {
         return;
      }
      int channel = 1;
      int melodyNote = -1;
      if(GamePanel.getFrameNum() > nextNoteFrame)
      {               
         channels[channel].programChange(instr[PIANO].getPatch().getProgram());
         int note = scale[(int)(Math.random()*scale.length)];
         if(GamePanel.getFrameNum() > nextNoteFrame2)    //if we are playing a pedalTone at the same time, change the note to a pentatonic
         {
            note = pedalTones[(int)(Math.random()*pedalTones.length)];
         }
         melodyNote = note;
         int velocity = rollDie(50,60);
         channels[channel].noteOn(note, velocity);
         int noteLength = noteLengths[(int)(Math.random()*(noteLengths.length-2))];
         noteLength = (int)(noteLength*((Math.abs(time - 13)/7.0 + 0.5)));    
         nextNoteFrame = GamePanel.getFrameNum() + noteLength; 
         soundOffFrame = GamePanel.getFrameNum() + noteLength;
      }
      if(GamePanel.getFrameNum() > nextNoteFrame2)
      {
         int noteIndex = (int)(Math.random()*pedalTones.length);
         int noteLength = sustainLengths[(int)(Math.random()*(sustainLengths.length))];
         if(rootPedalTone)
         {
            noteIndex = 0;    //always the root for the pedal tone
            noteLength = sustainLengths[0];
         }
         else 
         {
            noteIndex = (int)(Math.random()*pedalTones.length);
            noteLength = sustainLengths[(int)(Math.random()*(sustainLengths.length/2))];
         }
         int note = pedalTones[noteIndex] - OCTAVE;
         int note2 = note, note3 = note;
         int noteIndexAbove = noteIndex, noteIndexBelow = noteIndex;
         if(melodyNote != -1 && areDissonant(note, melodyNote))    //we are also playing a melody note at this time - let's make sure it is not dissonant
         {
            while(areDissonant(note2, melodyNote) && noteIndexAbove < pedalTones.length)
            {
               noteIndexAbove++;
               note2 = pedalTones[noteIndexAbove] - OCTAVE;
            }
            while(areDissonant(note2, melodyNote) && noteIndexBelow > 0)
            {
               noteIndexBelow--;
               note3 = pedalTones[noteIndexBelow] - OCTAVE;
            }
            if(!areDissonant(note2, melodyNote) && !areDissonant(note3, melodyNote))
            {
               if(Math.random() < 0.5)
               {
                  note = note2;
               }
               else
               {
                  note = note3;
               }
            }
            else if(!areDissonant(note2, melodyNote) )
            {
               note = note2;
            }
            else if(!areDissonant(note3, melodyNote) )
            {
               note = note3;
            }
         }
         int velocity = rollDie(50,60);
         channels[channel].noteOn(note, velocity);
         nextNoteFrame2 = GamePanel.getFrameNum() + noteLength;  //sustained notes
         soundOffFrame = GamePanel.getFrameNum() + noteLength;
      }
   }
   
   //given a ourNote, return its normalized value where 0 is the first ourNote in the scale (C)
   public static int normalize(int ourNote)
   {
      while(ourNote>=12)			//strip out any octaves
         ourNote-=12;   	
      return ourNote;
   }
   
   //returns true if note1 and note2 are dissonant
   public static boolean areDissonant(int note1, int note2)
   {
      return (isMin2(note1, note2) || isMaj2(note1, note2) || isMin7(note1, note2) || isMaj7(note1, note2) || isTritone(note1, note2));
   }
   
   //returns true if note1 and note2 are the same note (but maybe different ocataves)	
   public static boolean isRoot(int note1, int note2)
   {
      return (normalize(note1) == normalize(note2));
   }
   
    //returns true if the note2 is the min 2nd of note1
   public static boolean isMin2(int note1, int note2)
   {
      int sec = note1 + 1;
      return (normalize(sec) == normalize(note1));
   }
   
   	//returns true if the note2 is the major 2nd of note1
   public static boolean isMaj2(int note1, int note2)
   {
      int sec = note1 + 2;
      return (normalize(sec) == normalize(note1));
   }
   
   //returns true if the note2 is the min third of note1
   public static boolean isMin3(int note1, int note2)
   {
      int third = note1 + 3;
      return (normalize(third) == normalize(note1));
   }
   
   	//returns true if the note2 is the major third of note1
   public static boolean isMaj3(int note1, int note2)
   {
      int third = note1 + 4;
      return (normalize(third) == normalize(note1));
   }
   
    //returns true if the note2 is the fourth (subdominant) of note1
   public static boolean isFourth(int note1, int note2)
   {
      int fourth = note1 + 5;
      return (normalize(fourth) == normalize(note1));
   }
   
     //returns true if note2 is half of an octave (the tritone) from note1
   public static boolean isTritone(int note1, int note2)
   {
      int tri = note1 + 6;
      return (normalize(tri) == normalize(note1));
   }
   
     //returns true if the note2 is the fifth (dominant) of note1
   public static boolean isFifth(int note1, int note2)
   {
      int fifth = note1 + 7;
      return (normalize(fifth) == normalize(note1));
   }
   
   //returns true if the note2 is the min 6th of note1
   public static boolean isMin6(int note1, int note2)
   {
      int sixth = note1 + 8;
      return (normalize(sixth) == normalize(note1));
   }
   
    //returns true if the note2 is the major 6th of note1
   public static boolean isMaj6(int note1, int note2)
   {
      int sixth = note1 + 9;
      return (normalize(sixth) == normalize(note1));
   }
   
    //returns true if the note2 is the min 7th of note1
   public static boolean isMin7(int note1, int note2)
   {
      int sev = note1 + 10;
      return (normalize(sev) == normalize(note1));
   }
   
    //returns true if the note2 is the major 7th of note1
   public static boolean isMaj7(int note1, int note2)
   {
      int sev = note1 + 11;
      return (normalize(sev) == normalize(note1));
   }

}