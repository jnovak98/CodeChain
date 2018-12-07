package codechain;

import static org.junit.Assert.*;

import org.junit.Test;

public class CodeBlockTest {

	@Test
	public void testConstructorAddingRun() {
		CodeBlock<ValueNode<String,ValueNode>, ValueNode<Integer,ValueNode>,
			CodeBlock,ValueNode<Integer,ValueNode>> test = new CodeBlock<ValueNode<String,ValueNode>, 
			ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>(
				i->{
					int a = i.getValue().equals("one") ? 1 : 0;
					return new ValueNode<Integer,ValueNode>(a);
				});
		assertEquals(test.run(new ValueNode<String,ValueNode>("one")).getValue().intValue(),1);
		CodeBlock<ValueNode<Integer,ValueNode>, ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>> doubler = 
				new CodeBlock<ValueNode<Integer,ValueNode>, ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>(
				i->{
					int a = i.getValue().intValue()*2;
					return new ValueNode<Integer,ValueNode>(a);
				});
		CodeBlock<ValueNode<String,ValueNode>, 
			ValueNode<Integer,ValueNode>,CodeBlock<ValueNode<Integer,ValueNode>, 
				ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>,ValueNode<Integer,ValueNode>> newCode = test.addToEnd(doubler);
		int a = newCode.run(new ValueNode<String,ValueNode>("one")).getValue();
		assertTrue(a==2);
		CodeBlock<ValueNode<String,ValueNode>, 
		ValueNode<Integer,ValueNode>,CodeBlock<ValueNode<Integer,ValueNode>, 
			ValueNode<Integer,ValueNode>,CodeBlock<ValueNode<Integer,ValueNode>, 
			ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>,ValueNode<Integer,ValueNode>>,
				ValueNode<Integer,ValueNode>> quadrupler = newCode.addToEnd(doubler);
		assertTrue(quadrupler.run(new ValueNode<String,ValueNode>("one")).getValue().intValue()==4);
	}
	
	@Test
	public void testRunWithName(){
		CodeBlock<ValueNode<Integer,ValueNode>, ValueNode<Integer,ValueNode>,
			CodeBlock,ValueNode<Integer,ValueNode>> addOne = new CodeBlock<ValueNode<Integer,ValueNode>, 
			ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>("FunctionName",
				i->{
					return new ValueNode<Integer,ValueNode>(i.getValue()+1);
				});
		assertTrue(addOne.run("FunctionName", new ValueNode<Integer,ValueNode>(1)).getValue().intValue()==1+1);
		try{
			addOne.run("NotFunctionName", new ValueNode<Integer,ValueNode>(1));
			fail();
		} catch(IllegalArgumentException e){
			assertTrue(e.getMessage().equals("Cannot call function NotFunctionName on CodeBlock FunctionName"));
		}
	}
}
