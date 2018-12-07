package codechain;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValueNodeTest {

	@Test
	public void testConstructorAndGetValue() {
		ValueNode<String, ValueNode<Integer, ValueNode>> values = 
				new ValueNode<String, ValueNode<Integer, ValueNode>>("test", 
						new ValueNode<Integer,ValueNode>(1,null));
		String first = values.getValue();
		Integer second = values.next().getValue();
		assertTrue(first.equals("test"));
		assertTrue(second.intValue()==1);
	}
	
	@Test
	public void testAddToEnd(){
		ValueNode<String, ValueNode> addingTest= new ValueNode("test",null);
		ValueNode<String, ValueNode<Integer, ValueNode>> added =
				addingTest.addToEnd(new ValueNode<Integer,ValueNode>(1,null));
		ValueNode<String,ValueNode<Integer,ValueNode<Boolean,ValueNode>>> addedBool = 
				added.addToEnd(new ValueNode<Boolean,ValueNode>(true,null));
		String first = addedBool.getValue();
		Integer second = addedBool.next().getValue();
		Boolean third = addedBool.next().next().getValue();
		assertTrue(first.equals("test"));
		assertTrue(second.intValue()==1);
		assertTrue(third.booleanValue());
	}
	
	@Test
	public void testHasNext(){
		ValueNode<String, ValueNode<Integer, ValueNode>> values = 
				new ValueNode<String, ValueNode<Integer, ValueNode>>("test", 
						new ValueNode<Integer,ValueNode>(1,null));
		assertTrue(values.hasNext());
		assertFalse(values.next().hasNext());
	}

}
