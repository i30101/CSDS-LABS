import java.io.*;
import java.util.*;
//COMPLETE THE METHODS BELOW:  extension for using iterators for some, enhanced for-loops for others


/* A Set is simply a Collection in which duplicates are not allowed. 
In a Map, each element consists of a unique key and a value.
In a TreeMap, the elements are organized in a balanced and ordered tree, ordered by the keys. 
In a HashMap, the elements are organized in a hash table determined by the Object's hashCode(). 
A HashSet can be thought of as a HashMap in which each element has the same dummy value.
A TreeSet can be thought of as a TreeMap in which each element has the same dummy value.

interface java.util.Set
-> boolean add(Object x);			//boolean for successful add
-> boolean contains (Object x);	//search for x in the Set
-> boolean remove (Object x);		//boolean for successful remove
-> int size();							//the number of elements
-> Iterator iterator();				//to traverse the set
-> Object[] toArray();				//copies Set objects into an array which is returned

class java.util.HashSet implements java.util.Set
//places Objects into a set by their hashCode() method
class java.util.TreeSet implements java.util.Set
//places Objects into a balanced search tree

interface java.util.Map
-> Object put(Object key, Object value);	//adds unique key maped to value
-> Object get(object key);						//returns value associated with key
-> Object remove (Object key);				//returns value removed
-> int size();										//the number of elements
-> Set keySet();									//returns a set of all the keys
-> boolean containsKey(Object key);			//looks to see if key exists in the keyset									

class java.util.HashMap implements java.util.Map
//places Objects into a map by their hashCode() method
class java.util.TreeMap implements java.util.Map
//places Objects into a balanced search tree
*/

public class SetAndMaps {
    //pre:   s1 an s2 are not null
    //post:  this will return a TreeSet of all the elements from s1 and s2
    public static Set union(Set s1, Set s2) {
        Set ans = new TreeSet();	
        //***COMPLETE THIS METHOD***
        for(Object object : s1) {
            ans.add(object);
        }
        for(Object object: s2) {
            ans.add(object);
        }
        return ans;
    }

    //pre:   s1 an s2 are not null
    //post: this will return only those elements common to both s1 and s2
    public static Set intersection(Set s1, Set s2) {
        Set ans = new TreeSet();
        //***COMPLETE THIS METHOD***
        for(Object object : s1) {
            if(s2.contains(object)) {
                ans.add(object);
            }
        }
        return ans;
    }

    //pre:   s1 an s2 are not null
    //post: this will return the elements of s1 that are not in s2
    public static Set difference(Set s1, Set s2) {
        Set ans = new TreeSet();
        //***COMPLETE THIS METHOD***  
        for(Object object : s1) {
            if(!s2.contains(object)) {
                ans.add(object);
            }
        }
        return ans;
    }

    //pre:   s1 an s2 are not null
    //post:  this will return only those elements that are unique to s1 and unique to s2
    //			also known as the inverse of intersection
    public static Set exclusion(Set s1, Set s2) {
        Set ans = new TreeSet();
        //***COMPLETE THIS METHOD***     
        for(Object object : s1) {
            if(!s2.contains(object)) {
                ans.add(object);
            }
        }
        for(Object object : s2) {
            if(!s1.contains(object)) {
                ans.add(object);
            }
        }
        return ans;
    }

    //pre:   m is not null
    //post:  will return a map whose keySet is comprised of all the values in m
    //			and whose values are arrays of all the keys from m that mapped to that value
    //i.e. if m is a treeMap of students (keys) and grades (values), then flip returns
    //a TreeMap of grades (keys) and all the students who made that grade in an ArrayList (value)
    public static Map flip(Map<String, String> m) {
        Map<String, ArrayList<String>> ans = new TreeMap();
        //***COMPLETE THIS METHOD***
        Set oldKeys = m.keySet();
        for(Object oldKey : oldKeys) {
            Object newKey = m.get(oldKey);
            if(ans.containsKey(newKey)) {   // new array already has key
                ans.get(newKey).add((String) oldKey);
            }else {
                ArrayList<String> newList = new ArrayList<String>();
                newList.add((String) oldKey);
                ans.put((String) newKey, newList);
            }
        }
        return ans;
    }

    public static void main(String[] arg) {
        Set<String> keyOfG = new TreeSet();
        keyOfG.add("G");
        keyOfG.add("Am");
        keyOfG.add("Bm");
        keyOfG.add("C");
        keyOfG.add("D");
        keyOfG.add("Em");
        keyOfG.add("F#m7dim5");

        Set<String> keyOfC = new TreeSet();
        keyOfC.add("C");
        keyOfC.add("Dm");
        keyOfC.add("Em");
        keyOfC.add("F");
        keyOfC.add("G");
        keyOfC.add("Am");
        keyOfC.add("Bm7dim5");

        System.out.println("Chords in the key of G:");
        System.out.println(keyOfG+"\n");
        //[Am, Bm, C, D, Em, F#m7dim5, G]

        System.out.println("Chords in the key of C:");
        System.out.println(keyOfC+"\n");
        //[Am, Bm7dim5, C, Dm, Em, F, G]

        Set theirU = union(keyOfG, keyOfC);
        System.out.println("The Union of chords between the keys of G and C:");
        System.out.println(theirU+"\n");
        //[Am, Bm, Bm7dim5, C, D, Dm, Em, F, F#m7dim5, G]

        Set theirI = intersection(keyOfG, keyOfC);
        System.out.println("The Intersection of chords between the keys of G and C:");
        System.out.println(theirI+"\n");
        //[Am, C, Em, G]

        Set theirD = difference(keyOfG, keyOfC);
        System.out.println("The Difference in chords between the keys of G and C:");
        System.out.println(theirD+"\n");
        //[Bm, D, F#m7dim5]

        theirD = difference(keyOfC, keyOfG);
        System.out.println("The Difference in chords between the keys of C and G:");
        System.out.println(theirD+"\n");
        //[Bm7dim5, Dm, F]

        Set theirE = exclusion(keyOfG, keyOfC);
        System.out.println("The Exclusion of chords between the keys of G and C:");
        System.out.println(theirE+"\n");
        //[Bm, Bm7dim5, D, Dm, F, F#m7dim5]

        Map<String, String> tm = new TreeMap();
        tm.put("Clove", "C");
        tm.put("Ginger", "A");
        tm.put("Basil", "B");
        tm.put("Nutmeg", "B");
        tm.put("Mace", "C");
        tm.put("Cardamom", "B");
        tm.put("Garlic", "A");
        System.out.println("TreeMap of students (key) with their respective grades (value):");
        System.out.println(tm+"\n");
        //{Basil=B, Cardamom=B, Clove=C, Garlic=A, Ginger=A, Mace=C, Nutmeg=B}

        Map<String, ArrayList<String>> byGrade = flip(tm);
        System.out.println("TreeMap of grades (key) with all the students who made that grade (value):");
        System.out.println(byGrade+"\n");
        //{A=[Garlic, Ginger], B=[Basil, Cardamom, Nutmeg], C=[Clove, Mace]}
    }
}
