import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TreeOperations {
	
	public class Node{
		// List to store node keys
		public List<Integer> nodekeys;
		// List to store node values
		public List<Double> nodevalues;
		// List to store children of type Node
		public List<Node> childLi;
		
		// Pointer to point right Node
		public Node nextEle;
		// Pointer to point left Node
		public Node prevEle;
		
		// Default Constructor
		public Node() {
			nodekeys=new LinkedList<>();
			nodevalues=new LinkedList<>();
			childLi=new LinkedList<>();
			
			nextEle=null;
			prevEle=null;
		}
		
		// Parameterized constructor with KeyList
		public Node(List<Integer> keyList) {
			//nodekeys=new LinkedList<>();
			nodekeys=keyList;
			nodevalues=new LinkedList<>();
			childLi=new LinkedList<>();
		}
		
		// Parameterized constructor with KeyList and valueList
		public Node(List<Integer> keyList, List<Double> valueList) {
			//nodekeys=new LinkedList<>();
			nodekeys=keyList;
			nodevalues=valueList;
			childLi=new LinkedList<>();
		}
	}
	
	// Global variable to hold tree root
	public Node root;
	// Global variable to hold order of a tree
	public int order;
	
	// Insert Operation, used for inserting the given key, value pair into tree at that moment
	// This follows the insert approach of BPlus Tree
	public void Insert(int key, double value) {
		
		// This is to check for the first insertion into a Tree Node 
		boolean initialInsertion=true;
		
		// This is to hold the working node
		Node hold=null;
		// This is to find the index where the key has to be placed in a list
		int insertIndex=0;
		
		// flowList is to follow the flow from root to leaf level for inserting the given key
		List<Node> flowlist=new LinkedList<>();
		flowlist.add(root);
		
		// Initially the flowlist starts with root Node and this flow method helps in developing the rest of the flow
		// for inserting the key
		flow(root, key, flowlist);
		
		// Looping through the flow list from reverse direction
		while(flowlist.size()>0) {
			// This flag is to indicate that there is insertion that need to be done to parent of hold node
			boolean levelFlag=false;
			
			// This is leaf node where the key, value pair need to be inserted
			if(initialInsertion) {
				// The node in which insertion need to be done, is placed in hold by removing the node from flow list
				hold=flowlist.remove(flowlist.size()-1);
				// insertPos method is used to find the index where the insertion need to be done
				insertIndex=insertPos(hold, key);
				hold.nodekeys.add(insertIndex, key);
				hold.nodevalues.add(insertIndex, value);
				// as the first insertion is done, the flag is changed to false
				initialInsertion=false;
				
				// After insertion if the node key size is != order break from the loop
				if(hold.nodekeys.size()!=order) break;
				// If the node key list size matches to order
				else {
					List<Integer> keylist=new LinkedList<>(hold.nodekeys.subList(order/2, order));
					List<Double> valuelist=new LinkedList<>(hold.nodevalues.subList(order/2, order));
					// After accessing the half portion of hold node keys and values, creating a newNode 
					// with keylist and valuelist as parameters in the constructor
					Node newNode=new Node(keylist, valuelist);
					
					// Removing all of these keys and values from the current hold node
					hold.nodekeys.removeAll(keylist);
					hold.nodevalues.removeAll(valuelist);
					// Developing the double linked list bonding among the nodes
					newNode.nextEle=hold.nextEle;
					if(hold.nextEle!=null) hold.nextEle.prevEle=newNode;
					newNode.prevEle=hold;
					hold.nextEle=newNode;
					
					// Reached a point where a new root need to be created for the hold and newNode
					if(flowlist.size()==0) {
						// Creating newRoot node and initializing it with key and children's
						Node newRoot=new Node();
						newRoot.nodekeys.add(newNode.nodekeys.get(0));
						newRoot.childLi.add(hold);
						newRoot.childLi.add(newNode);
						// making the newRoot as root
						root=newRoot;
					}
					else {
						// Changing the key to first element of newNode keylist, which need to added to the
						// parent keylist
						key=newNode.nodekeys.get(0);
						levelFlag=true;
						Node parentNode=flowlist.get(flowlist.size()-1);
						// Adding the newNode to the parent childlist next to the hold node
						parentNode.childLi.add(parentNode.childLi.indexOf(hold)+1, newNode);
					}
				}
			}
			// Operation on non leaf node
			else {
				// remove the first node from last of flow list, as this is already made to hold
				flowlist.remove(flowlist.size()-1);
				// After insertion if the node key size is != order break from the loop
				if(hold.nodekeys.size()!=order) break;
				// If the node key list size matches to order
				else {
					List<Integer> keyli=new LinkedList<>(hold.nodekeys.subList(order/2+1, order));
					List<Node> children=new LinkedList<>(hold.childLi.subList(order/2+1, hold.childLi.size()));
					// After accessing the half+1 portion of hold node keys and children, creating a newNode
					// and initialize the node constructor parameterized with keylist
					Node newNode=new Node(keyli);
					newNode.childLi=children;
					// Getting rid of the keyli and children from the hold node 
					hold.nodekeys.removeAll(keyli);
					hold.childLi.removeAll(children);
					// Reached a point where a new root need to be created for the hold and newNode
					if(flowlist.size()==0) {
						// newRoot node initialized
						Node newRoot=new Node();
						// adding the key and child nodes to the newRoot node
						newRoot.nodekeys.add(hold.nodekeys.remove(hold.nodekeys.size()-1));
						newRoot.childLi.add(hold);
						newRoot.childLi.add(newNode);
						// making root to newRoot
						root=newRoot;
					}
					else {
						// Changing the key to last element of current hold, which need to be added to key list
						// of its parent
						key=hold.nodekeys.remove(hold.nodekeys.size()-1);
						levelFlag=true;
						// Accessing the parent of hold node and adding the newNode to the parent child list
						// next to the hold node
						Node parentNode=flowlist.get(flowlist.size()-1);
						parentNode.childLi.add(parentNode.childLi.indexOf(hold)+1, newNode);
					}
				}
			}
			
			// If the flow list size becomes zero indicates that I have processed all the node in the flow list
			if(flowlist.size()==0) break;
			// This levelflag==true indicates that a key to be added to the parent level
			if(levelFlag) { // Check on usage of flag
				// pointing the hold to the next element of flow list
				hold=flowlist.get(flowlist.size()-1);
				
				// Accessing the index where the key need to be added using insertPos method,
				// add the key to hold keylist at the result index
				insertIndex=insertPos(hold, key);
				hold.nodekeys.add(insertIndex, key);
			}
		}
	}
	
	// Insert position, this method fetches with position where the key can be inserted
	// in the keylist of the given node
	public int insertPos(Node hold, int key) {
		if(hold.nodekeys.size()==0) return 0;
		return Math.abs(Collections.binarySearch(hold.nodekeys, key)+1);
	}
	
	// This method is used to fetch the value of the given key if it is present in the tree,
	// else returns "Null"
	public String Search(int key) {
		List<Node> flowlist=new LinkedList<>();
		// result need to be returned
		String value="";
		// accessing the flowlist using flow method
		flowlist.add(root);
		flow(root, key, flowlist);
		// Taking the last node of flowlist, and getting the index of the key in node keylist
		int keyIndex=flowlist.get(flowlist.size()-1).nodekeys.indexOf(key);
		//System.out.println(flowli.get(flowli.size()-1).nodekeys);
		if(keyIndex!=-1) value= Double.toString(flowlist.get(flowlist.size()-1).nodevalues.get(keyIndex));
		//System.out.println(value);
		return value;
	}
	
	// This method is used for Searching values of keys between Key1 and Key2 inclusive
	public String Search(int key1, int key2) {
		List<Node> flowli=new LinkedList<>();
		String value="";
		flowli.add(root);
		// accessing the flowlist using flow method for key1
		flow(root, key1, flowli);
		
		// holding the last node of flowlist
		Node hold=flowli.get(flowli.size()-1);
		// Now with the help of double linked list developed among the leaf node,
		// accessing the keys present in between key1 and key2 inclusively
		while(hold!=null) {
			for(int id=0; id<hold.nodekeys.size(); id++) {
				if(key1<=hold.nodekeys.get(id)&&key2>=hold.nodekeys.get(id)) value=value+Double.toString(hold.nodevalues.get(id))+",";
				// On the first occurrence of key that is greater than key2 change hold to null and break from
				// for loop
				else if(key2<hold.nodekeys.get(id)) {
					hold=null;
					break;
				}
			}
			// Change hold to its next node
			if(hold!=null) { 
				hold=hold.nextEle;
			}
		}
		return value;
	}
	
	// This Delete operation is used in deleting the key, value record of the given key
	// from the tree
	public void Delete(int key) {
		List<Node> flowlist=new LinkedList<>();
		flowlist.add(root);
		// Accessing the flowlist using flow method for given key
		flow(root, key, flowlist);
		// Deletion operation is performed only when the last node of flowli contains the given key
		if(flowlist.get(flowlist.size()-1).nodekeys.contains(key)) {
			// Flag for tracking the first ever deletion of key from nodes of flowlist
			boolean initilDeletion=true;
			// node node for holding the working node
			Node hold=null;
			// Processing all the nodes in flowlist by looping through the list from opposite direction
			while(flowlist.size()>0) {
				// deletion of key and its value from the leaf node that conatins the key
				if(initilDeletion) {
					// holding the first node from last of the flowlist generated
					hold=flowlist.remove(flowlist.size()-1);
					// Removing the key and value pair from the hold node
					hold.nodevalues.remove(hold.nodekeys.indexOf(key));
					hold.nodekeys.remove(new Integer(key));
					// changing the flag to false, as we are done with deletion of key from leaf node
					initilDeletion=false;
					// when there are no keys present in the hold node
					if(hold.nodekeys.isEmpty()) {
						// hold being child for some node in the tree, which makes the hold node accessing the
						// Adjacent nodes for borrowing keys if the adjacent node having enough keys to give
						if(flowlist.size()!=0) {
							// fetching the parent of hold node
							Node curParent=flowlist.get(flowlist.size()-1);
							// adjucentIndex, Fetching the index of adjacent node from where hold node can borrow keys, if there no node 
							// capable of giving a key to hold node, the index returned is -1
							int adjId=adjucentIndex(curParent, hold);
							// Holding the index of hold node in its parent childList
							int holdId=curParent.childLi.indexOf(hold);
							
							// As the adjId!=-1 indicates there is an adjacent node from where the hold node
							// can borrow a key
							if(adjId!=-1) {
								Node adjucent=curParent.childLi.get(adjId);
								// add represents where the key from adjacent can be added to hold keylist
								// give says about from where the key from adjacent node can be accessed
								int add=-1, give=-1;
								// Borrow from left node to hold
								if(adjId==holdId-1) {
									add=0;
									give=adjucent.nodekeys.size()-1;
									// Replacing the key at adjId with the key from adjacent at give-1
									curParent.nodekeys.set(adjId, adjucent.nodekeys.get(give-1));
								}
								// Borrow from right node to hold
								else {
									add=hold.nodekeys.size();
									give=0;
									// Replacing the key at holdId with the key from adjacent at give-1
									curParent.nodekeys.set(holdId, adjucent.nodekeys.get(give+1));
								}
								// Adding key and value pair taken from adjacent node to hold node
								hold.nodekeys.add(add, adjucent.nodekeys.remove(give));
								hold.nodevalues.add(add, adjucent.nodevalues.remove(give));
							}
							
							// There are no adjacent nodes to hold node having enough keys to give to hold node
							else {
								// Adjusting the double linked list connections in order to remove the hold node
								if(hold.nextEle!=null) hold.nextEle.prevEle=hold.prevEle;
								if(hold.prevEle!=null) hold.prevEle.nextEle=hold.nextEle;
								// Deleting the key from parent of hold node according to holdId
								if(holdId==curParent.childLi.size()-1) curParent.nodekeys.remove(holdId-1);
								else curParent.nodekeys.remove(holdId);
								// Removing the hold node from its parent child list
								curParent.childLi.remove(hold);
							}
						}
						// All the keys present in the root are deleted	
						else break;
					}
					
					// Break if the hold is not empty
					else break;					
				}
				else {
					flowlist.remove(flowlist.size()-1);
					if(hold.nodekeys.isEmpty()) {
						// If flowlist is not empty, says hold node is not root node
						if(flowlist.size()!=0) {
							// fetching the parent of hold node
							Node curParent=flowlist.get(flowlist.size()-1);
							// adjucentIndex, Fetching the index of adjacent node from where hold node can borrow keys, if there no node 
							// capable of giving a key to hold node, the index returned is -1
							int adjId=adjucentIndex(curParent, hold);
							// Holding the index of hold node in its parent childList
							int holdId=curParent.childLi.indexOf(hold);
							//System.out.println(adjId);
							// As the adjId!=-1 indicates there is an adjacent node from where the hold node
							// can borrow a key
							if(adjId!=-1) {
								Node adjacent=curParent.childLi.get(adjId);
								// Barrow from left node to hold node
								// Add is the index where the child at give position of adjacent is removed and
								// and added to hold node
								int add=-1, give=-1;
								if(adjId==holdId-1) {
									add=0;
									give=adjacent.nodekeys.size()-1;
									// Borrowing the key from adjacent node via parent node
									hold.nodekeys.add(curParent.nodekeys.get(holdId-1));
									curParent.nodekeys.set(adjId, adjacent.nodekeys.get(give));
								}
								else {
									add=hold.nodekeys.size();
									give=0;
									// Borrowing the key from adjacent node via parent node
									hold.nodekeys.add(curParent.nodekeys.get(holdId));
									curParent.nodekeys.set(holdId, adjacent.nodekeys.get(give));
								}
								hold.childLi.add(add, adjacent.childLi.remove(give));
							}
							// There is no adjacent node capable of giving a key to hold node
							else {
								// adjacent node
								Node adjacent=null;
								// Index to where the child from hold node need to be added to adjacent node
								int add=-1;
								// hold node being last child node of its parent
								if(holdId==curParent.childLi.size()-1) {
									// adjacent node is left to hold node
									adjacent=curParent.childLi.get(holdId-1);
									// index add is size of adjacent node keylist
									add=adjacent.nodekeys.size();
									// adding key to adjacent node keylist at position add by removing a key
									// from its parent node at holdId-1
									adjacent.nodekeys.add(add, curParent.nodekeys.remove(holdId-1));
								}
								else {
									// adjacent node is right to hold node
									adjacent=curParent.childLi.get(holdId+1);
									//System.out.println(adjucent.nodekeys);
									// index add is 0
									add=0;
									adjacent.nodekeys.add(add, curParent.nodekeys.remove(holdId));
								}
								// adding the hold node child to adjacent node at position add
								adjacent.childLi.add(add, hold.childLi.get(0));
								// Removing the hold node form its parent childList
								curParent.childLi.remove(hold);
							}
						}
						// This says root node key list is empty, and the root is made to point to the child of the
						// current root node
						else root=hold.childLi.get(0);
					}
					// Break if the hold node is not empty
					else break;
				}
				// Break on successful processing of all the nodes in flowlist
				if(flowlist.size()==0) break;
				// hold node made to last indexed node in the flowlist
				hold=flowlist.get(flowlist.size()-1);
			}
		}
	}
	
	// Adjacent node index, this method is used to get the adjacent node index from where the current hold node 
	// can borrow key from
	public int adjucentIndex(Node parent, Node hold) {
		// Fetching the index of hold node in its parent childlist
		int holdId=parent.childLi.indexOf(hold);
		// Result varible
		int res=-1;
		// If hold is the right most node of its parent childList, then the only possibility is from node to
		// its left
		if(holdId==parent.childLi.size()-1) {
			if(parent.childLi.get(holdId-1).nodekeys.size()>1) res=holdId-1;
		}
		// If hold is the left most node of its parent childList, then the only possibility is from node to
		// its right
		else if(holdId==0) {
			if(parent.childLi.get(holdId+1).nodekeys.size()>1) res=holdId+1;
		}
		// Else checking on both sides of hold node
		else {
			if(parent.childLi.get(holdId+1).nodekeys.size()>1) res=holdId+1;
			else res=holdId-1;
		}
		return res;
	}
	
	//Insertion flow
	public void flow(Node head, int key, List<Node> flowlist) {
		// If head node being leaf of the tree return the flowlist
		if(head.childLi.isEmpty()) {
			return;
		}
		// Checking through the head node key list for first head node key grater that the input key
		int len=head.childLi.size();
		for(int i=0; i<len; i++) {
			//System.out.println("i:"+i+" child size:"+curChild.size()+" head keys:"+head.keys);
			if(i==len-1||key<head.nodekeys.get(i)) {
				// On successful finding the index i where the flow need to be continued, adding the index i 
				// child node of head node to flowlist
				flowlist.add(head.childLi.get(i));
				flow(head.childLi.get(i), key, flowlist);
				return;
			}
		}
	}
	
	// This Initialize method is used to initialize the BPlus Tree construction with order being x
	public void Initialize(int x) {
		root=new Node();
		order=x;
	}
	
}
