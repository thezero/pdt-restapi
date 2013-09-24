package info.thezero.eclipse.pdt.restapi.codeassist;


import java.util.List;
import java.util.LinkedList;

import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionStrategy;
import org.eclipse.php.core.codeassist.ICompletionStrategyFactory;

public class RestApiCompletionStrategyFactory implements ICompletionStrategyFactory {

	@Override
	public ICompletionStrategy[] create(ICompletionContext[] contexts) {
		List<ICompletionStrategy> result = new LinkedList<ICompletionStrategy>();
	    for (ICompletionContext context : contexts) {
	    	if (context.getClass() == RestApiCompletionContext.class) {
	    		result.add(new RestApiCompletionStrategy(context));
	    	}
	    }
	    return (ICompletionStrategy[]) result.toArray(new ICompletionStrategy[result.size()]);
	}

}
