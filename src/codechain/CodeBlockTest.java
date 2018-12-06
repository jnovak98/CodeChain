package codechain;

import static org.junit.Assert.*;

import org.junit.Test;

public class CodeBlockTest {

	@Test
	public void test() {
		CodeBlock<ValueNode<String,ValueNode>, ValueNode<Integer,ValueNode>,
			CodeBlock,ValueNode<Integer,ValueNode>> test = new CodeBlock<ValueNode<String,ValueNode>, 
			ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>(
				i->{
					int a = i.getValue().equals("one") ? 1 : 0;
					return new ValueNode<Integer,ValueNode>(a,null);
				}, null);
		assertEquals(test.run(new ValueNode<String,ValueNode>("one",null)).getValue().intValue(),1);
		CodeBlock<ValueNode<Integer,ValueNode>, ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>> doubler = 
				new CodeBlock<ValueNode<Integer,ValueNode>, ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>(
				i->{
					int a = i.getValue().intValue()*2;
					return new ValueNode<Integer,ValueNode>(a,null);
				}, null);
		CodeBlock<ValueNode<String,ValueNode>, 
			ValueNode<Integer,ValueNode>,CodeBlock<ValueNode<Integer,ValueNode>, 
				ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>,ValueNode<Integer,ValueNode>> newCode = test.addToEnd(doubler);
		int a = newCode.run(new ValueNode<String,ValueNode>("one",null)).getValue();
		System.out.println(a);
	}

}
