package codechain;

import static org.junit.Assert.*;
import java.util.function.Function;

import org.junit.Test;

import com.sun.xml.internal.ws.api.pipe.NextAction;

public class CodeChainTest {

	@Test
	public void test() {
		CodeChain<ValueNode<String,ValueNode>,ValueNode<Boolean,ValueNode>> chain = 
				CodeChain.build(new CodeBlock<ValueNode<String,ValueNode>, 
				ValueNode<Boolean,ValueNode>,CodeBlock,ValueNode<Boolean,ValueNode>>(
						i->{
							return new ValueNode<Boolean,ValueNode>(i.getValue().equals("return true")? true: false,null);
						}));

		CodeChain<ValueNode<String,ValueNode>,ValueNode<Integer,ValueNode>> added = chain.add(new CodeBlock<ValueNode<Boolean,ValueNode>, 
				ValueNode<Integer,ValueNode>,CodeBlock,ValueNode<Integer,ValueNode>>(
						i->{
							return new ValueNode<Integer,ValueNode>(i.getValue() ? 1 : 0);
						})); 
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
		chain = CodeChain.build(block);
		
		ValueNode<String,ValueNode> inputString = new ValueNode<String,ValueNode>("this.is.a.test.string");
		ValueNode<String,ValueNode<String,ValueNode>> stringAndDelimiter = 
				inputString.addToEnd(new ValueNode<String,ValueNode>("."));	
		
		assertTrue(chain.run(stringAndDelimiter).getValue().equals("thisisateststring"));
		assertTrue(chain.run(stringAndDelimiter).next().getValue().equals("...."));
	}
	
	@Test
	public void testCodeChainNoValueNodes() {
		CodeBlock<String, String, CodeBlock, String> stringRepeater = new CodeBlock<String, String, CodeBlock, String>(
				i->{
					String repeated = i + "" + i;
					return repeated;
				});
		CodeChain<String, String> chain = CodeChain.build();
		CodeChain<String, String> repeatOnce = chain.add(stringRepeater);
		CodeChain<String, String> repeatTwice = repeatOnce.add(stringRepeater);
		assertTrue(repeatTwice.run("a").equals("aaaa"));
	}
	
	@Test
	public void testAddChainToEnd(){
		CodeBlock<String, String, CodeBlock, String> stringRepeater = new CodeBlock<String, String, CodeBlock, String>(
				i->{
					String repeated = i + "" + i;
					return repeated;
				});
		CodeChain<String, String> repeatOnce = CodeChain.build(stringRepeater);
		CodeChain<String, String> repeatTwice = repeatOnce.add(repeatOnce);
		assertTrue(repeatTwice.run("a").equals("aaaa"));
		CodeChain<String, String> repeatFourTimes = repeatTwice.add(repeatTwice);
		assertTrue(repeatFourTimes.run("a").equals("aaaaaaaaaaaaaaaa"));
	}
}
