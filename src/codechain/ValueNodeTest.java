package codechain;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValueNodeTest {

	@Test
	public void test() {
		ValueNode<String, ValueNode<Integer, ValueNode>> values = 
				new ValueNode<String, ValueNode<Integer, ValueNode>>("test", 
						new ValueNode<Integer,ValueNode>(1,null));
		String first = values.getValue();
		Integer second = values.next().getValue();
		ValueNode<String, ValueNode> addingTest= new ValueNode("test2",null);
		ValueNode<String, ValueNode<Integer, ValueNode>> added =
				addingTest.addToEnd(new ValueNode<Integer,ValueNode>(1,null));
		String first2 = added.getValue();
		Integer second2 = added.next().getValue();
		
	}

}
