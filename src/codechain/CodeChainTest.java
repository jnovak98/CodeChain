package codechain;

import static org.junit.Assert.*;
import java.util.function.Function;

import org.junit.Test;

public class CodeChainTest {

	@Test
	public void test() {
		CodeChain<ValueNode<String,ValueNode>,ValueNode<Boolean,ValueNode>> chain = 
				new CodeChain(new CodeBlock<ValueNode<String,ValueNode>, 
				ValueNode<Boolean,ValueNode>,CodeBlock,ValueNode<Boolean,ValueNode>>(
						i->{
							System.out.println("Input String says : \"" + i.getValue() + "\"");
							return new ValueNode<Boolean,ValueNode>(i.getValue().equals("return true")? true: false,null);
						}, null));

		CodeChain<ValueNode<String,ValueNode>,ValueNode<Integer,ValueNode>> added = chain.add(new CodeBlock<ValueNode<Boolean,ValueNode>, 
				ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>(
						i->{
							System.out.println("Last Function returned: "+ i.getValue());
							return new ValueNode<Integer,ValueNode>(i.getValue() ? 1 : 0);
						}, null)); 
		assertTrue(added.run(new ValueNode<String,ValueNode>("return true")).getValue().intValue()==1);
		assertTrue(added.run(new ValueNode<String,ValueNode>("return false")).getValue().intValue()==0);
		
	}
	
	@Test
	public void testMultipleIO(){
		Function
			<ValueNode<String,ValueNode<String,ValueNode>>, 
			ValueNode<String,ValueNode<String,ValueNode>>> 
		separateSplitString = 
				i -> {
					String noDelimiters = i.getValue().replace(i.next().getValue(), "");
					StringBuilder s = new StringBuilder();
					for(int iter = 0; iter < i.getValue().length() - noDelimiters.length();iter++){
						s.append(i.next().getValue());
					}
					String removedDelimiters = s.toString();
					ValueNode<String,ValueNode<String,ValueNode>> returnValues = 
							new ValueNode<String,ValueNode<String,ValueNode>>(noDelimiters,
									new ValueNode<String,ValueNode>(removedDelimiters));
					return returnValues;
				};
				
		CodeBlock<
			ValueNode<String,ValueNode<String,ValueNode>>, 
			ValueNode<String,ValueNode<String,ValueNode>>,
			CodeBlock,
			ValueNode<String,ValueNode<String,ValueNode>>>
		block = new CodeBlock<
				ValueNode<String,ValueNode<String,ValueNode>>, 
				ValueNode<String,ValueNode<String,ValueNode>>,
				CodeBlock,
				ValueNode<String,ValueNode<String,ValueNode>>>
			(separateSplitString);
		
		CodeChain<				
			ValueNode<String,ValueNode<String,ValueNode>>, 
			ValueNode<String,ValueNode<String,ValueNode>>>
		chain = new CodeChain<
			ValueNode<String,ValueNode<String,ValueNode>>, 
			ValueNode<String,ValueNode<String,ValueNode>>>
		(block);
		
		ValueNode<String,ValueNode> inputString = new ValueNode<String,ValueNode>("this.is.a.test.string");
		ValueNode<String,ValueNode<String,ValueNode>> stringAndDelimiter = 
				inputString.addToEnd(new ValueNode<String,ValueNode>("."));	
		
		assertTrue(chain.run(stringAndDelimiter).getValue().equals("thisisateststring"));
		assertTrue(chain.run(stringAndDelimiter).next().getValue().equals("...."));
	}
}
